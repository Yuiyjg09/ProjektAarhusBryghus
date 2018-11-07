package gui;

import application.controller.Controller;
import application.model.*;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

class AdministrationsPane extends GridPane {
    private Label lblProduker, lblOutOfStock, lblAktiveKlippekort, lblSalgIdag, lblSolgtForIdag, lblSolgtForTotalt;
    private ListView<Klippekort> lvwKlippeKort;
    private ListView<Rundvisning> lvwRundvisninger;
    private Klippekort klippekortet;
    private Rundvisning rundvisningen;
    private final FileChooser fileChooserSave = new FileChooser();

    AdministrationsPane() {
        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(10);
        this.setGridLinesVisible(false);

        Label lblStatus = new Label("Status: ");
        this.add(lblStatus, 0,0);

        lblProduker = new Label("Antal Produkter: " + Controller.getProdukter().size());
        this.add(lblProduker,0,1);

        lblOutOfStock = new Label("Antal Produkter ikke på lager: " + Controller.getProdukterOutOfStock().size());
        this.add(lblOutOfStock, 0, 2);

        Button btnPrintLager = new Button("Print Lagerliste");
        this.add(btnPrintLager, 0, 3);
        btnPrintLager.setOnAction(event -> this.saveFileEvent(fileChooserSave));

        Label lblKlippeKort = new Label("Aktive Klippekort:");
        this.add(lblKlippeKort,0,4);

        lvwKlippeKort = new ListView<>();
        this.add(lvwKlippeKort,0,5);
        lvwKlippeKort.getItems().setAll(Controller.getAktiveKlippekort());

        ChangeListener<Klippekort> klippekortChangeListener = (oitem, olditem, newitem) -> this.selectedKlippekortChanged();
        lvwKlippeKort.getSelectionModel().selectedItemProperty().addListener(klippekortChangeListener);

        HBox klippekortbuttonsHBox = new HBox();
        this.add(klippekortbuttonsHBox, 0, 6);

        Button btnCreate = new Button("Opret og sælg Klippekort");
        klippekortbuttonsHBox.getChildren().add(btnCreate);
        btnCreate.setOnAction(event -> this.createKlippekort());

        Button btnEdit = new Button("Tilføj Klip");
        klippekortbuttonsHBox.getChildren().add(btnEdit);
        btnEdit.setOnAction(event -> this.editKlippekortAction());

        Button btnKlip = new Button("Klip -1");
        klippekortbuttonsHBox.getChildren().add(btnKlip);
        btnKlip.setOnAction(event -> this.klipAction());

        lblAktiveKlippekort = new Label("Antal Aktive Klippekort: " + Controller.getAktiveKlippekort().size());
        this.add(lblAktiveKlippekort,0,7);

        //Salgsinfo
        lblSalgIdag = new Label("Salg for i dag: " + Controller.getSalgForToday().size());
        this.add(lblSalgIdag,1,1);

        double totalIdag = 0.0;
        for (Salg salg:
             Controller.getSalgForToday()) {
            totalIdag += salg.getTotalPris();
        }
        lblSolgtForIdag = new Label("Solgt for i dag: " + totalIdag + " DKK");
        this.add(lblSolgtForIdag, 1, 2);

        double totalSolgt = 0.0;
        for (Salg salg:
             Controller.getSalg()) {
            totalSolgt += salg.getTotalPris();
        }
        lblSolgtForTotalt = new Label("Solgt for totalt: " + totalSolgt + " DKK");
        this.add(lblSolgtForTotalt, 1, 3);

        //Rundvisninger
        Label lblRundvisninger = new Label("Aktive Rundvisninger:");
        this.add(lblRundvisninger,1,4);

        lvwRundvisninger = new ListView<>();
        this.add(lvwRundvisninger, 1, 5);
        lvwRundvisninger.getItems().setAll(Controller.getAktiveRundvisninger());

        ChangeListener<Rundvisning> rundvisningChangeListener = (oitem2, olditem2, newitem2) -> this.selectedRundvisningChanged();
        lvwRundvisninger.getSelectionModel().selectedItemProperty().addListener(rundvisningChangeListener);

        HBox rundvisningButtonsHBox = new HBox();
        this.add(rundvisningButtonsHBox,1,6);

        Button btnCreateRundvisning = new Button("Opret Rundvisning");
        rundvisningButtonsHBox.getChildren().add(btnCreateRundvisning);
        btnCreateRundvisning.setOnAction(event -> this.createRundvisningAction());

        Button btnBetalRundvisning = new Button("Betal Rundvisning");
        rundvisningButtonsHBox.getChildren().add(btnBetalRundvisning);
        btnBetalRundvisning.setOnAction(event -> this.payRundvisningAction());
    }

    private void selectedKlippekortChanged() {klippekortet = lvwKlippeKort.getSelectionModel().getSelectedItem();}
    private void selectedRundvisningChanged() {rundvisningen = lvwRundvisninger.getSelectionModel().getSelectedItem();}

