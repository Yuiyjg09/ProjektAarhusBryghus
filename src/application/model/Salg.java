package application.model;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Salg {
    private static int nummer = 0;
    private LocalDateTime salgsdato;
    private boolean isBetalt;
    private double totalPris;
    private Sellable betalingsmetode;
    private ArrayList<Antal> antalProdukter;

    public Salg() {
        nummer++;
        this.salgsdato = LocalDateTime.now();
        this.isBetalt = false;
        this.betalingsmetode = null;
        this.antalProdukter = new ArrayList<>();
    }

    public ArrayList<Antal> getAntalProdukter() {
        return new ArrayList<Antal>(antalProdukter);
    }

    public void createAntal(Produkt produkt, int antal) {
        Antal antal1 = new Antal(produkt, this, antal);
        this.addAntal(antal1);
    }

    public void deleteAntal(Antal antal) {
        this.removeAntal(antal);
        antal.setProdukt(null);
        antal.setSalg(null);
    }

    public void setAntalProdukter(ArrayList<Antal> antalProdukter) {
        this.antalProdukter = antalProdukter;
    }

    public void addAntal(Antal antal) {
        antalProdukter.add(antal);
    }

    public void removeAntal(Antal antal) {
        antalProdukter.remove(antal);
    }

    public static int getNummer() {
        return nummer;
    }

    public LocalDateTime getSalgsdato() {
        return salgsdato;
    }

    public boolean isBetalt() {
        return isBetalt;
    }

    public double getTotalPris() {
        return totalPris;
    }

    public Sellable getBetalingsmetode() {
        return betalingsmetode;
    }

    private void setBetalt(boolean isBetalt) {
        this.isBetalt = isBetalt;
    }

    private void setBetalingsmetode(Sellable betalingsmetode) {
        this.betalingsmetode = betalingsmetode;
    }

    public void betal(boolean isBetalt, Sellable betalingsmetode) {
        if (isBetalt) {
            this.setBetalt(true);
            this.setBetalingsmetode(betalingsmetode);
        } else {
            this.setBetalt(false);
            this.setBetalingsmetode(null);
        }
    }
    
    public void beregnTotalPris() {
        double prisTotal = 0.0;
        for (Antal antal:
             this.getAntalProdukter()) {
            prisTotal += antal.beregnPris();
        }
        this.totalPris = prisTotal;
    }
}
