package gui;

import application.controller.Controller;
import application.model.Antal;
import application.model.Produkt;
import application.model.Salg;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class OpretSalgWindow extends Stage {
    private ListView<Produkt> lvwAllProdukter;
    private ListView<Antal> lvwProdukterToRemove;
    private TextField txfAntal, txfPant;
    private Produkt produktToAdd;
    private Antal produktToRemove;
    private CheckBox isUdlejningCheckBox;
    private DatePicker dpStart, dpRetur;
    private Salg salget;
    private Label priceTag;

    public OpretSalgWindow() {
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

        ChangeListener<Produkt> allProduktChangeListener = (oitem, olditem, newitem) -> this.selectedAllProdukterChanged();
        lvwAllProdukter.getSelectionModel().selectedItemProperty().addListener(allProduktChangeListener);

        //Antal
        Label lblAntal = new Label("Antal");
        pane.add(lblAntal, 1, 0);

        txfAntal = new TextField("1");
        pane.add(txfAntal, 1, 1);

        //Tilføj og Fjern knapper
        Button btnAdd = new Button("Læg i Kurv");
        pane.add(btnAdd, 1, 2);
        btnAdd.setOnAction(event -> this.addAction());

        Button btnRemove = new Button("Fjern fra Kurv");
        pane.add(btnRemove, 1, 3);
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

        //DatePickers
        Label lblStart = new Label("Start Dato");
        pane.add(lblStart, 1, 5);
        dpStart = new DatePicker();
        pane.add(dpStart, 1, 6);

        Label lblRetur = new Label("Retur Dato");
        pane.add(lblRetur,2,5);
        dpRetur = new DatePicker();
        pane.add(dpRetur, 2, 6);

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

    }

    private void addAction() {
        if (produktToAdd != null) {
            try {
                Antal antal = Controller.createAntal(produktToAdd, salget, Integer.parseInt(txfAntal.getText()));
                lvwProdukterToRemove.getItems().add(antal);

                updateControls();
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
