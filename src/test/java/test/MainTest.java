package test;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

/**
 * Classe de test de régression.
 * Permet de lancer tous les tests unitaires du projet et d’afficher les résultats.
 */
public class MainTest {

    public static void main(String[] args) {
        // Création d'un listener pour récupérer les résultats
        SummaryGeneratingListener listener = new SummaryGeneratingListener();

        // Création de la requête de découverte des classes de test
        LauncherDiscoveryRequest request = org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
                .request()
                .selectors(
                        selectClass(DepotTest.class),
                        selectClass(UtilisateurTest.class),
                        // Commenter les lignes suivantes si les tests n'existent pas encore :
                        // selectClass(CategorieProduitTest.class),
                        // selectClass(PoubelleTest.class),
                        // selectClass(BonDeCommandeTest.class),
                        selectClass(CommerceTest.class)
                        // selectClass(CentreDeTriTest.class),
                        // selectClass(ContratPartenariatTest.class)
                )
                .build();

        // Création du launcher
        Launcher launcher = LauncherFactory.create();

        // Enregistrement du listener
        launcher.registerTestExecutionListeners(listener);

        // Exécution de la requête
        launcher.execute(request);

        // Impression du résumé des tests
        var summary = listener.getSummary();
        summary.printTo(new java.io.PrintWriter(System.out));
    }
}
