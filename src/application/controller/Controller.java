package application.controller;

import application.model.*;
import storage.Storage;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
    public static void deleteProduktkategori(Produktkategori produktkategori) {
        try
        {
            if (produktkategori.getProdukter().isEmpty()) {
                Storage.removeProduktkategori(produktkategori);
            } else {
                throw new Exception("Denne kategori har tilhørende produkter");
            }
        }
         catch (Exception e) {
            e.getMessage();
            //e.printStackTrace();
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
    public static void updateProdukt(String navn, double stoerrelse, int lagerAntal, Produktkategori produktkategori, Produkt produkt, ArrayList<Pris> priser) {
        produkt.setNavn(navn);
        produkt.setStoerrelse(stoerrelse);
        produkt.setLagerAntal(lagerAntal);
        produkt.getKategori().removeProdukt(produkt);
        produkt.setKategori(produktkategori);
        produktkategori.addProdukt(produkt);
        produkt.setPriser(priser);
    }

    public static void deleteprodukt(Produkt produkt) {
        produkt.getKategori().removeProdukt(produkt);
        produkt.setKategori(null);
        for (Pris p : produkt.getPriser()) {
            Controller.deletePris(p);
        }
    }

    public static ArrayList<Prisliste> getProduktPrislister (Produkt p) {
        ArrayList<Prisliste> prisliste = new ArrayList<>();
        for (Pris pris : p.getPriser()) {
            prisliste.add(pris.getPrisliste());
        }

        return prisliste;
    }

    //------------------------------------------------
    // Prisliste
    public static Prisliste createPrisliste(String navn, String beskrivelse, LocalDateTime datoStart, LocalDateTime datoSlut) {
        Prisliste p1 = new Prisliste(navn, beskrivelse, datoStart, datoSlut);
        //p1.setPriser(priser);
        Storage.addPrisliste(p1);
        return p1;
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
     * @param priser ArrayList
     */
    public static void updatePrisliste(Prisliste prisliste, String navn, String beskrivelse, LocalDateTime datoStart, LocalDateTime datoSlut, ArrayList<Pris> priser) {
        prisliste.setNavn(navn);
        prisliste.setBeskrivelse(beskrivelse);
        prisliste.setDatoStart(datoStart);
        prisliste.setDatoSlut(datoSlut);

        for (Pris p : prisliste.getPriser()) {
            p.getProdukt().getPriser().remove(p);
        }

        prisliste.setPriser(priser);

        for (Pris p2 : priser) {
            p2.getProdukt().addPris(p2);
        }
    }

    public static void deletePrisliste (Prisliste prisliste) {
        for (Pris p : prisliste.getPriser()) {
            p.getProdukt().getPriser().remove(p);
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
    // Initialize Storage
    public static void initStorage() {

        Prisliste pl1 = Controller.createPrisliste("Butik","Standard butikspriser", null, null);
        Prisliste pl2 = Controller.createPrisliste("Fredagsbar","Fredagsbar priser", null, null);

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
