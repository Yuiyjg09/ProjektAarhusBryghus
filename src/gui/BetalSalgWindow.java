package gui;

import application.controller.Controller;
import application.model.*;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;



class BetalSalgWindow extends Stage {
    private Salg salget;
    private Udlejning udlejningen;
    private OpretSalgWindow opretSalgWindow;
    private CheckBox udlejningCheckBox;
    private Sellable betalingsmetode = null;
    private ComboBox<Sellable> sellableComboBox;
    private ComboBox<Klippekort> klippekortComboBox;
    private Klippekort klippekortet;
    private TextField txfTotalPris;

    BetalSalgWindow(Salg salget, OpretSalgWindow opretSalgWindow) {
            this.setTitle("Betal Salg");
            this.salget = salget;
            this.opretSalgWindow = opretSalgWindow;
            GridPane pane = new GridPane();
            pane.setGridLinesVisible(false);
            Scene scene = new Scene(pane);
            this.initContent(pane);
            this.setScene(scene);
    }

    BetalSalgWindow(Salg salget) {
            this.setTitle("Betal Salg");
            this.salget = salget;
            GridPane pane = new GridPane();
            pane.setGridLinesVisible(false);
            Scene scene = new Scene(pane);
            this.initContent(pane);
            this.setScene(scene);
    }

    BetalSalgWindow(Udlejning udlejning, OpretSalgWindow opretSalgWindow) {
        this.setTitle("Betal pant for udlejning");
        this.udlejningen = udlejning;
        this.opretSalgWindow = opretSalgWindow;
        GridPane pane = new GridPane();
        pane.setGridLinesVisible(false);
        Scene scene = new Scene(pane);
        this.initContent(pane);
        this.setScene(scene);
    }

    BetalSalgWindow(Udlejning udlejning) {
        this.setTitle("Betal pant for udlejning");
        this.udlejningen = udlejning;
        GridPane pane = new GridPane();
        pane.setGridLinesVisible(false);
        Scene scene = new Scene(pane);
        this.initContent(pane);
        this.setScene(scene);
    }

    private void initContent(GridPane pane) {
        if (salget == null && udlejningen != null) {
            //Betalingsmetode til Pant
            Label lblBetalingsMetode = new Label("Betalingsmetode til Pant");
            pane.add(lblBetalingsMetode, 0, 0);

            sellableComboBox = new ComboBox<>();
            sellableComboBox.getItems().setAll(Sellable.values());
            sellableComboBox.getItems().remove(Sellable.KLIPPEKORT);
            pane.add(sellableComboBox, 0, 1);

            ChangeListener<Sellable> sellableChangeListener = (oitem1, olditem1, newitem1) -> this.selectedSellableChanged();
            sellableComboBox.getSelectionModel().selectedItemProperty().addListener(sellableChangeListener);

            sellableComboBox.getSelectionModel().select(0);
            betalingsmetode = sellableComboBox.getSelectionModel().getSelectedItem();

            //Buttons
            Button btnBetal = new Button("Betal");
            btnBetal.setOnAction(event -> this.payAction());
            pane.add(btnBetal, 0, 2);

            Button btnCancel = new Button("Afbryd");
            btnCancel.setOnAction(event -> this.cancelAction());
            pane.add(btnCancel, 1, 2);

            //Betaling af udlejningen
            udlejningCheckBox = new CheckBox("Gå videre til betaling af Udlejningen");
            pane.add(udlejningCheckBox, 2, 0);

        } else {
            //Betalingsmetode
            Label lblBetalingsMetode = new Label("Betalingsmetode");
            pane.add(lblBetalingsMetode, 0, 0);

            sellableComboBox = new ComboBox<>();
            sellableComboBox.getItems().setAll(Sellable.values());
            pane.add(sellableComboBox, 0, 1);

            ChangeListener<Sellable> sellableChangeListener = (oitem1, olditem1, newitem1) -> this.selectedSellableChanged();
            sellableComboBox.getSelectionModel().selectedItemProperty().addListener(sellableChangeListener);

            sellableComboBox.getSelectionModel().select(0);
            betalingsmetode = sellableComboBox.getSelectionModel().getSelectedItem();

            klippekortComboBox = new ComboBox<>();
            klippekortComboBox.getItems().setAll(Controller.getAktiveKlippekort());
            pane.add(klippekortComboBox,0,2);

            ChangeListener<Klippekort> klippekortChangeListener = (oitem2, olditem2, newitem2) -> this.selectedKlippekortChanged();
            klippekortComboBox.getSelectionModel().selectedItemProperty().addListener(klippekortChangeListener);

            if (!klippekortComboBox.getItems().isEmpty()) {
                klippekortComboBox.getSelectionModel().select(0);
                klippekortet = klippekortComboBox.getSelectionModel().getSelectedItem();
            }
            klippekortComboBox.setDisable(true);

            Label lblTotalPris = new Label("Totalpris (DKK):");
            pane.add(lblTotalPris,0, 3);

            txfTotalPris = new TextField("" + salget.getTotalPris());
            pane.add(txfTotalPris, 0, 4);

            //Buttons
            Button btnBetal = new Button("Betal");
            btnBetal.setOnAction(event -> this.payAction());
            pane.add(btnBetal, 0, 5);

            Button btnCancel = new Button("Afbryd");
            btnCancel.setOnAction(event -> this.cancelAction());
            pane.add(btnCancel, 1, 5);
        }
    }

