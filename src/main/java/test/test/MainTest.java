package test;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.engine.discovery.DiscoverySelectors;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

/**
 * Classe de test de régression.
 * Permet de lancer tous les tests unitaires du projet et d’afficher les résultats.
 */
public class MainTest {

    public static void main(String[] args) {
        SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherDiscoveryRequest request = org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
                .request()
                .selectors(
                        selectClass(DepotTest.class),
                        selectClass(UtilisateurTest.class),
                        selectClass(CategorieProduitTest.class),
                        selectClass(PoubelleTest.class),
                        selectClass(BonDeCommandeTest.class),
                        selectClass(CommerceTest.class),
                        selectClass(CentreDeTriTest.class),
                        selectClass(ContratPartenariatTest.class)
                )
                .build();

        Launcher launcher = LauncherFactory.create();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        var summary = listener.getSummary();
        summary.printTo(System.out);
    }
}
