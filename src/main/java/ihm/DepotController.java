package ihm;

import dao.PoubelleDAO;
import dao.CentreDeTriDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.*;
import utils.DatabaseConnection;
import views.PageDepotController;

import java.sql.Connection;
import java.util.List;

public class DepotController {

    @FXML
    private Label centreLabel;

    @FXML
    private VBox poubelleListVBox;

    private Utilisateur utilisateurConnecte;

    /**
     * Setter pour passer l'utilisateur connecté au contrôleur.
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        afficherCentreEtPoubelles();
    }

    /**
     * Charge le centre et les poubelles associées.
     */
    private void afficherCentreEtPoubelles() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PoubelleDAO poubelleDAO = new PoubelleDAO(conn);
            CentreDeTriDAO centreDAO = new CentreDeTriDAO(conn);

            System.out.println("DEBUG - utilisateurConnecte.centreId = " + utilisateurConnecte.getCentreId());

            CentreDeTri centre = centreDAO.getById(utilisateurConnecte.getCentreId());
            centreLabel.setText("Centre de Tri : " + centre.getNom());

            // ➔ Ajout des boutons fonctionnels en haut de la page
            ajouterBoutonsFonctionnels();

            // ➔ Affichage des poubelles
            List<Poubelle> poubelles = poubelleDAO.getPoubellesByCentreId(utilisateurConnecte.getCentreId());
            System.out.println("Nombre de poubelles trouvées : " + poubelles.size());

            for (Poubelle p : poubelles) {
                Button boutonDepot = new Button("Déposer dans la poubelle " + p.getEmplacement());
                boutonDepot.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
                boutonDepot.setOnAction(e -> ouvrirPageDepot(p));
                poubelleListVBox.getChildren().add(boutonDepot);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Label erreurLabel = new Label("Erreur chargement centre ou poubelles.");
            erreurLabel.setStyle("-fx-text-fill: red;");
            poubelleListVBox.getChildren().add(erreurLabel);
        }
    }

    /**
     * Ajoute les boutons "Historique", "Bons", "Échanger Points" en haut de la page.
     */
    private HBox creerBoutonsFonctionnels() {
        HBox hboxBoutons = new HBox(20);
        hboxBoutons.setStyle("-fx-padding: 20px;");
        hboxBoutons.setAlignment(javafx.geometry.Pos.CENTER);

        Button btnHistorique = new Button("Voir Historique Dépôts");
        Button btnBons = new Button("Voir Bons de Commande");
        Button btnEchangerPoints = new Button("Échanger des Points");

        btnHistorique.setOnAction(e -> ouvrirHistorique());
        btnBons.setOnAction(e -> ouvrirBons());
        btnEchangerPoints.setOnAction(e -> ouvrirEchangerPoints());

        hboxBoutons.getChildren().addAll(btnHistorique, btnBons, btnEchangerPoints);
        return hboxBoutons;
    }

    private void ajouterBoutonsFonctionnels() {
        HBox hbox = creerBoutonsFonctionnels();
        poubelleListVBox.getChildren().add(0, hbox);
    }


    /**
     * Ouvre la page d'historique des dépôts.
     */
    private void ouvrirHistorique() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/historique-depots.fxml"));
            Parent root = loader.load();

            HistoriqueDepotsController controller = loader.getController();
            controller.setUtilisateur(utilisateurConnecte); // 🔥 on passe l'utilisateur connecté

            Stage stage = new Stage();
            stage.setTitle("Historique de Dépôts");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Ouvre la page des bons de commande.
     */
    private void ouvrirBons() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/liste-bons.fxml"));
            Parent root = loader.load();

            // TODO : passer utilisateurConnecte si nécessaire
            Stage stage = new Stage();
            stage.setTitle("Mes Bons de Commande");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ouvre la page pour échanger des points.
     */
    private void ouvrirEchangerPoints() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/echanger-points.fxml"));
            Parent root = loader.load();

            // TODO : passer utilisateurConnecte si nécessaire
            Stage stage = new Stage();
            stage.setTitle("Échanger mes Points");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ouvre la page de dépôt pour une poubelle spécifique.
     */
    private void ouvrirPageDepot(Poubelle poubelle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PageDepot.fxml"));
            Parent root = loader.load();

            PageDepotController controller = loader.getController();
            controller.setUtilisateurEtPoubelle(utilisateurConnecte, poubelle);

            Stage stage = new Stage();
            stage.setTitle("Déposer vos déchets - " + poubelle.getEmplacement());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
