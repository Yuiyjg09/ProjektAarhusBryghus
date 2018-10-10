package gui;

import application.controller.Controller;
import application.model.Pris;
import application.model.Prisliste;
import application.model.Produkt;
import application.model.Produktkategori;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import storage.Storage;

import java.util.ArrayList;

public class OpretProduktWindow extends Stage {
    private Produktkategori produktkategori;
    private Produkt produkt;

    public OpretProduktWindow(String title, Produktkategori produktkategori, Produkt produkt) {
        this.produktkategori = produktkategori;
        this.produkt = produkt;
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setResizable(false);

        this.setTitle(title);
        GridPane pane = new GridPane();
        this.initContent(pane);

        Scene scene = new Scene(pane);
        this.setScene(scene);

    }

    public OpretProduktWindow(String title) {
        this(title, null, null);
    }

    private Label lblNavn, lblStoerrelse, lblLagerAntal, lblPk, lblPl1, lblPl2, lblPris, lblError;
    private TextField txfNavn, txfStoerrelse, txfLagerAntal, txfPris;
    private ListView<Prisliste> lwPl1, lwPl2;
    private Button btnAdd, btnRemove, btnOk, btnCancel;
    private ComboBox<Produktkategori> cbPk;

    private void initContent(GridPane pane) {
        pane.setPadding(new Insets(10));
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setGridLinesVisible(false);

        lblNavn = new Label("Angiv navn:");
        pane.add(lblNavn,0,0);

        lblStoerrelse = new Label("Angiv størrelse:");
        pane.add(lblStoerrelse, 0,2);

        lblPk = new Label("Vælg produktkategori:");
        pane.add(lblPk, 1,0);

        lblLagerAntal = new Label("Angiv lagerantal:");
        pane.add(lblLagerAntal, 1,2);

        lblPl1 = new Label("Mulige prislister:");
        pane.add(lblPl1, 0,4);

        lblPl2 = new Label("Mulige prislister:");
        pane.add(lblPl2, 2,4);

        lblPris = new Label("Angiv pris (DKK):");
        pane.add(lblPris,1,4);

        lblError = new Label("");
        pane.add(lblError, 0,8,1,3);

        txfNavn = new TextField();
        pane.add(txfNavn,0,1);

        txfStoerrelse = new TextField();
        pane.add(txfStoerrelse,0,3);

        txfLagerAntal = new TextField();
        pane.add(txfLagerAntal,1,3);

        txfPris = new TextField();
        pane.add(txfPris,1,5);

        cbPk = new ComboBox<>();
        pane.add(cbPk,1,1);
        cbPk.getItems().addAll(Storage.getProduktkategorier());

        lwPl1 = new ListView<>();
        pane.add(lwPl1,0,5, 1,4);
        lwPl1.getItems().addAll(Storage.getPrislister());

        lwPl1 = new ListView<>();
        pane.add(lwPl1,2,5,1,4);

        btnAdd = new Button("Tilføj");
        pane.add(btnAdd,1,6);

        btnRemove = new Button("Fjern");
        pane.add(btnRemove,1,7);

        btnOk = new Button("Ok");
        pane.add(btnOk, 0,9);

        btnCancel = new Button("Cancel");
        pane.add(btnCancel, 2,9);

        initControls();
    }

    private void okAction() {}

    private void cancelAction() {}

    private void addAction() {}

    private void removeAction() {}

    private void initControls() {
        if (produkt != null) {
            txfNavn.setText(produkt.getNavn());
            txfStoerrelse.setText("" + produkt.getStoerrelse());
            txfLagerAntal.setText("" + produkt.getLagerAntal());
            cbPk.setValue(produkt.getKategori());
            lwPl2.getItems().addAll(Controller.getProduktPrislister(produkt));
        }
    }

}
