package gui;

import application.controller.Controller;
import application.model.Pris;
import application.model.Prisliste;
import application.model.Produkt;
import application.model.Produktkategori;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import storage.Storage;

import java.util.HashMap;

class OpretProduktWindow extends Stage {
    private Produktkategori produktkategori;
    private Produkt produktet;
    private Prisliste prislisteSelected, prislisteToRemove;
    private HashMap<Prisliste, Double> priser = new HashMap<>();
    private TextField txfNavn, txfStoerrelse, txfLagerAntal, txfPris;
    private ListView<Prisliste> lvwPrislisteSelected, lvwPrislisteToRemove;
    private ComboBox<Produktkategori> cbPk;

    OpretProduktWindow(String title, Produktkategori produktkategori, Produkt produkt) {
        this.produktkategori = produktkategori;
        this.produktet = produkt;
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setResizable(false);

        this.setTitle(title);
        GridPane pane = new GridPane();
        this.initContent(pane);

        Scene scene = new Scene(pane);
        this.setScene(scene);

    }

    OpretProduktWindow(String title) {
        this(title, null, null);
    }

    private void initContent(GridPane pane) {
        pane.setPadding(new Insets(10));
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setGridLinesVisible(false);

        Label lblNavn = new Label("Angiv navn:");
        pane.add(lblNavn,0,0);

        Label lblStoerrelse = new Label("Angiv størrelse:");
        pane.add(lblStoerrelse, 0,2);

        Label lblProduktKategori = new Label("Vælg produktkategori:");
        pane.add(lblProduktKategori, 1,0);

        Label lblLagerAntal = new Label("Angiv lagerantal:");
        pane.add(lblLagerAntal, 1,2);

        Label lblPrislister = new Label("Mulige prislister:");
        pane.add(lblPrislister, 0,4);

        Label lblPrislisterAdded = new Label("Mulige prislister:");
        pane.add(lblPrislisterAdded, 2,4);

        Label lblPris = new Label("Angiv pris (DKK):");
        pane.add(lblPris,1,4);

        Label lblError = new Label("");
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

        ChangeListener<Produktkategori> produktkategoriChangeListener = (oitempk, olditempk, newitempk) -> this.selectChangeListenerProduktKategori();
        cbPk.getSelectionModel().selectedItemProperty().addListener(produktkategoriChangeListener);

        lvwPrislisteSelected = new ListView<>();
        pane.add(lvwPrislisteSelected,0,5, 1,4);
        lvwPrislisteSelected.getItems().addAll(Storage.getPrislister());

        ChangeListener<Prisliste> prislisteChangeListener = (oitem, olditem, newitem) -> this.selectChangeListenerPrislisteSelected();
        lvwPrislisteSelected.getSelectionModel().selectedItemProperty().addListener(prislisteChangeListener);

        lvwPrislisteToRemove = new ListView<>();
        pane.add(lvwPrislisteToRemove,2,5,1,4);
        lvwPrislisteToRemove.getItems().addAll();

        ChangeListener<Prisliste> prislisteAddedChangeListener = (oitem1, olditem1, newitem1) -> this.selectChangeListenerPrislisteToRemove();
        lvwPrislisteToRemove.getSelectionModel().selectedItemProperty().addListener(prislisteAddedChangeListener);

        Button btnAdd = new Button("Tilføj");
        pane.add(btnAdd,1,6);
        btnAdd.setOnAction(event -> this.addAction());

        Button btnRemove = new Button("Fjern");
        pane.add(btnRemove,1,7);
        btnRemove.setOnAction(event -> this.removeAction());

        Button btnOk = new Button("Ok");
        pane.add(btnOk, 0,9);
        btnOk.setOnAction(event -> this.okAction());

        Button btnCancel = new Button("Afbryd");
        pane.add(btnCancel, 2,9);
        btnCancel.setOnAction(event -> this.cancelAction());

        initControls();
    }

    private void selectChangeListenerProduktKategori() { produktkategori = cbPk.getSelectionModel().getSelectedItem(); }

    private void selectChangeListenerPrislisteSelected() { prislisteSelected = lvwPrislisteSelected.getSelectionModel().getSelectedItem(); }

    private void selectChangeListenerPrislisteToRemove() { prislisteToRemove = lvwPrislisteToRemove.getSelectionModel().getSelectedItem(); }

    private void okAction() {
        if (!txfNavn.getText().isEmpty()
            && !txfStoerrelse.getText().isEmpty()
            && !txfLagerAntal.getText().isEmpty()
            && produktkategori != null) {
            try {
                int lagerantal = Integer.parseInt(txfLagerAntal.getText());
                double stoerrelse = Double.parseDouble(txfStoerrelse.getText());
                if (produktet != null) {
                    Controller.updateProdukt(txfNavn.getText(), stoerrelse, lagerantal, produktkategori, produktet);
                } else {
                    produktet = Controller.createProdukt(txfNavn.getText(), stoerrelse, lagerantal, produktkategori);
                }
                for (Pris pris :
                        produktet.getPriser()) {
                    Controller.deletePris(pris);
                }
                for (Prisliste key :
                        priser.keySet()) {
                    Controller.createPris(priser.get(key), produktet, key);
                }

                this.close();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Indtast en gyldig pris");
                alert.setTitle("Ugyldig Pris");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Indtast manglende information");
            alert.setTitle("Manglende Information");
            alert.showAndWait();
        }
    }

    private void cancelAction() {this.close();}

    private void addAction() {
        if (prislisteSelected != null
            && !lvwPrislisteToRemove.getItems().contains(prislisteSelected)
            && !txfPris.getText().isEmpty()) {
            lvwPrislisteToRemove.getItems().add(prislisteSelected);
            try {
                priser.put(prislisteSelected, Double.parseDouble(txfPris.getText()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Indtast en gyldig pris");
                alert.setTitle("Manglende Pris");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Ingen prisliste valgt/Ingen pris/Produktet er allerede tilføjet");
            alert.setTitle("Fejl");
            alert.showAndWait();
        }
    }

    private void removeAction() {
        if (prislisteToRemove != null && lvwPrislisteToRemove.getItems().contains(prislisteToRemove)) {
            lvwPrislisteToRemove.getItems().remove(prislisteToRemove);
            priser.remove(prislisteToRemove);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Ingen Prisliste valgt");
            alert.setTitle("Fejl");
            alert.showAndWait();
        }
    }

    private void initControls() {
        if (produktet != null) {
            txfNavn.setText(produktet.getNavn());
            txfStoerrelse.setText("" + produktet.getStoerrelse());
            txfLagerAntal.setText("" + produktet.getLagerAntal());
            cbPk.setValue(produktet.getKategori());
            lvwPrislisteToRemove.getItems().addAll(Controller.getProduktPrislister(produktet));
        }
    }

}
