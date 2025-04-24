package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoriqueDepotDAO {
    private final Connection conn;

    public HistoriqueDepotDAO(Connection conn) {
        this.conn = conn;
    }
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

    public void insert(int utilisateurID, int depotID) throws SQLException {
        String sql = "INSERT INTO HistoriqueDepot (utilisateurID, depotID) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurID);
            stmt.setInt(2, depotID);
            stmt.executeUpdate();
        }
    }

    public void delete(int utilisateurID, int depotID) throws SQLException {
        String sql = "DELETE FROM HistoriqueDepot WHERE utilisateurID = ? AND depotID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurID);
            stmt.setInt(2, depotID);
            stmt.executeUpdate();
        }
    }
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

}
