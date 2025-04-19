package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une poubelle intelligente capable de gérer les dépôts de déchets.
 * Elle contrôle l'accès, les types de déchets autorisés, et calcule les taux de remplissage et pénalités.
 */
public class Poubelle {

    // ========== ATTRIBUTS ==========

    /** Identifiant unique de la poubelle */
    private int id;

    /** Capacité maximale de la poubelle (en unités de quantité) */
    private int capaciteMax;

    /** Emplacement géographique de la poubelle */
    private String emplacement;

    /** Type de poubelle (ex: Bleue, Jaune, etc.) */
    private TypePoubelle typePoubelle;

    /** Quantité actuelle de déchets présents dans la poubelle */
    private int quantiteActuelle;

    /** Liste des dépôts effectués dans cette poubelle */
    private List<Depot> historiqueDepots;

    /** Codes d'accès autorisés (accès sans authentification complète) */
    private List<Integer> accesAutorises;

    /** Utilisateurs autorisés à accéder à cette poubelle */
    private List<Utilisateur> utilisateursAutorises;

    /** Seuil à partir duquel une alerte est déclenchée */
    private int seuilAlerte;

    /** Types de déchets autorisés dans cette poubelle */
    private List<NatureDechet> dechetsAutorises;

    // ========== CONSTRUCTEUR ==========

    public Poubelle(int id, int capaciteMax, String emplacement, TypePoubelle typePoubelle, int seuilAlerte) {
        this.id = id;
        this.capaciteMax = capaciteMax;
        this.emplacement = emplacement;
        this.typePoubelle = typePoubelle;
        this.quantiteActuelle = 0;
        this.historiqueDepots = new ArrayList<>();
        this.accesAutorises = new ArrayList<>();
        this.utilisateursAutorises = new ArrayList<>();
        this.dechetsAutorises = typePoubelle.getTypesAcceptes();
        this.seuilAlerte = seuilAlerte;
    }

    // ========== MÉTHODES UML ==========

    /** Verifie si un utilisateur est autorise à accéder à la poubelle */
    public boolean identifierUtilisateur(Utilisateur u) {
        return utilisateursAutorises.contains(u);
    }

    /** Verifie si un déchet est autorisé dans cette poubelle */
    public boolean verifierTypeDechets(NatureDechet type) {
        return dechetsAutorises.contains(type);
    }

    /** Attribue les points de fidélite a un utilisateur pour un depot */
    public void attribuerPoint(Utilisateur u) {
        int total = historiqueDepots.stream()
                .filter(d -> d.getUtilisateur().equals(u))
                .mapToInt(Depot::getPoints)
                .sum();
        u.ajouterPoints(total);
    }

    /** Envoie une alerte au centre de tri si le seuil est depassee */
    public void notifierCentreTri() {
        if (quantiteActuelle >= seuilAlerte) {
            System.out.println("⚠ Alerte : seuil atteint à la poubelle #" + id);
        }
    }

    /** Verifie si le code d'acces fourni est valide */
    public boolean verifierAcces(int code) {
        return accesAutorises.contains(code);
    }

    /** Ajoute un depot à la poubelle */
    public void ajouterDechets(Depot d) {
        historiqueDepots.add(d);
        quantiteActuelle += d.getQuantite();
        notifierCentreTri();
    }

    /** Verifie si la poubelle est pleine */
    public boolean estPleine() {
        return quantiteActuelle >= capaciteMax;
    }

    /** Calcule une penalité en fonction du non-respect des règles de tri */
    public void calculerPenalite(Utilisateur u) {
        System.out.println("Pénalité enregistrée pour l'utilisateur : " + u.getNom());
    }

    /** Accepte un dépôt si tout est conforme */
    public boolean accepterDepot(Depot d, Utilisateur u) {
        if (!verifierAcces(u.getCodeAcces())) return false;
        if (!verifierTypeDechets(d.getType())) return false;
        if (estPleine()) return false;
        ajouterDechets(d);
        attribuerPoint(u);
        return true;
    }

    /** Calcule le taux de remplissage (entre 0 et 1) */
    public float getTauxRemplissage() {
        return (float) quantiteActuelle / capaciteMax;
    }

    // ========== GETTERS ==========

    public int getId() { return id; }

    public int getCapaciteMax() { return capaciteMax; }

    public String getEmplacement() { return emplacement; }

    public TypePoubelle getTypePoubelle() { return typePoubelle; }

    public int getQuantiteActuelle() { return quantiteActuelle; }

    public List<Depot> getHistoriqueDepots() { return historiqueDepots; }

    public List<NatureDechet> getDechetsAutorises() { return dechetsAutorises; }

    public List<Utilisateur> getUtilisateursAutorises() { return utilisateursAutorises; }

    public List<Integer> getAccesAutorises() { return accesAutorises; }

    public int getSeuilAlerte() { return seuilAlerte; }

}
