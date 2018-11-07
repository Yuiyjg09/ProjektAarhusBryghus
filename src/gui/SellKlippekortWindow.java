package gui;

import application.controller.Controller;
import application.model.Klippekort;
import application.model.Salg;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;


public class SellKlippekortWindow extends Stage {
    private Label lblPris;
    private TextField txfPris, txfKlip;

    public SellKlippekortWindow() {
        this.setTitle("Opret og Sælg Klippekort");
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

        VBox klipVBox = new VBox();
        pane.add(klipVBox,0,0);

        Label lblKlip = new Label("Antal Totale Klip:");
        klipVBox.getChildren().add(lblKlip);

        txfKlip = new TextField("4");
        klipVBox.getChildren().add(txfKlip);

        VBox prisVBox = new VBox();
        pane.add(prisVBox,0,1);

        lblPris = new Label("Total-Pris:");
        prisVBox.getChildren().add(lblPris);

        txfPris = new TextField("100.0");
        prisVBox.getChildren().add(txfPris);

        HBox buttons = new HBox();
        pane.add(buttons,0,2);

        Button btnCreateAndPay = new Button("Opret og gå til betaling");
        buttons.getChildren().add(btnCreateAndPay);
        btnCreateAndPay.setOnAction(event -> this.createAndPayAction());

        Button btnCancel = new Button("Afbryd");
        buttons.getChildren().add(btnCancel);
        btnCancel.setOnAction(event -> this.cancelAction());
    }

    private void createAndPayAction() {
        try {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Er du sikker på, at du fortsætte?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {
                if (Integer.parseInt(txfKlip.getText()) > 0 && Double.parseDouble(txfPris.getText()) > 0.0) {
                    int klip = Integer.parseInt(txfKlip.getText());
                    double pris = Double.parseDouble(txfPris.getText());

                    Klippekort klippekort = Controller.createKlippekort(klip + "-klips Klippekort", 1, 1, Controller.getKlippekortKategori(), klip);
                    Controller.getKlippekortPrisliste().createPris(pris, klippekort);
                    Salg salg = Controller.createSalg();
                    salg.createAntal(klippekort, 1);

                    BetalSalgWindow betalSalgWindow = new BetalSalgWindow(salg);
                    betalSalgWindow.showAndWait();
                    this.close();
                } else throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl - Ugyldigt input");
            alert.setContentText("Ugyldigt input - Klip- og Pris-felterne må kun indeholde henholdsvis heltal og kommatal");
            alert.showAndWait();
        }
    }

    private void cancelAction() {
        this.close();
    }
}
