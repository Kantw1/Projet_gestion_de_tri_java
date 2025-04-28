package model;

import java.time.LocalDateTime;

/**
 * Représente un dépôt de déchets effectué par un utilisateur dans une poubelle.
 * Chaque dépôt a un type, un poids, une quantité et génère des points de fidélité.
 */
public class Depot {

    // ========== ATTRIBUTS ==========

    private int id;
    private NatureDechet type;
    private float poids;
    private int quantite;
    private LocalDateTime heureDepot;
    private int points;
    private Poubelle poubelle;
    private Utilisateur utilisateur;

    // ========== CONSTRUCTEUR ==========

    public Depot(int id, NatureDechet type, float poids, int quantite,
                 LocalDateTime heureDepot, Poubelle poubelle, Utilisateur utilisateur) {
        this.id = id;
        this.type = type;
        this.poids = poids;
        this.quantite = quantite;
        this.heureDepot = heureDepot;
        this.poubelle = poubelle;
        this.utilisateur = utilisateur;
        this.points = calculerPointsAttribues(); // Calcul automatique
    }

    // ========== GETTERS/SETTERS ==========

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

    public LocalDateTime getDateDepot() {
        return heureDepot;
    }

    public void setId(int id) {this.id = id;}

    public int getPoints() {
        return points;
    }

    public Poubelle getPoubelle() {
        return poubelle;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    // ========== MÉTHODES MÉTIERS ==========

    public int calculerQuantiteDechets() {
        return quantite;
    }

    public boolean verifierTypeDechet() {
        return poubelle.getTypePoubelle().getTypesAcceptes().contains(type);
    }

    public boolean verifierConformite(Poubelle p) {
        return p.verifierAcces(utilisateur.getCodeAcces())
                && p.getTypePoubelle().getTypesAcceptes().contains(type);
    }

    public String afficherDepot() {
        return "Dépôt #" + id +
                " | Type: " + type +
                ", Quantité: " + quantite +
                ", Poids: " + poids + "kg" +
                ", Points: " + points +
                ", Date: " + heureDepot;
    }

    /**
     * Calcule les points attribués en fonction du type de déchet.
     */
    private int calculerPointsAttribues() {
        int pointsParUnite = switch (type) {
            case PLASTIQUE -> 2;
            case VERRE     -> 3;
            case CARTON    -> 1;
            case METAL     -> 4;
            case PAPIER    -> 1;
            default -> throw new IllegalStateException("Type de déchet inconnu : " + type);
        };
        return quantite * pointsParUnite;
    }
}
