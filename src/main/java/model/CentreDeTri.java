package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un centre de tri responsable de la gestion des poubelles,
 * du traitement des déchets et du suivi des commerces partenaires.
 */
public class CentreDeTri {

    // ========== ATTRIBUTS ==========

    /** Nom du centre de tri */
    private String nom;

    /** Adresse du centre de tri */
    private String adresse;

    /** Liste des poubelles associées au centre */
    private List<Poubelle> listePoubelle;

    /** Liste des commerces partenaires du centre */
    private List<Commerce> listeCommerce;

    /** Identifiant unique du centre */
    private int id;

    /** Liste des quartiers desservis par le centre */
    private List<String> quartiersDesservis;

    /** Historique des dépôts rejetés ou invalides */
    private List<Depot> historiqueRejets;

    // ========== CONSTRUCTEUR ==========

    public CentreDeTri(String nom, String adresse, int id) {
        this.nom = nom;
        this.adresse = adresse;
        this.id = id;
        this.listePoubelle = new ArrayList<>();
        this.listeCommerce = new ArrayList<>();
        this.quartiersDesservis = new ArrayList<>();
        this.historiqueRejets = new ArrayList<>();
    }

    // ========== MÉTHODES UML ==========

    /**
     * Ajoute une poubelle à la liste des poubelles du centre.
     */
    public void ajouterPoubelle(Poubelle p) {
        if (!listePoubelle.contains(p)) {
            listePoubelle.add(p);
        }
    }

    /**
     * Déclenche la collecte des déchets dans toutes les poubelles du centre.
     */
    public void collecterDechets() {
        for (Poubelle p : listePoubelle) {
            p.ajouterDechets(); // méthode appelée pour simuler une collecte
        }
    }

    /**
     * Génére des statistiques globales sur les déchets traités par le centre.
     */
    public void genererStatistiques() {
        // Implémentation à définir selon les besoins d'analyse
    }

    /**
     * Traite les dépôts non conformes ou rejetés par les poubelles.
     */
    public void traiterRejet() {
        for (Poubelle p : listePoubelle) {
            // Traitement éventuel des dépôts invalides
        }
    }

    /**
     * Renvoie la liste des commerces associés à ce centre.
     */
    public List<Commerce> getCommerce() {
        return listeCommerce;
    }

    /**
     * Renvoie la liste des poubelles gérées par ce centre.
     */
    public List<Poubelle> getPoubelle() {
        return listePoubelle;
    }

    /**
     * Retire une poubelle de la gestion du centre.
     */
    public void retirerPoubelle(Poubelle p) {
        listePoubelle.remove(p);
    }

    /**
     * Ajoute un commerce partenaire au centre.
     */
    public void ajouterCommerce(Commerce c) {
        if (!listeCommerce.contains(c)) {
            listeCommerce.add(c);
        }
    }

    /**
     * Retire un commerce partenaire du centre.
     */
    public void retirerCommerce(Commerce c) {
        listeCommerce.remove(c);
    }

    /**
     * Analyse les dépôts effectués selon les quartiers desservis.
     */
    public void analyserDepotsParQuartier() {
        // Implémentation à compléter pour analyse par secteur
    }

    /**
     * Analyse les dépôts effectués en fonction du type de déchets.
     */
    public void analyserDepotsParType() {
        // Implémentation à compléter pour analyse par nature de déchet
    }

    // ========== GETTERSS ==========

    /**
     * Renvoie l'identifiant du centre.
     */
    public int getId() {
        return id;
    }

    /**
     * Renvoie le nom du centre.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Renvoie l'adresse du centre.
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Renvoie les quartiers desservis par le centre.
     */
    public List<String> getQuartiersDesservis() {
        return quartiersDesservis;
    }

    /**
     * Renvoie l'historique des rejets.
     */
    public List<Depot> getHistoriqueRejets() {
        return historiqueRejets;
    }
}
