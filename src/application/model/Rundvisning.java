package application.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Rundvisning extends Produkt{
    private LocalDate dato;
    private LocalTime tidStart;
    private LocalTime tidSlut;

    public Rundvisning(String navn, double stoerrelse, int lagerAntal, Produktkategori kategori, LocalDate dato, LocalTime tidStart, LocalTime tidSlut) {
        super(navn, stoerrelse, lagerAntal, kategori);
        this.dato = dato;
        this.tidStart = tidStart;
        this.tidSlut = tidSlut;
    }

    public LocalDate getDato() {
        return dato;
    }

    public void setDato(LocalDate dato) {
        this.dato = dato;
    }

    public LocalTime getTidStart() {
        return tidStart;
    }

    public void setTidStart(LocalTime tidStart) {
        this.tidStart = tidStart;
    }

    public LocalTime getTidSlut() {
        return tidSlut;
    }

    public void setTidSlut(LocalTime tidSlut) {
        this.tidSlut = tidSlut;
    }

    @Override
    public String toString() {
        return "Rundvisning{" +
                "navn=" + super.getNavn() +
                ", dato=" + dato +
                ", tidStart=" + tidStart +
                ", tidSlut=" + tidSlut +
                '}';
    }
}
