package ihm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Utilisateur;
import dao.UtilisateurDAO;
import utils.DatabaseConnection;
import java.io.IOException;

public class InscriptionController {

    @FXML
    private TextField nomField, codeAccesField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleInscription(ActionEvent event) {
        try {
            // Récupérer les informations d'inscription
            String nom = nomField.getText();
            int codeAcces = Integer.parseInt(codeAccesField.getText());

            // Créer un utilisateur avec un ID fictif (par exemple, 0) pour l'inscription
            Utilisateur utilisateur = new Utilisateur(0, nom, codeAcces);

            // Insertion de l'utilisateur dans la base de données
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(DatabaseConnection.getConnection());
            utilisateurDAO.insert(utilisateur);

            // Redirection vers la page de connexion après inscription
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/hello-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomField.getScene().getWindow();
            Scene scene = new Scene(root);

            // Appliquer la même taille que la fenêtre de connexion (500x400)
            stage.setScene(scene);
            stage.setWidth(500);  // Largeur de la fenêtre
            stage.setHeight(400); // Hauteur de la fenêtre

            // Affichage de la nouvelle scène
            stage.show();
        } catch (NumberFormatException e) {
            errorLabel.setText("Veuillez entrer un code valide (nombre).");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erreur lors de l'inscription.");
        }
    }
    @FXML
    private void handleGoToConnexion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/hello-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomField.getScene().getWindow();
            Scene scene = new Scene(root);

            // Taille identique à celle de connexion
            stage.setScene(scene);
            stage.setWidth(500);
            stage.setHeight(400);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Erreur lors du retour à la connexion.");
        }
    }

}
