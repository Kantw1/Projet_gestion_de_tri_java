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
    private int centreId;

    private List<Depot> historiqueDepots;
    private List<Poubelle> poubellesAccessibles;

    // === Constructeurs ===

    /**
     * Constructeur utilisé pour un utilisateur simple (par défaut le rôle est "utilisateur")
     */
    public Utilisateur(int id, String nom, int codeAcces, int centreId) {
        this(id, nom, codeAcces, "utilisateur", centreId);
    }

    /**
     * Constructeur complet, avec rôle spécifié (utilisateur ou admin).
     */
    public Utilisateur(int id, String nom, int codeAcces, String role, int centreId) {
        this.id = id;
        this.nom = nom;
        this.codeAcces = codeAcces;
        this.role = role;
        this.ptsFidelite = 0;
        this.historiqueDepots = new ArrayList<>();
        this.poubellesAccessibles = new ArrayList<>();
        this.centreId = centreId;
    }

    // === Logique métier ===

    public void deposerDechets(Poubelle p, Depot d) {
        if (p.accepterDepot(d, this)) {
            historiqueDepots.add(d);
            ptsFidelite += d.getPoints();
        }
    }

    public BonDeCommande convertirPoints(CategorieProduit categorie) {
        if (categorie != null && categorie.estEligible(ptsFidelite)) {
            ptsFidelite -= categorie.getPointNecessaire();
            return new BonDeCommande(0, this, categorie, java.time.LocalDate.now(), categorie.getPointNecessaire());
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

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getCentreId() {
        return centreId;
    }

    public void setCentreId(int centreId) {
        this.centreId = centreId;
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
