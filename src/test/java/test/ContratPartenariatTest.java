package test;

import model.CategorieProduit;
import model.CentreDeTri;
import model.Commerce;
import model.ContratPartenariat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContratPartenariatTest {

    private ContratPartenariat contrat;
    private CategorieProduit categorie1;
    private CategorieProduit categorie2;
    private CentreDeTri centre;
    private Commerce commerce;
    private LocalDate debut;
    private LocalDate fin;

    @BeforeEach
    public void setUp() {
        debut = LocalDate.of(2024, 1, 1);
        fin = LocalDate.of(2025, 12, 31);
        centre = new CentreDeTri(1, "Centre Nord", "Rue des Fleurs");
        commerce = new Commerce("Biocoop", "Biens alimentaires");
        contrat = new ContratPartenariat(10, debut, fin, centre, commerce);

        categorie1 = new CategorieProduit(1, "Papier", List.of("ramette", "journal"), 10);
        categorie2 = new CategorieProduit(2, "Verre", List.of("bouteille"), 15);
    }

    @Test
    public void testAjoutCategorie() {
        contrat.ajouterCategorie(categorie1);
        assertTrue(contrat.getCategories().contains(categorie1));
    }

    @Test
    public void testRetraitCategorie() {
        contrat.ajouterCategorie(categorie1);
        contrat.retirerCategorie(categorie1);
        assertFalse(contrat.getCategories().contains(categorie1));
    }

    @Test
    public void testCategorieAutorisee() {
        contrat.ajouterCategorie(categorie1);
        assertTrue(contrat.estCategorieAutorisee(categorie1));
        assertFalse(contrat.estCategorieAutorisee(categorie2));
    }

    @Test
    public void testContratValide() {
        assertTrue(contrat.estValide(LocalDate.of(2025, 1, 1)));
    }

    @Test
    public void testContratInvalideAvantDebut() {
        assertFalse(contrat.estValide(LocalDate.of(2023, 12, 31)));
    }

    @Test
    public void testContratInvalideApresFin() {
        assertFalse(contrat.estValide(LocalDate.of(2026, 1, 1)));
    }

    @Test
    public void testGetCentreEtCommerce() {
        assertEquals(centre, contrat.getCentre());
        assertEquals(commerce, contrat.getCommerce());
    }

    @Test
    public void testGetDates() {
        assertEquals(debut, contrat.getDateDebut());
        assertEquals(fin, contrat.getDateFin());
    }
}

