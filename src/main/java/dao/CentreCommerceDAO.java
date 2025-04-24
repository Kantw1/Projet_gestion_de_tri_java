package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CentreCommerceDAO {
    private final Connection conn;

    public CentreCommerceDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(int centreID, int commerceID) throws SQLException {
        String sql = "INSERT INTO CentreCommerce (centreID, commerceID) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, centreID);
            stmt.setInt(2, commerceID);
            stmt.executeUpdate();
        }
    }

    public void delete(int centreID, int commerceID) throws SQLException {
        String sql = "DELETE FROM CentreCommerce WHERE centreID = ? AND commerceID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, centreID);
            stmt.setInt(2, commerceID);
            stmt.executeUpdate();
        }
    }

    public List<Integer> getCommercesByCentre(int centreID) throws SQLException {
        List<Integer> commerces = new ArrayList<>();
        String sql = "SELECT commerceID FROM CentreCommerce WHERE centreID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, centreID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                commerces.add(rs.getInt("commerceID"));
            }
        }
        return commerces;
    }

    public List<Integer> getCentresByCommerce(int commerceID) throws SQLException {
        List<Integer> centres = new ArrayList<>();
        String sql = "SELECT centreID FROM CentreCommerce WHERE commerceID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, commerceID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                centres.add(rs.getInt("centreID"));
            }
        }
        return centres;
    }

    public boolean exists(int centreID, int commerceID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM CentreCommerce WHERE centreID = ? AND commerceID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, centreID);
            stmt.setInt(2, commerceID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}
