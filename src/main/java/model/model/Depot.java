package model;

import java.time.LocalDateTime;

/**
 * Représente un dépôt de déchets effectué par un utilisateur dans une poubelle.
 * Chaque dépôt a un type, une quantité, un poids et génère des points fidélité.
 */
public class Depot {

    // ================= ATTRIBUTS =================

    /** Identifiant unique du dépôt */
    private int id;

    /** Type du déchet déposé */
    private NatureDechet type;

    /** Poids du déchet déposé (en kg) */
    private float poids;

    /** Quantité d'unités déposées */
    private int quantite;

    /** Date et heure du dépôt */
    private LocalDateTime heureDepot;

    /** Nombre de points attribués pour ce dépôt */
    private int points;

    /** Poubelle utilisée */
    private Poubelle poubelle;

    /** Utilisateur qui a déposé */
    private Utilisateur utilisateur;

    // ================= CONSTRUCTEUR =================

    public Depot(int id, NatureDechet type, float poids, int quantite,
                 LocalDateTime heureDepot, Poubelle poubelle, Utilisateur utilisateur) {
        this.id = id;
        this.type = type;
        this.poids = poids;
        this.quantite = quantite;
        this.heureDepot = heureDepot;
        this.poubelle = poubelle;
        this.utilisateur = utilisateur;
        this.points = calculerPointsAttribues(); // auto-calcul au moment du dépôt
    }

    // ================= MÉTHODES GETTERS =================

    public int getId() {
        return id;
    }

    public NatureDechet getType() {
        return type;
    }

    public float getPoids() {
        return poids;
    }

    public int getQuantite() {
        return quantite;
    }

    public LocalDateTime getHeureDepot() {
        return heureDepot;
    }

    public int getPoints() {
        return points;
    }

    public Poubelle getPoubelle() {
        return poubelle;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    // ================= MÉTHODES UML =================

    /**
     * Retourne la quantité de déchets déposés (simple wrapper ici).
     */
    public int calculerQuantiteDechets() {
        return quantite;
    }

    /**
     * Vérifie que le type de déchet est accepté par la poubelle utilisée.
     */
    public boolean verifierTypeDechet() {
        return poubelle.getTypePoubelle().getTypesAcceptes().contains(type);
    }

    /**
     * Vérifie que le dépôt est conforme à une poubelle donnée :
     * accès autorisé + type de déchet autorisé.
     */
    public boolean verifierConformite(Poubelle p) {
        return p.verifierAcces(utilisateur.getCodeAcces()) &&
                p.getTypePoubelle().getTypesAcceptes().contains(type);
    }

    /**
     * Renvoie une chaîne lisible décrivant ce dépôt.
     */
    public String afficherDepot() {
        return "Dépôt #" + id +
                " | Type: " + type +
                ", Quantité: " + quantite +
                ", Poids: " + poids + "kg" +
                ", Points: " + points +
                ", Date: " + heureDepot;
    }

    /**
     * Calcule les points a attribuer à l'utilisateur pour ce dépôt,
     * en fonction de la nature du déchet.
     * Bareme, on a utilise l'exemple dans le sujet :
     * - Plastique : 2 pts / unité
     * - Verre : 3 pts / unité
     * - Carton : 1 pt / unité
     * - Métal : 4 pts / unité
     * - Papier : 1 pt / unité
     */
    private int calculerPointsAttribues() {
        int pointsParUnite = switch (type) {
            case model.NatureDechet.PLASTIQUE -> 2;
            case model.NatureDechet.VERRE     -> 3;
            case model.NatureDechet.CARTON    -> 1;
            case model.NatureDechet.METAL     -> 4;
            case model.NatureDechet.PAPIER    -> 1;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
        return quantite * pointsParUnite;
    }
}
