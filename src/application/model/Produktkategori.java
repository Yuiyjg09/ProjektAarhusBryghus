package application.model;

import java.util.ArrayList;

public class Produktkategori {
    private String navn;
    private String beskrivelse;
    private Maalbar metrik;
    private ArrayList<Produkt> produkter;

    public Produktkategori(String navn, String beskrivelse, Maalbar metrik) {
        this.navn = navn;
        this.beskrivelse = beskrivelse;
        this.metrik = metrik;
        this.produkter = new ArrayList<Produkt>();
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public ArrayList<Produkt> getProdukter() {
        return new ArrayList<Produkt>(produkter);
    }

    public void addProdukt(Produkt produkt) {
        this.produkter.add(produkt);
    }

    public void removeProdukt(Produkt produkt) {
        this.produkter.remove(produkt);
    }

    public Maalbar getMetrik() {
        return metrik;
    }

    public void setMetrik(Maalbar metrik) {
        this.metrik = metrik;
    }

    @Override
    public String toString() {
        return "Produktkategori{" +
                "navn='" + navn + '\'' +
                ", metrik=" + metrik +
                '}';
    }
}
