package ihm;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminController {

    @FXML
    private Label infoLabel;

    @FXML
    private void handleVoirUtilisateurs() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ListeUtilisateursView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Liste des utilisateurs");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleAjouterUtilisateur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AjouterAdminView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Administrateur");
            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(HelloApplication.class.getResource("/style.css").toExternalForm());

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVoirCentres() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ListeCentresView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) infoLabel.getScene().getWindow();
            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(HelloApplication.class.getResource("/style.css").toExternalForm());

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            infoLabel.setText("Erreur lors de l'ouverture de la liste des centres !");
        }
    }
    @FXML
    private void handleVoirContrats() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ListeContratsView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Liste des Contrats de Partenariat");
            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(HelloApplication.class.getResource("/style.css").toExternalForm());

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleDeconnexion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/hello-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) infoLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(HelloApplication.class.getResource("/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
