package application.controller;
import application.model.*;
import storage.Storage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller {

    //------------------------------------------------
    // Poduktkategori
    /***
     * Metoden opretter, tilføjer til storage og returnere et nyt produktkategori objekt.
     * @param navn String
     * @param beskrivelse String
     * @param metrik Metrik
     * @return Ny produktkategori
     */
    public static Produktkategori createProduktkategori(String navn, String beskrivelse, Maalbar metrik) {
        Produktkategori produktkategori = new Produktkategori(navn, beskrivelse, metrik);
        Storage.addProduktkategori(produktkategori);
        return produktkategori;
    }

    public static ArrayList<Produktkategori> getProduktkategorier() {
        return Storage.getProduktkategorier();
    }

    /***
     * Metode opdatere et produktkategori objekt
     * @param navn String
     * @param beskrivelse String
     * @param metrik Metrik
     * @param produktkategori Produktkategori
     */
    public static void updateProduktkategori(String navn, String beskrivelse, Maalbar metrik, Produktkategori produktkategori) {
        produktkategori.setNavn(navn);
        produktkategori.setBeskrivelse(beskrivelse);
        produktkategori.setMetrik(metrik);
    }

    /***
     * Metoden fjerner et produktkategori objekt fra storage, hvis den ikke indeholder nogen tilknyttede produkter
     * @param produktkategori Produktkategori
     */

    public static void deleteProduktkategori(Produktkategori produktkategori) throws Exception {
        if (produktkategori.getProdukter().isEmpty()) {
            Storage.removeProduktkategori(produktkategori);
        } else {
            throw new Exception("Denne kategori har tilhørende produkter");
        }
    }

    //------------------------------------------------
    // Produkt
    /***
     * Metoden opretter et produkt objekt, relationer til oprettede pris-objekter, relation til et produktkategori objekt og returnere det nye produkt objekt
     * @param navn String
     * @param stoerrelse double
     * @param lagerAntal int
     * @param produktkategori Produktkategori
     //* @param priser ArrayList
     * @return Produkt
     */
    public static Produkt createProdukt(String navn, double stoerrelse, int lagerAntal, Produktkategori produktkategori) {
        Produkt produkt = new Produkt(navn, stoerrelse, lagerAntal, produktkategori);
        Storage.addProdukt(produkt);
        produktkategori.addProdukt(produkt);
        return produkt;
    }

    public static ArrayList<Produkt> getProdukter() {
        return Storage.getProdukter();
    }

    public static ArrayList<Produkt> getProdukterOutOfStock() {
        ArrayList<Produkt> outOfStock = new ArrayList<>();
        for (Produkt produkt:
             getProdukter()) {
            if (produkt.getLagerAntal() == 0) {
                outOfStock.add(produkt);
            }
        }
        return outOfStock;
    }

    /***
     * Metoden updatere et produkt objekt og dens relation et produktkategori objekt, samt opdatere dens relation til Pris
     * @param navn String
     * @param stoerrelse double
     * @param lagerAntal int
     * @param produktkategori Produktkategori
     * @param produkt Produkt
     */
    public static void updateProdukt(String navn, double stoerrelse, int lagerAntal, Produktkategori produktkategori, Produkt produkt) {
        produkt.setNavn(navn);
        produkt.setStoerrelse(stoerrelse);
        produkt.setLagerAntal(lagerAntal);
        produkt.getKategori().removeProdukt(produkt);
        produkt.setKategori(produktkategori);
        produktkategori.addProdukt(produkt);
    }

    public static void deleteprodukt(Produkt produkt) {
        produkt.getKategori().removeProdukt(produkt);
        produkt.setKategori(null);
        for (Pris p : produkt.getPriser()) {
            Controller.deletePris(p);
        }
        Storage.removeProdukt(produkt);
    }

    public static ArrayList<Prisliste> getProduktPrislister(Produkt produkt) {
        ArrayList<Prisliste> prisliste = new ArrayList<>();
        for (Pris pris : produkt.getPriser()) {
            prisliste.add(pris.getPrisliste());
        }

        return prisliste;
    }

    //------------------------------------------------
    // Prisliste
    public static Prisliste createPrisliste(String navn, String beskrivelse, LocalDateTime datoStart, LocalDateTime datoSlut) {
        Prisliste prisliste = new Prisliste(navn, beskrivelse, datoStart, datoSlut);
        Storage.addPrisliste(prisliste);
        return prisliste;
    }

    public static ArrayList<Prisliste> getPrislister() {
        return Storage.getPrislister();
    }

    /***
     * Metoden updatere et prisliste objekt, samt relationerne mellem pris og produkt for det pågældende prisliste objekt
     * @param prisliste Prisliste
     * @param navn String
     * @param beskrivelse String
     * @param datoStart Datetime
     * @param datoSlut Datetime
     */
    public static void updatePrisliste(Prisliste prisliste, String navn, String beskrivelse, LocalDateTime datoStart, LocalDateTime datoSlut) {
        prisliste.setNavn(navn);
        prisliste.setBeskrivelse(beskrivelse);
        prisliste.setDatoStart(datoStart);
        prisliste.setDatoSlut(datoSlut);
    }

    public static void deletePrisliste (Prisliste prisliste) {
        for (Pris pris : prisliste.getPriser()) {
            pris.getProdukt().getPriser().remove(pris);
        }

        Storage.removePrisliste(prisliste);
    }

    //------------------------------------------------
    // Pris
    public static void createPris(double pris, Produkt produkt, Prisliste prisliste) {
        prisliste.createPris(pris, produkt);
    }

    public static void deletePris(Pris pris) {
        pris.getPrisliste().deletePris(pris);
    }

    //------------------------------------------------
    // Salg

    public static Salg createSalg() {
        Salg salg = new Salg();
        Storage.addSalg(salg);
        return salg;
    }

    public static void deleteSalg(Salg salg) {
        Storage.removeSalg(salg);
    }

    public static void betal(Salg salg, boolean isBetalt, Sellable betalingsmetode) {
        salg.betal(isBetalt, betalingsmetode);
    }

    public static ArrayList<Salg> getSalgForToday() {
        ArrayList<Salg> result = new ArrayList<>();
        for (Salg salg:
             Storage.getSalgs()) {
            if (salg.getSalgsdato().toLocalDate().equals(LocalDate.now())) {
                result.add(salg);
            }
        }
        for (Udlejning udlejning:
             Storage.getUdlejnings()) {
            if (udlejning.getSalgsdato().toLocalDate().equals(LocalDate.now())) {
                result.add(udlejning);
            }
        }
        return result;
    }

    public static ArrayList<Salg> getSalg() {
        return Storage.getSalgs();
    }

    //------------------------------------------------
    // Antal

    public static Antal createAntal(Produkt produkt, Salg salg, int antal) {
        return salg.createAntal(produkt, antal);
    }

    public static void deleteAntal(Antal antal) {
        antal.getSalg().removeAntal(antal);
    }

    //------------------------------------------------
    // Udlejning

    public static Udlejning createUdlejning(double pant, LocalDate datoStart, LocalDate datoSlut) {
        Udlejning udlejning = new Udlejning(pant, datoStart, datoSlut);
        Storage.addUdlejning(udlejning);
        return udlejning;
    }

    public static void betalPant(Udlejning udlejning, Sellable pantBetalingsmetode) {
        udlejning.betalPant(pantBetalingsmetode);
    }

    public static ArrayList<Udlejning> getUdlejninger() {
        return Storage.getUdlejnings();
    }

    public static ArrayList<Udlejning> getAktiveUdlejninger() {
        ArrayList<Udlejning> aktiveUdlejninger = new ArrayList<>();
        for (Udlejning udlejning:
                getUdlejninger()) {
            if (udlejning.getDatoSlut().isAfter(LocalDate.now())
                    || !udlejning.isBetalt()) {
                aktiveUdlejninger.add(udlejning);
            }
        }
        return aktiveUdlejninger;
    }

    //------------------------------------------------
    // Dage
    public static void addDayToPrisliste(DayOfWeek day, Prisliste prisliste) {
        if (!prisliste.getGyldigeDage().contains(day)) {
            prisliste.addDayOfWeek(day);
        }
    }

    public   static void addAllDaysToPrisliste(Prisliste prisliste) {
        resetDage(prisliste);
        prisliste.setGyldigeDage(new ArrayList<>(Arrays.asList(DayOfWeek.values())));
    }

    public static void resetDage(Prisliste prisliste) {
        prisliste.setGyldigeDage(new ArrayList<>());
    }

    //------------------------------------------------
    //Klippekort
    public static Klippekort createKlippekort(String navn, double stoerrelse, int lagerAntal, Produktkategori produktkategori, int klip) {
        Klippekort klippekort = new Klippekort(navn, stoerrelse, lagerAntal, produktkategori, klip);
        Storage.addKlippekort(klippekort);
        return klippekort;
    }

    public static ArrayList<Klippekort> getAktiveKlippekort() {
        ArrayList<Klippekort> aktiveKlippekorts = new ArrayList<>();
        for (Klippekort klippekort:
             Storage.getKlippekorts()) {
            if (klippekort.getKlip() > 0) {
                aktiveKlippekorts.add(klippekort);
            }
        }
        return aktiveKlippekorts;
    }

    public static Produktkategori getKlippekortKategori() {
        Produktkategori produktkategorien = null;
        for (Produktkategori produktkategori:
             Storage.getProduktkategorier()) {
            if (produktkategori.getNavn().equals("Klippekort")) {
                produktkategorien = produktkategori;
            }
        }
        return produktkategorien;
    }

    public static Prisliste getKlippekortPrisliste() {
        Prisliste prislisten = null;
        for (Prisliste prisliste:
             Storage.getPrislister()) {
            if (prisliste.getNavn().equals("Klippekort")) {
                prislisten = prisliste;
            }
        }
        return prislisten;
    }

    //------------------------------------------------
    //Gaveæske
    public static GaveAeske createGaveAeske(String navn, double stoerrelse, int lagerAntal, Produktkategori produktkategori) {
        GaveAeske gaveAeske = new GaveAeske(navn, stoerrelse, lagerAntal, produktkategori);
        Storage.addGaveAeske(gaveAeske);
//        Storage.addProdukt(gaveAeske);
        return gaveAeske;
    }

    public static void deleteGaveAeske(GaveAeske gaveAeske) {
        Storage.removeGaveAeske(gaveAeske);
    }

    public static void addIndholdToGaveAeske(GaveAeske gaveAeske, Produkt produkt, int antal) {
        gaveAeske.putIndhold(produkt, antal);
    }

    public static Prisliste getGaveAeskePrisliste() {
        for (Prisliste prisliste:
             getPrislister()) {
            if (prisliste.getNavn().equals("GaveAesker")){
                return prisliste;
            }
        }
        return null;
    }

    public static ArrayList<GaveAeske> getGaveAesker() {
        return Storage.getGaveAesker();
    }

    //------------------------------------------------
    //Rundvisning
    public static Rundvisning createRundvisning(String navn, double stoerrelse, int lagerAntal, Produktkategori produktkategori, LocalDate dato, LocalTime start, LocalTime slut) {
        Rundvisning rundvisning = new Rundvisning(navn,stoerrelse,lagerAntal,produktkategori, dato, start, slut);
        Storage.addRundvisning(rundvisning);
        return rundvisning;
    }

    public static ArrayList<Rundvisning> getAktiveRundvisninger() {
        ArrayList<Rundvisning> rundvisninger = new ArrayList<>();
        for (Rundvisning rundvisning:
                Storage.getRundvisninger()) {
            if (!rundvisning.getDato().isBefore(LocalDate.now().plusDays(1))) {
                rundvisninger.add(rundvisning);
            }
        }
        return rundvisninger;
    }

    public static Produktkategori getRundvisningsproduktkategori() {
        for (Produktkategori produktkategori:
             getProduktkategorier()) {
            if (produktkategori.getNavn().equals("Rundvisninger")) {
                return produktkategori;
            }
        }
        return null;
    }

    public static Prisliste getRundvisningsPrisliste() {
        for (Prisliste prisliste:
             getPrislister()) {
            if (prisliste.getNavn().equals("Rundvisninger")) {
                return prisliste;
            }
        }
        return null;
    }

    //------------------------------------------------
    // Initialize Storages
    public static void initStorage() {

        Prisliste pl1 = Controller.createPrisliste("Butik","Standard butikspriser", LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(1, 0)),
                LocalDateTime.of(LocalDate.of(2030, 1, 1), LocalTime.of(23, 0)));
        Prisliste pl2 = Controller.createPrisliste("Fredagsbar","Fredagsbar priser", LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(15, 0)),
                LocalDateTime.of(LocalDate.of(2030, 1, 1), LocalTime.of(19, 0)));
        Prisliste pl3 = Controller.createPrisliste("Klippekort","Priser for Klippekort", LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(1, 0)),
                LocalDateTime.of(LocalDate.of(2030, 1, 1), LocalTime.of(23, 0)));
        Prisliste pl4 = Controller.createPrisliste("Rundvisninger","Priser for Rundvisninger", LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(1, 0)),
                LocalDateTime.of(LocalDate.of(2030, 1, 1), LocalTime.of(23, 0)));
        Prisliste pl5 = Controller.createPrisliste("GaveAesker","Priser for Gaveæsker", LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(1, 0)),
                LocalDateTime.of(LocalDate.of(2030, 1, 1), LocalTime.of(23, 0)));

        Controller.addAllDaysToPrisliste(pl1);
        Controller.addDayToPrisliste(DayOfWeek.FRIDAY, pl2);
        Controller.addAllDaysToPrisliste(pl3);
        Controller.addAllDaysToPrisliste(pl4);
        Controller.addAllDaysToPrisliste(pl5);

        Produktkategori pk1 = Controller.createProduktkategori("Flaske øl", "Indeholder alle varianter af flaske øl", Maalbar.cL);
        Produktkategori pk2 = Controller.createProduktkategori("Fadøl", "Indeholder alle varianter af fadøl", Maalbar.cL);
        Controller.createProduktkategori("Klippekort", "Diverse klippekort", Maalbar.Stk);
        Controller.createProduktkategori("Rundvisninger", "Diverse Rundvisninger", Maalbar.Stk);

        Produkt p1 = Controller.createProdukt("Klosterbryg", 50,150, pk1);
        Produkt p2 = Controller.createProdukt("Extra Pilsner", 50, 100,pk1);
        Produkt p3 = Controller.createProdukt("Blondie", 50, 100,pk1);

        Produkt p4 = Controller.createProdukt("Klosterbryg", 40,150, pk2);
        Produkt p5 = Controller.createProdukt("Extra Pilsner", 40, 100,pk2);
        Produkt p6 = Controller.createProdukt("Blondie", 40, 100,pk2);

        Controller.createPris(35, p1, pl1);
        Controller.createPris(35, p2, pl1);
        Controller.createPris(35, p3, pl1);
        Controller.createPris(50, p1, pl2);
        Controller.createPris(50, p2, pl2);
        Controller.createPris(50, p3, pl2);
        Controller.createPris(30, p4, pl2);
        Controller.createPris(30, p5, pl2);
        Controller.createPris(30, p6, pl2);
    }
}
