package gui;

import application.controller.Controller;
import application.model.Antal;
import application.model.Produkt;
import application.model.Salg;
import application.model.Udlejning;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

class OpretSalgWindow extends Stage {
    private ListView<Produkt> lvwAllProdukter;
    private ListView<Antal> lvwProdukterToRemove;
    private TextField txfAntal, txfPant;
    private Produkt produktToAdd;
    private Antal produktToRemove;
    private CheckBox isUdlejningCheckBox;
    private DatePicker dpStart, dpRetur;
    private Salg salget;
    private Label priceTag;

    OpretSalgWindow() {
        this.salget = Controller.createSalg();
        this.setTitle("Opret Salg");
        GridPane pane = new GridPane();
        pane.setGridLinesVisible(false);
        Scene scene = new Scene(pane);
        this.initContent(pane);
        this.setScene(scene);
    }

    private void initContent(GridPane pane) {
        //Alle Produkter
        Label lblProdukter = new Label("Alle Produkter");
        pane.add(lblProdukter,0,0);

        lvwAllProdukter = new ListView<>();
        pane.add(lvwAllProdukter, 0, 1);
        lvwAllProdukter.getItems().setAll(Controller.getProdukter());
        lvwAllProdukter.getItems().addAll(Controller.getGaveAesker());

        ChangeListener<Produkt> allProduktChangeListener = (oitem, olditem, newitem) -> this.selectedAllProdukterChanged();
        lvwAllProdukter.getSelectionModel().selectedItemProperty().addListener(allProduktChangeListener);

        VBox antalVBox = new VBox();
        pane.add(antalVBox,1,1);
        //Antal
        Label lblAntal = new Label("Antal");
        antalVBox.getChildren().add(lblAntal);

        txfAntal = new TextField("1");
        antalVBox.getChildren().add(txfAntal);

        //Tilføj og Fjern knapper
        Button btnAdd = new Button("Læg i Kurv");
        antalVBox.getChildren().add(btnAdd);
        btnAdd.setOnAction(event -> this.addAction());

        Button btnRemove = new Button("Fjern fra Kurv");
        antalVBox.getChildren().add(btnRemove);
        btnRemove.setOnAction(event -> this.removeAction());

        //Produkter i salget
        Label lblProdukterToSell = new Label("Valgte Produkter");
        pane.add(lblProdukterToSell, 2, 0);

        lvwProdukterToRemove = new ListView<>();
        pane.add(lvwProdukterToRemove, 2, 1);
        lvwProdukterToRemove.getItems().setAll();

        ChangeListener<Antal> produkterToRemoveChangeListener = (oitem1, olditem1, newitem1) -> this.selectedProduktToRemoveChanged();
        lvwProdukterToRemove.getSelectionModel().selectedItemProperty().addListener(produkterToRemoveChangeListener);

        //Til Udlejning
        //Afkrydsningskasse til udlejning
        isUdlejningCheckBox = new CheckBox();
        isUdlejningCheckBox.setText("Udlejning");
        pane.add(isUdlejningCheckBox, 0, 4);

        ChangeListener<Boolean> isUdlejningChangeListener = (oitem2, olditem2, newitem2) -> this.selectedIsUdlejningChanged();
        isUdlejningCheckBox.selectedProperty().addListener(isUdlejningChangeListener);
        isUdlejningCheckBox.setSelected(false);

        //Pant
        Label lblPant = new Label("Pant");
        pane.add(lblPant, 0, 5);

        txfPant = new TextField();
        pane.add(txfPant, 0, 6);
        txfPant.setDisable(true);

        //DatePickers
        Label lblStart = new Label("Start Dato");
        pane.add(lblStart, 1, 5);
        dpStart = new DatePicker();
        pane.add(dpStart, 1, 6);
        dpStart.setDisable(true);

        Label lblRetur = new Label("Retur Dato");
        pane.add(lblRetur,2,5);
        dpRetur = new DatePicker();
        pane.add(dpRetur, 2, 6);
        dpRetur.setDisable(true);

        //PriceTag - Totalpris for salget
        priceTag = new Label("Pris: 0.0 DKK");
        pane.add(priceTag, 0 ,7);

        //Buttons
        Button btnOpret = new Button("Opret Salg/Betal");
        pane.add(btnOpret, 0, 8);
        btnOpret.setOnAction(event -> this.createAction());

        Button btnCancel = new Button("Afbryd");
        pane.add(btnCancel, 1, 8);
        btnCancel.setOnAction(event -> this.cancelAction());

        this.setOnCloseRequest(event -> this.cancelAction());
    }

    private void selectedAllProdukterChanged() {produktToAdd = lvwAllProdukter.getSelectionModel().getSelectedItem();}

