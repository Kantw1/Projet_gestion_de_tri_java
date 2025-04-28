package ihm;

import dao.CommerceDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Commerce;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class AjouterCommerceController {

    @FXML
    private TextField nomField;

    private CommerceDAO commerceDAO;

    @FXML
    private void initialize() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            commerceDAO = new CommerceDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleValider() {
        String nom = nomField.getText();

        if (nom.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom du commerce doit être renseigné.");
            return;
        }

        try {
            Commerce commerce = new Commerce(0, nom, null); // ✅ Pas de centre associé
            commerceDAO.insert(commerce);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Commerce ajouté avec succès !");
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur BDD", "Impossible d'ajouter le commerce.");
        }
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
