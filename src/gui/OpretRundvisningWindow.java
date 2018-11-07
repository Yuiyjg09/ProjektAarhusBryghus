package gui;

import application.controller.Controller;
import application.model.Rundvisning;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class OpretRundvisningWindow extends Stage {
    private TextField txfNavn, txfPris, txfRundvisningStart, txfRundvisningSlut;
    private DatePicker dpRundvisning;

    OpretRundvisningWindow() {
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setResizable(false);

        GridPane pane = new GridPane();
        this.initContent(pane);

        Scene scene = new Scene(pane);
        this.setScene(scene);
    }

    private void initContent(GridPane pane) {
        pane.setPadding(new Insets(10));
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setGridLinesVisible(false);

        Label lblNavn = new Label("Angiv navn:");
        pane.add(lblNavn,0,0);

        txfNavn = new TextField();
        pane.add(txfNavn,0,1);

        Label lblPris = new Label("Angiv pris (DKK):");
        pane.add(lblPris,0,2);

        txfPris = new TextField("100");
        pane.add(txfPris,0,3);

        Label lblDato = new Label("Dato");
        pane.add(lblDato,0,4);

        dpRundvisning = new DatePicker();
        pane.add(dpRundvisning,0,5);

        Label lblRundvisningStart = new Label("Starttidspunkt:");
        pane.add(lblRundvisningStart, 0, 6);

        txfRundvisningStart = new TextField("HH:mm");
        pane.add(txfRundvisningStart, 0, 7);

        Label lblRundvisningSlut = new Label("Sluttidspunkt");
        pane.add(lblRundvisningSlut,0,8);

        txfRundvisningSlut = new TextField("HH:mm");
        pane.add(txfRundvisningSlut,0,9);

        HBox buttons = new HBox();
        pane.add(buttons,0,10);

        Button btnCreate = new Button("Opret");
        buttons.getChildren().add(btnCreate);
        btnCreate.setOnAction(event -> this.createAction());

        Button btnCancel = new Button("Afbryd");
        buttons.getChildren().add(btnCancel);
        btnCancel.setOnAction(event -> this.cancelAction());
    }

    private void createAction() {
        try {
            if (!txfNavn.getText().isEmpty()
                && !txfRundvisningStart.getText().isEmpty()
                    && !txfRundvisningSlut.getText().isEmpty()
                && !txfPris.getText().isEmpty()) {
                double pris = Double.parseDouble(txfPris.getText());
                Rundvisning rundvisning = Controller.createRundvisning(txfNavn.getText(), 1, 1,
                        Controller.getRundvisningsproduktkategori(),
                        dpRundvisning.getValue(), LocalTime.parse(txfRundvisningStart.getText(), DateTimeFormatter.ofPattern("HH:mm")),
                        LocalTime.parse(txfRundvisningSlut.getText(), DateTimeFormatter.ofPattern("HH:mm")));

                Controller.getRundvisningsPrisliste().createPris(pris, rundvisning);

                this.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Fejl - Manglende Information");
                alert.setContentText("Manglende Information - Udfyld venligst alle påkrævede felter");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl - Ugyldigt Format");
            alert.setContentText("Ugyldigt Format - Benyt venligst et gyldigt format for de indtastede informationer");
            alert.showAndWait();
        }
    }

    private void cancelAction() {this.close();}
}
