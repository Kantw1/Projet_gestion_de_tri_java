package ihm;

import dao.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import utils.DatabaseConnection;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EchangerPointsController {

    @FXML
    private TableView<LigneProduit> tableView;

    @FXML
    private TableColumn<LigneProduit, String> commerceColumn;

    @FXML
    private TableColumn<LigneProduit, String> categorieColumn;

    @FXML
    private TableColumn<LigneProduit, Integer> pointsColumn;

    @FXML
    private Label pointsLabel;

    @FXML
    private Button acheterCategorieButton;

    @FXML
    private Button acheterToutButton;

    private Utilisateur utilisateurConnecte;
    private CentreDeTri centreUtilisateur;
    private List<Commerce> commercesDisponibles;

    // ===== Classe interne pour la TableView =====
    public static class LigneProduit {
        private final Commerce commerce;
        private final CategorieProduit categorieProduit;

        public LigneProduit(Commerce commerce, CategorieProduit categorieProduit) {
            this.commerce = commerce;
            this.categorieProduit = categorieProduit;
        }

        public Commerce getCommerce() {
            return commerce;
        }

        public CategorieProduit getCategorieProduit() {
            return categorieProduit;
        }

        public String getNomCommerce() {
            return commerce != null ? commerce.getNom() : "";
        }

        public String getNomCategorie() {
            return categorieProduit != null ? categorieProduit.getNom() : "";
        }

        public Integer getPointsNecessaires() {
            return categorieProduit != null ? categorieProduit.getPointNecessaire() : 0;
        }
    }

    @FXML
    public void initialize() {
        commerceColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNomCommerce()));
        categorieColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNomCategorie()));
        pointsColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPointsNecessaires()).asObject());
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;

        try (Connection conn = DatabaseConnection.getConnection()) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(conn);

            int nouveauxPoints = utilisateurDAO.getPointsFideliteById(utilisateur.getId());
            utilisateurConnecte.setPtsFidelite(nouveauxPoints);

            pointsLabel.setText("Points disponibles : " + utilisateurConnecte.getPtsFidelite());
            chargerProduitsDisponibles();
        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Erreur lors du chargement des points utilisateur.");
        }
    }

    private void chargerProduitsDisponibles() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(conn);
            CentreDeTriDAO centreDeTriDAO = new CentreDeTriDAO(conn);
            ContratPartenariatDAO contratDAO = new ContratPartenariatDAO(conn);
            CategorieProduitDAO categorieProduitDAO = new CategorieProduitDAO(conn);

            int centreId = utilisateurDAO.getCentreIdByUtilisateurId(utilisateurConnecte.getId());
            centreUtilisateur = centreDeTriDAO.getById(centreId);

            List<ContratPartenariat> contratsActifs = new ArrayList<>();
            for (ContratPartenariat contrat : contratDAO.getContratsByCentre(centreUtilisateur)) {
                if (contrat.getDateDebut().isBefore(java.time.LocalDate.now()) &&
                        contrat.getDateFin().isAfter(java.time.LocalDate.now())) {
                    contratsActifs.add(contrat);
                }
            }

            List<LigneProduit> lignes = new ArrayList<>();
            commercesDisponibles = new ArrayList<>();

            for (ContratPartenariat contrat : contratsActifs) {
                Commerce commerce = contrat.getCommerce();
                commercesDisponibles.add(commerce);

                List<CategorieProduit> categories = categorieProduitDAO.getCategoriesByCommerce(commerce.getId());
                for (CategorieProduit categorie : categories) {
                    lignes.add(new LigneProduit(commerce, categorie));
                }
            }

            tableView.setItems(FXCollections.observableArrayList(lignes));

        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Erreur lors du chargement des produits.");
        }
    }

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAcheterCategorie() {
        LigneProduit selection = tableView.getSelectionModel().getSelectedItem();
        if (selection == null) {
            afficherErreur("Veuillez sélectionner une catégorie.");
            return;
        }

        int pointsNecessaires = selection.getPointsNecessaires();
        if (utilisateurConnecte.getPtsFidelite() < pointsNecessaires) {
            afficherErreur("Points insuffisants pour cette catégorie.");
            return;
        }

        if (confirmer("Confirmer l'achat de cette catégorie pour " + pointsNecessaires + " points ?")) {
            effectuerAchat(selection);
        }
    }

    private void effectuerAchat(LigneProduit ligne) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(conn);
            BonDeCommandeDAO bonDeCommandeDAO = new BonDeCommandeDAO(conn);

            int nouveauxPoints = utilisateurConnecte.getPtsFidelite() - ligne.getPointsNecessaires();
            utilisateurDAO.updateFidelite(utilisateurConnecte.getId(), nouveauxPoints);
            utilisateurConnecte.setPtsFidelite(nouveauxPoints);

            BonDeCommande bon = new BonDeCommande(
                    0,
                    utilisateurConnecte,
                    ligne.getCategorieProduit(),
                    java.time.LocalDate.now(),
                    ligne.getPointsNecessaires()
            );

            bonDeCommandeDAO.insert(bon);

            conn.commit();

            afficherSucces("Achat effectué avec succès !");
            tableView.getItems().remove(ligne);
            pointsLabel.setText("Points disponibles : " + utilisateurConnecte.getPtsFidelite());

        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de l'achat.");
        }
    }


    @FXML
    private void handleAcheterTout() {
        if (tableView.getItems().isEmpty()) {
            afficherErreur("Aucun produit disponible.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(conn);
            BonDeCommandeDAO bonDeCommandeDAO = new BonDeCommandeDAO(conn);

            List<LigneProduit> selection = new ArrayList<>(tableView.getItems());

            for (LigneProduit ligne : selection) {
                int pointsCategorie = ligne.getPointsNecessaires();
                if (utilisateurConnecte.getPtsFidelite() < pointsCategorie) {
                    continue;
                }

                utilisateurConnecte.setPtsFidelite(utilisateurConnecte.getPtsFidelite() - pointsCategorie);
                utilisateurDAO.updateFidelite(utilisateurConnecte.getId(), utilisateurConnecte.getPtsFidelite());

                BonDeCommande bon = new BonDeCommande(
                        0,
                        utilisateurConnecte,
                        ligne.getCategorieProduit(),
                        java.time.LocalDate.now(),
                        ligne.getPointsNecessaires()
                );
                bonDeCommandeDAO.insert(bon);


                tableView.getItems().remove(ligne);
            }

            conn.commit();
            afficherSucces("Tous les achats ont été effectués !");
            pointsLabel.setText("Points disponibles : " + utilisateurConnecte.getPtsFidelite());

        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de l'achat multiple.");
        }
    }

    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherSucces(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean confirmer(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
