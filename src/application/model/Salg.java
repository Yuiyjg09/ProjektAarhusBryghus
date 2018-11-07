package application.model;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Salg {
    private static int nummer = 0;
    private int salgsNummer;
    LocalDateTime salgsdato;
    boolean isBetalt;
    double totalPris;
    Sellable betalingsmetode;
    ArrayList<Antal> antalProdukter;

    public Salg() {
        nummer++;
        this.salgsNummer = nummer;
        this.salgsdato = LocalDateTime.now();
        this.isBetalt = false;
        this.betalingsmetode = null;
        this.antalProdukter = new ArrayList<>();
        this.totalPris = 0.0;
    }

    public ArrayList<Antal> getAntalProdukter() {
        return new ArrayList<>(antalProdukter);
    }

    public Antal createAntal(Produkt produkt, int antal) {
        Antal antal1 = new Antal(produkt, this, antal);
        this.addAntal(antal1);
        return antal1;
    }

    public void deleteAntal(Antal antal) {
        this.removeAntal(antal);
        antal.setProdukt(null);
        antal.setSalg(null);
    }

    public void setAntalProdukter(ArrayList<Antal> antalProdukter) {
        this.antalProdukter = antalProdukter;
    }

    private void addAntal(Antal antal) {
        antalProdukter.add(antal);
    }

    public void removeAntal(Antal antal) {
        antalProdukter.remove(antal);
    }

    public int getNummer() {
        return salgsNummer;
    }

    public LocalDateTime getSalgsdato() {
        return salgsdato;
    }

    public boolean isBetalt() {
        return isBetalt;
    }

    public double getTotalPris() {
        beregnTotalPris();
        return totalPris;
    }

    public double getTotalPrisEdited() {
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
       setBetalt(isBetalt);
       setBetalingsmetode(betalingsmetode);
    }

    public void beregnTotalPris() {
        double prisTotal = 0.0;
        for (Antal antal:
             this.getAntalProdukter()) {
            prisTotal += antal.beregnPris();
        }
        this.totalPris = prisTotal;
    }

    public void setTotalPris(double totalPris) {
        this.totalPris = totalPris;
    }

    @Override
    public String toString() {
        return "Salg{" +
                "nummer=" + salgsNummer +
                ", salgsdato=" + salgsdato +
                ", isBetalt=" + isBetalt +
                ", totalPris=" + this.getTotalPris() +
                "DKK, betalingsmetode=" + betalingsmetode +
                ", antalProdukter=" + antalProdukter +
                '}';
    }
}
