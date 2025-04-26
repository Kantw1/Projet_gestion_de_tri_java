package com.example.gestion_dechet.ihm;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DepotController {

    @FXML
    private ComboBox<String> poubelleComboBox;
    @FXML
    private TextField plastiqueField, verreField, cartonField, metalField;
    @FXML
    private Label resultLabel;

    @FXML
    public void initialize() {
        poubelleComboBox.getItems().addAll("Poubelle Verte", "Poubelle Jaune", "Poubelle Bleue", "Poubelle Classique");
    }

    @FXML
    public void validerDepot() {
        String poubelle = poubelleComboBox.getValue();
        int plastique = parseField(plastiqueField.getText());
        int verre = parseField(verreField.getText());
        int carton = parseField(cartonField.getText());
        int metal = parseField(metalField.getText());

        if (poubelle == null) {
            resultLabel.setText("Veuillez choisir une poubelle.");
            resultLabel.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        int points = calculerPoints(poubelle, plastique, verre, carton, metal);
        resultLabel.setText("Points obtenus : " + points);
        resultLabel.setTextFill(points >= 0 ? javafx.scene.paint.Color.GREEN : javafx.scene.paint.Color.RED);
    }

    private int parseField(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private int calculerPoints(String poubelle, int plastique, int verre, int carton, int metal) {
        switch (poubelle) {
            case "Poubelle Jaune":
                return plastique * 2 + carton * 2 + metal * 2 - verre * 3;
            case "Poubelle Verte":
                return verre * 3 - (plastique + carton + metal);
            case "Poubelle Bleue":
                return carton * 2 - (plastique + verre + metal);
            case "Poubelle Classique":
                return -(plastique + verre + carton + metal);
            default:
                return 0;
        }
    }
}
