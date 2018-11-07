import application.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class testPrisliste {
    private Produkt produkt;
    private Prisliste prisliste;
    private double pris;

    @Before
    public void setup() {
        produkt = new Produkt("En ny Øl", 1, 1,
                new Produktkategori("En ny Produktkategori", "En Beskrivelse", Maalbar.cL));
        prisliste = new Prisliste("En ny Prisliste", "En Beskrivelse",
                LocalDateTime.of(2018, 10, 30, 10, 0),
                LocalDateTime.of(2018, 12, 15, 22, 0));
        pris = 35;
    }

    @Test
    public void tc1plc() {
        prisliste.createPris(pris, produkt);
        assertFalse(prisliste.getPriser().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void tc2plc() {
        prisliste.createPris(pris, null);
        assertFalse(prisliste.getPriser().isEmpty());
    }

    @Test
    public void tc3plc() {
        Pris prisen = prisliste.createPris(-35, produkt);
        assertTrue(prisliste.getPriser().contains(prisen));
    }

    @Test
    public void tc1pld() {
        Pris prisen =prisliste.createPris(pris, produkt);

        prisliste.deletePris(prisen);
        assertTrue(prisliste.getPriser().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void tc2pld() {
        Pris prisen =prisliste.createPris(pris, produkt);

        prisliste.deletePris(null);
        assertFalse(prisliste.getPriser().isEmpty());
    }

    @After
    public void reset() {
        for (Pris pris1:
             prisliste.getPriser()) {
            prisliste.deletePris(pris1);
        }
    }
}
