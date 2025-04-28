package dao;

import model.BonDeCommande;
import model.CategorieProduit;
import model.Commerce;
import model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BonDeCommandeDAO {

    private final Connection conn;

    public BonDeCommandeDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(BonDeCommande bdc) throws SQLException {
        String sql = "INSERT INTO BonDeCommande (utilisateurID, etatCommande, dateCommande, commerceID, pointsUtilises) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, bdc.getUtilisateur().getId());
            stmt.setString(2, bdc.getEtatCommande());
            stmt.setDate(3, Date.valueOf(bdc.getDateCommande()));
            stmt.setInt(4, bdc.getCommerce().getId());
            stmt.setInt(5, bdc.getPointsUtilises());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenere = rs.getInt(1);
                    // Enregistre les catégories liées
                    for (CategorieProduit cp : bdc.getProduits()) {
                        try (PreparedStatement joinStmt = conn.prepareStatement(
                                "INSERT INTO CommandeCategorieProduit (bonDeCommandeID, categorieProduitID) VALUES (?, ?)")) {
                            joinStmt.setInt(1, idGenere);
                            joinStmt.setInt(2, cp.getId());
                            joinStmt.executeUpdate();
                        }
                    }
                }
            }
        }
    }

    public BonDeCommande getById(int id, Utilisateur u, Commerce c, List<CategorieProduit> categories) throws SQLException {
        String sql = "SELECT * FROM BonDeCommande WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BonDeCommande bdc = new BonDeCommande(
                            rs.getInt("id"),
                            u,
                            categories,
                            c
                    );
                    bdc.setEtatCommande(rs.getString("etatCommande"));
                    bdc.setPointsUtilises(rs.getInt("pointsUtilises"));
                    return bdc;
                }
            }
        }
        return null;
    }

    public List<BonDeCommande> getByUtilisateurId(int utilisateurId) throws SQLException {
        List<BonDeCommande> bons = new ArrayList<>();
        String sql = "SELECT id, etatCommande, pointsUtilises FROM BonDeCommande WHERE utilisateurID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BonDeCommande bdc = new BonDeCommande(
                            rs.getInt("id"),
                            null, // utilisateur sera géré si besoin
                            new ArrayList<>(), // liste vide de produits pour l'affichage simple
                            null // commerce non utilisé ici
                    );
                    bdc.setEtatCommande(rs.getString("etatCommande"));
                    bdc.setPointsUtilises(rs.getInt("pointsUtilises"));
                    bons.add(bdc);
                }
            }
        }
        return bons;
    }


    public List<BonDeCommande> getAll(Utilisateur u, Commerce c, List<CategorieProduit> categories) throws SQLException {
        List<BonDeCommande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM BonDeCommande WHERE utilisateurID = ? AND commerceID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, u.getId());
            stmt.setInt(2, c.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BonDeCommande bdc = new BonDeCommande(
                            rs.getInt("id"),
                            u,
                            categories,
                            c
                    );
                    bdc.setEtatCommande(rs.getString("etatCommande"));
                    bdc.setPointsUtilises(rs.getInt("pointsUtilises"));
                    commandes.add(bdc);
                }
            }
        }
        return commandes;
    }

    public void update(BonDeCommande bdc) throws SQLException {
        String sql = "UPDATE BonDeCommande SET etatCommande = ?, dateCommande = ?, pointsUtilises = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bdc.getEtatCommande());
            stmt.setDate(2, Date.valueOf(bdc.getDateCommande()));
            stmt.setInt(3, bdc.getPointsUtilises());
            stmt.setInt(4, bdc.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        // Supprimer d’abord les catégories liées
        try (PreparedStatement stmt1 = conn.prepareStatement(
                "DELETE FROM CommandeCategorieProduit WHERE bonDeCommandeID = ?")) {
            stmt1.setInt(1, id);
            stmt1.executeUpdate();
        }

        // Ensuite la commande elle-même
        try (PreparedStatement stmt2 = conn.prepareStatement(
                "DELETE FROM BonDeCommande WHERE id = ?")) {
            stmt2.setInt(1, id);
            stmt2.executeUpdate();
        }
    }
}
