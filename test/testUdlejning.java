import application.controller.Controller;
import application.model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class testUdlejning {
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
    public void tc1udp() {
        Udlejning udlejning = new Udlejning(5,
                LocalDate.of(2018, 11, 5), LocalDate.of(2018, 11, 28));
        udlejning.createAntal(produkt, antal);
        udlejning.betalPant(Sellable.DANKORT);
        Assert.assertTrue(udlejning.getPantBetalingsmetode() == Sellable.DANKORT && udlejning.isPantBetalt());
    }

    @Test
    public void tc2udp() {
        Udlejning udlejning = new Udlejning(5,
                LocalDate.of(2018, 11, 5), LocalDate.of(2018, 11, 28));
        udlejning.createAntal(produkt, antal);
        udlejning.betalPant(null);
        Assert.assertTrue(udlejning.getPantBetalingsmetode() == null && udlejning.isPantBetalt());
    }

    @Test
    public void tc3udp() {
        Udlejning udlejning = new Udlejning(-5,
                LocalDate.of(2018, 11, 5), LocalDate.of(2018, 11, 28));
        udlejning.createAntal(produkt, antal);
        udlejning.betalPant(Sellable.DANKORT);
        Assert.assertTrue(udlejning.getPantBetalingsmetode() == Sellable.DANKORT && udlejning.isPantBetalt());
    }

    @After
    public void reset() {
        for (Pris pris1:
                prisliste.getPriser()) {
            prisliste.deletePris(pris1);
        }
    }
}
