package gui;

import application.controller.Controller;
import application.model.Pris;
import application.model.Prisliste;
import application.model.Produkt;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;

class OpretPrislisteWindow extends Stage {
    private TextArea txaBeskrivelse;
    private ListView<Produkt> lvwProdukter, lvwTilfoejet;
    private TextField txfNavn, txfDatoStart, txfDatoSlut, txfPris;
    private DatePicker dpStart, dpSlut;
    private Produkt produktSelected, produktToRemove;
    private HashMap<Produkt, Double> priser = new HashMap<>();
    private Prisliste prislisten = null;
    private CheckBox[] dage = new CheckBox[DayOfWeek.values().length];

    OpretPrislisteWindow() {
        GridPane pane = new GridPane();
        pane.setGridLinesVisible(false);
        Scene scene = new Scene(pane);
        this.initContent(pane);
        this.setScene(scene);
    }

    OpretPrislisteWindow(Prisliste prisliste) {
        this.prislisten = prisliste;
        GridPane pane = new GridPane();
        pane.setGridLinesVisible(false);
        Scene scene = new Scene(pane);
        this.initContent(pane);
        this.setScene(scene);
    }

    private void initContent(GridPane gridPane) {
        //Navn
        Label lblNavn = new Label("Navn: ");
        gridPane.add(lblNavn, 0, 0);
        txfNavn = new TextField();
        gridPane.add(txfNavn, 0, 1, 1, 1);
        if (prislisten != null) {
            txfNavn.setText(prislisten.getNavn());
        }

        //DatoStart
        Label lblDatoStart = new Label("Start-Dato: ");
        gridPane.add(lblDatoStart, 0, 2);
        dpStart = new DatePicker();
        gridPane.add(dpStart, 0, 3);
        txfDatoStart = new TextField("HH:mm");
        gridPane.add(txfDatoStart, 1, 3);
        if (prislisten != null && prislisten.getDatoStart() != null) {
            dpStart.setValue(prislisten.getDatoStart().toLocalDate());
            txfDatoStart.setText(prislisten.getDatoStart().toLocalTime().toString());
        }

        //DatoSlut
        Label lblDatoSlut = new Label("Slut-Dato");
        gridPane.add(lblDatoSlut, 0, 5);
        dpSlut = new DatePicker();
        gridPane.add(dpSlut, 0, 6);
        txfDatoSlut = new TextField("HH:mm");
        gridPane.add(txfDatoSlut, 1, 6);
        if (prislisten != null && prislisten.getDatoSlut() != null) {
            dpSlut.setValue(prislisten.getDatoSlut().toLocalDate());
            txfDatoSlut.setText(prislisten.getDatoSlut().toLocalTime().toString());
        }

        //Beskrivelse
        Label lblBeskrivelse = new Label("Beskrivelse: ");
        gridPane.add(lblBeskrivelse, 2, 0);
        txaBeskrivelse = new TextArea();
        gridPane.add(txaBeskrivelse, 2, 1, 1, 1);
        if (prislisten != null) {
            txaBeskrivelse.setText(prislisten.getBeskrivelse());
        }

        //Produkter ikke-tilføjet
        Label lblProdukter = new Label("Produkter Ikke-Tilføjet");
        gridPane.add(lblProdukter, 0, 7);
        lvwProdukter = new ListView<>();
        gridPane.add(lvwProdukter, 0, 8);
        lvwProdukter.getItems().addAll(Controller.getProdukter());

        ChangeListener<Produkt> produktChangeListener = (opitem1, olditem1, newitem1) -> this.selectChangeListnerProdukt();
        lvwProdukter.getSelectionModel().selectedItemProperty().addListener(produktChangeListener);

        //Pris
        Label lblPris = new Label("Pris: ");
        gridPane.add(lblPris, 1, 8);
        txfPris = new TextField();
        gridPane.add(txfPris, 1, 9);

        //Produkter tilføjet
        Label lblProdukterTilfoejet = new Label("Produkter Tilføjet");
        gridPane.add(lblProdukterTilfoejet,2, 7);
        lvwTilfoejet = new ListView<>();
        gridPane.add(lvwTilfoejet, 2, 8);
        if (prislisten != null) {
          ArrayList<Produkt> produkter = new ArrayList<>();
            for (Pris pris:
                 prislisten.getPriser()) {
                produkter.add(pris.getProdukt());
            }
            lvwTilfoejet.getItems().addAll(produkter);
        } else {
            lvwTilfoejet.getItems().addAll();
        }

        ChangeListener<Produkt> produktAddChangeListener = (opitem, olditem, newitem) -> this.selectChangeListnerAdded();
        lvwTilfoejet.getSelectionModel().selectedItemProperty().addListener(produktAddChangeListener);

        //Dage
        Label lbldage = new Label("Dage");
        gridPane.add(lbldage, 2, 3);
        HBox box = new HBox(7);
        for (int i = 0; i < DayOfWeek.values().length; i++) {
            CheckBox dag = dage[i] = new CheckBox(DayOfWeek.of(i + 1).name());
            box.getChildren().add(dag);
        }
        gridPane.add(box, 2, 4);
        if (prislisten != null) {
            for (CheckBox dag :
                    dage) {
                if (prislisten.getGyldigeDage().contains(DayOfWeek.valueOf(dag.getText()))) {
                    dag.setSelected(true);
                }
            }
        }

        //Buttons
        Button btnAdd = new Button("Tilføj");
        gridPane.add(btnAdd, 1, 10);
        btnAdd.setOnAction(event -> this.addAction());

        Button btnRemove = new Button("Fjern");
        gridPane.add(btnRemove, 1, 11);
        btnRemove.setOnAction(event -> this.removeAction());

        Button btnOK = new Button("Opret Prisliste");
        gridPane.add(btnOK, 0, 12);
        btnOK.setOnAction(event -> this.okAction());

        Button btnCancel = new Button("Afbryd");
        gridPane.add(btnCancel, 1, 12);
        btnCancel.setOnAction(event -> this.cancelAction());
    }

