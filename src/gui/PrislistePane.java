package gui;

import application.controller.Controller;
import application.model.Pris;
import application.model.Prisliste;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

class PrislistePane extends GridPane {
    private ListView<Prisliste> lwPrislister;
    private ListView<Pris> lwPriser;
    private Prisliste pl1;
    private final FileChooser fileChooserSave = new FileChooser();

    PrislistePane() {
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
        lwPrislister.getItems().addAll(Controller.getPrislister());

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
        int selectedIndex = lwPrislister.getSelectionModel().getSelectedIndex();
        lwPrislister.getItems().setAll(Controller.getPrislister());
        lwPrislister.getSelectionModel().select(selectedIndex);
        pl1 = lwPrislister.getSelectionModel().getSelectedItem();
    }

    private void selectedPrislisteChanged() {
        lwPrislister.getItems().clear();
        lwPrislister.getItems().addAll(Controller.getPrislister());
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

        lwPrislister.getItems().setAll(Controller.getPrislister());
        updateControls();
    }

    private void updateAction() {
        if (pl1 != null) {
            OpretPrislisteWindow opretPrislisteWindow = new OpretPrislisteWindow(pl1);
            opretPrislisteWindow.showAndWait();

            lwPrislister.getItems().setAll(Controller.getPrislister());
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
            Stage owner = (Stage) this.getScene().getWindow();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Slet Prisliste");
            alert.initOwner(owner);
            alert.setHeaderText("Er du sikker?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Controller.deletePrisliste(pl1);
                lwPrislister.getItems().setAll(Controller.getPrislister());
                updateControls();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl");
            alert.setContentText("Ingen Prisliste valgt");
            alert.showAndWait();
        }
    }

    private void printAction() {
        if (pl1 != null) {
            ArrayList<String> sb = new ArrayList<>();
            sb.add(pl1.toString() + " \n");
            for (Pris pris:
                 pl1.getPriser()) {
                sb.add( pris.getProdukt().toString() + ", Pris: " + pris.getPris() + " DKK \n");
            }
            this.saveFileEvent(fileChooserSave, sb);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl");
            alert.setContentText("Vælg en Prisliste for at fortsætte");
            alert.showAndWait();
        }
    }

    private static void configureFileChooserSave(final FileChooser fileChooser, String name) {
        fileChooser.setTitle("Save File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName(name);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt")
        );
    }

    private void saveFileEvent(FileChooser fileChooserSave, ArrayList<String> toWrite) {
        configureFileChooserSave(fileChooserSave, pl1.getNavn());
        File currentFile = fileChooserSave.showSaveDialog(null);
        if (currentFile != null) {
            try {
                FileWriter fileWriter = new FileWriter(currentFile);
                for (String str:
                     toWrite) {
                    fileWriter.append(str).append(System.getProperty("line.separator"));
                }
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
