package testDAO;

import dao.*;
import model.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainTestDAO {
    public static void main(String[] args) {
        try {
            // Connexion à la base
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");

            // Initialisation des DAO
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(conn);
            CategorieProduitDAO categorieDAO = new CategorieProduitDAO(conn);
            CentreDeTriDAO centreDAO = new CentreDeTriDAO(conn);
            CommerceDAO commerceDAO = new CommerceDAO(conn);
            ContratPartenariatDAO contratDAO = new ContratPartenariatDAO(conn);
            BonDeCommandeDAO bdcDAO = new BonDeCommandeDAO(conn);
            PoubelleDAO poubelleDAO = new PoubelleDAO(conn);
            DepotDAO depotDAO = new DepotDAO(conn);
            HistoriqueDepotDAO historiqueDAO = new HistoriqueDepotDAO(conn);
            CommerceCategorieProduitDAO ccpDAO = new CommerceCategorieProduitDAO(conn);
            ProduitCategorieDAO pcDAO = new ProduitCategorieDAO(conn);
            CentrePoubelleDAO cpDAO = new CentrePoubelleDAO(conn);
            CentreCommerceDAO ccDAO = new CentreCommerceDAO(conn);

            // === Création des objets ===
            Utilisateur u = new Utilisateur(0, "Jean", 1234);
            u.setPtsFidelite(200);
            utilisateurDAO.insert(u);
            u = utilisateurDAO.getAll().getLast();

            CentreDeTri centre = new CentreDeTri(0, "TriTest", "Rue du Recyclage");
            centreDAO.insert(centre);
            centre = centreDAO.getAll().getLast();

            Poubelle poubelle = new Poubelle(0, 120, "Rue Test", TypePoubelle.JAUNE, 50);
            poubelleDAO.insert(poubelle);
            poubelle = poubelleDAO.getAll().getLast();

            Commerce commerce = new Commerce(0, "EcoMarket", centre);
            commerceDAO.insert(commerce);
            commerce = commerceDAO.getAll(centre).getLast();

            cpDAO.insert(centre.getId(), poubelle.getId());
            ccDAO.insert(centre.getId(), commerce.getId());

            CategorieProduit cp = new CategorieProduit(0, "Électroménager", 80, 0.15f);
            categorieDAO.insert(cp);
            cp = categorieDAO.getAll().getLast();

            pcDAO.insert("Aspirateur", cp.getId());
            ccpDAO.insert(commerce.getId(), cp.getId());

            List<CategorieProduit> listeProduits = new ArrayList<>();
            listeProduits.add(cp);

            ContratPartenariat contrat = new ContratPartenariat(0,
                    LocalDate.now().minusDays(10),
                    LocalDate.now().plusDays(10),
                    centre, commerce);
            contrat.ajouterCategorie(cp);
            contratDAO.insert(contrat);

            BonDeCommande commande = new BonDeCommande(0, u, listeProduits, commerce);
            commande.utiliserPoints(); // simule la validation
            bdcDAO.insert(commande);

            // Création d’un dépôt et ajout dans l’historique
            Depot depot = new Depot(0, NatureDechet.PLASTIQUE, 2.5f, 3, LocalDateTime.now(), poubelle, u);
            depotDAO.insert(depot);
            depot = depotDAO.getAll(poubelle, u).getLast();

            historiqueDAO.insert(u.getId(), depot.getId());

            // === Affichage final ===
            System.out.println("\n=== Utilisateurs ===");
            for (Utilisateur user : utilisateurDAO.getAll()) {
                System.out.println("ID: " + user.getId() + " | Nom: " + user.getNom() + " | Points: " + user.getPtsFidelite());
            }

            System.out.println("\n=== Catégories de Produits ===");
            for (CategorieProduit c : categorieDAO.getAll()) {
                System.out.println("ID: " + c.getId() + " | Nom: " + c.getNom() +
                        " | Points nécessaires: " + c.getPointNecessaire() + " | Réduction: " + c.getBonReduction());
            }

            System.out.println("\n=== Produits associés à la catégorie ===");
            for (String produit : pcDAO.getProduitsByCategorie(cp.getId())) {
                System.out.println("- " + produit);
            }

            System.out.println("\n=== Commerces ===");
            for (Commerce c : commerceDAO.getAll(centre)) {
                System.out.println("ID: " + c.getId() + " | Nom: " + c.getNom());
            }

            System.out.println("\n=== Contrats ===");
            for (ContratPartenariat ct : contratDAO.getAll(centre, commerce)) {
                System.out.println("Contrat #" + ct.getId() + " | Début: " + ct.getDateDebut() + " | Fin: " + ct.getDateFin());
            }

            System.out.println("\n=== Bons de Commande ===");
            for (BonDeCommande b : bdcDAO.getAll(u, commerce, listeProduits)) {
                System.out.println("ID: " + b.getId() + " | État: " + b.getEtatCommande() +
                        " | Points utilisés: " + b.getPointsUtilises() + " | Date: " + b.getDateCommande());
            }

            System.out.println("\n=== Dépôts de l'utilisateur ===");
            for (Depot d : depotDAO.getAll(poubelle, u)) {
                System.out.println("Déchet: " + d.getType() + " | Poids: " + d.getPoids() + " | Points: " + d.getPoints());
            }

            System.out.println("\n=== Historique Dépôts ===");
            for (int id : historiqueDAO.getDepotsByUtilisateur(u.getId())) {
                System.out.println("Dépot ID lié à l'utilisateur : " + id);
            }

            conn.close();
            System.out.println("\n✅ TEST FINAL TERMINÉ AVEC SUCCÈS.");

        } catch (Exception e) {
            System.err.println("❌ ERREUR : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
