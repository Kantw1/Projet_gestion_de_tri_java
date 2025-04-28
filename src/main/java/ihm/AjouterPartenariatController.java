package ihm;

import dao.CommerceDAO;
import dao.ContratPartenariatDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.CentreDeTri;
import model.Commerce;
import model.ContratPartenariat;
import utils.DatabaseConnection;
import java.util.ArrayList;


import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AjouterPartenariatController {

    @FXML
    private ComboBox<Commerce> commerceCombo;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private DatePicker dateFinPicker;

    private CentreDeTri centre; // centre sur lequel on travaille

    private CommerceDAO commerceDAO;
    private ContratPartenariatDAO contratDAO;

    @FXML
    private void handleValider() {
        Commerce selectedCommerce = commerceCombo.getValue();
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();

        if (selectedCommerce == null || dateDebut == null || dateFin == null) {
            showAlert(Alert.AlertType.ERROR, "Champs incomplets", "Veuillez remplir tous les champs !");
            return;
        }

        try {
            ContratPartenariat newContrat = new ContratPartenariat(0, dateDebut, dateFin, centre, selectedCommerce);
            contratDAO.insert(newContrat);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Contrat ajouté avec succès !");
            // Fermer la fenêtre
            Stage stage = (Stage) commerceCombo.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout du contrat !");
        }
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void initialize() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            commerceDAO = new CommerceDAO(conn);
            contratDAO = new ContratPartenariatDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCentre(CentreDeTri centre) {
        this.centre = centre;
        try {
            List<Commerce> commercesDisponibles = commerceDAO.getAvailableForCentre(centre);
            commerceCombo.setItems(FXCollections.observableArrayList(commercesDisponibles));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
