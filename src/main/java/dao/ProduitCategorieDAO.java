package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitCategorieDAO {
    private final Connection conn;

    public ProduitCategorieDAO(Connection conn) {
        this.conn = conn;
    }

    /** Insère un produit lié à une catégorie */
    public void insert(String produit, int categorieID) throws SQLException {
        String sql = "INSERT INTO ProduitCategorie (produit, categorieID) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produit);
            stmt.setInt(2, categorieID);
            stmt.executeUpdate();
        }
    }

    /** Récupère tous les produits d’une catégorie */
    public List<String> getProduitsByCategorie(int categorieID) throws SQLException {
        List<String> produits = new ArrayList<>();
        String sql = "SELECT produit FROM ProduitCategorie WHERE categorieID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categorieID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                produits.add(rs.getString("produit"));
            }
        }
        return produits;
    }

    /** Récupère tous les liens produit-catégorie */
    public List<String> getAllProduits() throws SQLException {
        List<String> produits = new ArrayList<>();
        String sql = "SELECT produit FROM ProduitCategorie";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                produits.add(rs.getString("produit"));
            }
        }
        return produits;
    }

    /** Supprime tous les produits d’une catégorie */
    public void deleteByCategorie(int categorieID) throws SQLException {
        String sql = "DELETE FROM ProduitCategorie WHERE categorieID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categorieID);
            stmt.executeUpdate();
        }
    }

    /** Supprime un produit spécifique d’une catégorie */
    public void deleteProduitFromCategorie(String produit, int categorieID) throws SQLException {
        String sql = "DELETE FROM ProduitCategorie WHERE produit = ? AND categorieID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produit);
            stmt.setInt(2, categorieID);
            stmt.executeUpdate();
        }
    }
}
