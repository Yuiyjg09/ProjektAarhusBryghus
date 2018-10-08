package application.model;

public class Produkt {
    private String navn;
    private String metrik;
    private int lagerAntal;
    private Produktkategori kategori;

    public Produkt(String navn, String metrik, int lagerAntal) {
        this.navn = navn;
        this.metrik = metrik;
        this.lagerAntal = lagerAntal;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getMetrik() {
        return metrik;
    }

    public void setMetrik(String metrik) {
        this.metrik = metrik;
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
