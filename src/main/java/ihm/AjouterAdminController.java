package ihm;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Utilisateur;
import dao.UtilisateurDAO;
import utils.DatabaseConnection;

public class AjouterAdminController {

    @FXML
    private TextField nomField;

    @FXML
    private PasswordField motDePasseField;

    @FXML
    private void handleCreerAdmin() {
        String nom = nomField.getText();
        String codeAccesStr = motDePasseField.getText(); // en réalité ici on récupère le code d'accès (mot de passe à 4 chiffres)

        if (nom.isEmpty() || codeAccesStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        // Vérification que le code d'accès est exactement 4 chiffres
        if (!codeAccesStr.matches("\\d{4}")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le code d'accès doit être composé de 4 chiffres.");
            return;
        }

        try {
            int codeAcces = Integer.parseInt(codeAccesStr);

            Utilisateur nouvelAdmin = new Utilisateur(0, nom, codeAcces, "admin", 0);

            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(utils.DatabaseConnection.getConnection());
            utilisateurDAO.insert(nouvelAdmin);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Administrateur ajouté avec succès !");
            nomField.clear();
            motDePasseField.clear();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'administrateur.");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
