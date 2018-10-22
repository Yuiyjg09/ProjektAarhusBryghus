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
    }

    public static ArrayList<Prisliste> getProduktPrislister (Produkt produkt) {
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
            if (salg.getSalgsdato().toLocalDate() == LocalDate.now()) {
                result.add(salg);
            }
        }
        return result;
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
    // Dage
    public static void addDayToPrisliste(DayOfWeek day, Prisliste prisliste) {
        if (!prisliste.getGyldigeDage().contains(day)) {
            prisliste.addDayOfWeek(day);
        }
    }

    public static void removeDayFromPrisliste(DayOfWeek day, Prisliste prisliste) {
        if (prisliste.getGyldigeDage().contains(day)) {
            prisliste.removeDayOfWeek(day);
        }
    }

    public static void addAllDaysToPrisliste(Prisliste prisliste) {
        resetDage(prisliste);
        prisliste.setGyldigeDage(new ArrayList<>(Arrays.asList(DayOfWeek.values())));
    }

    public static void resetDage(Prisliste prisliste) {
        prisliste.setGyldigeDage(new ArrayList<>());
    }

    //------------------------------------------------
    // Initialize Storages
    public static void initStorage() {

        Prisliste pl1 = Controller.createPrisliste("Butik","Standard butikspriser", LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 0)),
                LocalDateTime.of(LocalDate.of(2030, 1, 1), LocalTime.of(10, 0)));
        Prisliste pl2 = Controller.createPrisliste("Fredagsbar","Fredagsbar priser", LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(15, 0)),
                LocalDateTime.of(LocalDate.of(2030, 1, 1), LocalTime.of(19, 0)));

        Controller.addAllDaysToPrisliste(pl1);
        Controller.addDayToPrisliste(DayOfWeek.FRIDAY, pl2);

        Produktkategori pk1 = Controller.createProduktkategori("Flaske øl", "Indeholder alle varianter af flaske øl", Maalbar.CL);
        Produktkategori pk2 = Controller.createProduktkategori("Fadøl", "Indeholder alle varianter af fadøl", Maalbar.CL);

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
