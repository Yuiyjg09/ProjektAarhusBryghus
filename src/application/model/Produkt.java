package application.model;

public class Produkt {
    private String navn;
    private double stoerrelse;
    private int lagerAntal;
    private Produktkategori kategori;

    public Produkt(String navn, double stoerrelse, int lagerAntal, Produktkategori kategori) {
        this.navn = navn;
        this.stoerrelse = stoerrelse;
        this.lagerAntal = lagerAntal;
        this.kategori = kategori;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public double getStoerrelse() {
        return stoerrelse;
    }

    public void setStoerrelse(double stoerrelse) {
        this.stoerrelse = stoerrelse;
    }

    public int getLagerAntal() {
        return lagerAntal;
    }

    public void setLagerAntal(int lagerAntal) {
        this.lagerAntal = lagerAntal;
    }

    public Produktkategori getKategori() {
        return kategori;
    }

    public void setKategori(Produktkategori kategori) {
        this.kategori = kategori;
    }
}
