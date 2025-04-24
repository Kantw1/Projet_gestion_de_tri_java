package dao;

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

    public void insert(Poubelle p) throws SQLException {
        String sql = "INSERT INTO poubelle (capaciteMax, emplacement, typePoubelle, quantiteActuelle, seuilAlerte) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, p.getCapaciteMax());
            stmt.setString(2, p.getEmplacement());
            stmt.setString(3, p.getTypePoubelle().name());
            stmt.setInt(4, p.getQuantiteActuelle());
            stmt.setInt(5, p.getSeuilAlerte());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGenere = rs.getInt(1);
                // p.setId(idGenere); // à activer si tu as un setter
            }
        }
    }

    public Poubelle getById(int id) throws SQLException {
        String sql = "SELECT * FROM poubelle WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Poubelle(
                            rs.getInt("id"),
                            rs.getInt("capaciteMax"),
                            rs.getString("emplacement"),
                            TypePoubelle.valueOf(rs.getString("typePoubelle")),
                            rs.getInt("seuilAlerte")
                    );
                }
            }
        }
        return null;
    }

    public List<Poubelle> getAll() throws SQLException {
        List<Poubelle> liste = new ArrayList<>();
        String sql = "SELECT * FROM poubelle";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Poubelle p = new Poubelle(
                        rs.getInt("id"),
                        rs.getInt("capaciteMax"),
                        rs.getString("emplacement"),
                        TypePoubelle.valueOf(rs.getString("typePoubelle")),
                        rs.getInt("seuilAlerte")
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

    /**
     * Supprime une poubelle de la base de données.
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM poubelle WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
