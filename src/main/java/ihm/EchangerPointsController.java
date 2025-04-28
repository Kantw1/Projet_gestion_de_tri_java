package ihm;

import dao.UtilisateurDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.BonDeCommande;
import model.Utilisateur;
import utils.DatabaseConnection;

import java.sql.Connection;

public class EchangerPointsController {

    @FXML
    private TextField pointsField;

    @FXML
    private Label resultLabel;

    private Utilisateur utilisateurConnecte;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
    }

    @FXML
    private void handleEchanger() {
        try {
            int points = Integer.parseInt(pointsField.getText());
            if (points <= 0 || points > utilisateurConnecte.getPtsFidelite()) {
                resultLabel.setText("Points invalides ou insuffisants.");
                return;
            }

            BonDeCommande bon = utilisateurConnecte.convertirPoints(points);
            if (bon != null) {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    UtilisateurDAO utilisateurDAO = new UtilisateurDAO(conn);
                    utilisateurDAO.updateFidelite(utilisateurConnecte.getId(), utilisateurConnecte.getPtsFidelite());
                }
                resultLabel.setText("Points échangés avec succès !");
            } else {
                resultLabel.setText("Échange impossible.");
            }

        } catch (NumberFormatException e) {
            resultLabel.setText("Entrez un nombre valide.");
        } catch (Exception e) {
            e.printStackTrace();
            resultLabel.setText("Erreur pendant l'échange.");
        }
    }
}
