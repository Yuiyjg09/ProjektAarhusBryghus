package gui;

import application.model.Pris;
import application.model.Prisliste;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import storage.Storage;

public class PrislistePane extends GridPane {
    private Label lblPrislister, lblProdukter;
    private ListView<Prisliste> lwPrislister;
    private ListView<Pris> lwPriser;

    public PrislistePane() {
        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(10);
        this.setGridLinesVisible(false);

        lblPrislister = new Label("Prislister");
        this.add(lblPrislister,0,0);

        lblProdukter = new Label("Produkter");
        this.add(lblProdukter,1,0);

        lwPrislister = new ListView<>();
        this.add(lwPrislister,0,1);
        lwPrislister.getItems().addAll(Storage.getPrislister());

        lwPriser = new ListView<>();
        this.add(lwPriser,1,1);

        ChangeListener<Prisliste> pll = (op, oldItem, newItem) -> this.selectedPrislisteChanged();
        lwPrislister.getSelectionModel().selectedItemProperty().addListener(pll);
    }

    private void selectedPrislisteChanged() {
        lwPriser.getItems().clear();
        Prisliste pl = lwPrislister.getSelectionModel().getSelectedItem();
        if (pl != null) {
            lwPriser.getItems().addAll(pl.getPriser());
        }
    }
}
