import application.controller.Controller;
import application.model.Klippekort;
import application.model.Rundvisning;
import application.model.Sellable;
import application.model.Udlejning;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

public class testController {
    private Udlejning udlejningen;
    private Klippekort klippekortet;
    private Rundvisning rundvisningen;

    @Before
    public void setup() {
        Controller.initStorage();
    }

    @Test
    public void tc1cou() {
        udlejningen = Controller.createUdlejning(5,
                LocalDate.of(2018, 10, 30),
                LocalDate.of(2018, 11, 22));
        udlejningen.betal(true, Sellable.KONTANT);
        Assert.assertTrue(Controller.getAktiveUdlejninger().contains(udlejningen));
    }

    @Test
    public void tc2cou() {
        udlejningen = Controller.createUdlejning(5,
                LocalDate.of(2018, 10, 30),
                LocalDate.of(2018, 11, 3));
        udlejningen.betal(true, Sellable.KONTANT);
        Assert.assertFalse(Controller.getAktiveUdlejninger().contains(udlejningen));
    }

    @Test
    public void tc3cou() {
        udlejningen = Controller.createUdlejning(5,
                LocalDate.of(2018, 11, 21),
                LocalDate.of(2018, 11, 22));
        udlejningen.betal(true, Sellable.KONTANT);
        Assert.assertTrue(Controller.getAktiveUdlejninger().contains(udlejningen));
    }

    @Test
    public void tc1cok() {
        klippekortet = Controller.createKlippekort("Et klippekort", 1, 1,
                Controller.getKlippekortKategori(), 10);
        Assert.assertTrue(Controller.getAktiveKlippekort().contains(klippekortet));
    }

    @Test
    public void tc2cok() {
        klippekortet = Controller.createKlippekort("Et klippekort", 1, 1,
                Controller.getKlippekortKategori(), 0);
        Assert.assertFalse(Controller.getAktiveKlippekort().contains(klippekortet));
    }

    @Test
    public void tc3cok() {
        klippekortet = Controller.createKlippekort("Et klippekort", 1, 1,
                Controller.getKlippekortKategori(), -1);
        Assert.assertFalse(Controller.getAktiveKlippekort().contains(klippekortet));
    }

    @Test
    public void tc1cor() {
        rundvisningen = Controller.createRundvisning("En rundvisning", 1, 1,
                Controller.getRundvisningsproduktkategori(), LocalDate.of(2018, 11, 22),
                LocalTime.of(13, 0), LocalTime.of(14, 30));
        Assert.assertTrue(Controller.getAktiveRundvisninger().contains(rundvisningen));
    }

    @Test
    public void tc2cor() {
        rundvisningen = Controller.createRundvisning("En rundvisning", 1, 1,
                Controller.getRundvisningsproduktkategori(), LocalDate.of(2018, 11, 6),
                LocalTime.of(13, 0), LocalTime.of(14, 30));
        Assert.assertFalse(Controller.getAktiveRundvisninger().contains(rundvisningen));
    }

    @Test
    public void tc3cor() {
        rundvisningen = Controller.createRundvisning("En rundvisning", 1, 1,
                Controller.getRundvisningsproduktkategori(), LocalDate.of(2018, 11, 5),
                LocalTime.of(13, 0), LocalTime.of(14, 30));
        Assert.assertFalse(Controller.getAktiveRundvisninger().contains(rundvisningen));
    }
}
