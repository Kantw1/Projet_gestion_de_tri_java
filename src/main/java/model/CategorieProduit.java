package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une catégorie de produits échangeables via les points de fidélité.
 * Chaque catégorie contient une liste de noms de produits, un seuil minimal de points,
 * et un pourcentage de réduction applicable.
 */
public class CategorieProduit {

    // ========== ATTRIBUTS ==========

    /** Identifiant unique de la catégorie */
    private int id;

    /** Nom de la catégorie (ex. "Alimentaire", "Hygiène") */
    private String nom;

    /** Liste des produits dans cette catégorie (par leur nom) */
    private List<String> produits;

    /** Nombre de points nécessaires pour accéder à un produit de cette catégorie */
    private int pointNecessaire;

    /** Pourcentage de réduction accordé pour un produit (ex : 0.2 = 20%) */
    private float bonReduction;

    // ========== CONSTRUCTEUR ==========

    public CategorieProduit(int id, String nom, int pointNecessaire, float bonReduction) {
        this.id = id;
        this.nom = nom;
        this.pointNecessaire = pointNecessaire;
        this.bonReduction = bonReduction;
        this.produits = new ArrayList<>();
    }

    // ========== GETTERS ==========

    /** Retourne l'identifiant de la catégorie */
    public int getId() {
        return id;
    }

    /** Renvoie le nom de la catégorie */
    public String getNom() {
        return nom;
    }

    /** Renvoie le nombre de points nécessaires pour un produit de cette catégorie */
    public int getPointNecessaire() {
        return pointNecessaire;
    }

    /** Renvoie la réduction applicable sous forme de pourcentage */
    public float getBonReduction() {
        return bonReduction;
    }

    /** Retourne la liste des produits */
    public List<String> getProduits() {
        return produits;
    }

    // ========== MÉTHODES UML ==========

    /** Ajoute un produit dans la catégorie */
    public void associerProduit(String nomProduit) {
        if (!produits.contains(nomProduit)) {
            produits.add(nomProduit);
        }
    }

    /** Vérifie si la catégorie contient un produit donné */
    public boolean verifierProduit(String nomProduit) {
        return produits.contains(nomProduit);
    }

    /** Vérifie si un utilisateur est éligible avec un certain nombre de points */
    public boolean estEligible(int pointsUtilisateur) {
        return pointsUtilisateur >= pointNecessaire;
    }

    /** Applique la réduction sur un prix donné */
    public float appliquerReduction(float prixOriginal) {
        return prixOriginal * (1 - bonReduction);
    }

    /** Retire un produit de la liste (s’il existe) */
    public void retirerProduit(String nomProduit) {
        produits.remove(nomProduit);
    }
}
