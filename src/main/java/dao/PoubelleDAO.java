package dao;

import model.CentreDeTri;
import model.Poubelle;
import model.TypePoubelle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PoubelleDAO {
    private final Connection conn;

    public PoubelleDAO(Connection conn) {
        this.conn = conn;
    }



    public void insert(Poubelle p, int centreId) throws SQLException {
        String sqlPoubelle = "INSERT INTO poubelle (capaciteMax, emplacement, typePoubelle, quantiteActuelle, seuilAlerte) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sqlPoubelle, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, p.getCapaciteMax());
            stmt.setString(2, p.getEmplacement());
            stmt.setString(3, p.getTypePoubelle().name());
            stmt.setInt(4, p.getQuantiteActuelle());
            stmt.setInt(5, p.getSeuilAlerte());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int poubelleId = rs.getInt(1);

                // 🔥 Maintenant on lie la poubelle au centre dans centrepoubelle
                String sqlLien = "INSERT INTO centrepoubelle (centreID, poubelleID) VALUES (?, ?)";
                try (PreparedStatement stmtLien = conn.prepareStatement(sqlLien)) {
                    stmtLien.setInt(1, centreId);
                    stmtLien.setInt(2, poubelleId);
                    stmtLien.executeUpdate();
                }
            }
        }
    }

    public Poubelle getById(int id) throws SQLException {
        String sql = """
                     SELECT p.*, c.id AS centreId, c.nom AS centreNom, c.adresse AS centreAdresse
                     FROM poubelle p
                     JOIN centrepoubelle cp ON p.id = cp.poubelleID
                     JOIN centre_de_tri c ON cp.centreID = c.id
                     WHERE p.id = ?
                     """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CentreDeTri centre = new CentreDeTri(
                            rs.getInt("centreId"),
                            rs.getString("centreNom"),
                            rs.getString("centreAdresse")
                    );

                    return new Poubelle(
                            rs.getInt("id"),
                            rs.getInt("capaciteMax"),
                            rs.getString("emplacement"),
                            TypePoubelle.valueOf(rs.getString("typePoubelle")),
                            rs.getInt("seuilAlerte"),
                            centre
                    );
                }
            }
        }
        return null;
    }

    public List<Poubelle> getAll() throws SQLException {
        List<Poubelle> liste = new ArrayList<>();
        String sql = """
            SELECT 
                p.id AS poubelleId, p.capaciteMax, p.emplacement, p.typePoubelle, p.quantiteActuelle, p.seuilAlerte,
                c.id AS centreId, c.nom AS centreNom, c.adresse AS centreAdresse
            FROM Poubelle p
            JOIN CentrePoubelle cp ON p.id = cp.poubelleID
            JOIN CentreDeTri c ON cp.centreID = c.id
            ORDER BY c.nom ASC
            """;
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                CentreDeTri centre = new CentreDeTri(
                        rs.getInt("centreId"),
                        rs.getString("centreNom"),
                        rs.getString("centreAdresse")
                );

                Poubelle p = new Poubelle(
                        rs.getInt("poubelleId"),
                        rs.getInt("capaciteMax"),
                        rs.getString("emplacement"),
                        TypePoubelle.valueOf(rs.getString("typePoubelle")),
                        rs.getInt("seuilAlerte"),
                        centre
                );
                liste.add(p);
            }
        }
        return liste;
    }

    public void update(Poubelle p) throws SQLException {
        String sql = "UPDATE poubelle SET capaciteMax = ?, emplacement = ?, typePoubelle = ?, quantiteActuelle = ?, seuilAlerte = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p.getCapaciteMax());
            stmt.setString(2, p.getEmplacement());
            stmt.setString(3, p.getTypePoubelle().name());
            stmt.setInt(4, p.getQuantiteActuelle());
            stmt.setInt(5, p.getSeuilAlerte());
            stmt.setInt(6, p.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        // Supprimer d'abord dans centrepoubelle !
        String sqlLien = "DELETE FROM centrepoubelle WHERE poubelleID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlLien)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        // Puis supprimer dans poubelle
        String sqlPoubelle = "DELETE FROM poubelle WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlPoubelle)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    public List<Poubelle> getPoubellesByCentreId(int centreId) throws SQLException {
        List<Poubelle> liste = new ArrayList<>();
        String sql = """
        SELECT 
            p.id AS poubelleId, p.capaciteMax, p.emplacement, p.typePoubelle, p.quantiteActuelle, p.seuilAlerte
        FROM Poubelle p
        JOIN CentrePoubelle cp ON p.id = cp.poubelleID
        WHERE cp.centreID = ?
        ORDER BY p.id ASC
        """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, centreId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Poubelle p = new Poubelle(
                        rs.getInt("poubelleId"),
                        rs.getInt("capaciteMax"),
                        rs.getString("emplacement"),
                        TypePoubelle.valueOf(rs.getString("typePoubelle")),
                        rs.getInt("seuilAlerte"),
                        null // ici pas besoin de recharger le Centre, on sait déjà pour qui c'est
                );
                liste.add(p);
            }
        }
        return liste;
    }

}
