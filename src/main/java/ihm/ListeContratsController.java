    package ihm;
    import java.io.IOException;


    import dao.CommerceDAO;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.TableColumn;
    import javafx.scene.control.TableView;
    import javafx.scene.control.Button;
    import javafx.scene.layout.VBox;
    import javafx.stage.Stage;
    import javafx.scene.control.cell.PropertyValueFactory;
    import model.Commerce;
    import utils.DatabaseConnection;

    import java.sql.Connection;
    import java.sql.SQLException;
    import java.util.List;

    public class ListeContratsController {

        @FXML
        private TableView<Commerce> commerceTable;
        @FXML
        private TableColumn<Commerce, Integer> idCol;
        @FXML
        private TableColumn<Commerce, String> nomCol;

        private CommerceDAO commerceDAO;

        @FXML
        private void initialize() {
            try {
                Connection conn = DatabaseConnection.getConnection();
                commerceDAO = new CommerceDAO(conn);

                idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
                nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));

                loadCommerces();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private void loadCommerces() throws SQLException {
            List<Commerce> commerces = commerceDAO.getAllWithoutCentre();
            ObservableList<Commerce> commerceList = FXCollections.observableArrayList(commerces);
            commerceTable.setItems(commerceList);
        }

        @FXML
        private void handleVoirContrats() {
            Commerce selected = commerceTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ListeContratsAssociesView.fxml"));
                    Parent root = loader.load();

                    ListeContratsAssociesController controller = loader.getController();
                    controller.setCommerce(selected);

                    Stage stage = new Stage();
                    stage.setTitle("Contrats de : " + selected.getNom());
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @FXML
        private void handleVoirCategories() {
            Commerce selected = commerceTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ListeCategoriesView.fxml"));
                    Parent root = loader.load();

                    ListeCategoriesController controller = loader.getController();
                    controller.setCommerce(selected);

                    Stage stage = new Stage();
                    stage.setTitle("Catégories de : " + selected.getNom());
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        @FXML
        private void handleAjouterCommerce() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AjouterCommerceView.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Ajouter un Commerce");
                stage.setScene(new Scene(root));
                stage.showAndWait();

                // Recharge la liste après ajout
                loadCommerces();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
