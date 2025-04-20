package dao;

import model.Utilisateur;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UtilisateurDAO {

    public static void insertUtilisateur(Utilisateur u) {
        String sql = "INSERT INTO Utilisateur (Nom, ProfilEco, CodeAcces) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNom());
            stmt.setInt(2, u.getPtsFidelite());
            stmt.setInt(3, u.getCodeAcces());

            stmt.executeUpdate();
            System.out.println("Utilisateur inséré avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


//hibernate