    private void selectedSellableChanged() {
        betalingsmetode = sellableComboBox.getSelectionModel().getSelectedItem();
        if (betalingsmetode == Sellable.KLIPPEKORT) {
            klippekortComboBox.setDisable(false);
        }
        if (betalingsmetode != Sellable.KLIPPEKORT && udlejningen == null && klippekortComboBox != null) {
            klippekortComboBox.setDisable(true);
        }
    }

    private void selectedKlippekortChanged() {klippekortet = klippekortComboBox.getSelectionModel().getSelectedItem();}

    private void payAction() {
            if (udlejningen != null && salget == null) {
                if (udlejningCheckBox.isSelected()) {
                    Controller.betalPant(udlejningen, betalingsmetode);
                    BetalUdlejningWindow betalUdlejningWindow = new BetalUdlejningWindow(udlejningen);
                    betalUdlejningWindow.show();

                    if (opretSalgWindow != null) {
                        opretSalgWindow.close();
                    }
                    this.close();
                } else {
                    Controller.betalPant(udlejningen, betalingsmetode);
                    if (opretSalgWindow != null) {
                        opretSalgWindow.close();
                    }
                    this.close();
                }
            } else if (udlejningen == null && salget != null) {
                try {
                    if (Double.parseDouble(txfTotalPris.getText()) >= 0.0) {
                        if (betalingsmetode == Sellable.KLIPPEKORT && klippekortet != null) {
                            int antal = 0;
                            for (Antal antal1:
                                 salget.getAntalProdukter()) {
                                antal += antal1.getAntal();
                            }
                            if (klippekortet.getKlip() >= antal) {
                                klippekortet.setKlip(klippekortet.getKlip() - salget.getAntalProdukter().size());
                                Controller.betal(salget, true, betalingsmetode);
                                salget.setTotalPris(Double.parseDouble(txfTotalPris.getText()));
                                if (opretSalgWindow != null) {
                                    opretSalgWindow.close();
                                }
                                this.close();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setContentText("Ikke nok klip - Der er ikke nok klip på dette klippekort");
                                alert.setTitle("Fejl - Ikke nok klip");
                                alert.showAndWait();
                            }
                        } else {
                            Controller.betal(salget, true, betalingsmetode);
                            salget.setTotalPris(Double.parseDouble(txfTotalPris.getText()));
                            if (opretSalgWindow != null) {
                                opretSalgWindow.close();
                            }
                            this.close();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Fejl - Ugyldig pris");
                        alert.setContentText("Ugyldig pris - Indtast venligst en gyldig pris");
                        alert.showAndWait();
                    }
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Fejl - Formart-fejl: pris");
                    alert.setContentText("Formart-fejl: pris - Indtast venligst en gyldig pris");
                    alert.showAndWait();
                }
            } else {
                throw new NullPointerException();
            }
    }

    private void cancelAction() {
        if (opretSalgWindow != null) {
            opretSalgWindow.show();
        }
        this.close();
    }


}
