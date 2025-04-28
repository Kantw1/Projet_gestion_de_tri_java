package ihm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Utilisateur;
import dao.UtilisateurDAO;
import utils.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

public class InscriptionController {

    @FXML
    private TextField nomField, codeAccesField;

    @FXML
    private ComboBox<String> centreComboBox;

    @FXML
    private Label errorLabel;

    private Map<String, Integer> centreMap = new HashMap<>(); // nom du centre -> id du centre

    @FXML
    public void initialize() {
        chargerCentres();
    }

    private void chargerCentres() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT id, nom FROM CentreDeTri";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String nomCentre = rs.getString("nom");
                    int idCentre = rs.getInt("id");
                    centreComboBox.getItems().add(nomCentre);
                    centreMap.put(nomCentre, idCentre);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erreur chargement centres.");
        }
    }

    @FXML
    private void handleInscription(ActionEvent event) {
        try {
            String nom = nomField.getText();
            String codeAccesText = codeAccesField.getText();
            String centreSelectionne = centreComboBox.getValue();

            if (nom.isEmpty() || codeAccesText.isEmpty() || centreSelectionne == null) {
                errorLabel.setText("Veuillez remplir tous les champs.");
                return;
            }

            if (!codeAccesText.matches("\\d{4}")) {
                errorLabel.setText("Code d'accès : 4 chiffres obligatoires.");
                return;
            }

            int codeAcces = Integer.parseInt(codeAccesText);
            int centreId = centreMap.get(centreSelectionne);

            Utilisateur utilisateur = new Utilisateur(0, nom, codeAcces);
            utilisateur.setCentreId(centreId); // on affecte le centre choisi

            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(DatabaseConnection.getConnection());
            utilisateurDAO.insert(utilisateur);

            // Redirection vers la page de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/hello-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.setScene(new Scene(root, 500, 400));
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
            stage.setScene(new Scene(root, 500, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Erreur retour connexion.");
        }
    }
}
