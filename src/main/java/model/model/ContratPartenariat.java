package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un contrat de partenariat entre un centre de tri et un commerce.
 * Ce contrat précise la période de validité ainsi que les catégories de produits concernées.
 */
public class ContratPartenariat {

    // ========== ATTRIBUTS ==========

    /** Date de début de validité du contrat */
    private LocalDate dateDebut;

    /** Date de fin de validité du contrat */
    private LocalDate dateFin;

    /** Liste des catégories de produits concernées par le contrat */
    private List<CategorieProduit> categoriesConcernees;

    /** Identifiant unique du contrat */
    private int id;

    /** Centre de tri avec lequel le contrat est établi */
    private CentreDeTri centre;

    /** Commerce concerné par ce contrat */
    private Commerce commerceConcerne;

    // ========== CONSTRUCTEUR ==========

    public ContratPartenariat(int id, LocalDate dateDebut, LocalDate dateFin,
                              CentreDeTri centre, Commerce commerceConcerne) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.centre = centre;
        this.commerceConcerne = commerceConcerne;
        this.categoriesConcernees = new ArrayList<>();
    }

    // ========== MÉTHODES UML ==========

    /**
     * Définit les règles d’utilisation du contrat.
     * Cette méthode peut être utilisée pour initialiser des paramètres internes ou vérifier la conformité.
     */
    public void definirRegleUtilisation() {
        // Implémentation future des règles
    }

    /**
     * Renvoie la liste des catégories de produits concernées par le contrat.
     */
    public List<CategorieProduit> getCategories() {
        return categoriesConcernees;
    }

    /**
     * Ajoute une catégorie de produit à la liste des catégories concernées par le contrat.
     */
    public void ajouterCategorie(CategorieProduit cp) {
        if (!categoriesConcernees.contains(cp)) {
            categoriesConcernees.add(cp);
        }
    }

    /**
     * Supprime une catégorie de produit de la liste des catégories concernées par le contrat.
     */
    public void retirerCategorie(CategorieProduit cp) {
        categoriesConcernees.remove(cp);
    }

    /**
     * Vérifie si une catégorie de produit est autorisée par le contrat.
     */
    public boolean estCategorieAutorisee(CategorieProduit cp) {
        return categoriesConcernees.contains(cp);
    }

    /**
     * Vérifie si le contrat est valide à une date donnée.
     */
    public boolean estValide(LocalDate date) {
        return (date != null && !date.isBefore(dateDebut) && !date.isAfter(dateFin));
    }

    /**
     * Renvoie la date de début du contrat.
     */
    public LocalDate getDateDebut() {
        return dateDebut;
    }

    /**
     * Renvoie la date de fin du contrat.
     */
    public LocalDate getDateFin() {
        return dateFin;
    }

    /**
     * Renvoie le centre de tri associé à ce contrat.
     */
    public CentreDeTri getCentre() {
        return centre;
    }

    /**
     * Renvoie le commerce associé à ce contrat.
     */
    public Commerce getCommerce() {
        return commerceConcerne;
    }
}
