package ihm;

import dao.CategorieProduitDAO;
import dao.CommerceCategorieProduitDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.CategorieProduit;
import model.Commerce;
import utils.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListeCategoriesController {

    @FXML
    private TableView<CategorieProduit> categorieTable;
    @FXML
    private TableColumn<CategorieProduit, String> nomCol;
    @FXML
    private TableColumn<CategorieProduit, Integer> pointsCol;
    @FXML
    private TableColumn<CategorieProduit, Float> reductionCol;

    private Commerce commerce;
    private CommerceCategorieProduitDAO ccDAO;
    private CategorieProduitDAO cpDAO;

    public void setCommerce(Commerce commerce) {
        this.commerce = commerce;
        try {
            loadCategories();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            ccDAO = new CommerceCategorieProduitDAO(conn);
            cpDAO = new CategorieProduitDAO(conn);

            nomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
            pointsCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPointNecessaire()).asObject());
            reductionCol.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getBonReduction() * 100).asObject()); // affiché en %
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCategories() throws SQLException {
        List<Integer> categorieIds = ccDAO.getCategoriesByCommerce(commerce.getId());
        List<CategorieProduit> categories = new ArrayList<>();

        for (Integer id : categorieIds) {
            categories.add(cpDAO.getById(id));
        }

        ObservableList<CategorieProduit> observableList = FXCollections.observableArrayList(categories);
        categorieTable.setItems(observableList);
    }

    @FXML
    private void handleAjouterCategorie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AjouterCategorieView.fxml"));
            Parent root = loader.load();

            // Passer le commerce au controller AjouterCategorieController
            AjouterCategorieController controller = loader.getController();
            controller.setCommerce(commerce); // <-- très important !

            Stage stage = new Stage();
            stage.setTitle("Ajouter une Catégorie");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Après ajout, on recharge
            loadCategories();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
