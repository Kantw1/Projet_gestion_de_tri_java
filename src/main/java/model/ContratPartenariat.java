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

    private int id;  // ✅ utilisé pour l'accès DAO
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private List<CategorieProduit> categoriesConcernees;
    private CentreDeTri centre;
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

    // ========== GETTERS POUR DAO ==========

    /** Identifiant unique du contrat */
    public int getId() {
        return id;
    }

    /** Date de début du contrat */
    public LocalDate getDateDebut() {
        return dateDebut;
    }

    /** Date de fin du contrat */
    public LocalDate getDateFin() {
        return dateFin;
    }

    /** Centre de tri concerné */
    public CentreDeTri getCentre() {
        return centre;
    }

    /** Commerce concerné */
    public Commerce getCommerce() {
        return commerceConcerne;
    }

    // ========== MÉTHODES UML MÉTIER ==========

    public List<CategorieProduit> getCategories() {
        return categoriesConcernees;
    }

    public void ajouterCategorie(CategorieProduit cp) {
        if (!categoriesConcernees.contains(cp)) {
            categoriesConcernees.add(cp);
        }
    }

    public void retirerCategorie(CategorieProduit cp) {
        categoriesConcernees.remove(cp);
    }

    public boolean estCategorieAutorisee(CategorieProduit cp) {
        return categoriesConcernees.contains(cp);
    }

    public boolean estValide(LocalDate date) {
        return (date != null && !date.isBefore(dateDebut) && !date.isAfter(dateFin));
    }

    public void definirRegleUtilisation() {
        // À compléter si logique spécifique
    }
}
