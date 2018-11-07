package gui;

import application.controller.Controller;
import application.model.Klippekort;
import application.model.Salg;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class EditKlippekortWindow extends Stage {
    private TextField txfKlip = new TextField();
    private Klippekort klippekortet;

    EditKlippekortWindow(Klippekort klippekort) {
        this.klippekortet = klippekort;
        this.setTitle("Ændre Klip");
        GridPane pane = new GridPane();
        pane.setGridLinesVisible(false);
        Scene scene = new Scene(pane);
        this.initContent(pane);
        this.setScene(scene);
    }

    private void initContent(GridPane pane) {
        pane.setPadding(new Insets(20));
        pane.setHgap(20);
        pane.setVgap(10);
        pane.setGridLinesVisible(false);

        Label lblNummer = new Label("KortNummer: " + klippekortet.getKortNummer());
        pane.add(lblNummer,0,0);

        VBox klipVBox = new VBox();
        pane.add(klipVBox,0,1);

        Label lblKlip = new Label("Antal Totale Klip:");
        klipVBox.getChildren().add(lblKlip);

        txfKlip = new TextField();
        txfKlip.setText("" + klippekortet.getKlip());
        klipVBox.getChildren().add(txfKlip);

        HBox buttons = new HBox();
        pane.add(buttons,0,2);

        Button btnYes = new Button("Ok");
        buttons.getChildren().add(btnYes);
        btnYes.setOnAction(event -> this.okAction());

        Button btnCancel = new Button("Afbryd");
        buttons.getChildren().add(btnCancel);
        btnCancel.setOnAction(event -> this.cancelAction());
    }

    private void okAction() {
        try {
            if (Integer.parseInt(txfKlip.getText()) >= 0) {
                int klip = Integer.parseInt(txfKlip.getText());
                klippekortet.setKlip(klip);

                this.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Fejl - Ugyldigt antal klip");
                alert.setContentText("Antal klip må ikke være mindre end 0 - Indtast venligst et gyldigt antal af klip (Heltal)");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl - Ugyldigt antal klip");
            alert.setContentText("Ugyldigt antal klip - Indtast venligst et gyldigt antal af klip (Heltal)");
            alert.showAndWait();
        }
    }

    private void cancelAction() {
        this.close();
    }
}
