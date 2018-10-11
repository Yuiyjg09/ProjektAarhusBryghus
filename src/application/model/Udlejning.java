package application.model;

import java.time.LocalDate;

public class Udlejning extends Salg{
    private boolean isPantBetalt;
    private double pant;
    private LocalDate datoStart;
    private LocalDate datoSlut;

    public Udlejning(double pant, LocalDate datoStart, LocalDate datoSlut) {
        super();
        this.pant = pant;
        this.datoStart = datoStart;
        this.datoSlut = datoSlut;
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

    public void betalPant() {
        if (isPantBetalt()) {
            this.isPantBetalt = false;
        } else {
            this.isPantBetalt = true;
        }
    }
}
