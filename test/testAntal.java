import application.controller.Controller;
import application.model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

public class testAntal {
    private Produkt produkt;
    private Prisliste prisliste;
    private double pris;
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
        pris = 35;
        prisen = prisliste.createPris(pris, produkt);
        Controller.addAllDaysToPrisliste(prisliste);
        antal = 2;
        salg = new Salg();
    }

    @Test
    public void tc1anb() {
        Antal antallet = salg.createAntal(produkt, antal);
        Assert.assertEquals(70, antallet.beregnPris(), 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void tc2anb() {
        Antal antallet = salg.createAntal(null, antal);
        Assert.assertEquals(70, antallet.beregnPris(), 0.0);
    }

    @Test
    public void tc3anb() {
        Antal antallet = salg.createAntal(produkt, antal);
        prisliste.deletePris(prisen);
        Assert.assertEquals(0, antallet.beregnPris(), 0.0);
    }

    @Test
    public void tc4anb() {
        Antal antallet = salg.createAntal(produkt, 0);
        Assert.assertEquals(0, antallet.beregnPris(), 0.0);
    }

    @Test
    public void tc5anb() {
        Antal antallet = salg.createAntal(produkt, -2);
        Assert.assertEquals(-70, antallet.beregnPris(), 0.0);
    }

    @After
    public void reset() {
        for (Pris pris1:
                prisliste.getPriser()) {
            prisliste.deletePris(pris1);
        }
    }

}
