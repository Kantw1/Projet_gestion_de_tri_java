package dao;

import model.BonDeCommande;
import model.CategorieProduit;
import model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BonDeCommandeDAO {

    private final Connection conn;

    public BonDeCommandeDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Insère un nouveau bon de commande.
     */
    public void insert(BonDeCommande bon) throws SQLException {
        String sql = "INSERT INTO BonDeCommande (utilisateurID, categorieProduitID, dateCommande, pointsUtilises) VALUES (?, ?, NOW(), ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bon.getUtilisateur().getId());
            stmt.setInt(2, bon.getCategorieProduit().getId());
            stmt.setInt(3, bon.getPointsUtilises());
            stmt.executeUpdate();
        }
    }

    /**
     * Récupère tous les bons de commande liés à un utilisateur donné.
     */
    public List<BonDeCommande> getByUtilisateurId(int utilisateurId) throws SQLException {
        List<BonDeCommande> bons = new ArrayList<>();
        String sql = """
        SELECT bc.id, bc.dateCommande, bc.pointsUtilises, 
               cp.id AS catId, cp.nom AS catNom, cp.pointNecessaire, cp.bonReduction
        FROM BonDeCommande bc
        JOIN CategorieProduit cp ON bc.categorieProduitID = cp.id
        WHERE bc.utilisateurID = ?
        ORDER BY bc.dateCommande DESC
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Utilisateur utilisateur = new Utilisateur(utilisateurId, null, 0, "utilisateur", -1); // utilisateur simplifié
                    CategorieProduit categorie = new CategorieProduit(
                            rs.getInt("catId"),
                            rs.getString("catNom"),
                            rs.getInt("pointNecessaire"),
                            rs.getFloat("bonReduction")
                    );
                    BonDeCommande bon = new BonDeCommande(
                            rs.getInt("id"),
                            utilisateur,
                            categorie,
                            rs.getDate("dateCommande").toLocalDate(),
                            rs.getInt("pointsUtilises")
                    );
                    bons.add(bon);
                }
            }
        }
        return bons;
    }


    /**
     * Supprime un bon de commande par son ID.
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM BonDeCommande WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
