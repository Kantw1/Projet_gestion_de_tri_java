package ihm;

import dao.DepotDAO;
import dao.HistoriqueDepotDAO;
import dao.UtilisateurDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.*;
import utils.DatabaseConnection;

import java.sql.Connection;

/**
 * Contrôleur pour la page de dépôt : dépôt de déchets dans une poubelle spécifique.
 */
public class PageDepotController {

    @FXML
    private Label titreDepotLabel;

    @FXML
    private Label poubelleInfoLabel;

    @FXML
    private TextField plastiqueField, verreField, cartonField, metalField;

    @FXML
    private Label resultLabel;

    private Utilisateur utilisateur;
    private Poubelle poubelle;

    public void setUtilisateurEtPoubelle(Utilisateur utilisateur, Poubelle poubelle) {
        this.utilisateur = utilisateur;
        this.poubelle = poubelle;
        initialiserPage();
    }

    private void initialiserPage() {
        titreDepotLabel.setText("Déposer dans : " + poubelle.getEmplacement());
        poubelleInfoLabel.setText("Type de poubelle : " + poubelle.getTypePoubelle().toString());
    }

    @FXML
    public void validerDepot() {
        int plastique = parseField(plastiqueField.getText());
        int verre = parseField(verreField.getText());
        int carton = parseField(cartonField.getText());
        int metal = parseField(metalField.getText());

        int pointsTotaux = 0;

        try (Connection conn = DatabaseConnection.getConnection()) {
            DepotDAO depotDAO = new DepotDAO(conn);
            HistoriqueDepotDAO historiqueDepotDAO = new HistoriqueDepotDAO(conn);
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(conn);

            if (plastique > 0) {
                pointsTotaux += deposerDechet(depotDAO, historiqueDepotDAO, NatureDechet.PLASTIQUE, plastique);
            }
            if (verre > 0) {
                pointsTotaux += deposerDechet(depotDAO, historiqueDepotDAO, NatureDechet.VERRE, verre);
            }
            if (carton > 0) {
                pointsTotaux += deposerDechet(depotDAO, historiqueDepotDAO, NatureDechet.CARTON, carton);
            }
            if (metal > 0) {
                pointsTotaux += deposerDechet(depotDAO, historiqueDepotDAO, NatureDechet.METAL, metal);
            }

            // points de fidélité de l'utilisateur en base
            utilisateur.ajouterPoints(pointsTotaux);
            utilisateurDAO.updateFidelite(utilisateur.getId(), utilisateur.getPtsFidelite());

            resultLabel.setText("Points obtenus : " + pointsTotaux);
            resultLabel.setTextFill(pointsTotaux >= 0 ? javafx.scene.paint.Color.GREEN : javafx.scene.paint.Color.RED);

        } catch (Exception e) {
            e.printStackTrace();
            resultLabel.setText("Erreur lors du dépôt.");
            resultLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }

    private int parseField(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private int deposerDechet(DepotDAO depotDAO, HistoriqueDepotDAO historiqueDepotDAO, NatureDechet type, int quantite) throws Exception {
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

        depotDAO.insert(depot);

        //Calcul des points : on prend les points du depot
        int basePoints = depot.getPoints(); // 2, 3, 4, etc. selon type

        // On vérifie si le type de déchet est accepté par la poubelle
        boolean dechetConforme = depot.verifierTypeDechet();

        if (dechetConforme) {
            return basePoints; // Bonus normal
        } else {
            return -basePoints; // perte des points de base
        }
    }
}
