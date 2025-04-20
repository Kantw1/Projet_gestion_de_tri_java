package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PoubelleTest {

    private Poubelle poubelle;
    private Utilisateur utilisateur;
    private Depot depot;

    @BeforeEach
    public void setUp() {
        poubelle = new Poubelle(1, 100, "Quartier A", TypePoubelle.JAUNE, 80);
        utilisateur = new Utilisateur(1, "Alice", 1234);
        poubelle.getAccesAutorises().add(1234);
        poubelle.getUtilisateursAutorises().add(utilisateur);
        depot = new Depot(1, NatureDechet.PLASTIQUE, 0.5f, 1, LocalDateTime.now(), poubelle, utilisateur);
    }

    @Test
    public void testIdentifierUtilisateur() {
        assertTrue(poubelle.identifierUtilisateur(utilisateur));
    }

    @Test
    public void testVerifierTypeDechets() {
        assertTrue(poubelle.verifierTypeDechets(NatureDechet.PLASTIQUE));
    }

    @Test
    public void testAttribuerPoint() {
        utilisateur.ajouterPoints(0);
        poubelle.getHistoriqueDepots().add(depot);
        poubelle.attribuerPoint(utilisateur);
        assertTrue(utilisateur.getPtsFidelite() > 0);
    }

    @Test
    public void testNotifierCentreTri() {
        poubelle.getHistoriqueDepots().clear();
        poubelle.ajouterDechets(depot);
        // ici on ne teste pas l'affichage, seulement que ça ne lève pas d'erreur
    }

    @Test
    public void testVerifierAcces() {
        assertTrue(poubelle.verifierAcces(1234));
    }

    @Test
    public void testAjouterDechets() {
        int ancienneQuantite = poubelle.getQuantiteActuelle();
        poubelle.ajouterDechets(depot);
        assertEquals(ancienneQuantite + depot.getQuantite(), poubelle.getQuantiteActuelle());
    }

    @Test
    public void testEstPleine() {
        poubelle.ajouterDechets(new Depot(2, NatureDechet.PLASTIQUE, 10f, 100, LocalDateTime.now(), poubelle, utilisateur));
        assertTrue(poubelle.estPleine());
    }

    @Test
    public void testCalculerPenalite() {
        poubelle.ajouterDechets(new Depot(3, NatureDechet.PLASTIQUE, 10f, 100, LocalDateTime.now(), poubelle, utilisateur));
        // pénalité appelée pour test, retour visuel/logique attendu côté système
        poubelle.calculerPenalite(utilisateur);
    }

    @Test
    public void testAccepterDepot() {
        boolean result = poubelle.accepterDepot(depot, utilisateur);
        assertTrue(result);
    }

    @Test
    public void testGetTauxRemplissage() {
        poubelle.ajouterDechets(depot);
        float taux = poubelle.getTauxRemplissage();
        assertTrue(taux > 0);
    }

    @Test
    public void testGetDechetsAutorises() {
        List<NatureDechet> types = poubelle.getDechetsAutorises();
        assertNotNull(types);
        assertTrue(types.contains(NatureDechet.PLASTIQUE));
    }
}
