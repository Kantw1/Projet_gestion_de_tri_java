package dao;

import model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {
    private final Connection conn;

    public UtilisateurDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Insère un utilisateur dans la base.
     */
    public void insert(Utilisateur u) throws SQLException {
        String sql = "INSERT INTO Utilisateur (nom, ptsFidelite, codeAcces) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, u.getNom());
            stmt.setInt(2, u.getPtsFidelite());
            stmt.setInt(3, u.getCodeAcces());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setId(rs.getInt(1)); // ✅ affectation de l'ID généré
                }
            }
        }
    }

    /**
     * Récupère un utilisateur par son ID.
     */
    public Utilisateur getById(int id) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur u = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("codeAcces")
                );
                u.setPtsFidelite(rs.getInt("ptsFidelite"));
                return u;
            }
        }
        return null;
    }

    /**
     * Récupère tous les utilisateurs.
     */
    public List<Utilisateur> getAll() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM Utilisateur";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Utilisateur u = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("codeAcces")
                );
                u.setPtsFidelite(rs.getInt("ptsFidelite"));
                utilisateurs.add(u);
            }
        }
        return utilisateurs;
    }

    /**
     * Met à jour les informations d’un utilisateur.
     */
    public void update(Utilisateur u) throws SQLException {
        String sql = "UPDATE Utilisateur SET nom = ?, ptsFidelite = ?, codeAcces = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getNom());
            stmt.setInt(2, u.getPtsFidelite());
            stmt.setInt(3, u.getCodeAcces());
            stmt.setInt(4, u.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Supprime un utilisateur de la base.
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Utilisateur WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Met à jour uniquement les points de fidélité.
     */
    public void updateFidelite(int id, int newPoints) throws SQLException {
        String sql = "UPDATE Utilisateur SET ptsFidelite = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newPoints);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }
    /**
     * Récupère un utilisateur par son code d'accès.
     */
    public Utilisateur getByCodeAcces(int codeAcces) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE codeAcces = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codeAcces);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur u = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("codeAcces")
                );
                u.setPtsFidelite(rs.getInt("ptsFidelite"));
                return u;
            }
        }
        return null;
    }

    /**
     * Récupère un utilisateur par son nom.
     */
    public Utilisateur getByNom(String nom) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE nom = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur u = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("codeAcces")
                );
                u.setPtsFidelite(rs.getInt("ptsFidelite"));
                return u;
            }
        }
        return null;
    }
}
