package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PoubelleTest {

    private Poubelle poubelle;
    private Utilisateur utilisateur;
    private Depot depot;

    @BeforeEach
    void setUp() {
        CentreDeTri centreDeTri = new CentreDeTri(1, "Centre Principal", "Adresse Centre"); // Corrigé
        utilisateur = new Utilisateur(1, "Alice", 1234, 1); // Correction : constructeur complet

        poubelle = new Poubelle(1, 100, "Quartier A", TypePoubelle.JAUNE, 80, 90, centreDeTri);

        // Ajout du code d'accès
        poubelle.getAccesAutorises().add(1234);
        poubelle.getUtilisateursAutorises().add(utilisateur);

        depot = new Depot(1, NatureDechet.PLASTIQUE, 0.5f, 1, LocalDateTime.now(), poubelle, utilisateur);
    }

    @Test
    void testIdentifierUtilisateur() {
        assertTrue(poubelle.identifierUtilisateur(utilisateur));
    }

    @Test
    void testVerifierTypeDechets() {
        assertTrue(poubelle.verifierTypeDechets(NatureDechet.PLASTIQUE));
    }

    @Test
    void testAttribuerPoint() {
        utilisateur.setPtsFidelite(0);
        poubelle.getHistoriqueDepots().add(depot);
        poubelle.attribuerPoint(utilisateur);
        assertTrue(utilisateur.getPtsFidelite() > 0);
    }

    @Test
    void testNotifierCentreTri() {
        poubelle.setQuantiteActuelle(90); // Au seuil
        poubelle.notifierCentreTri(); // Pas d'assertion, on vérifie qu'aucune erreur n'est levée
    }

    @Test
    void testVerifierAcces() {
        assertTrue(poubelle.verifierAcces(1234));
    }

    @Test
    void testAjouterDechets() {
        int ancienneQuantite = poubelle.getQuantiteActuelle();
        poubelle.ajouterDechets(depot);
        assertEquals(ancienneQuantite + depot.getQuantite(), poubelle.getQuantiteActuelle());
    }

    @Test
    void testEstPleine() {
        Depot grosDepot = new Depot(2, NatureDechet.PLASTIQUE, 10f, 100, LocalDateTime.now(), poubelle, utilisateur);
        poubelle.ajouterDechets(grosDepot);
        assertTrue(poubelle.estPleine());
    }

    @Test
    void testCalculerPenalite() {
        assertDoesNotThrow(() -> poubelle.calculerPenalite(utilisateur));
    }

    @Test
    void testAccepterDepot() {
        boolean result = poubelle.accepterDepot(depot, utilisateur);
        assertTrue(result);
    }

    @Test
    void testGetTauxRemplissage() {
        poubelle.ajouterDechets(depot);
        float taux = poubelle.getTauxRemplissage();
        assertTrue(taux > 0);
    }

    @Test
    void testGetDechetsAutorises() {
        List<NatureDechet> types = poubelle.getDechetsAutorises();
        assertNotNull(types);
        assertTrue(types.contains(NatureDechet.PLASTIQUE));
    }
}
