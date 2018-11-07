import application.controller.Controller;
import application.model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

public class testSalg {
    private Produkt produkt;
    private Prisliste prisliste;
    private int antal;
    private Salg salg;
    private Pris prisen;

    @Before
    public void setup() {
        produkt = new Produkt("En ny Øl", 1, 1,
                new Produktkategori("En ny Produktkategori", "En Beskrivelse", Maalbar.cL));
        prisliste = new Prisliste("En ny Prisliste", "En Beskrivelse",
                LocalDateTime.of(2018, 10, 30, 10, 0),
                LocalDateTime.of(2018, 12, 15, 22, 0));
        double pris = 35;
        prisen = prisliste.createPris(pris, produkt);
        Controller.addAllDaysToPrisliste(prisliste);
        antal = 2;
        salg = new Salg();
    }

    @Test
    public void tc1sac() {
        Antal antallet = salg.createAntal(produkt, antal);
        Assert.assertTrue(salg.getAntalProdukter().contains(antallet));
    }

    @Test
    public void tc2sac() {
        Antal antallet = salg.createAntal(produkt, -2);
        Assert.assertTrue(salg.getAntalProdukter().contains(antallet));
    }

    @Test()
    public void tc3sac() {
        Antal antallet = salg.createAntal(null, antal);
        Assert.assertTrue(salg.getAntalProdukter().contains(antallet));
    }

    @Test
    public void tc1sab() {
        salg.betal(true, Sellable.KONTANT);
        Assert.assertTrue(salg.isBetalt());
    }

    @Test
    public void tc2sab() {
        salg.betal(false, Sellable.KONTANT);
        Assert.assertFalse(salg.isBetalt());
    }

    @Test
    public void tc3sab() {
        salg.betal(true, null);
        Assert.assertTrue(salg.isBetalt());
    }

    @Test
    public void tc1sat() {
        salg.createAntal(produkt, antal);
        Assert.assertEquals(70, salg.getTotalPris(), 0.0);
    }

    @Test
    public void tc2sat() {
        salg.createAntal(produkt, 0);
        Assert.assertEquals(0, salg.getTotalPris(), 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void tc3sat() {
        salg.createAntal(null, 0);
        Assert.assertEquals(70, salg.getTotalPris(), 0.0);
    }

    @Test
    public void tc4sat() {
        prisliste.deletePris(prisen);
        salg.createAntal(produkt, antal);
        Assert.assertEquals(0, salg.getTotalPris(), 0.0);
    }

    @After
    public void reset() {
        for (Pris pris1:
                prisliste.getPriser()) {
            prisliste.deletePris(pris1);
        }
    }
}
