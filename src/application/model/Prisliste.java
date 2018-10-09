package application.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Prisliste {
    private String navn;
    private String beskrivelse;
    private LocalDateTime datoStart;
    private LocalDateTime datoSlut;
    private ArrayList<Pris> priser = new ArrayList<Pris>();

    public Prisliste(String navn, String beskrivelse, LocalDateTime datoStart, LocalDateTime datoSlut) {
        this.navn = navn;
        this.beskrivelse = beskrivelse;
        this.datoStart = datoStart;
        this.datoSlut = datoSlut;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public LocalDateTime getDatoStart() {
        return datoStart;
    }

    public void setDatoStart(LocalDateTime datoStart) {
        this.datoStart = datoStart;
    }

    public LocalDateTime getDatoSlut() {
        return datoSlut;
    }

    public void setDatoSlut(LocalDateTime datoSlut) {
        this.datoSlut = datoSlut;
    }

    public ArrayList<Pris> getPriser() {
        return new ArrayList<Pris>(priser);
    }

    public void addPris(Pris pris) {
        this.priser.add(pris);
    }
}
