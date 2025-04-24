package dao;

import model.CentreDeTri;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CentreDeTriDAO {
    private final Connection conn;

    public CentreDeTriDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(CentreDeTri c) throws SQLException {
        String sql = "INSERT INTO centreDeTri (nom, adresse) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getAdresse());
            stmt.executeUpdate();
        }
    }

    public CentreDeTri getById(int id) throws SQLException {
        String sql = "SELECT * FROM centreDeTri WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new CentreDeTri(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse")
                );
            }
        }
        return null;
    }

    public List<CentreDeTri> getAll() throws SQLException {
        List<CentreDeTri> centres = new ArrayList<>();
        String sql = "SELECT * FROM centreDeTri";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                centres.add(new CentreDeTri(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse")
                ));
            }
        }
        return centres;
    }

    public void update(CentreDeTri c) throws SQLException {
        String sql = "UPDATE centreDeTri SET nom = ?, adresse = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getAdresse());
            stmt.setInt(3, c.getId());
            stmt.executeUpdate();
        }
    }
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM centreDeTri WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

}
