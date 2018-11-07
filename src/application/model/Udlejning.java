package application.model;

import java.time.LocalDate;

public class Udlejning extends Salg{
    private boolean isPantBetalt;
    private double pant;
    private LocalDate datoStart;
    private LocalDate datoSlut;
    private Sellable pantBetalingsmetode;

    public Udlejning(double pant, LocalDate datoStart, LocalDate datoSlut) {
        super();
        this.pant = pant;
        this.datoStart = datoStart;
        this.datoSlut = datoSlut;
        this.isPantBetalt = false;
        this.pantBetalingsmetode = null;
    }

    public boolean isPantBetalt() {
        return isPantBetalt;
    }

    public double getPant() {
        return pant;
    }

    public void setPant(double pant) {
        this.pant = pant;
    }

    public LocalDate getDatoStart() {
        return datoStart;
    }

    public void setDatoStart(LocalDate datoStart) {
        this.datoStart = datoStart;
    }

    public LocalDate getDatoSlut() {
        return datoSlut;
    }

    public void setDatoSlut(LocalDate datoSlut) {
        this.datoSlut = datoSlut;
    }

    public void betalPant(Sellable pantBetalingsmetode) {
        if (isPantBetalt()) {
            this.isPantBetalt = false;
        } else {
            this.isPantBetalt = true;
            setPantBetalingsmetode(pantBetalingsmetode);
        }
    }

    public void setPantBetalt(boolean pantBetalt) {
        isPantBetalt = pantBetalt;
    }

    public Sellable getPantBetalingsmetode() {
        return pantBetalingsmetode;
    }

    private void setPantBetalingsmetode(Sellable pantBetalingsmetode) {
        this.pantBetalingsmetode = pantBetalingsmetode;
    }

    @Override
    public void beregnTotalPris() {
        if (isPantBetalt()) {
            double prisTotal = 0.0;
            for (Antal antal:
                super.getAntalProdukter()) {
                prisTotal += antal.beregnPris();
            }
            super.setTotalPris(prisTotal - getPant());
        } else super.beregnTotalPris();
    }

    @Override
    public String toString() {
        return "Udlejning{" +
                "isPantBetalt=" + isPantBetalt +
                ", pant=" + pant +
                ", datoStart=" + datoStart +
                ", datoSlut=" + datoSlut +
                ", pantBetalingsmetode=" + pantBetalingsmetode +
                ", nummer=" + getNummer() +
                "salgsdato=" + super.salgsdato +
                ", isBetalt=" + super.isBetalt +
                ", totalPris=" + super.totalPris +
                ", betalingsmetode=" + super.betalingsmetode +
                ", antalProdukter=" + super.antalProdukter +
                '}';
    }
}