    private void selectedProduktToRemoveChanged() {produktToRemove = lvwProdukterToRemove.getSelectionModel().getSelectedItem();}

    private void selectedIsUdlejningChanged() {
        if (isUdlejningCheckBox.isSelected()) {
            txfPant.setDisable(false);
            dpStart.setDisable(false);
            dpRetur.setDisable(false);
        } else {
            txfPant.setDisable(true);
            dpStart.setDisable(true);
            dpRetur.setDisable(true);
        }
    }

    private void updateControls() {
        priceTag.setText("Pris: " + salget.getTotalPris() + " DKK");
        int selectedIndex = lvwProdukterToRemove.getSelectionModel().getSelectedIndex();
        lvwProdukterToRemove.getItems().setAll(salget.getAntalProdukter());
        lvwProdukterToRemove.getSelectionModel().select(selectedIndex);
        produktToRemove = lvwProdukterToRemove.getSelectionModel().getSelectedItem();
    }

    private void createAction() {
        Alert confirm = new Alert(Alert.AlertType.INFORMATION);
        confirm.setTitle("Fortsæt");
        confirm.setContentText("Er du sikker på at du vil fortsætte?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean canBeSold = true;
            for (Antal antal:
                 salget.getAntalProdukter()) {
                if (antal.beregnPris() <= 0) {
                    canBeSold = false;
                }
            }
            if (salget.getTotalPris() > 0 && canBeSold) {
                if (isUdlejningCheckBox.isSelected()) {
                    try {
                        Udlejning udlejning = Controller.createUdlejning(Double.parseDouble(txfPant.getText()), dpStart.getValue(), dpRetur.getValue());
                        for (Antal antal :
                                lvwProdukterToRemove.getItems()) {
                            udlejning.createAntal(antal.getProdukt(), antal.getAntal());
                            salget.deleteAntal(antal);
                        }
                        Controller.deleteSalg(salget);
                        BetalSalgWindow betalSalgWindow = new BetalSalgWindow(udlejning, this);
                        betalSalgWindow.showAndWait();

                        this.close();
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Ugyldig antal - Kun tal er tilladt");
                        alert.setTitle("Fejl - Ugyldig antal");
                        alert.showAndWait();
                    }
                } else {
                    Alert pay = new Alert(Alert.AlertType.CONFIRMATION, "Vil du fortsætte til betaling?", ButtonType.YES, ButtonType.NO);
                    pay.setTitle("Fuldfør Salg");
                    Optional<ButtonType> payResult = pay.showAndWait();
                    if (payResult.isPresent() && payResult.get() == ButtonType.YES) {
                        BetalSalgWindow betalSalgWindow = new BetalSalgWindow(salget, this);
                        betalSalgWindow.showAndWait();
                    } else {
                        this.close();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Du har produkter, der ikke kan sælges nu");
                alert.setContentText("Du har produkter, der ikke kan sælges nu - Deres pris er 0 DKK, fordi produktet ikke kan sælges uden for prislistens gyldige tidspunkter");
                alert.showAndWait();
            }
        }
    }

    private void addAction() {
        if (produktToAdd != null) {
            try {
                if (Integer.parseInt(txfAntal.getText()) <= produktToAdd.getLagerAntal()) {
                    Antal antal = Controller.createAntal(produktToAdd, salget, Integer.parseInt(txfAntal.getText()));
                    lvwProdukterToRemove.getItems().add(antal);
                    antal.getProdukt().setLagerAntal(antal.getProdukt().getLagerAntal() - antal.getAntal());

                    updateControls();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Fejl - Produktet er ikke på lager");
                    alert.setContentText("Produktet er ikke på lager - Antal på lager: " + produktToAdd.getLagerAntal() + " stk");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Fejl - Ugyldigt Input");
                alert.setContentText("Indtast et Antal (Heltal)");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl - Intet Produkt valgt");
            alert.setContentText("Intet Produkt valgt - Vælg venligst et Produkt");
            alert.showAndWait();
        }
    }

    private void removeAction() {
        if (produktToRemove != null) {
            Controller.deleteAntal(produktToRemove);
            produktToRemove.getProdukt().setLagerAntal(produktToRemove.getProdukt().getLagerAntal() + produktToRemove.getAntal());
            lvwProdukterToRemove.getItems().remove(produktToRemove);

            updateControls();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl - Intet Produkt valgt");
            alert.setContentText("Intet Produkt valgt - Vælg venligst et Produkt");
            alert.showAndWait();
        }
    }

    private void cancelAction() {
        Controller.deleteSalg(salget);
        this.salget = null;
        this.close();
    }
}
