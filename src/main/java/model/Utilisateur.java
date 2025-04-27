package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un utilisateur qui peut effectuer des dépôts de déchets,
 * accumuler des points de fidélité et accéder à certaines poubelles autorisées.
 */
public class Utilisateur {

    private int id;
    private String nom;
    private int ptsFidelite;
    private int codeAcces;
    private String role; // 'utilisateur' ou 'admin'

    private List<Depot> historiqueDepots;
    private List<Poubelle> poubellesAccessibles;

    // === Constructeurs ===

    public Utilisateur(int id, String nom, int codeAcces) {
        this(id, nom, codeAcces, "utilisateur"); // par défaut utilisateur simple
    }

    public Utilisateur(int id, String nom, int codeAcces, String role) {
        this.id = id;
        this.nom = nom;
        this.codeAcces = codeAcces;
        this.role = role;
        this.ptsFidelite = 0;
        this.historiqueDepots = new ArrayList<>();
        this.poubellesAccessibles = new ArrayList<>();
    }

    // === Logique métier ===

    public void deposerDechets(Poubelle p, Depot d) {
        if (p.accepterDepot(d, this)) {
            historiqueDepots.add(d);
            ptsFidelite += d.getPoints();
        }
    }

    public BonDeCommande convertirPoints(int points) {
        if (points > 0 && points <= ptsFidelite) {
            ptsFidelite -= points;

            BonDeCommande commande = new BonDeCommande(0, this, new ArrayList<>(), null);
            commande.setEtatCommande("validée");
            commande.setPointsUtilises(points);

            return commande;
        }
        return null;
    }

    public boolean acheterProduits(CategorieProduit produit) {
        if (produit.estEligible(ptsFidelite)) {
            ptsFidelite -= produit.getPointNecessaire();
            return true;
        }
        return false;
    }

    public void ajouterPoints(int points) {
        this.ptsFidelite += points;
    }

    public void ajouterPoubelleAccessible(Poubelle p) {
        if (!poubellesAccessibles.contains(p)) {
            poubellesAccessibles.add(p);
        }
    }

    // === Getters & Setters ===

    public int getId() {
        return id;
    }

    /** Permet de définir l'ID après insertion en base de données. */
    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPtsFidelite() {
        return ptsFidelite;
    }

    public void setPtsFidelite(int ptsFidelite) {
        this.ptsFidelite = ptsFidelite;
    }

    public int getCodeAcces() {
        return codeAcces;
    }

    public List<Depot> getHistoriqueDepots() {
        return historiqueDepots;
    }

    public List<Poubelle> getPoubellesAccessibles() {
        return poubellesAccessibles;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
