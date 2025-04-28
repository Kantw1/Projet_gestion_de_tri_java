package dao;

import model.CategorieProduit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieProduitDAO {
    private final Connection conn;

    public CategorieProduitDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(CategorieProduit cp) throws SQLException {
        String sql = "INSERT INTO categorieProduit (nom, bonReduction, pointNecessaire) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cp.getNom());
            stmt.setFloat(2, cp.getBonReduction());
            stmt.setInt(3, cp.getPointNecessaire());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGenere = rs.getInt(1);
                // cp.setId(idGenere); // optionnel si setter disponible
            }
        }
    }

    public CategorieProduit getById(int id) throws SQLException {
        String sql = "SELECT * FROM categorieProduit WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new CategorieProduit(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getInt("pointNecessaire"),
                            rs.getFloat("bonReduction")
                    );
                }
            }
        }
        return null;
    }

    public List<CategorieProduit> getCategoriesByCommerceId(int commerceId) throws SQLException {
        List<CategorieProduit> categories = new ArrayList<>();
        String sql = "SELECT cp.id, cp.nom, cp.pointNecessaire, cp.bonReduction " +
                "FROM CommerceCategorieProduit ccp " +
                "JOIN CategorieProduit cp ON ccp.categorieID = cp.id " +
                "WHERE ccp.commerceID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, commerceId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                categories.add(new CategorieProduit(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("pointNecessaire"),
                        rs.getFloat("bonReduction")
                ));
            }
        }
        return categories;
    }


    public List<CategorieProduit> getAll() throws SQLException {
        List<CategorieProduit> categories = new ArrayList<>();
        String sql = "SELECT * FROM categorieProduit";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                categories.add(new CategorieProduit(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("pointNecessaire"),
                        rs.getFloat("bonReduction")
                ));
            }
        }
        return categories;
    }

    public void update(CategorieProduit cp) throws SQLException {
        String sql = "UPDATE categorieProduit SET nom = ?, bonReduction = ?, pointNecessaire = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cp.getNom());
            stmt.setFloat(2, cp.getBonReduction());
            stmt.setInt(3, cp.getPointNecessaire());
            stmt.setInt(4, cp.getId());
            stmt.executeUpdate();
        }
    }
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM categorieProduit WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

}
