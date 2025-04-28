package dao;

import model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {
    private final Connection conn;

    public UtilisateurDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Insère un utilisateur dans la base, puis l'associe à un centre s'il est défini et si ce n'est pas un admin.
     */
    public void insert(Utilisateur u) throws SQLException {
        String sql = "INSERT INTO Utilisateur (nom, ptsFidelite, codeAcces, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, u.getNom());
            stmt.setInt(2, u.getPtsFidelite());
            stmt.setInt(3, u.getCodeAcces());
            stmt.setString(4, u.getRole());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setId(rs.getInt(1));
                }
            }
        }

        // ➔ associer l'utilisateur à un centre
        // ➔ UNIQUEMENT si ce n'est pas  un admin
        if (!"admin".equalsIgnoreCase(u.getRole()) && u.getCentreId() > 0) {
            String sqlCentreUtilisateur = "INSERT INTO CentreUtilisateur (centreID, utilisateurID) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCentreUtilisateur)) {
                stmt.setInt(1, u.getCentreId());
                stmt.setInt(2, u.getId());
                stmt.executeUpdate();
            }
        }
    }

    /**
     * Met à jour l'association d'un utilisateur à un centre donné.
     * Si aucune association n'existait, elle est créée.
     */
    public void updateCentreUtilisateur(int utilisateurId, int nouveauCentreId) throws SQLException {
        // Vérifie d'abord si l'association existe
        String selectSql = "SELECT * FROM CentreUtilisateur WHERE utilisateurID = ?";
        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setInt(1, utilisateurId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                // Si une association existe déjà ➔ on fait un UPDATE
                String updateSql = "UPDATE CentreUtilisateur SET centreID = ? WHERE utilisateurID = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, nouveauCentreId);
                    updateStmt.setInt(2, utilisateurId);
                    updateStmt.executeUpdate();
                }
            } else {
                // Sinon ➔ on fait un INSERT
                String insertSql = "INSERT INTO CentreUtilisateur (centreID, utilisateurID) VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, nouveauCentreId);
                    insertStmt.setInt(2, utilisateurId);
                    insertStmt.executeUpdate();
                }
            }
        }
    }

    /**
     * Récupère un utilisateur par son ID.
     */
    public Utilisateur getById(int id) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur u = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("codeAcces"),
                        rs.getString("role"),
                        getCentreByUtilisateurId(rs.getInt("id"))
                );
                u.setPtsFidelite(rs.getInt("ptsFidelite"));
                return u;
            }
        }
        return null;
    }

    /**
     * Récupère l'ID du centre associé à un utilisateur donné.
     * @param utilisateurId l'identifiant de l'utilisateur
     * @return l'ID du centre associé, ou -1 s'il n'est associé à aucun centre
     */
    public int getCentreByUtilisateurId(int utilisateurId) throws SQLException {
        String sql = "SELECT centreID FROM CentreUtilisateur WHERE utilisateurID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("centreID");
            }
        }
        return -1; // retourne -1 si aucun centre n'est associé
    }

    /**
     * Récupère tous les utilisateurs.
     */
    public List<Utilisateur> getAll() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM Utilisateur";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Utilisateur u = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("codeAcces"),
                        rs.getString("role"),
                        getCentreByUtilisateurId(rs.getInt("id"))
                );
                u.setPtsFidelite(rs.getInt("ptsFidelite"));
                utilisateurs.add(u);
            }
        }
        return utilisateurs;
    }

    /**
     * Met à jour les informations d’un utilisateur.
     */
    public void update(Utilisateur u) throws SQLException {
        String sql = "UPDATE Utilisateur SET nom = ?, ptsFidelite = ?, codeAcces = ?, role = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getNom());
            stmt.setInt(2, u.getPtsFidelite());
            stmt.setInt(3, u.getCodeAcces());
            stmt.setString(4, u.getRole());
            stmt.setInt(5, u.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Supprime un utilisateur de la base.
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Utilisateur WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Met à jour uniquement les points de fidélité.
     */
    public void updateFidelite(int id, int newPoints) throws SQLException {
        String sql = "UPDATE Utilisateur SET ptsFidelite = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newPoints);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Récupère un utilisateur par son code d'accès.
     */
    public Utilisateur getByCodeAcces(int codeAcces) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE codeAcces = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codeAcces);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur u = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("codeAcces"),
                        rs.getString("role"),
                        getCentreByUtilisateurId(rs.getInt("id"))
                );
                u.setPtsFidelite(rs.getInt("ptsFidelite"));
                return u;
            }
        }
        return null;
    }


    /**
     * Récupère un utilisateur par son nom.
     */
    public Utilisateur getByNom(String nom) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE nom = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur u = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("codeAcces"),
                        rs.getString("role"),
                        getCentreByUtilisateurId(rs.getInt("id"))
                );
                u.setPtsFidelite(rs.getInt("ptsFidelite"));
                return u;
            }
        }
        return null;
    }

    /**
     * Vérifie si un utilisateur est administrateur à partir de son ID.
     */
    public boolean isAdmin(int id) throws SQLException {
        String sql = "SELECT role FROM Utilisateur WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return "admin".equalsIgnoreCase(rs.getString("role"));
            }
        }
        return false;
    }

    public Utilisateur getByNomAndCodeAcces(String nom, int codeAcces) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE nom = ? AND codeAcces = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setInt(2, codeAcces);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur u = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("codeAcces"),
                        rs.getString("role"),
                        getCentreByUtilisateurId(rs.getInt("id"))
                );
                u.setPtsFidelite(rs.getInt("ptsFidelite"));
                return u;
            }
        }
        return null;
    }
    public int getCentreIdByUtilisateurId(int utilisateurId) throws SQLException {
        String sql = "SELECT centreID FROM CentreUtilisateur WHERE utilisateurID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("centreID");
                } else {
                    throw new SQLException("Aucun centre trouvé pour l'utilisateur ID=" + utilisateurId);
                }
            }
        }
    }
    // DAO Utilisateur
    public int getPointsFideliteById(int id) throws SQLException {
        String sql = "SELECT ptsFidelite FROM Utilisateur WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ptsFidelite");
                }
            }
        }
        return 0;
    }

}
