package application.model;

import java.util.HashMap;

public class GaveAeske extends Produkt {
    private HashMap<Produkt, Integer> indhold = new HashMap<>();

    public GaveAeske(String navn, double stoerrelse, int lagerAntal, Produktkategori kategori) {
        super(navn, stoerrelse, lagerAntal, kategori);
    }

    public void putIndhold(Produkt produkt, int antal) {
        indhold.put(produkt, antal);
    }

    public void removeIndhold(Produkt produkt) {
        indhold.remove(produkt);
    }

    public HashMap<Produkt, Integer> getIndhold() {
        return indhold;
    }

    public void setIndhold(HashMap<Produkt, Integer> indhold) {
        this.indhold = indhold;
    }
}
