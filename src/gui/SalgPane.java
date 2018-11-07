package gui;

import application.controller.Controller;
import application.model.Salg;
import application.model.Udlejning;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

class SalgPane extends GridPane {
    private ListView<Salg> salgListView;
    private Salg salget = null;
    private final FileChooser fileChooserSave = new FileChooser();

    SalgPane() {
        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(10);
        this.setGridLinesVisible(false);

        //Salg for i dag
        Label lblSalg = new Label("Salg (gennemført idag):");
        this.add(lblSalg, 0, 0, 3, 1);

        salgListView = new ListView<>();
        this.add(salgListView, 0, 1);
        salgListView.getItems().setAll(Controller.getSalgForToday());

        ChangeListener<Salg> salgChangeListener = (oitem, olditem, newitem) -> this.selectSalgChanged();
        salgListView.getSelectionModel().selectedItemProperty().addListener(salgChangeListener);

        //Buttons & HBox
        HBox btnHBox = new HBox();
        this.add(btnHBox, 0, 2);
        //Opret Salg knap
        Button btnOpretSalg = new Button("Opret Salg");
        btnHBox.getChildren().add(btnOpretSalg);
        btnOpretSalg.setOnAction(event -> this.createAction());

        //Betal salg knap
        Button btnBetalSalg = new Button("Betal Salg");
        btnHBox.getChildren().add(btnBetalSalg);
        btnBetalSalg.setOnAction(event -> this.payAction());

        //Print Slutseddel knap
        Button btnPrintSalg = new Button("Print Slutseddel");
        btnHBox.getChildren().add(btnPrintSalg);
        btnPrintSalg.setOnAction(event -> this.printAction());

        updateControls();
    }

    void updateControls() {
        int selectedIndex = salgListView.getSelectionModel().getSelectedIndex();
        salgListView.getItems().setAll(Controller.getSalgForToday());
        salgListView.getSelectionModel().select(selectedIndex);
        salget = salgListView.getSelectionModel().getSelectedItem();
    }

    private void selectSalgChanged() {salget = salgListView.getSelectionModel().getSelectedItem();}

    private void createAction() {
        OpretSalgWindow window = new OpretSalgWindow();
        window.showAndWait();

        salgListView.getItems().setAll(Controller.getSalgForToday());
        updateControls();
    }

    private void payAction() {
        if (salget != null && !salget.isBetalt() && !salget.toString().contains("Udlejning")) {
            BetalSalgWindow betalSalgWindow;
            betalSalgWindow = new BetalSalgWindow(salget);
            betalSalgWindow.showAndWait();

            salgListView.getItems().setAll(Controller.getSalgForToday());
            updateControls();
        } else if (salget != null && salget.isBetalt()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl - Betal Salg");
            alert.setContentText("Det valgte Salg er allerede betalt");
            alert.showAndWait();
        } else if (salget != null && salget.toString().contains("Udlejning")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl - Betal Salg");
            alert.setContentText("Det valgte element er en Udlejning - Udlejninger betales i udlejnings-delen af systemet");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl - Betal Salg");
            alert.setContentText("Intet Salg er valgt - Vælg venligst et Salg");
            alert.showAndWait();
        }
    }


    private void printAction() {
        ArrayList<String> strings = new ArrayList<>();
        double totalPris = 0.0;
        strings.add("Slutseddel - DATO: " + LocalDate.now().toString());
        for (Salg salg:
             Controller.getSalgForToday()) {
            strings.add(salg.toString());
            totalPris += salg.getTotalPrisEdited();
        }
        strings.add("Totalt Salg: " + totalPris + " DKK");
        saveFileEvent(fileChooserSave, strings);
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
        configureFileChooserSave(fileChooserSave, ("Slutseddel - DATO: " + LocalDate.now().toString()));
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
