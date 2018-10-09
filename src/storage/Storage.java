package storage;

import application.model.Produktkategori;

import java.util.ArrayList;

public class Storage {
    public static ArrayList<Produktkategori> produktkategorier = new ArrayList<>();

    public static ArrayList<Produktkategori> getProduktkategorier() {
        return new ArrayList<Produktkategori>(produktkategorier);
    }

    public static void addProduktkategori(Produktkategori produktkategori) {
        produktkategorier.add(produktkategori);
    }

    public static void removeProduktkategori(Produktkategori produktkategori) {
        produktkategorier.remove(produktkategori);
    }
}
