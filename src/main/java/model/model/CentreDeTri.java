package model;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

/**
 * Représente un centre de tri responsable de la gestion des poubelles,
 * du traitement des déchets et du suivi des commerces partenaires.
 */
public class CentreDeTri {

    // ========== ATTRIBUTS ==========

    /** Identifiant unique du centre */
    private int id;

    /** Nom du centre de tri */
    private String nom;

    /** Adresse du centre de tri */
    private String adresse;

    /** Liste des poubelles associées au centre */
    private List<Poubelle> listePoubelle;

    /** Liste des commerces partenaires du centre */
    private List<Commerce> listeCommerce;

    /** Liste des quartiers desservis par le centre */
    private List<String> quartiersDesservis;

    /** Historique des dépôts rejetés ou invalides */
    private List<Depot> historiqueRejets;

    // ========== CONSTRUCTEUR ==========

    public CentreDeTri(int id, String nom, String adresse) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
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
     * Déclenche la collecte des déchets dans toutes les poubelles du centre.
     */
    public void collecterDechets() {
        for (Poubelle p : listePoubelle) {
            for (Depot d : p.getHistoriqueDepots()) {
                p.ajouterDechets(d);
            }
        }
    }

    /**
     * Génére des statistiques globales sur les déchets traités par le centre.
     */
    public void genererStatistiques() {
        int totalDepots = 0;
        int quantiteTotale = 0;

        for (Poubelle p : listePoubelle) {
            List<Depot> depots = p.getHistoriqueDepots();
            totalDepots += depots.size();
            for (Depot d : depots) {
                quantiteTotale += d.getQuantite();
            }
        }

        double moyenneDepotsParPoubelle = listePoubelle.isEmpty() ? 0 : (double) totalDepots / listePoubelle.size();

        System.out.println("=== Statistiques Globales ===");
        System.out.println("Nombre total de dépôts : " + totalDepots);
        System.out.println("Quantité totale déposée : " + quantiteTotale + " kg");
        System.out.println("Moyenne de dépôts par poubelle : " + String.format("%.2f", moyenneDepotsParPoubelle));
    }

    /**
     * Analyse les dépôts effectués selon les quartiers desservis.
     */
    public void analyserDepotsParQuartier() {
        Map<String, Integer> depotsParQuartier = new HashMap<>();

        for (Poubelle p : listePoubelle) {
            String quartier = p.getEmplacement();
            int total = depotsParQuartier.getOrDefault(quartier, 0);

            for (Depot d : p.getHistoriqueDepots()) {
                total += d.getQuantite();
            }

            depotsParQuartier.put(quartier, total);
        }

        System.out.println("=== Analyse par Quartier ===");
        for (Map.Entry<String, Integer> entry : depotsParQuartier.entrySet()) {
            System.out.println("Quartier : " + entry.getKey() + " -> " + entry.getValue() + " kg déposés");
        }
    }

    /**
     * Analyse les dépôts effectués en fonction du type de déchets.
     */
    public void analyserDepotsParType() {
        Map<NatureDechet, Integer> totalParType = new EnumMap<>(NatureDechet.class);

        for (Poubelle p : listePoubelle) {
            for (Depot d : p.getHistoriqueDepots()) {
                NatureDechet type = d.getType();
                int total = totalParType.getOrDefault(type, 0);
                total += d.getQuantite();
                totalParType.put(type, total);
            }
        }

        System.out.println("=== Analyse par Type de Déchet ===");
        for (Map.Entry<NatureDechet, Integer> entry : totalParType.entrySet()) {
            System.out.println("Type : " + entry.getKey() + " -> " + entry.getValue() + " kg déposés");
        }
    }

    /**
     * Traite les dépôts non conformes ou rejetés par les poubelles.
     * Les dépôts rejetés sont enregistrés dans l'historique de rejets du centre.
     */
    public void traiterRejet() {
        for (Poubelle p : listePoubelle) {
            List<Depot> depots = p.getHistoriqueDepots();
            List<Depot> rejets = new ArrayList<>();

            for (Depot d : depots) {
                if (!p.verifierTypeDechets(d.getType())) {
                    rejets.add(d);
                }
            }

            // Retire les dépôts rejetés de la poubelle
            depots.removeAll(rejets);

            // Enregistre les rejets dans l'historique du centre
            historiqueRejets.addAll(rejets);
        }
    }

    // ========== GETTERS ==========

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
     * Renvoie la liste des poubelles gérées par ce centre.
     */
    public List<Poubelle> getPoubelle() {
        return listePoubelle;
    }

    /**
     * Renvoie la liste des commerces associés à ce centre.
     */
    public List<Commerce> getCommerce() {
        return listeCommerce;
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
