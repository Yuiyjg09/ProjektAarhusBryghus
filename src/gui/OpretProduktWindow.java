package gui;

import application.controller.Controller;
import application.model.Pris;
import application.model.Prisliste;
import application.model.Produkt;
import application.model.Produktkategori;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import storage.Storage;

import java.util.ArrayList;
import java.util.HashMap;

public class OpretProduktWindow extends Stage {
    private Produktkategori produktkategori;
    private Produkt produkt;
    private HashMap<Prisliste, Double> priserToAdd = new HashMap<>();

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
    private ListView<Pris> lwPriser;
    private Button btnAdd, btnRemove, btnOk, btnCancel;
    private ComboBox<Produktkategori> cbPk;
    private HBox hbox;
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

        lblPl2 = new Label("Valgte prislister:");
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

        lwPl2 = new ListView<>();
        pane.add(lwPl2,2,5,1,4);

        btnAdd = new Button("Tilføj");
        pane.add(btnAdd,1,6);
        btnAdd.setOnAction(event -> this.addAction());
        btnAdd.setMaxWidth(Double.MAX_VALUE);

        btnRemove = new Button("Fjern");
        pane.add(btnRemove,1,7);
        btnRemove.setOnAction(event -> this.removeAction());
        btnRemove.setMaxWidth(Double.MAX_VALUE);

        btnOk = new Button("Ok");
       // pane.add(btnOk, 0,9);
        btnOk.setOnAction(event -> this.okAction());
        btnOk.setMaxWidth(Double.MAX_VALUE);

        btnCancel = new Button("Cancel");
        //pane.add(btnCancel, 2,9);
        btnCancel.setOnAction(event -> this.cancelAction());
        btnCancel.setMaxWidth(Double.MAX_VALUE);

        hbox = new HBox(20);
        pane.add(hbox,1,9);
        hbox.getChildren().addAll(btnOk, btnCancel);
        hbox.setAlignment(Pos.CENTER);
        hbox.setHgrow(btnOk, Priority.ALWAYS);
        hbox.setHgrow(btnCancel, Priority.ALWAYS);

        ChangeListener<Prisliste> l1 = (op, oldObj, newObj) -> this.selectedPrislisteChanged();
        lwPl2.getSelectionModel().selectedItemProperty().addListener(l1);

        initControls();
    }

    private void selectedPrislisteChanged() {
        Prisliste selectedPl = lwPl2.getSelectionModel().getSelectedItem();
        if (selectedPl != null) {
            txfPris.setText("" + priserToAdd.get(selectedPl));
        }
    }

    private void okAction() {
        Produktkategori pk = cbPk.getSelectionModel().getSelectedItem();
        String navn = txfNavn.getText();
        double stoerrelse = -1;
        int lagerAntal = -1;
        ArrayList<Pris> priser = new ArrayList<>();

        if (navn.length() < 1) {
            createErrAlert("Der skal angives et produktnavn");
            return;
        }

        try {
            stoerrelse = Double.parseDouble(txfStoerrelse.getText().trim());
        } catch (NumberFormatException e) {
            createErrAlert("Størrelse skal angives decimal eller heltal");
            return;
        }

        try {
            lagerAntal = Integer.parseInt(txfLagerAntal.getText().trim());
        } catch (NumberFormatException e) {
            createErrAlert("Lagerantal skal angives som heltal");
            return;
        }

        if (pk == null) {
            createErrAlert("Der skal angives en produktkategori");
            return;
        }

        if (priserToAdd.size() < 1) {
            createErrAlert("OBS! Produktet er ikke tilføjet en prisliste");
            return;
        }

        if (produkt != null) {
            for (Pris pris : produkt.getPriser()) {
                Controller.deletePris(pris);
            }

            for (Prisliste key: priserToAdd.keySet()) {
                Pris nyPris = Controller.createPris(Double.parseDouble(priserToAdd.get(key).toString()),produkt,key);
                priser.add(nyPris);
            }

           Controller.updateProdukt(navn, stoerrelse, lagerAntal, produktkategori, produkt, priser);

            this.hide();
        } else {
            Produkt nytProdukt = Controller.createProdukt(navn,stoerrelse,lagerAntal,pk);
            for (Prisliste key: priserToAdd.keySet()) {
                Pris nyPris = Controller.createPris(Double.parseDouble(priserToAdd.get(key).toString()),nytProdukt,key);
                priser.add(nyPris);
            }

            this.hide();
        }

    }

    private void cancelAction() {
        this.hide();
    }

    private void addAction() {
        double pris = -1;
        Prisliste pl = lwPl1.getSelectionModel().getSelectedItem();

        if (pl != null) {
            try {
                pris = Double.parseDouble(txfPris.getText().trim());
            } catch (NumberFormatException e) {
                createErrAlert("Pris skal angives som decimal eller heltal");
                return;
            }
            if (!priserToAdd.containsKey(pl)) {
                priserToAdd.put(pl, pris);
                lwPl2.getItems().add(pl);
            } else {
                createErrAlert("Produktet eksistere allerede på denne prisliste");
            }


        } else {
            createErrAlert("Ups - der er ikke valgt en prisliste");
        }
    }

    private void removeAction() {
        Prisliste pl = lwPl2.getSelectionModel().getSelectedItem();

        if (pl != null) {
            priserToAdd.remove(pl);
            lwPl2.getItems().remove(pl);
        } else {
            createErrAlert("Ups - der er ikke valgt en prisliste");
        }
    }

    private Alert createAlert(String title, String headerText) {
        Stage owner = (Stage) this.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.initOwner(owner);
        alert.setHeaderText(headerText);

        return alert;
    }

    private Alert createErrAlert(String headerText) {
        Stage owner = (Stage) this.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(headerText);
        alert.initOwner(owner);
        alert.showAndWait();

        return alert;
    }

    private void initControls() {
        if (produkt != null) {
            txfNavn.setText(produkt.getNavn());
            txfStoerrelse.setText("" + produkt.getStoerrelse());
            txfLagerAntal.setText("" + produkt.getLagerAntal());
            cbPk.setValue(produkt.getKategori());
            for (Pris p : produkt.getPriser()) {
                lwPl2.getItems().add(p.getPrisliste());
                priserToAdd.put(p.getPrisliste(),p.getPris());
            }
        } else {

        }
    }

}
