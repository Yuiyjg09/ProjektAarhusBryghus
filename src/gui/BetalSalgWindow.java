package gui;

import application.controller.Controller;
import application.model.Salg;
import application.model.Sellable;
import application.model.Udlejning;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class BetalSalgWindow extends Stage {
    private Salg salget;
    private Udlejning udlejningen;
    private OpretSalgWindow opretSalgWindow;
    private CheckBox udlejningCheckBox = null;
    private Sellable betalingsmetode = null;

    public BetalSalgWindow(Salg salget, OpretSalgWindow opretSalgWindow) {
        this.salget = salget;
        this.opretSalgWindow = opretSalgWindow;
        GridPane pane = new GridPane();
        pane.setGridLinesVisible(false);
        Scene scene = new Scene(pane);
        this.initContent(pane);
        this.setScene(scene);
    }

    public BetalSalgWindow(Udlejning udlejningen, OpretSalgWindow opretSalgWindow) {
        this.udlejningen = udlejningen;
        this.opretSalgWindow = opretSalgWindow;
        GridPane pane = new GridPane();
        pane.setGridLinesVisible(false);
        Scene scene = new Scene(pane);
        this.initContent(pane);
        this.setScene(scene);
    }

    public BetalSalgWindow(Salg salget) {
        this.salget = salget;
        GridPane pane = new GridPane();
        pane.setGridLinesVisible(false);
        Scene scene = new Scene(pane);
        this.initContent(pane);
        this.setScene(scene);
    }

    public BetalSalgWindow(Udlejning udlejningen) {
        this.udlejningen = udlejningen;
        GridPane pane = new GridPane();
        pane.setGridLinesVisible(false);
        Scene scene = new Scene(pane);
        this.initContent(pane);
        this.setScene(scene);
    }

    private void initContent(GridPane pane) {
        if (salget == null && udlejningen != null) {
            //Betalingsmetode til Pant
            Label lblBetalingsMetode = new Label("Betalingsmetode til Pant");
            pane.add(lblBetalingsMetode, 0, 0);

            ComboBox<Sellable> sellableComboBox = new ComboBox<>();
            sellableComboBox.getItems().setAll(Sellable.values());
            pane.add(sellableComboBox, 0, 1);

            //Buttons
            Button btnBetal = new Button("Betal");
            btnBetal.setOnAction(event -> this.payAction());
            pane.add(btnBetal, 0, 2);

            Button btnCancel = new Button("Afbryd");
            btnCancel.setOnAction(event -> this.cancelAction());
            pane.add(btnCancel, 1, 2);

            //Betaling af udlejningen
            udlejningCheckBox = new CheckBox();
            pane.add(udlejningCheckBox, 2, 0);

        } else {
            //Betalingsmetode
            Label lblBetalingsMetode = new Label("Betalingsmetode");
            pane.add(lblBetalingsMetode, 0, 0);

            ComboBox<Sellable> sellableComboBox = new ComboBox<>();
            sellableComboBox.getItems().setAll(Sellable.values());
            pane.add(sellableComboBox, 0, 1);

            //Buttons
            Button btnBetal = new Button("Betal");
            btnBetal.setOnAction(event -> this.payAction());
            pane.add(btnBetal, 0, 2);

            Button btnCancel = new Button("Afbryd");
            btnCancel.setOnAction(event -> this.cancelAction());
            pane.add(btnCancel, 1, 2);
        }
    }

    private void payAction() {
        if (udlejningen != null && salget == null) {
            if (udlejningCheckBox.isSelected()) {
                Controller.betal(udlejningen, true, betalingsmetode);
                BetalUdlejningWindow betalUdlejningWindow = new BetalUdlejningWindow(udlejningen);
                betalUdlejningWindow.show();

                opretSalgWindow.close();
                this.close();
            } else {
                Controller.betal(udlejningen, true, betalingsmetode);
                opretSalgWindow.close();
                this.close();
            }
        } else {
            Controller.betal(salget, true, betalingsmetode);
        }
    }

    private void cancelAction() {
        if (opretSalgWindow != null) {
            opretSalgWindow.show();
        }
        this.close();
    }


}