    private void createKlippekort() {
        SellKlippekortWindow sellKlippekortWindow = new SellKlippekortWindow();
        sellKlippekortWindow.showAndWait();

        lvwKlippeKort.getItems().setAll(Controller.getAktiveKlippekort());
        updateControls();
    }

    private void editKlippekortAction() {
        if (klippekortet != null) {
            EditKlippekortWindow editKlippekortWindow = new EditKlippekortWindow(klippekortet);
            editKlippekortWindow.showAndWait();

            lvwKlippeKort.getItems().setAll(Controller.getAktiveKlippekort());
            updateControls();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl - Intet Klippekort Valgt");
            alert.setContentText("Intet klippekort valgt - Vælg venligst et klippekort for at fortsætte");
            alert.showAndWait();
        }
    }

    private void klipAction() {
        if (klippekortet != null) {
            klippekortet.setKlip(klippekortet.getKlip() - 1);
            lvwKlippeKort.getItems().setAll(Controller.getAktiveKlippekort());
            updateControls();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl - Intet Klippekort Valgt");
            alert.setContentText("Intet klippekort valgt - Vælg venligst et klippekort for at fortsætte");
            alert.showAndWait();
        }
    }

    void updateControls() {
        int selectedIndex = lvwKlippeKort.getSelectionModel().getSelectedIndex();
        lvwKlippeKort.getItems().setAll(Controller.getAktiveKlippekort());
        lvwKlippeKort.getSelectionModel().select(selectedIndex);
        klippekortet = lvwKlippeKort.getSelectionModel().getSelectedItem();
        lblAktiveKlippekort.setText("Antal Aktive Klippekort: " + Controller.getAktiveKlippekort().size());
        lblSalgIdag.setText("Antal salg i dag: " + Controller.getSalgForToday().size());
        double totalIdag = 0.0;
        for (Salg salg:
                Controller.getSalgForToday()) {
            totalIdag += salg.getTotalPris();
        }
        lblSolgtForIdag.setText("Solgt for i dag: " + totalIdag + " DKK");
        double totalSolgt = 0.0;
        for (Salg salg:
                Controller.getSalg()) {
            totalSolgt += salg.getTotalPris();
        }
        lblSolgtForTotalt.setText("Solgt for totalt: " + totalSolgt + " DKK");
        int selectedIndex2 = lvwRundvisninger.getSelectionModel().getSelectedIndex();
        lvwRundvisninger.getItems().setAll(Controller.getAktiveRundvisninger());
        lvwRundvisninger.getSelectionModel().select(selectedIndex2);
        rundvisningen = lvwRundvisninger.getSelectionModel().getSelectedItem();
        lblProduker.setText("Antal Produkter: " + Controller.getProdukter().size());
        lblOutOfStock.setText("Antal Produkter ikke på lager: " + Controller.getProdukterOutOfStock().size());
    }

    private static void configureFileChooserSave(final FileChooser fileChooser, String name) {
        fileChooser.setTitle("Save File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName(name);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt")
        );
    }

    private void saveFileEvent(FileChooser fileChooserSave) {
        configureFileChooserSave(fileChooserSave, ("Lagerliste - DATO: " + LocalDate.now().toString()));
        File currentFile = fileChooserSave.showSaveDialog(null);
        if (currentFile != null) {
            try {
                FileWriter fileWriter = new FileWriter(currentFile);
                for (Produkt produkt:
                        Controller.getProdukter()) {
                    fileWriter.append(produkt.toString()).append(System.getProperty("line.separator"));
                }
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createRundvisningAction() {
        OpretRundvisningWindow opretRundvisningWindow = new OpretRundvisningWindow();
        opretRundvisningWindow.showAndWait();

        lvwRundvisninger.getItems().setAll(Controller.getAktiveRundvisninger());
        updateControls();
    }

    private void payRundvisningAction() {
        boolean canBeSold = true;
        for (Salg salg:
             Controller.getSalg()) {
            for (Antal antal:
                 salg.getAntalProdukter()) {
                if (antal.getProdukt() == rundvisningen) {
                    canBeSold = false;
                }
            }
        }
        if (canBeSold) {
            if (rundvisningen != null) {
                Salg salget = Controller.createSalg();
                salget.createAntal(rundvisningen, 1);

                BetalSalgWindow betalSalgWindow = new BetalSalgWindow(salget);
                betalSalgWindow.showAndWait();

                lvwRundvisninger.getItems().setAll(Controller.getAktiveRundvisninger());
                updateControls();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Fejl - Ingen Rundvisning Valgt");
                alert.setContentText("Ingen Rundvisning valgt - Vælg venligst en rundvisning for at fortsætte");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl - Allerede Betalt");
            alert.setContentText("Allerede Betalt - Rundvisningen er allerede betalt");
            alert.showAndWait();
        }
    }
}
