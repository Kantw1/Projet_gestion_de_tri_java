package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un bon de commande dans le système de récompenses.
 * Ce bon permet à un utilisateur d'échanger ses points contre des réductions.
 */
public class BonDeCommande {

    // ===================== ATTRIBUTS =====================

    /** Identifiant unique du bon de commande */
    private int id;

    /** Utilisateur ayant passé cette commande */
    private Utilisateur utilisateur;

    /** Liste des catégories de produits pour lesquelles des réductions sont demandées */
    private List<CategorieProduit> ReductionsDisponibles;

    /** État actuel de la commande (ex: "en attente", "validée", "annulée") */
    private String etatCommande;

    /** Commerce auprès duquel la commande est passée */
    private Commerce commerce;

    /** Date de création de la commande */
    private LocalDate DateCommande;

    /** Nombre total de points utilisés pour cette commande */
    private int pointsUtilises;

    // ===================== CONSTRUCTEUR =====================

    public BonDeCommande(int id, Utilisateur utilisateur, List<CategorieProduit> reductions, Commerce commerce) {
        this.id = id;
        this.utilisateur = utilisateur;
        this.ReductionsDisponibles = reductions != null ? reductions : new ArrayList<>();
        this.commerce = commerce;
        this.DateCommande = LocalDate.now();
        this.etatCommande = "en attente";
        this.pointsUtilises = 0;
    }

    // ===================== MÉTHODES =====================

    /**
     * Verifie si l'utilisateur a assez de points pour valider la commande.
     */
    public boolean verifierSoldeUtilisateur() {
        int total = getTotalPointsUtilises();
        return utilisateur.getPtsFidelite() >= total;
    }

    public void setPointsUtilises(int pointsUtilises) {
        this.pointsUtilises = pointsUtilises;
    }


    /**
     * Utilise les points de l'utilisateur pour valider la commande.
     */
    public void utiliserPoints() {
        if (verifierSoldeUtilisateur()) {
            pointsUtilises = getTotalPointsUtilises();
            utilisateur.setPtsFidelite(utilisateur.getPtsFidelite() - pointsUtilises);
        }
    }

    /**
     * Valide la commande si les conditions sont remplies.
     */
    public boolean validerCommande() {
        if (verifierSoldeUtilisateur()) {
            utiliserPoints();
            this.etatCommande = "validée";
            return true;
        }
        return false;
    }

    /**
     * Annule la commande si elle n'a pas encore ete validee.
     */
    public boolean annulerCommande() {
        if (!etatCommande.equals("validée")) {
            this.etatCommande = "annulée";
            return true;
        }
        return false;
    }

    /**
     * Calcule le total de points necessaires pour cette commande.
     */
    public int getTotalPointsUtilises() {
        int total = 0;
        for (CategorieProduit cp : ReductionsDisponibles) {
            total += cp.getPointNecessaire();
        }
        return total;
    }

    // ===================== GETTERS et SETTERS =====================

    public List<CategorieProduit> getProduits() {
        return ReductionsDisponibles;
    }

    public String getEtatCommande() {
        return etatCommande;
    }

    public void setEtatCommande(String etat) {
        this.etatCommande = etat;
    }

    public LocalDate getDateCommande() {
        return DateCommande;
    }

    public Commerce getCommerce() {
        return commerce;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public int getPointsUtilises() {
        return pointsUtilises;
    }

    public int getId() {
        return id;
    }
}
