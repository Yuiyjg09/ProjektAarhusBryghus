package storage;

import application.model.Prisliste;
import application.model.Produktkategori;

import java.util.ArrayList;

public class Storage {
    private static ArrayList<Produktkategori> produktkategorier = new ArrayList<>();
    private static ArrayList<Prisliste> prislister = new ArrayList<>();

    public static ArrayList<Produktkategori> getProduktkategorier() {
        return new ArrayList<Produktkategori>(produktkategorier);
    }

    public static void addProduktkategori(Produktkategori produktkategori) {
        produktkategorier.add(produktkategori);
    }

    public static void removeProduktkategori(Produktkategori produktkategori) {
        produktkategorier.remove(produktkategori);
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
}
