package dao;

import model.Depot;
import model.NatureDechet;
import model.Poubelle;
import model.Utilisateur;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DepotDAO {
    private final Connection conn;

    public DepotDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Depot d) throws SQLException {
        String sql = "INSERT INTO depot (type, poids, quantite, heureDepot, points, poubelleID, utilisateurID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, d.getType().name());
            stmt.setFloat(2, d.getPoids());
            stmt.setInt(3, d.getQuantite());
            stmt.setTimestamp(4, Timestamp.valueOf(d.getHeureDepot()));
            stmt.setInt(5, d.getPoints());
            stmt.setInt(6, d.getPoubelle().getId());
            stmt.setInt(7, d.getUtilisateur().getId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    d.setId(rs.getInt(1)); // 🔥 on met à jour l'ID du dépôt !
                }
            }
        }
    }

    public Depot getById(int id, Poubelle p, Utilisateur u) throws SQLException {
        String sql = "SELECT * FROM depot WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Depot(
                            rs.getInt("id"),
                            NatureDechet.valueOf(rs.getString("type")),
                            rs.getFloat("poids"),
                            rs.getInt("quantite"),
                            rs.getTimestamp("heureDepot").toLocalDateTime(),
                            p,
                            u
                    );
                }
            }
        }
        return null;
    }

    public List<Depot> getAll(Poubelle p, Utilisateur u) throws SQLException {
        List<Depot> liste = new ArrayList<>();
        String sql = "SELECT * FROM depot";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Depot d = new Depot(
                        rs.getInt("id"),
                        NatureDechet.valueOf(rs.getString("type")),
                        rs.getFloat("poids"),
                        rs.getInt("quantite"),
                        rs.getTimestamp("heureDepot").toLocalDateTime(),
                        p,
                        u
                );
                liste.add(d);
            }
        }
        return liste;
    }

    public void update(Depot d) throws SQLException {
        String sql = "UPDATE depot SET type = ?, poids = ?, quantite = ?, heureDepot = ?, points = ?, poubelleID = ?, utilisateurID = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, d.getType().name());
            stmt.setFloat(2, d.getPoids());
            stmt.setInt(3, d.getQuantite());
            stmt.setTimestamp(4, Timestamp.valueOf(d.getHeureDepot()));
            stmt.setInt(5, d.getPoints());
            stmt.setInt(6, d.getPoubelle().getId());
            stmt.setInt(7, d.getUtilisateur().getId());
            stmt.setInt(8, d.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM depot WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // --- FONCTIONS AJOUTÉES DE RÉCUPÉRATION ---

    /**
     * Récupère tous les dépôts pour une poubelle donnée.
     */
    public List<Depot> getByPoubelleId(int poubelleId) throws SQLException {
        List<Depot> liste = new ArrayList<>();
        String sql = "SELECT * FROM depot WHERE poubelleID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, poubelleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Depot d = new Depot(
                            rs.getInt("id"),
                            NatureDechet.valueOf(rs.getString("type")),
                            rs.getFloat("poids"),
                            rs.getInt("quantite"),
                            rs.getTimestamp("heureDepot").toLocalDateTime(),
                            null, // pas d'objet Poubelle rechargé ici
                            null  // pas d'objet Utilisateur rechargé ici
                    );
                    liste.add(d);
                }
            }
        }
        return liste;
    }

    /**
     * Récupère tous les dépôts faits par un utilisateur donné.
     */
    public List<Depot> getByUtilisateurId(int utilisateurId) throws SQLException {
        List<Depot> liste = new ArrayList<>();
        String sql = "SELECT * FROM depot WHERE utilisateurID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Depot d = new Depot(
                            rs.getInt("id"),
                            NatureDechet.valueOf(rs.getString("type")),
                            rs.getFloat("poids"),
                            rs.getInt("quantite"),
                            rs.getTimestamp("heureDepot").toLocalDateTime(),
                            null,
                            null
                    );
                    liste.add(d);
                }
            }
        }
        return liste;
    }

    /**
     * Récupère tous les dépôts effectués dans une plage de dates.
     */
    public List<Depot> getByDateRange(LocalDateTime debut, LocalDateTime fin) throws SQLException {
        List<Depot> liste = new ArrayList<>();
        String sql = "SELECT * FROM depot WHERE heureDepot BETWEEN ? AND ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(debut));
            stmt.setTimestamp(2, Timestamp.valueOf(fin));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Depot d = new Depot(
                            rs.getInt("id"),
                            NatureDechet.valueOf(rs.getString("type")),
                            rs.getFloat("poids"),
                            rs.getInt("quantite"),
                            rs.getTimestamp("heureDepot").toLocalDateTime(),
                            null,
                            null
                    );
                    liste.add(d);
                }
            }
        }
        return liste;
    }

    /**
     * Calcule le poids total déposé dans une poubelle donnée.
     */
    public float getTotalPoidsByPoubelle(int poubelleId) throws SQLException {
        String sql = "SELECT SUM(poids) AS total FROM depot WHERE poubelleID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, poubelleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getFloat("total");
                }
            }
        }
        return 0;
    }

}
