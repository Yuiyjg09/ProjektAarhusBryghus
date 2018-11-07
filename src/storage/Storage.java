package storage;

import application.model.*;

import java.util.ArrayList;

public class Storage {
    private static ArrayList<Produktkategori> produktkategorier = new ArrayList<>();
    private static ArrayList<Produkt> produkter = new ArrayList<>();
    private static ArrayList<Prisliste> prislister = new ArrayList<>();
    private static ArrayList<Salg> salgs = new ArrayList<>();
    private static ArrayList<Udlejning> udlejnings = new ArrayList<>();
    private static ArrayList<Klippekort> klippekorts = new ArrayList<>();
    private static ArrayList<Rundvisning> rundvisninger = new ArrayList<>();
    private static ArrayList<GaveAeske> gaveAesker = new ArrayList<>();

    public static ArrayList<Produktkategori> getProduktkategorier() {
        return new ArrayList<>(produktkategorier);
    }

    public static void addProduktkategori(Produktkategori produktkategori) {
        produktkategorier.add(produktkategori);
    }

    public static void removeProduktkategori(Produktkategori produktkategori) {
        produktkategorier.remove(produktkategori);
    }

    public static ArrayList<Produkt> getProdukter() {
        return new ArrayList<>(produkter);
    }

    public static void addProdukt(Produkt produkt) {
        produkter.add(produkt);
    }

    public static void removeProdukt(Produkt produkt) {
        produkter.remove(produkt);
    }

    public static ArrayList<Prisliste> getPrislister() {
        return new ArrayList<>(prislister);
    }

    public static void addPrisliste(Prisliste prisliste) {
        prislister.add(prisliste);
    }

    public static void removePrisliste(Prisliste prisliste) {
        prislister.remove(prisliste);
    }

    public static ArrayList<Salg> getSalgs() {
        return new ArrayList<>(salgs);
    }

    public static void addSalg(Salg salg) {
        salgs.add(salg);
    }

    public static void removeSalg(Salg salg) {
        salgs.remove(salg);
    }

    public static ArrayList<Udlejning> getUdlejnings() {
        return new ArrayList<>(udlejnings);
    }

    public static void addUdlejning(Udlejning udlejning) {
        udlejnings.add(udlejning);
    }

    public static void removeUdlejning(Udlejning udlejning) {
        udlejnings.remove(udlejning);
    }

    public static ArrayList<Klippekort> getKlippekorts() {
        return new ArrayList<>(klippekorts);
    }

    public static void addKlippekort(Klippekort klippekort) {
        klippekorts.add(klippekort);
    }

    public static void removeKlippekort(Klippekort klippekort) {
        klippekorts.remove(klippekort);
    }

    public static ArrayList<Rundvisning> getRundvisninger() {
        return new ArrayList<>(rundvisninger);
    }

    public static void addRundvisning(Rundvisning rundvisning) {
        rundvisninger.add(rundvisning);
    }

    public static void removeRundvisning(Rundvisning rundvisning) {
        rundvisninger.remove(rundvisning);
    }

    public static ArrayList<GaveAeske> getGaveAesker() {
        return new ArrayList<>(gaveAesker);
    }

    public static void addGaveAeske(GaveAeske gaveAeske) {
        gaveAesker.add(gaveAeske);
    }

    public static void removeGaveAeske(GaveAeske gaveAeske) {
        gaveAesker.remove(gaveAeske);
    }
}
