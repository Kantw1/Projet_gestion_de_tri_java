package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un commerce partenaire qui propose des réductions
 * sur des catégories de produits en échange de points.
 */
public class Commerce {

    // ========== ATTRIBUTS ==========

    /** Nom du commerce (ex. "Carrefour", "Décathlon") */
    private String nom;

    private int id;

    public int getId() {
        return id;
    }

    /** Liste des catégories de produits disponibles dans ce commerce */
    private List<CategorieProduit> categoriesProduits;

    /** Contrat de partenariat signé avec le centre de tri */
    private ContratPartenariat contrat;

    /** Historique des bons de commande échangés dans ce commerce */
    private List<BonDeCommande> historiqueCommandes;

    /** Centre de tri auquel ce commerce est rattaché */
    private CentreDeTri centre;

    // ========== CONSTRUCTEUR ==========

    public Commerce(int id, String nom, CentreDeTri centre) {
        this.id = id;
        this.nom = nom;
        this.categoriesProduits = new ArrayList<>();
        this.historiqueCommandes = new ArrayList<>();
        this.centre = centre;
    }

    // ========== MÉTHODES UML ==========

    /**
     * Permet à un utilisateur d’échanger ses points contre un produit d’une catégorie.
     * @return true si l’échange a été accepté.
     */
    public boolean echangerPoints(Utilisateur u, CategorieProduit cp) {
        if (cp != null && cp.estEligible(u.getPtsFidelite())) {
            BonDeCommande bon = u.convertirPoints(cp); // retire les points et crée un bon
            return bon != null;
        }
        return false;
    }

    /** Retourne la liste des catégories de produits proposées. */
    public List<CategorieProduit> getCategoriesProduits() {
        return categoriesProduits;
    }

    /** Vérifie que les catégories respectent le contrat de partenariat. */
    public boolean verifierConditionsContrat(ContratPartenariat contrat) {
        return contrat != null &&
                contrat.estValide(java.time.LocalDate.now()) &&
                categoriesProduits.stream().allMatch(c -> contrat.estCategorieAutorisee(c));
    }

    /** Accepte un bon de commande si les conditions sont respectées. */
    public boolean accepterCommande(BonDeCommande commande) {
        if (verifierConditionsContrat(this.contrat)) {
            historiqueCommandes.add(commande);
            return true;
        }
        return false;
    }

    /**
     * Retourne le taux de réduction associé à une catégorie.
     */
    public float getReductionPourCategorie(CategorieProduit cp) {
        return cp.getBonReduction();
    }

    /** Ajoute une catégorie de produits dans le commerce. */
    public void ajouterCategorie(CategorieProduit cp) {
        if (!categoriesProduits.contains(cp)) {
            categoriesProduits.add(cp);
        }
    }

    /** Supprime une catégorie de produits du commerce. */
    public void supprimerCategorie(CategorieProduit cp) {
        categoriesProduits.remove(cp);
    }

    /** Retourne la liste des bons de commande échangés dans ce commerce. */
    public List<BonDeCommande> getHistoriqueCommandes() {
        return historiqueCommandes;
    }

    /** Retourne le centre de tri associé à ce commerce. */
    public CentreDeTri getCentre() {
        return centre;
    }

    // ========== GETTERS ==========

    public String getNom() {
        return nom;
    }

    public void setCentre(CentreDeTri centre) {
        this.centre = centre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCategoriesProduits(List<CategorieProduit> categoriesProduits) {
        this.categoriesProduits = categoriesProduits;
    }

    public ContratPartenariat getContrat() {
        return contrat;
    }

    public void setContrat(ContratPartenariat contrat) {
        this.contrat = contrat;
    }

    @Override
    public String toString() {
        return nom; // afficher uniquement le nom du commerce dans la ComboBox
    }

}
