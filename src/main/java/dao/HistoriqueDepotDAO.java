package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoriqueDepotDAO {
    private final Connection conn;

    public HistoriqueDepotDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Vérifie si une entrée existe déjà (par utilisateur et dépôt)
     */
    public boolean existe(int utilisateurID, int depotID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM HistoriqueDepot WHERE utilisateurID = ? AND depotID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurID);
            stmt.setInt(2, depotID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Insère un nouvel historique de dépôt
     */
    public void insert(int utilisateurID, int depotID, Timestamp dateDepot, String typeDechet, int pointsGagnes) throws SQLException {
        String sql = "INSERT INTO HistoriqueDepot (utilisateurID, depotID, dateDepot, typeDechet, pointsGagnes) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurID);
            stmt.setInt(2, depotID);
            stmt.setTimestamp(3, dateDepot);
            stmt.setString(4, typeDechet);
            stmt.setInt(5, pointsGagnes);
            stmt.executeUpdate();
        }
    }

    /**
     * Supprime une entrée d'historique
     */
    public void delete(int utilisateurID, int depotID) throws SQLException {
        String sql = "DELETE FROM HistoriqueDepot WHERE utilisateurID = ? AND depotID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurID);
            stmt.setInt(2, depotID);
            stmt.executeUpdate();
        }
    }

    /**
     * Récupère tous les dépôts d'un utilisateur
     */
    public List<Integer> getDepotsByUtilisateur(int utilisateurID) throws SQLException {
        List<Integer> depots = new ArrayList<>();
        String sql = "SELECT depotID FROM HistoriqueDepot WHERE utilisateurID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                depots.add(rs.getInt("depotID"));
            }
        }
        return depots;
    }

    /**
     * Récupère tous les utilisateurs ayant fait un dépôt précis
     */
    public List<Integer> getUtilisateursByDepot(int depotID) throws SQLException {
        List<Integer> utilisateurs = new ArrayList<>();
        String sql = "SELECT utilisateurID FROM HistoriqueDepot WHERE depotID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, depotID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                utilisateurs.add(rs.getInt("utilisateurID"));
            }
        }
        return utilisateurs;
    }

    /**
     * Récupère tout l'historique des dépôts pour un utilisateur donné
     * (avec type de déchet, date et points gagnés)
     */
    public List<String> getHistoriqueDetailleByUtilisateur(int utilisateurID) throws SQLException {
        List<String> historique = new ArrayList<>();
        String sql = "SELECT depotID, dateDepot, typeDechet, pointsGagnes FROM HistoriqueDepot WHERE utilisateurID = ? ORDER BY dateDepot DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String ligne = "Depot #" + rs.getInt("depotID") +
                        " | Date: " + rs.getTimestamp("dateDepot") +
                        " | Type: " + rs.getString("typeDechet") +
                        " | Points: " + rs.getInt("pointsGagnes");
                historique.add(ligne);
            }
        }
        return historique;
    }
    /**
     * Récupère tout l'historique des dépôts pour une poubelle donnée
     */
    public List<String> getHistoriqueByPoubelle(int poubelleID) throws SQLException {
        List<String> historique = new ArrayList<>();
        String sql = """
        SELECT d.id AS depotID, d.heureDepot, d.type, d.points
        FROM Depot d
        WHERE d.poubelleID = ?
        ORDER BY d.heureDepot DESC
    """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, poubelleID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String ligne = "Dépot #" + rs.getInt("depotID") +
                        " | Date: " + rs.getTimestamp("heureDepot") +
                        " | Type: " + rs.getString("type") +
                        " | Points: " + rs.getInt("points");
                historique.add(ligne);
            }
        }
        return historique;
    }

}
