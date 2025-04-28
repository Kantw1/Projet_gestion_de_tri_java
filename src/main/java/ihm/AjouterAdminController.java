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
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField motDePasseField;

    @FXML
    private void handleCreerAdmin() {
        String nom = nomField.getText();
        String prenom = prenomField.getText(); // Pour l'instant inutilisé sauf si tu modifies ta base
        String email = emailField.getText();    // Pareil : prévu mais pas encore utilisé en base
        String motDePasse = motDePasseField.getText(); // Non utilisé ici car Utilisateur n'a pas ce champ

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || motDePasse.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        try {
            int codeAcces = (int) (Math.random() * 100000);

            // CORRECTION ici : utiliser ton constructeur existant
            Utilisateur nouvelAdmin = new Utilisateur(0, nom, codeAcces, "admin");

            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(utils.DatabaseConnection.getConnection());
            utilisateurDAO.insert(nouvelAdmin);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Administrateur ajouté avec succès !");
            nomField.clear();
            prenomField.clear();
            emailField.clear();
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