    private void selectChangeListnerProdukt() {
        produktSelected = lvwProdukter.getSelectionModel().getSelectedItem();
    }

    private void selectChangeListnerAdded() {
        produktToRemove = lvwTilfoejet.getSelectionModel().getSelectedItem();
    }

    private void addAction() {
        if (produktSelected != null
                && !lvwTilfoejet.getItems().contains(produktSelected)
                && !txfPris.getText().isEmpty()) {
            lvwTilfoejet.getItems().add(produktSelected);
            try {
                priser.put(produktSelected, Double.parseDouble(txfPris.getText()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Ingen produkter valgt");
            alert.setTitle("Fejl");
            alert.showAndWait();
        }
    }

    private void removeAction() {
        if (produktToRemove != null && lvwTilfoejet.getItems().contains(produktToRemove)) {
            lvwTilfoejet.getItems().remove(produktToRemove);
            priser.remove(produktToRemove);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Fejl");
            alert.setTitle("Fejl");
            alert.showAndWait();
        }
    }

    private void okAction() {
        if(!txfNavn.getText().isEmpty()
                && !txfDatoStart.getText().isEmpty()
                && !txfDatoSlut.getText().isEmpty()
                && !txaBeskrivelse.getText().isEmpty()
                && isDagSelected()) {
            try {
                LocalDateTime dtStart = LocalDateTime.of(dpStart.getValue(), LocalTime.parse(txfDatoStart.getText(), DateTimeFormatter.ofPattern("HH:mm")));
                LocalDateTime dtSlut = LocalDateTime.of(dpSlut.getValue(), LocalTime.parse(txfDatoSlut.getText(), DateTimeFormatter.ofPattern("HH:mm")));

                if (prislisten != null) {
                    Controller.updatePrisliste(prislisten, txfNavn.getText(), txaBeskrivelse.getText(), dtStart, dtSlut);
                } else {
                    prislisten = Controller.createPrisliste(txfNavn.getText(),
                            txaBeskrivelse.getText(),
                            dtStart,
                            dtSlut);
                }
                for (Pris pris:
                        prislisten.getPriser()) {
                    Controller.deletePris(pris);
                }
                for (Produkt key:
                        priser.keySet()) {
                    Controller.createPris(Double.parseDouble(priser.get(key).toString()), key, prislisten);
                }
                Controller.resetDage(prislisten);

                for (CheckBox box:
                        dage) {
                    if (box.isSelected()) {
                        Controller.addDayToPrisliste(DayOfWeek.valueOf(box.getText()), prislisten);
                    }
                }

                this.close();
            } catch (DateTimeParseException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Forkert Dato-format: HH-mm");
                alert.setTitle("Format Fejl");
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Fejl: Manglende Information");
            alert.setTitle("Indtast info om den nye prisliste");
            alert.showAndWait();
        }
    }

    private boolean isDagSelected() {
        for (CheckBox dag:
             dage) {
            if (dag.isSelected()) {
                return true;
            }
        }
        return false;
    }

    private void cancelAction() {
        this.close();
    }


}
