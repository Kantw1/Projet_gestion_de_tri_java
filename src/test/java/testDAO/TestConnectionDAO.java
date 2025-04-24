package testDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnectionDAO {

    public static void main(String[] args) {
        try {
            // Chargement explicite du driver JDBC (très important avec Maven)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Paramètres de connexion (à adapter selon ton environnement)
            String url = "jdbc:mysql://localhost:3306/BDD";
            String user = "root";
            String password = "";

            // Connexion à la base
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connexion MySQL réussie !");
            conn.close();

        } catch (ClassNotFoundException e) {
            System.out.println("❌ Le driver JDBC MySQL est introuvable.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ La connexion à MySQL a échoué.");
            e.printStackTrace();
        }
    }
}
