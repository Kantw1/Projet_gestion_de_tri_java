package ihm;

import dao.CategorieProduitDAO;
import dao.CommerceCategorieProduitDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.CategorieProduit;
import model.Commerce;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class AjouterCategorieController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField pointsField;
    @FXML
    private TextField reductionField;

    private CategorieProduitDAO categorieDAO;
    private CommerceCategorieProduitDAO commerceCategorieDAO;

    private Commerce commerce; // Très important !

    public void setCommerce(Commerce commerce) {
        this.commerce = commerce;
    }

    @FXML
    private void initialize() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            categorieDAO = new CategorieProduitDAO(conn);
            commerceCategorieDAO = new CommerceCategorieProduitDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleValider() {
        String nom = nomField.getText();
        String pointsStr = pointsField.getText();
        String reductionStr = reductionField.getText();

        if (nom.isEmpty() || pointsStr.isEmpty() || reductionStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        try {
            int points = Integer.parseInt(pointsStr);
            float reduction = Float.parseFloat(reductionStr) / 100; // ex: 20% devient 0.2

            // 1. Insérer la catégorie
            CategorieProduit categorie = new CategorieProduit(0, nom, points, reduction);
            int categorieId = categorieDAO.insertAndGetId(categorie); // Nouvelle méthode qui retourne l'ID généré

            // 2. Associer la catégorie au commerce
            commerceCategorieDAO.associerCategorieACommerce(commerce.getId(), categorieId);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Catégorie ajoutée et liée au commerce !");
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "Points et Réduction doivent être des nombres valides.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur BDD", "Impossible d'ajouter la catégorie.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
