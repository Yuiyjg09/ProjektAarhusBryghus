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
import javafx.scene.layout.VBox;
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

    private Label lblNavn, lblStoerrelse, lblLagerAntal, lblPk, lblPl1, lblPl2, lblPris;
    private TextField txfNavn, txfStoerrelse, txfLagerAntal, txfPris;
    private ListView<Prisliste> lwPl1, lwPrislisterToAdd;
    private ListView<Pris> lwPriser;
    private Button btnAdd, btnRemove, btnOk, btnCancel;
    private ComboBox<Produktkategori> cbPk;
    private HBox hb1, hb2,hb3;
    private VBox vb1, vb2, vb3, vb4, vb5;

    private void initContent(GridPane pane) {
        pane.setPadding(new Insets(20));
        pane.setHgap(20);
        pane.setVgap(20);
        pane.setGridLinesVisible(false);

        lblNavn = new Label("Angiv navn:");
        lblStoerrelse = new Label("Angiv størrelse:");
        lblPk = new Label("Vælg produktkategori:");
        lblLagerAntal = new Label("Angiv lagerantal:");
        lblPl1 = new Label("Mulige prislister:");
        lblPl2 = new Label("Valgte prislister:");
        lblPris = new Label("Angiv pris (DKK):");
        txfNavn = new TextField();
        txfStoerrelse = new TextField();
        txfLagerAntal = new TextField();
        txfPris = new TextField();
        cbPk = new ComboBox<>();
        cbPk.getItems().addAll(Storage.getProduktkategorier());
        lwPl1 = new ListView<>();
        lwPl1.getItems().addAll(Storage.getPrislister());
        lwPrislisterToAdd = new ListView<>();

        btnAdd = new Button("Tilføj");
        btnAdd.setOnAction(event -> this.addAction());
        btnAdd.setMaxWidth(Double.MAX_VALUE);

        btnRemove = new Button("Fjern");
        btnRemove.setOnAction(event -> this.removeAction());
        btnRemove.setMaxWidth(Double.MAX_VALUE);

        btnOk = new Button("Ok");
        btnOk.setOnAction(event -> this.okAction());
        btnOk.setMaxWidth(Double.MAX_VALUE);

        btnCancel = new Button("Cancel");
        btnCancel.setOnAction(event -> this.cancelAction());
        btnCancel.setMaxWidth(Double.MAX_VALUE);

        vb1 = new VBox(10);
        vb1.getChildren().addAll(lblNavn,txfNavn,lblLagerAntal,txfLagerAntal);

        vb2 = new VBox(10);
        vb2.getChildren().addAll(lblPk,cbPk,lblStoerrelse,txfStoerrelse);

        vb3 = new VBox(10);
        vb3.getChildren().addAll(lblPl1,lwPl1);

        vb4 = new VBox(10);
        vb4.getChildren().setAll(lblPris, txfPris,btnAdd,btnRemove);

        vb5 = new VBox(10);
        vb5.getChildren().addAll(lblPl2,lwPrislisterToAdd);

        hb1 = new HBox(20);
        pane.add(hb1,0,0);
        hb1.getChildren().addAll(vb1,vb2);

        hb2 = new HBox(20);
        pane.add(hb2,0,1);
        hb2.getChildren().addAll(vb3,vb4,vb5);
        hb2.setAlignment(Pos.CENTER_LEFT);

        hb3 = new HBox(20);
        pane.add(hb3,0,2);
        hb3.getChildren().addAll(btnOk, btnCancel);
        hb3.setAlignment(Pos.CENTER);
        hb3.setHgrow(btnOk, Priority.ALWAYS);
        hb3.setHgrow(btnCancel, Priority.ALWAYS);

        ChangeListener<Prisliste> l1 = (op, oldObj, newObj) -> this.selectedPrislisteChanged();
        lwPrislisterToAdd.getSelectionModel().selectedItemProperty().addListener(l1);

        initControls();
    }

    private void selectedPrislisteChanged() {
        Prisliste selectedPl = lwPrislisterToAdd.getSelectionModel().getSelectedItem();
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
                lwPrislisterToAdd.getItems().add(pl);
            } else {
                createErrAlert("Produktet eksistere allerede på denne prisliste");
            }


        } else {
            createErrAlert("Ups - der er ikke valgt en prisliste");
        }
    }

    private void removeAction() {
        Prisliste pl = lwPrislisterToAdd.getSelectionModel().getSelectedItem();

        if (pl != null) {
            priserToAdd.remove(pl);
            lwPrislisterToAdd.getItems().remove(pl);
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
                lwPrislisterToAdd.getItems().add(p.getPrisliste());
                priserToAdd.put(p.getPrisliste(),p.getPris());
            }
        } else {

        }
    }

}
