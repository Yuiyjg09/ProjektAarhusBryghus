package gui;

import application.controller.Controller;
import application.model.Antal;
import application.model.Sellable;
import application.model.Udlejning;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

class BetalUdlejningWindow extends Stage {
    private ListView<Antal> lvwProdukterToRemove;
    private Udlejning udlejningen;
    private Label priceTag;
    private Antal antal;
    private ArrayList<Antal> antalArrayList = new ArrayList<>();
    private CheckBox pantBox;
    private ComboBox<Sellable> sellableComboBox;

    BetalUdlejningWindow(Udlejning udlejning) {
        this.udlejningen = udlejning;
        this.setTitle("Betal Udlejning");
        GridPane pane = new GridPane();
        pane.setGridLinesVisible(false);
        Scene scene = new Scene(pane);
        this.initContent(pane);
        this.setScene(scene);
    }

    private void initContent(GridPane pane) {
        //Produkter to remove
        Label lblProdukter = new Label("Produkter i udlejningen");
        pane.add(lblProdukter, 0, 0);
        lvwProdukterToRemove = new ListView<>();
        pane.add(lvwProdukterToRemove, 0, 1);
        lvwProdukterToRemove.getItems().setAll(udlejningen.getAntalProdukter());

        ChangeListener<Antal> antalChangeListener = (oitem, olditem, newitem) -> this.selectedProduktChanged();
        lvwProdukterToRemove.getSelectionModel().selectedItemProperty().addListener(antalChangeListener);

        //Betalingsmetode
        Label lblBetalingsMetode = new Label("Betalingsmetode");
        pane.add(lblBetalingsMetode, 0, 2);

        sellableComboBox = new ComboBox<>();
        sellableComboBox.getItems().setAll(Sellable.values());
        pane.add(sellableComboBox, 0, 3);

        //Reset & Remove buttons
        Button btnRemove = new Button("Fjern produkt");
        pane.add(btnRemove, 0, 4);
        btnRemove.setOnAction(event -> this.removeAction());

        Button btnReset = new Button("Gendan alle produkter");
        pane.add(btnReset,1,4);
        btnReset.setOnAction(event -> this.resetAction());

        //PantBox
        pantBox = new CheckBox("Træk ikke Panten fra?");
        pane.add(pantBox,0,5);

        ChangeListener<Boolean> pantChangeListener = (oitem1, olditem1, newitem1) -> this.selectedPantChanged();
        pantBox.selectedProperty().addListener(pantChangeListener);

        //PriceTag
        priceTag = new Label("Pris: " + udlejningen.getTotalPris() + " DKK");
        pane.add(priceTag, 0, 6);

        //Buttons
        Button btnBetal = new Button("Betal");
        btnBetal.setOnAction(event -> this.payAction());
        pane.add(btnBetal, 0, 7);

        Button btnCancel = new Button("Afbryd");
        btnCancel.setOnAction(event -> this.cancelAction());
        pane.add(btnCancel, 1, 7);
    }

    private void selectedProduktChanged() {antal = lvwProdukterToRemove.getSelectionModel().getSelectedItem();}

    private void updateControls() {
        int selectedIndex = lvwProdukterToRemove.getSelectionModel().getSelectedIndex();
        lvwProdukterToRemove.getItems().setAll(udlejningen.getAntalProdukter());
        lvwProdukterToRemove.getSelectionModel().select(selectedIndex);
        antal = lvwProdukterToRemove.getSelectionModel().getSelectedItem();
    }

    private void removeAction() {
        if (antal != null) {
            antalArrayList.add(antal);
            Controller.deleteAntal(antal);
            if (antal.getAntal() > 1) {
                Controller.createAntal(antal.getProdukt(), udlejningen, antal.getAntal() - 1);
            }
            updateControls();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Intet produkt er valgt - Vælg venligst et produkt");
            alert.setTitle("Fejl - Intet produkt valgt");
            alert.showAndWait();
        }
    }

    private void resetAction() {
        for (Antal antal:
             udlejningen.getAntalProdukter()) {
            Controller.deleteAntal(antal);
        }
        for (Antal antal:
            antalArrayList) {
            Controller.createAntal(antal.getProdukt(), udlejningen, antal.getAntal());
            antalArrayList.remove(antal);
        }
        updateControls();
    }

    private void selectedPantChanged() {
        if (pantBox.isSelected()) {
            priceTag.setText("Pris: " + (udlejningen.getTotalPris() + udlejningen.getPant()) + " DKK");
        } else {
            priceTag.setText("Pris: " + udlejningen.getTotalPris() + " DKK");
        }
    }

    private void cancelAction() {
        this.close();
    }

    private void payAction() {
        if (sellableComboBox.getSelectionModel().getSelectedItem() != null) {
            Controller.betal(udlejningen, true, sellableComboBox.getValue());
            if (pantBox.isSelected()) {
                udlejningen.setTotalPris(udlejningen.getTotalPris() + udlejningen.getPant());
            }
            this.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Ingen Betalingsmetode valgt - Vælg venligst en betalingsmetode for at fuldføre betalingen");
            alert.setTitle("Manglende Betalingsmetode");
            alert.showAndWait();
        }
    }


}
