package gui;

import application.controller.Controller;
import application.model.Udlejning;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

class Udlejningspane extends GridPane {
    private ListView<Udlejning> lvwAktiveUdlejninger, lvwUdlejninger;
    private Udlejning udlejningen = null;
    private Label lblAntalAktive, lblAntal;

    Udlejningspane() {
        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(10);
        this.setGridLinesVisible(false);

        //TODO

        //Aktive Udlejninger
        Label lblUdlejninger = new Label("Aktive Udlejninger:");
        this.add(lblUdlejninger, 0,0);
        lvwAktiveUdlejninger = new ListView<>();
        this.add(lvwAktiveUdlejninger,0,1);
        lvwAktiveUdlejninger.getItems().setAll(Controller.getAktiveUdlejninger());

        ChangeListener<Udlejning> udlejningChangeListener = (oitem, olditem, newitem) -> this.selectedUdlejningChanged();
        lvwAktiveUdlejninger.getSelectionModel().selectedItemProperty().addListener(udlejningChangeListener);

        Label lblAlleUdlejninger = new Label("Alle Udlejninger");
        this.add(lblAlleUdlejninger, 1,0);
        lvwUdlejninger = new ListView<>();
        this.add(lvwUdlejninger, 1, 1);
        lvwUdlejninger.getItems().setAll(Controller.getUdlejninger());

        ChangeListener<Udlejning> alleUdlejningerChangeListener = (oitem1, olditem1, newitem1) -> this.selectedAlleUdlejningerChanged();
        lvwUdlejninger.getSelectionModel().selectedItemProperty().addListener(alleUdlejningerChangeListener);

        lblAntalAktive = new Label("Antal Aktive Udlejninger: " + Controller.getAktiveUdlejninger().size());
        this.add(lblAntalAktive, 0,2);

        lblAntal = new Label("Antal Totale Udlejninger: " + Controller.getUdlejninger().size());
        this.add(lblAntal,1,2);

        Button btnBetal = new Button("Betal Udlejning");
        this.add(btnBetal,0, 3);
        btnBetal.setOnAction(event -> this.payAction());

        selectedAlleUdlejningerChanged();
        updateControls();
    }

    private void selectedUdlejningChanged() {
        udlejningen = lvwAktiveUdlejninger.getSelectionModel().getSelectedItem();
        updateControls();
    }

    private void selectedAlleUdlejningerChanged() {
        lvwUdlejninger.getItems().setAll(Controller.getUdlejninger());
        lblAntal.setText("Antal Totale Udlejninger: " + Controller.getUdlejninger().size());
        updateControls();
    }

    void updateControls() {
        int selectedIndex = lvwAktiveUdlejninger.getSelectionModel().getSelectedIndex();
        lvwAktiveUdlejninger.getItems().setAll(Controller.getAktiveUdlejninger());
        lvwAktiveUdlejninger.getSelectionModel().select(selectedIndex);
        udlejningen = lvwAktiveUdlejninger.getSelectionModel().getSelectedItem();
        lvwUdlejninger.getItems().setAll(Controller.getUdlejninger());
        lblAntalAktive.setText("Antal Aktive Udlejninger: " + Controller.getAktiveUdlejninger().size());
        lblAntal.setText("Antal Totale Udlejninger: " + Controller.getUdlejninger().size());
    }

    private void payAction() {
        if (udlejningen != null) {
            BetalUdlejningWindow betalUdlejningWindow = new BetalUdlejningWindow(udlejningen);
            betalUdlejningWindow.showAndWait();

            lvwAktiveUdlejninger.getItems().setAll(Controller.getAktiveUdlejninger());
            updateControls();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl - Ingen Udlejning Valgt");
            alert.setContentText("Ingen Udlejning Valgt - Vælg venligst en Udlejning for at betale den");
            alert.showAndWait();

            updateControls();
        }
    }
}
