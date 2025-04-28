package ihm;

import dao.DepotDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Depot;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListeHistoriqueController {

    @FXML
    private TableView<Depot> historiqueTable;
    @FXML
    private TableColumn<Depot, String> dateCol;
    @FXML
    private TableColumn<Depot, String> typeCol;
    @FXML
    private TableColumn<Depot, Integer> pointsCol;
    @FXML
    private TableColumn<Depot, String> utilisateurCol;

    private int poubelleId;

    public void setPoubelleId(int poubelleId) {
        this.poubelleId = poubelleId;
        loadHistorique();
    }

    private void loadHistorique() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            DepotDAO dao = new DepotDAO(conn);

            List<Depot> depots = dao.getByPoubelleId(poubelleId);

            ObservableList<Depot> data = FXCollections.observableArrayList(depots);

            // Formater les colonnes
            dateCol.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(
                            cellData.getValue().getHeureDepot().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    )
            );

            typeCol.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType().name())
            );

            pointsCol.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getPoints()).asObject()
            );

            utilisateurCol.setCellValueFactory(cellData -> {
                if (cellData.getValue().getUtilisateur() != null && cellData.getValue().getUtilisateur().getId() != 0) {
                    return new javafx.beans.property.SimpleStringProperty(
                            String.valueOf(cellData.getValue().getUtilisateur().getId())
                    );
                } else {
                    return new javafx.beans.property.SimpleStringProperty("Inconnu");
                }
            });



            historiqueTable.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
