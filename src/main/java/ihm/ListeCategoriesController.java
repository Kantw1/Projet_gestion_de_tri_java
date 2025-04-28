package ihm;

import dao.CategorieProduitDAO;
import dao.CommerceCategorieProduitDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.CategorieProduit;
import model.Commerce;
import utils.DatabaseConnection;

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
            reductionCol.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getBonReduction() * 100).asObject()); // en pourcentage
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
}
