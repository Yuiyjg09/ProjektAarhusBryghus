package application.model;

public class Pris {
    private double pris;
    private Produkt produkt;
    private Prisliste prisliste;

    public Pris(double pris, Produkt produkt, Prisliste prisliste) {
        this.pris = pris;
        this.produkt = produkt;
        this.prisliste = prisliste;
    }

    public Produkt getProdukt() {
        return produkt;
    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

    public Prisliste getPrisliste() {
        return prisliste;
    }

    public void setPrisliste(Prisliste prisliste) {
        this.prisliste = prisliste;
    }

    public double getPris() {
        return pris;
    }

    public void setPris(double pris) {
        this.pris = pris;
    }
}
