package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un utilisateur qui peut effectuer des dépôts de déchets,
 * accumuler des points de fidélité et accéder à certaines poubelles autorisées.
 */
public class Utilisateur {

    // ========== ATTRIBUTS ==========

    /** Identifiant unique de l'utilisateur */
    private int id;

    /** Nom de l'utilisateur */
    private String nom;

    /** Points de fidélité accumulés par l'utilisateur */
    private int ptsFidelite;

    /** Code d'accès personnel permettant l'identification auprès des poubelles */
    private int codeAcces;

    /** Historique des dépôts effectués par l'utilisateur */
    private List<Depot> historiqueDepots;

    /** Liste des poubelles auxquelles l'utilisateur a accès */
    private List<Poubelle> poubellesAccessibles;

    // ========== CONSTRUCTEUR ==========

    /**
     * Initialise un nouvel utilisateur avec un identifiant, un nom et un code d'accès.
     */
    public Utilisateur(int id, String nom, int codeAcces) {
        this.id = id;
        this.nom = nom;
        this.codeAcces = codeAcces;
        this.ptsFidelite = 0;
        this.historiqueDepots = new ArrayList<>();
        this.poubellesAccessibles = new ArrayList<>();
    }

    // ========== MÉTHODES UML ==========

    /**
     * Permet à l'utilisateur d'effectuer un dépôt dans une poubelle donnée.
     * Le dépôt est ajouté à l'historique, et les points de fidélité sont mis à jour.
     */
    public void deposerDechets(Poubelle p, Depot d) {
        if (p.accepterDepot(d, this)) {
            historiqueDepots.add(d);
            ptsFidelite += d.getPoints();
        }
    }

    /**
     * Renvoie l'historique complet des dépôts de l'utilisateur.
     */
    public List<Depot> consulterHistorique() {
        return historiqueDepots;
    }

    /**
     * Convertit un nombre de points de fidélité en bon de commande.
     * Le bon généré dépend du nombre de points utilisés.
     */
    public BonDeCommande convertirPoints(int points) {
        if (points > 0 && points <= ptsFidelite) {
            ptsFidelite -= points;
            return new BonDeCommande(0, this, new ArrayList<>(), null);
        }
        return null;
    }

    /**
     * Renvoie le code d'accès personnel de l'utilisateur.
     */
    public int getCodeAcces() {
        return codeAcces;
    }

    /**
     * Renvoie le nom de l'utilisateur.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Renvoie le nombre actuel de points de fidélité de l'utilisateur.
     */
    public int getPtsFidelite() {
        return ptsFidelite;
    }

    /**
     * Renvoie directement l'historique des dépôts (accès direct à la liste).
     */
    public Object getHistoriqueDepots() {
        return historiqueDepots;
    }

    /**
     * Modifie le nombre de points de fidélité de l'utilisateur.
     * @param ptsFidelite Le nouveau solde de points.
     */
    public void setPtsFidelite(int ptsFidelite) {
        this.ptsFidelite = ptsFidelite;
    }


    /**
     * Permet à l'utilisateur d'acheter un produit en échange de points.
     * L'achat n’est validé que si l’utilisateur dispose des points nécessaires.
     */
    public boolean acheterProduits(CategorieProduit produit) {
        if (produit.estEligible(ptsFidelite)) {
            ptsFidelite -= produit.getPointNecessaire();
            return true;
        }
        return false;
    }

    /**
     * Ajoute une poubelle accessible à l'utilisateur.
     */
    public void ajouterPoubelleAccessible(Poubelle p) {
        if (!poubellesAccessibles.contains(p)) {
            poubellesAccessibles.add(p);
        }
    }

    /**
     * Renvoie la liste des poubelles accessibles à cet utilisateur.
     */
    public List<Poubelle> getPoubellesAccessibles() {
        return poubellesAccessibles;
    }

    /**
     * Ajoute un certain nombre de points de fidélité à l'utilisateur.
     * @param points Le nombre de points à ajouter.
     */
    public void ajouterPoints(int points) {
        this.ptsFidelite += points;
    }
}
