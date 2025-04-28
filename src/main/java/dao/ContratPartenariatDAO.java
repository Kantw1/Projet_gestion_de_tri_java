package dao;

import model.CentreDeTri;
import model.Commerce;
import model.ContratPartenariat;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContratPartenariatDAO {

    private final Connection conn;

    public ContratPartenariatDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(ContratPartenariat contrat) throws SQLException {
        String sql = "INSERT INTO contratPartenariat (dateDebut, dateFin, centreID, commerceID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(contrat.getDateDebut()));
            stmt.setDate(2, Date.valueOf(contrat.getDateFin()));
            stmt.setInt(3, contrat.getCentre().getId());
            stmt.setInt(4, contrat.getCommerce().getId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGenere = rs.getInt(1);
                // contrat.setId(idGenere); // si setter ajouté
            }
        }
    }

    public ContratPartenariat getById(int id, CentreDeTri centre, Commerce commerce) throws SQLException {
        String sql = "SELECT * FROM contratPartenariat WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ContratPartenariat(
                        rs.getInt("id"),
                        rs.getDate("dateDebut").toLocalDate(),
                        rs.getDate("dateFin").toLocalDate(),
                        centre,
                        commerce
                );
            }
        }
        return null;
    }

    public List<ContratPartenariat> getAll() throws SQLException {
        List<ContratPartenariat> contrats = new ArrayList<>();
        String sql = "SELECT cp.id, cp.dateDebut, cp.dateFin, c.id AS commerceId, c.nom AS commerceNom " +
                "FROM contratPartenariat cp " +
                "JOIN commerce c ON cp.commerceID = c.id";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Commerce commerce = new Commerce(
                        rs.getInt("commerceId"),
                        rs.getString("commerceNom"),
                        null // On n'a pas besoin ici du CentreDeTri dans la table
                );

                ContratPartenariat contrat = new ContratPartenariat(
                        rs.getInt("id"),
                        rs.getDate("dateDebut").toLocalDate(),
                        rs.getDate("dateFin").toLocalDate(),
                        null, // Pas besoin de CentreDeTri complet ici
                        commerce
                );
                contrats.add(contrat);
            }
        }
        return contrats;
    }


    public void update(ContratPartenariat contrat) throws SQLException {
        String sql = "UPDATE contratPartenariat SET dateDebut = ?, dateFin = ?, centreID = ?, commerceID = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(contrat.getDateDebut()));
            stmt.setDate(2, Date.valueOf(contrat.getDateFin()));
            stmt.setInt(3, contrat.getCentre().getId());
            stmt.setInt(4, contrat.getCommerce().getId());
            stmt.setInt(5, contrat.getId()); // l'attribut doit rester accessible
            stmt.executeUpdate();
        }
    }
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM contratPartenariat WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    /**
     * Récupère les commerces associés à un centre de tri donné, avec les dates du contrat.
     */
    public List<ContratPartenariat> getContratsByCentre(CentreDeTri centre) throws SQLException {
        List<ContratPartenariat> contrats = new ArrayList<>();
        String sql = """
        SELECT cp.id, cp.dateDebut, cp.dateFin, 
               c.id AS commerceId, c.nom AS commerceNom
        FROM contratPartenariat cp
        JOIN commerce c ON cp.commerceID = c.id
        WHERE cp.centreID = ?
        """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, centre.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Commerce commerce = new Commerce(
                            rs.getInt("commerceId"),
                            rs.getString("commerceNom"),
                            centre
                    );

                    ContratPartenariat contrat = new ContratPartenariat(
                            rs.getInt("id"),
                            rs.getDate("dateDebut").toLocalDate(),
                            rs.getDate("dateFin").toLocalDate(),
                            centre,
                            commerce
                    );

                    contrats.add(contrat);
                }
            }
        }
        return contrats;
    }
    public List<ContratPartenariat> getContratsByCommerce(Commerce commerce) throws SQLException {
        List<ContratPartenariat> contrats = new ArrayList<>();
        String sql = """
        SELECT cp.id, cp.dateDebut, cp.dateFin,
               ct.id AS centreId,
               c.id AS commerceId, c.nom AS commerceNom
        FROM contratPartenariat cp
        JOIN commerce c ON cp.commerceID = c.id
        JOIN centreDeTri ct ON cp.centreID = ct.id
        WHERE c.id = ?
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, commerce.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CentreDeTri centre = new CentreDeTri(
                        rs.getInt("centreId"),
                        "",
                        ""
                );

                Commerce comm = new Commerce(
                        rs.getInt("commerceId"),
                        rs.getString("commerceNom"),
                        centre
                );

                ContratPartenariat contrat = new ContratPartenariat(
                        rs.getInt("id"),
                        rs.getDate("dateDebut").toLocalDate(),
                        rs.getDate("dateFin").toLocalDate(),
                        centre,
                        comm
                );
                contrats.add(contrat);
            }
        }
        return contrats;
    }


}
