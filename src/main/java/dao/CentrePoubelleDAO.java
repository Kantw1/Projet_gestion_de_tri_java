package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CentrePoubelleDAO {
    private final Connection conn;

    public CentrePoubelleDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(int centreID, int poubelleID) throws SQLException {
        String sql = "INSERT INTO CentrePoubelle (centreID, poubelleID) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, centreID);
            stmt.setInt(2, poubelleID);
            stmt.executeUpdate();
        }
    }

    public void delete(int centreID, int poubelleID) throws SQLException {
        String sql = "DELETE FROM CentrePoubelle WHERE centreID = ? AND poubelleID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, centreID);
            stmt.setInt(2, poubelleID);
            stmt.executeUpdate();
        }
    }

    public List<Integer> getPoubellesByCentre(int centreID) throws SQLException {
        List<Integer> poubelles = new ArrayList<>();
        String sql = "SELECT poubelleID FROM CentrePoubelle WHERE centreID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, centreID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                poubelles.add(rs.getInt("poubelleID"));
            }
        }
        return poubelles;
    }

    public List<Integer> getCentresByPoubelle(int poubelleID) throws SQLException {
        List<Integer> centres = new ArrayList<>();
        String sql = "SELECT centreID FROM CentrePoubelle WHERE poubelleID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, poubelleID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                centres.add(rs.getInt("centreID"));
            }
        }
        return centres;
    }
    public boolean exists(int centreID, int poubelleID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM CentrePoubelle WHERE centreID = ? AND poubelleID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, centreID);
            stmt.setInt(2, poubelleID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

}
