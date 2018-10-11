package storage;

import application.model.Prisliste;
import application.model.Produkt;
import application.model.Produktkategori;
import application.model.Salg;

import java.util.ArrayList;

public class Storage {
    private static ArrayList<Produktkategori> produktkategorier = new ArrayList<>();
    private static ArrayList<Produkt> produkter = new ArrayList<>();
    private static ArrayList<Prisliste> prislister = new ArrayList<>();
    private static ArrayList<Salg> salgs = new ArrayList<>();

    public static ArrayList<Produktkategori> getProduktkategorier() {
        return new ArrayList<Produktkategori>(produktkategorier);
    }

    public static void addProduktkategori(Produktkategori produktkategori) {
        produktkategorier.add(produktkategori);
    }

    public static void removeProduktkategori(Produktkategori produktkategori) {
        produktkategorier.remove(produktkategori);
    }

    public static ArrayList<Produkt> getProdukter() {
        return new ArrayList<Produkt>(produkter);
    }

    public static void addProdukt(Produkt produkt) {
        produkter.add(produkt);
    }

    public static void removeProdukt(Produkt produkt) {
        produkter.remove(produkt);
    }

    public static ArrayList<Prisliste> getPrislister() {
        return new ArrayList<Prisliste>(prislister);
    }

    public static void addPrisliste(Prisliste prisliste) {
        prislister.add(prisliste);
    }

    public static void removePrisliste(Prisliste prisliste) {
        prislister.remove(prisliste);
    }

    public static ArrayList<Salg> getSalgs() {
        return new ArrayList<Salg>(salgs);
    }

    public static void addSalg(Salg salg) {
        salgs.add(salg);
    }

    public static void removeSalg(Salg salg) {
        salgs.remove(salg);
    }
}
