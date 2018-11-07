package application.model;

public class Klippekort extends Produkt {
    private int klip;
    private int kortNummer;
    private static int nummer = 0;

    public Klippekort(String navn, double stoerrelse, int lagerAntal, Produktkategori kategori, int klip) {
        super(navn, stoerrelse, lagerAntal, kategori);
        nummer++;
        this.kortNummer = nummer;
        this.klip = klip;
    }

    public int getKlip() {
        return klip;
    }

    public void setKlip(int klip) {
        this.klip = klip;
    }

    public int getKortNummer() {
        return kortNummer;
    }

    public void setKortNummer(int kortNummer) {
        this.kortNummer = kortNummer;
    }

    public static int getNummer() {
        return nummer;
    }

    @Override
    public String toString() {
        return "Klippekort{" +
                "klip=" + klip +
                ", kortNummer=" + kortNummer +
                '}';
    }
}
