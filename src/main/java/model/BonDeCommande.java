package model;

import java.time.LocalDate;

/**
 * Classe représentant un bon de commande dans le système de récompenses.
 * Un bon permet d’échanger ses points contre une seule réduction (catégorie produit).
 */
public class BonDeCommande {

    private int id;
    private Utilisateur utilisateur;
    private CategorieProduit categorieProduit;
    private Commerce commerce;
    private LocalDate dateCommande;
    private int pointsUtilises;

    public BonDeCommande(int id, Utilisateur utilisateur, CategorieProduit categorieProduit, LocalDate dateCommande, int pointsUtilises) {
        this.id = id;
        this.utilisateur = utilisateur;
        this.categorieProduit = categorieProduit;
        this.dateCommande = dateCommande;
        this.pointsUtilises = pointsUtilises;
    }


    // ========== GETTERS ==========

    public int getId() {
        return id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public CategorieProduit getCategorieProduit() {
        return categorieProduit;
    }

    public Commerce getCommerce() {
        return commerce;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public int getPointsUtilises() {
        return pointsUtilises;
    }

    // ========== SETTERS ==========

    public void setPointsUtilises(int pointsUtilises) {
        this.pointsUtilises = pointsUtilises;
    }

    // ========== MÉTHODES UTILITAIRES ==========

    /** Retourne le nom de la catégorie produit liée */
    public String getNomCategorieProduit() {
        return categorieProduit != null ? categorieProduit.getNom() : "Inconnu";
    }

    /** Retourne le nom du commerce lié */
    public String getNomCommerce() {
        return commerce != null ? commerce.getNom() : "Inconnu";
    }
    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

}
