package application.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class Antal {
    private Produkt produkt;
    private Salg salg;
    private int antal;

    Antal(Produkt produkt, Salg salg, int antal) {
        this.produkt = produkt;
        this.salg = salg;
        this.antal = antal;
    }

    public Produkt getProdukt() {
        return produkt;
    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

    public Salg getSalg() {
        return salg;
    }

    public void setSalg(Salg salg) {
        this.salg = salg;
    }

    public int getAntal() {
        return antal;
    }

    public void setAntal(int antal) {
        this.antal = antal;
    }

    public double beregnPris() {
        LocalDateTime dateTime = LocalDateTime.MAX;
        double prisen = 0.0;

        for (Pris pris:
             getProdukt().getPriser()) {
            if (pris.getPrisliste().getDatoSlut().isBefore(dateTime)
                && pris.getPrisliste().getDatoSlut().isAfter(this.getSalg().getSalgsdato())
                && pris.getPrisliste().getDatoStart().isBefore(this.getSalg().getSalgsdato())
                && pris.getPrisliste().getGyldigeDage().contains(LocalDateTime.now().getDayOfWeek())
                  && pris.getPrisliste().getDatoSlut().toLocalTime().isAfter(this.getSalg().getSalgsdato().toLocalTime())
                && pris.getPrisliste().getDatoStart().toLocalTime().isBefore(this.getSalg().getSalgsdato().toLocalTime())
            ) {
                dateTime = pris.getPrisliste().getDatoSlut();
                prisen = pris.getPris();
            }
        }
        return prisen * this.getAntal();
    }

    @Override
    public String toString() {
        return produkt.getNavn() + ", antal: " + antal + ", pris: " + beregnPris() + " DKK";
    }
}
