package model;

import java.util.Arrays;
import java.util.List;

/**
 * Enumération des types de poubelles, chacun ayant une couleur et une liste
 * de types de déchets autorisés.
 */
public enum TypePoubelle {

    // === TYPES DE POUBELLE ===

    JAUNE(Arrays.asList(NatureDechet.PLASTIQUE, NatureDechet.CARTON, NatureDechet.METAL)),
    VERT(Arrays.asList(NatureDechet.VERRE)),
    BLEU(Arrays.asList(NatureDechet.PAPIER)),
    GRIS(Arrays.asList(NatureDechet.PLASTIQUE, NatureDechet.METAL, NatureDechet.VERRE, NatureDechet.CARTON, NatureDechet.PAPIER)),
    MARRON(Arrays.asList()); // déchets organiques, non précisés ici

    // === ATTRIBUTS ===

    private final List<NatureDechet> typesAcceptes;

    // === CONSTRUCTEUR ===

    TypePoubelle(List<NatureDechet> typesAcceptes) {
        this.typesAcceptes = typesAcceptes;
    }

    // === ACCESSEUR ===

    /**
     * Retourne la liste des types de déchets acceptés par cette poubelle.
     */
    public List<NatureDechet> getTypesAcceptes() {
        return typesAcceptes;
    }

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
