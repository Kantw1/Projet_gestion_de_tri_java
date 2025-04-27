package com.example.gestion_dechet.ihm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/views/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 400); // taille de la fenêtre
        stage.setTitle("Dépôt de déchets"); // titre de ta fenêtre
        stage.setScene(scene);
        stage.show();
    }
}