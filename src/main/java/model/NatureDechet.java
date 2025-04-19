package model;

public enum NatureDechet {
    PLASTIQUE(0.3),  // pp
    VERRE(0.5),      // pv
    CARTON(0.2),     // pc
    METAL(0.6),      // pm
    PAPIER(0.1);     // pour poubelle BLEUE

    private final double poidsUnitaire;

    NatureDechet(double poidsUnitaire) {
        this.poidsUnitaire = poidsUnitaire;
    }

    public double getPoidsUnitaire() {
        return poidsUnitaire;
    }

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
