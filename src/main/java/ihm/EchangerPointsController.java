package ihm;

import dao.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
            return commerce.getNom();
        }

        public String getNomCategorie() {
            return categorieProduit.getNom();
        }

        public Integer getPointsNecessaires() {
            return categorieProduit.getPointNecessaire();
        }
    }

    @FXML
    public void initialize() {
        commerceColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNomCommerce()));
        categorieColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNomCategorie()));
        pointsColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPointsNecessaires()).asObject());
    }

    // Appelé pour passer l'utilisateur connecté depuis l'écran précédent
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        chargerProduitsDisponibles();
    }

    private void chargerProduitsDisponibles() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(conn);
            CentreDeTriDAO centreDeTriDAO = new CentreDeTriDAO(conn);
            CentreCommerceDAO centreCommerceDAO = new CentreCommerceDAO(conn);
            CategorieProduitDAO categorieProduitDAO = new CategorieProduitDAO(conn);

            int centreId = utilisateurDAO.getCentreByUtilisateurId(utilisateurConnecte.getId());
            centreUtilisateur = centreDeTriDAO.getById(centreId);
            commercesDisponibles = new ArrayList<>();

            List<LigneProduit> lignes = new ArrayList<>();

            List<Integer> commerceIds = centreCommerceDAO.getCommercesByCentre(centreId);

            for (int commerceId : commerceIds) {
                Commerce commerce = new Commerce(commerceId, "", centreUtilisateur);
                commercesDisponibles.add(commerce);

                List<CategorieProduit> categories = categorieProduitDAO.getCategoriesByCommerceId(commerceId);
                for (CategorieProduit categorie : categories) {
                    lignes.add(new LigneProduit(commerce, categorie));
                }
            }

            tableView.setItems(FXCollections.observableArrayList(lignes));
            pointsLabel.setText("Points disponibles : " + utilisateurConnecte.getPtsFidelite());

        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Erreur lors du chargement des données.");
        }
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

            BonDeCommande bon = new BonDeCommande(0, utilisateurConnecte, List.of(ligne.getCategorieProduit()), ligne.getCommerce());
            bonDeCommandeDAO.insert(bon);

            conn.commit();

            afficherSucces("Achat effectué avec succès !");
            pointsLabel.setText("Points disponibles : " + utilisateurConnecte.getPtsFidelite());
            tableView.getItems().remove(ligne);

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

            for (Commerce commerce : commercesDisponibles) {
                List<LigneProduit> produitsCommerce = selection.stream()
                        .filter(lp -> lp.getCommerce().getId() == commerce.getId())
                        .toList();

                if (produitsCommerce.isEmpty()) continue;

                int totalPoints = produitsCommerce.stream()
                        .mapToInt(LigneProduit::getPointsNecessaires)
                        .sum();

                if (utilisateurConnecte.getPtsFidelite() < totalPoints) {
                    afficherErreur("Points insuffisants pour tout acheter chez " + commerce.getNom());
                    continue;
                }

                if (confirmer("Acheter toutes les réductions chez ce commerce pour " + totalPoints + " points ?")) {
                    for (LigneProduit ligne : produitsCommerce) {
                        int pointsCategorie = ligne.getPointsNecessaires();
                        int nouveauxPoints = utilisateurConnecte.getPtsFidelite() - pointsCategorie;

                        utilisateurConnecte.setPtsFidelite(nouveauxPoints);
                        utilisateurDAO.updateFidelite(utilisateurConnecte.getId(), nouveauxPoints);

                        BonDeCommande bon = new BonDeCommande(0, utilisateurConnecte, List.of(ligne.getCategorieProduit()), commerce);
                        bonDeCommandeDAO.insert(bon);

                        tableView.getItems().remove(ligne);
                    }
                    afficherSucces("Tous les achats chez " + commerce.getNom() + " effectués !");
                    pointsLabel.setText("Points disponibles : " + utilisateurConnecte.getPtsFidelite());
                }
            }

            conn.commit();

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
