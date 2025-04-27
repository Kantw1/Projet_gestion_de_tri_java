package ihm;

import dao.UtilisateurDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.Utilisateur;
import utils.DatabaseConnection;

public class HelloController {

    @FXML
    private TextField codeAccesField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            int codeAcces = Integer.parseInt(codeAccesField.getText());
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(DatabaseConnection.getConnection());
            Utilisateur utilisateur = utilisateurDAO.getByCodeAcces(codeAcces);

            if (utilisateur != null) {
                // Connexion réussie → Changer de scène vers DepotView.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DepotView.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) codeAccesField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                errorLabel.setText("Code d'accès incorrect.");
            }

        } catch (NumberFormatException e) {
            errorLabel.setText("Veuillez entrer un code valide (nombre).");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erreur lors de la connexion.");
        }
    }
}
