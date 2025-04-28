package ihm;

import dao.DepotDAO;
import dao.HistoriqueDepotDAO;
import dao.PoubelleDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.*;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur pour la page de dépôt des déchets.
 * Gère la sélection de la poubelle et le calcul des points en fonction du tri.
 */
public class DepotController {

    @FXML
    private ComboBox<String> poubelleComboBox;

    @FXML
    private TextField plastiqueField, verreField, cartonField, metalField;

    @FXML
    private Label resultLabel;

    private Map<String, Poubelle> emplacementPoubelleMap = new HashMap<>();

    private Utilisateur utilisateurConnecte;

    /**
     * Cette méthode est appelée automatiquement au chargement du FXML,
     * mais on ne charge pas encore les poubelles ici.
     */
    @FXML
    public void initialize() {
        // On ne fait rien ici pour le moment
        // Car l'utilisateur connecté n'est pas encore passé au contrôleur
    }

    /**
     * Setter pour passer l'utilisateur connecté au contrôleur.
     * Dès que l'utilisateur est connu, on charge ses poubelles.
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        chargerPoubellesDuCentre();
    }

    /**
     * Charge les poubelles du centre associé à l'utilisateur connecté.
     */
    private void chargerPoubellesDuCentre() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PoubelleDAO poubelleDAO = new PoubelleDAO(conn);
            List<Poubelle> poubelles = poubelleDAO.getPoubellesByCentreId(utilisateurConnecte.getCentreId());

            for (Poubelle p : poubelles) {
                emplacementPoubelleMap.put(p.getEmplacement(), p);
                poubelleComboBox.getItems().add(p.getEmplacement());
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultLabel.setText("Erreur chargement des poubelles.");
        }
    }

    /**
     * Lors du clic sur "Valider dépôt", calcule les points obtenus selon la poubelle choisie et les déchets déposés.
     */
    @FXML
    public void validerDepot() {
        String emplacementChoisi = poubelleComboBox.getValue();
        if (emplacementChoisi == null) {
            resultLabel.setText("Veuillez choisir une poubelle.");
            resultLabel.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        Poubelle poubelle = emplacementPoubelleMap.get(emplacementChoisi);

        int plastique = parseField(plastiqueField.getText());
        int verre = parseField(verreField.getText());
        int carton = parseField(cartonField.getText());
        int metal = parseField(metalField.getText());

        int pointsTotaux = 0; // on va additionner tous les points

        try (Connection conn = DatabaseConnection.getConnection()) {
            DepotDAO depotDAO = new DepotDAO(conn);
            HistoriqueDepotDAO historiqueDepotDAO = new HistoriqueDepotDAO(conn);

            // Plastique
            if (plastique > 0) {
                pointsTotaux += deposerDechet(depotDAO, historiqueDepotDAO, NatureDechet.PLASTIQUE, plastique, poubelle, utilisateurConnecte);
            }

            // Verre
            if (verre > 0) {
                pointsTotaux += deposerDechet(depotDAO, historiqueDepotDAO, NatureDechet.VERRE, verre, poubelle, utilisateurConnecte);
            }

            // Carton
            if (carton > 0) {
                pointsTotaux += deposerDechet(depotDAO, historiqueDepotDAO, NatureDechet.CARTON, carton, poubelle, utilisateurConnecte);
            }

            // Métal
            if (metal > 0) {
                pointsTotaux += deposerDechet(depotDAO, historiqueDepotDAO, NatureDechet.METAL, metal, poubelle, utilisateurConnecte);
            }

            // Affichage final des points
            resultLabel.setText("Points obtenus : " + pointsTotaux);
            resultLabel.setTextFill(pointsTotaux >= 0 ? javafx.scene.paint.Color.GREEN : javafx.scene.paint.Color.RED);

        } catch (Exception e) {
            e.printStackTrace();
            resultLabel.setText("Erreur lors du dépôt.");
            resultLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }


    /**
     * Utilitaire pour parser proprement un champ texte en entier.
     */
    private int parseField(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Calcule les points en fonction du type de poubelle et des déchets déposés.
     */
    private int calculerPoints(TypePoubelle typePoubelle, int plastique, int verre, int carton, int metal) {
        switch (typePoubelle) {
            case JAUNE:
                // Jaune accepte plastique, carton, métal ➔ bonus
                return plastique * 2 + carton * 2 + metal * 2 - verre * 3;
            case VERTE:
                // Verte accepte verre uniquement ➔ bonus pour verre, malus pour le reste
                return verre * 3 - (plastique + carton + metal);
            case BLEU:
                // Bleu accepte papier uniquement (ici assimilé à carton dans ton calcul pour l'instant)
                return carton * 2 - (plastique + verre + metal);
            case GRIS:
                // Gris accepte tout ➔ pas de malus, tout est accepté
                return plastique + verre + carton + metal;
            default:
                return 0;
        }
    }

    /**
     * Dépose un déchet, calcule les points, insère dans la BDD et retourne les points gagnés.
     */
    private int deposerDechet(DepotDAO depotDAO, HistoriqueDepotDAO historiqueDepotDAO, NatureDechet type, int quantite, Poubelle poubelle, Utilisateur utilisateur) throws Exception {
        float poids = (float) (quantite * type.getPoidsUnitaire());

        Depot depot = new Depot(
                0,
                type,
                poids,
                quantite,
                java.time.LocalDateTime.now(),
                poubelle,
                utilisateur
        );

        depotDAO.insert(depot); // 🔥 On insère et récupère l'ID

        // Maintenant qu'on a l'ID généré, on insère l'historique
        //historiqueDepotDAO.insert(utilisateur.getId(), depot.getId());

        return depot.getPoints();
    }


}
