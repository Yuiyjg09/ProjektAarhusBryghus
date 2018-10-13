package application.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Prisliste {
    private String navn;
    private String beskrivelse;
    private LocalDateTime datoStart;
    private LocalDateTime datoSlut;
    private ArrayList<Pris> priser = new ArrayList<Pris>();
    private HashMap<Produkt, Double> priser2 = new HashMap<>();

    public Prisliste(String navn, String beskrivelse, LocalDateTime datoStart, LocalDateTime datoSlut) {
        this.navn = navn;
        this.beskrivelse = beskrivelse;
        this.datoStart = datoStart;
        this.datoSlut = datoSlut;
    }

    public HashMap<Produkt, Double> getPriser2() {
        return new HashMap<>(priser2);
    }

    public void setPriser2(HashMap<Produkt, Double> priser2) {
        this.priser2 = priser2;
    }

    public void addProduktPris(double pris, Produkt produkt) {
        this.priser2.put(produkt,pris);
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

    public void removePris(Pris pris) {
        this.priser.remove(pris);
    }

    public void setPriser(ArrayList<Pris> priser) {
        this.priser = priser;
    }

    public Pris createPris(double stkpris, Produkt produkt) {
        Pris pris = new Pris(stkpris, produkt, this);
        produkt.addPris(pris);
        this.addPris(pris);
        return pris;
    }

    public void updatePris(double stkpris, Pris pris) {
        pris.setPris(stkpris);
    }

    public void deletePris(Pris pris) {
        pris.getProdukt().removePris(pris);
        pris.getPrisliste().removePris(pris);
        //pris.setProdukt(null);
        //pris.setPrisliste(null);
    }

    @Override
    public String toString() {
        if (priser == null) {
            return navn + " (ingen)";
        } else {
            return navn + " (" + priser.size() + ")";
        }
    }
}
