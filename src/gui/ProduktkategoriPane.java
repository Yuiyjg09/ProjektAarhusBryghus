package gui;

import application.model.Produkt;
import application.model.Produktkategori;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import storage.Storage;


public class ProduktkategoriPane extends GridPane {
    private Label lblProduktkategorier, lblProdukter;
    private ListView<Produktkategori> lwProduktkategorier;
    private ListView<Produkt> lwProdukter;

    public ProduktkategoriPane() {
        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(10);
        this.setGridLinesVisible(false);

        lblProduktkategorier = new Label("Produktkategorier");
        this.add(lblProduktkategorier,0,0);

        lblProdukter = new Label("Produkter");
        this.add(lblProdukter, 1, 0);

        lwProduktkategorier = new ListView<>();
        this.add(lwProduktkategorier,0,1);
        lwProduktkategorier.getItems().addAll(Storage.getProduktkategorier());

        lwProdukter = new ListView<>();
        this.add(lwProdukter,1,1);

        ChangeListener<Produktkategori> l1 = (op, oldProduktkategori, newProduktkategori) -> this.selectedProduktkategoriChanged();
        lwProduktkategorier.getSelectionModel().selectedItemProperty().addListener(l1);
    }

    private void selectedProduktkategoriChanged() {
        lwProdukter.getItems().clear();
        Produktkategori pk = lwProduktkategorier.getSelectionModel().getSelectedItem();
        if (pk != null) {
            lwProdukter.getItems().addAll(pk.getProdukter());
        }

    }
}
