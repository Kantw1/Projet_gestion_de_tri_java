package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CommerceCategorieProduitDAO {
    private final Connection conn;

    public CommerceCategorieProduitDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(int commerceID, int categorieID) throws SQLException {
        String sql = "INSERT INTO CommerceCategorieProduit (commerceID, categorieID) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, commerceID);
            stmt.setInt(2, categorieID);
            stmt.executeUpdate();
        }
    }

    public void delete(int commerceID, int categorieID) throws SQLException {
        String sql = "DELETE FROM CommerceCategorieProduit WHERE commerceID = ? AND categorieID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, commerceID);
            stmt.setInt(2, categorieID);
            stmt.executeUpdate();
        }
    }
    public List<Integer> getCategoriesByCommerce(int commerceID) throws SQLException {
        List<Integer> categories = new ArrayList<>();
        String sql = "SELECT categorieID FROM CommerceCategorieProduit WHERE commerceID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, commerceID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                categories.add(rs.getInt("categorieID"));
            }
        }
        return categories;
    }
    public List<Integer> getCommercesByCategorie(int categorieID) throws SQLException {
        List<Integer> commerces = new ArrayList<>();
        String sql = "SELECT commerceID FROM CommerceCategorieProduit WHERE categorieID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categorieID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                commerces.add(rs.getInt("commerceID"));
            }
        }
        return commerces;
    }

}
