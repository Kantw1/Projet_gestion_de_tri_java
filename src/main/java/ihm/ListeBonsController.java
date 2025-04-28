package ihm;

import dao.BonDeCommandeDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.BonDeCommande;
import model.Utilisateur;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.util.List;

public class ListeBonsController {
    @FXML
    private void handleRetour() {
        // Obtient la scène de la table
        Stage stage = (Stage) tableBons.getScene().getWindow();  // Utilise tableBons au lieu de tableDepots

        // Ferme la fenêtre actuelle
        stage.close();
    }
    @FXML
    private TableView<BonDeCommande> tableBons;

    @FXML
    private TableColumn<BonDeCommande, String> colProduit;

    @FXML
    private TableColumn<BonDeCommande, Integer> colPointsUtilises;

    @FXML
    private TableColumn<BonDeCommande, String> colEtat;

    private Utilisateur utilisateurConnecte;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        chargerBons();
    }

    @FXML
    public void initialize() {
        colProduit.setCellValueFactory(new PropertyValueFactory<>("produitsString")); // méthode getProduitsString()
        colPointsUtilises.setCellValueFactory(new PropertyValueFactory<>("pointsUtilises"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etatCommande"));
    }

    private void chargerBons() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            BonDeCommandeDAO bonDAO = new BonDeCommandeDAO(conn);
            List<BonDeCommande> bons = bonDAO.getByUtilisateurId(utilisateurConnecte.getId());
            tableBons.getItems().setAll(bons);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
