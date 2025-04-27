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
    private TextField nomField;

    @FXML
    private TextField codeAccesField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            String nom = nomField.getText().trim();
            int codeAcces = Integer.parseInt(codeAccesField.getText().trim());

            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(DatabaseConnection.getConnection());
            Utilisateur utilisateur = utilisateurDAO.getByNomAndCodeAcces(nom, codeAcces);

            if (utilisateur != null) {
                Stage stage = (Stage) nomField.getScene().getWindow();
                FXMLLoader loader;

                if ("admin".equalsIgnoreCase(utilisateur.getRole())) {
                    loader = new FXMLLoader(getClass().getResource("/views/AdminView.fxml"));
                } else {
                    loader = new FXMLLoader(getClass().getResource("/views/DepotView.fxml"));
                }

                Parent root = loader.load();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                errorLabel.setText("Nom ou code d'accès incorrect.");
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Veuillez entrer un code valide (nombre).");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erreur lors de la connexion.");
        }
    }


    @FXML
    private void handleInscriptionRedirection(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/InscriptionView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) codeAccesField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erreur lors de la redirection vers l'inscription.");
        }
    }
}
