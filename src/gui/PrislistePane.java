package gui;

import application.controller.Controller;
import application.model.Pris;
import application.model.Prisliste;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import storage.Storage;

public class PrislistePane extends GridPane {
    private ListView<Prisliste> lwPrislister;
    private ListView<Pris> lwPriser;
    private Prisliste pl1;

    public PrislistePane() {
        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(10);
        this.setGridLinesVisible(false);

        Label lblPrislister = new Label("Prislister");
        this.add(lblPrislister,0,0);

        Label lblProdukter = new Label("Produkter");
        this.add(lblProdukter,1,0);

        lwPrislister = new ListView<>();
        this.add(lwPrislister,0,1);
        lwPrislister.getItems().addAll(Storage.getPrislister());

        lwPriser = new ListView<>();
        this.add(lwPriser,1,1);

        ChangeListener<Prisliste> pll = (op, oldItem, newItem) -> this.selectedPrislisteChanged();
        lwPrislister.getSelectionModel().selectedItemProperty().addListener(pll);

        Button btnCreate = new Button("Opret Prisliste");
        this.add(btnCreate, 0, 2);
        btnCreate.setOnAction(event -> this.createAction());

        Button btnUpdate = new Button("Rediger Prisliste");
        this.add(btnUpdate, 1, 2);
        btnUpdate.setOnAction(event -> this.updateAction());

        Button btnRemove = new Button("Slet Prisliste");
        this.add(btnRemove, 0, 3);
        btnRemove.setOnAction(event -> this.removeAction());

        Button btnPrint = new Button("Print Prisliste");
        this.add(btnPrint, 1, 3);
        btnPrint.setOnAction(event -> this.printAction());

        updateControls();
    }

    private void updateControls() {
        lwPrislister.getItems().clear();
        lwPrislister.getItems().setAll(Storage.getPrislister());
        lwPrislister.getSelectionModel().select(0);
    }

    private void selectedPrislisteChanged() {
        lwPriser.getItems().clear();
        pl1 = lwPrislister.getSelectionModel().getSelectedItem();
        if (pl1 != null) {
            lwPriser.getItems().addAll(pl1.getPriser());
            if (!pl1.getPriser().isEmpty()) {
                lwPriser.getSelectionModel().select(0);
            }
        }

        updateControls();
    }

    private void createAction() {
        OpretPrislisteWindow opretPrislisteWindow = new OpretPrislisteWindow();
        opretPrislisteWindow.showAndWait();

        updateControls();
    }

    private void updateAction() {
        if (pl1 != null) {
            OpretPrislisteWindow opretPrislisteWindow = new OpretPrislisteWindow(pl1);
            opretPrislisteWindow.showAndWait();

            updateControls();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl");
            alert.setContentText("Ingen Prisliste valgt");
            alert.showAndWait();
        }
    }

    private void removeAction() {
        if (pl1 != null) {
            Controller.deletePrisliste(pl1);

            updateControls();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl");
            alert.setContentText("Ingen Prisliste valgt");
            alert.showAndWait();
        }
    }

    private void printAction() {}
}
