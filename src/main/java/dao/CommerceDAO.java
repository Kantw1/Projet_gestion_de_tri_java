package dao;

import model.CentreDeTri;
import model.Commerce;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommerceDAO {
    private final Connection conn;

    public CommerceDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Insère un commerce en base de données.
     * Le champ ID est auto-généré par la BDD.
     */
    public void insert(Commerce commerce) throws SQLException {
        String sql = "INSERT INTO commerce (nom, centreID) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, commerce.getNom());
            stmt.setInt(2, commerce.getCentre().getId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGenere = rs.getInt(1);
                // si jamais tu ajoutes un setId() dans Commerce, tu peux faire :
                // commerce.setId(idGenere);
            }
        }
    }

    /**
     * Récupère un commerce par son ID.
     */
    public Commerce getById(int id, CentreDeTri centre) throws SQLException {
        String sql = "SELECT * FROM commerce WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Commerce(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            centre
                    );
                }
            }
        }
        return null;
    }

    /**
     * Récupère tous les commerces associés à un centre donné.
     */
    public List<Commerce> getAll(CentreDeTri centre) throws SQLException {
        List<Commerce> commerces = new ArrayList<>();
        String sql = "SELECT * FROM commerce WHERE centreID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, centre.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    commerces.add(new Commerce(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            centre
                    ));
                }
            }
        }
        return commerces;
    }

    /**
     * Met à jour le nom et le centre d'un commerce.
     */
    public void update(Commerce commerce) throws SQLException {
        String sql = "UPDATE commerce SET nom = ?, centreID = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, commerce.getNom());
            stmt.setInt(2, commerce.getCentre().getId());
            stmt.setInt(3, commerce.getId());
            stmt.executeUpdate();
        }
    }
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM commerce WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

}
