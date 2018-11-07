package gui;

import application.controller.Controller;
import application.model.*;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import storage.Storage;

import java.util.HashMap;

class OpretGaveAeskeWindow extends Stage {
    private Produktkategori produktkategori;
    private Produkt produktet, produktToRemove;
    private HashMap<Produkt, Integer> indhold = new HashMap<>();
    private TextField txfNavn, txfStoerrelse, txfLagerAntal, txfAntal, txfPris;
    private ListView<Produkt> lvwProduktToAdd, lvwProduktsToRemove;
    private ComboBox<Produktkategori> cbPk;

    OpretGaveAeskeWindow() {
        this.setTitle("Opret Gaveæske");
        GridPane pane = new GridPane();
        this.initContent(pane);

        Scene scene = new Scene(pane);
        this.setScene(scene);
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

        Label lblPrislister = new Label("Mulige Produkter:");
        pane.add(lblPrislister, 0,4);

        Label lblPrislisterAdded = new Label("Tilføjede Produkter:");
        pane.add(lblPrislisterAdded, 2,4);

        Label lblAntal = new Label("Angiv antal (stk.):");
        pane.add(lblAntal,1,4);

        txfNavn = new TextField();
        pane.add(txfNavn,0,1);

        txfStoerrelse = new TextField();
        pane.add(txfStoerrelse,0,3);

        txfLagerAntal = new TextField();
        pane.add(txfLagerAntal,1,3);

        txfAntal = new TextField("1");
        pane.add(txfAntal,1,5);

        cbPk = new ComboBox<>();
        pane.add(cbPk,1,1);
        cbPk.getItems().addAll(Storage.getProduktkategorier());

        ChangeListener<Produktkategori> produktkategoriChangeListener = (oitempk, olditempk, newitempk) -> this.selectChangeListenerProduktKategori();
        cbPk.getSelectionModel().selectedItemProperty().addListener(produktkategoriChangeListener);

        lvwProduktToAdd = new ListView<>();
        pane.add(lvwProduktToAdd,0,5, 1,4);
        lvwProduktToAdd.getItems().addAll(Storage.getProdukter());

        ChangeListener<Produkt> produktChangeListener = (oitem, olditem, newitem) -> this.selectChangeListenerProduktSelected();
        lvwProduktToAdd.getSelectionModel().selectedItemProperty().addListener(produktChangeListener);

        lvwProduktsToRemove = new ListView<>();
        pane.add(lvwProduktsToRemove,2,5,1,4);
        lvwProduktsToRemove.getItems().addAll();

        ChangeListener<Produkt> produktAddedChangeListener = (oitem1, olditem1, newitem1) -> this.selectChangeListenerProduktToRemove();
        lvwProduktsToRemove.getSelectionModel().selectedItemProperty().addListener(produktAddedChangeListener);

        Label lblPris = new Label("Pris (DKK):");
        pane.add(lblPris,0, 11);

        txfPris = new TextField();
        pane.add(txfPris, 0, 12);

        Button btnAdd = new Button("Tilføj Produkt");
        pane.add(btnAdd,1,6);
        btnAdd.setOnAction(event -> this.addAction());

        Button btnRemove = new Button("Fjern Produkt");
        pane.add(btnRemove,1,7);
        btnRemove.setOnAction(event -> this.removeAction());

        Button btnOk = new Button("Opret Produkt");
        pane.add(btnOk, 0,13);
        btnOk.setOnAction(event -> this.okAction());

        Button btnCancel = new Button("Afbryd");
        pane.add(btnCancel, 2,13);
        btnCancel.setOnAction(event -> this.cancelAction());

    }

    private void selectChangeListenerProduktKategori() { produktkategori = cbPk.getSelectionModel().getSelectedItem(); }

    private void selectChangeListenerProduktSelected() { produktet = lvwProduktToAdd.getSelectionModel().getSelectedItem(); }

    private void selectChangeListenerProduktToRemove() { produktToRemove = lvwProduktsToRemove.getSelectionModel().getSelectedItem(); }

    private void okAction() {
        if (!txfNavn.getText().isEmpty()
                && !txfStoerrelse.getText().isEmpty()
                && !txfLagerAntal.getText().isEmpty()
                && !txfPris.getText().isEmpty()
                && produktkategori != null) {
            try {
                int lagerantal = Integer.parseInt(txfLagerAntal.getText());
                double stoerrelse = Double.parseDouble(txfStoerrelse.getText());
                double pris = Double.parseDouble(txfPris.getText());
                GaveAeske gaveAeske = Controller.createGaveAeske(txfNavn.getText(), stoerrelse, lagerantal, produktkategori);

                for (Produkt produkt:
                     indhold.keySet()) {
                    Controller.addIndholdToGaveAeske(gaveAeske, produkt, indhold.get(produkt));
                    produkt.setLagerAntal(produkt.getLagerAntal() - indhold.get(produkt));
                }
                Controller.getGaveAeskePrisliste().createPris(pris, gaveAeske);

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
        if (!lvwProduktsToRemove.getItems().contains(produktet)) {
            if (!txfAntal.getText().isEmpty()) {
                if (produktet != null) {
                    try {
                        if (Integer.parseInt(txfAntal.getText()) > produktet.getLagerAntal()) {
                            lvwProduktsToRemove.getItems().add(produktet);
                            indhold.put(produktet, Integer.parseInt(txfAntal.getText()));
                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Fejl - Ugyldigt Antal");
                            alert.setContentText("Ugyldigt Antal - Der er kun: " + produktet.getLagerAntal() + " stk på lager af: " + produktet.getNavn());
                            alert.showAndWait();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Manglende Pris - Indtast en gyldig pris");
                        alert.setTitle("Manglende Pris");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Ingen prisliste valgt - Vælg venligst en prisliste");
                    alert.setTitle("Fejl - Ingen prisliste valgt");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Ingen pris - Indtast venligst en gyldig pris");
                alert.setTitle("Fejl - Ingen pris");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Prislisten er allerede tilføjet - Hvis du vil ændre prisen, så skal prislisten fjernes først og derefter tilføjes igen");
            alert.setTitle("Fejl - Prislisten er allerede tilføjet");
            alert.showAndWait();
        }
    }

    private void removeAction() {
        if (produktToRemove != null && lvwProduktsToRemove.getItems().contains(produktToRemove)) {
            lvwProduktsToRemove.getItems().remove(produktToRemove);
            indhold.remove(produktToRemove);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Ingen Prisliste valgt");
            alert.setTitle("Fejl");
            alert.showAndWait();
        }
    }
}
