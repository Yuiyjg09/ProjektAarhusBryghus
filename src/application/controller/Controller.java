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
            e.printStackTrace();
        }
    }

    //------------------------------------------------
    // Produkt

    public static Produkt createProdukt(String navn, double stoerrelse, int lagerAntal, Produktkategori produktkategori, ArrayList<Pris> priser) {
        Produkt produkt = new Produkt(navn, stoerrelse, lagerAntal, produktkategori);
        produkt.setPriser(priser);
        produktkategori.addProdukt(produkt);
        return produkt;
    }

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
    }

    //------------------------------------------------
    // Prisliste

    public static Prisliste createPrisliste(String navn, String beskrivelse, LocalDateTime datoStart, LocalDateTime datoSlut, ArrayList<Pris> priser) {
        Prisliste p1 = new Prisliste(navn, beskrivelse, datoStart, datoSlut);
        p1.setPriser(priser);
        Storage.addPrisliste(p1);
        return p1;
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

    public static Pris createPris(double pris, Produkt produkt, Prisliste prisliste) {
        Pris p1 = new Pris(pris, produkt, prisliste);
        return p1;
    }

    public static void updatePris(Pris pris, double nypris, Produkt produkt, Prisliste prisliste) {
        pris.setPris(nypris);
    }
}
