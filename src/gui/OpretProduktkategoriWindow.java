package gui;

import application.controller.Controller;
import application.model.Maalbar;
import application.model.Produktkategori;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static gui.MainApp.createErrAlert;

public class OpretProduktkategoriWindow extends Stage {
    private Produktkategori produktkategori;

    public OpretProduktkategoriWindow(String title, Produktkategori produktkategori) {
        this.produktkategori = produktkategori;
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setResizable(false);

        this.setTitle(title);
        GridPane pane = new GridPane();
        this.initContent(pane);

        Scene scene = new Scene(pane);
        this.setScene(scene);

    }

    public OpretProduktkategoriWindow(String title) {
        this(title, null);
    }

    private Label lblNavn, lblMetrik, lblBeksrivelse;
    private TextField txfnavn;
    private ComboBox cbMetrik;
    private TextArea txaBeskrivelse;
    private Button btnOk, btnCancel;
    private VBox vb1, vb2;
    private HBox hb1, hb2;

    private void initContent(GridPane pane) {
        pane.setPadding(new Insets(20));
        pane.setHgap(20);
        pane.setVgap(20);
        pane.setGridLinesVisible(false);

        lblNavn = new Label("Angiv navn");
        lblMetrik = new Label("Vælg metrik");
        lblBeksrivelse = new Label("Angiv beskrivelse");
        txfnavn = new TextField();
        cbMetrik = new ComboBox();
        cbMetrik.getItems().setAll(Maalbar.values());
        txaBeskrivelse = new TextArea();
        txaBeskrivelse.setPrefWidth(250);
        txaBeskrivelse.setPrefHeight(150);

        btnOk = new Button("Ok");
        btnOk.setOnAction(event -> this.okAction());
        btnOk.setMaxWidth(Double.MAX_VALUE);

        btnCancel = new Button("Cancel");
        btnCancel.setOnAction(event -> this.cancelAction());
        btnCancel.setMaxWidth(Double.MAX_VALUE);

        vb1 = new VBox(10);
        vb1.getChildren().setAll(lblNavn,txfnavn,lblMetrik,cbMetrik);

        vb2 = new VBox(10);
        vb2.getChildren().addAll(lblBeksrivelse,txaBeskrivelse);

        hb1 = new HBox(20);
        pane.add(hb1,0,0);
        hb1.getChildren().addAll(vb1,vb2);
        hb1.setAlignment(Pos.CENTER_LEFT);

        hb2 = new HBox(10);
        pane.add(hb2,0,1);
        hb2.getChildren().addAll(btnOk,btnCancel);
        hb2.setAlignment(Pos.CENTER);
        hb2.setHgrow(btnOk, Priority.ALWAYS);
        hb2.setHgrow(btnCancel, Priority.ALWAYS);

        this.initControls();
    }

    private void okAction() {
        String navn = txfnavn.getText().trim();
        Maalbar metrik = (Maalbar) cbMetrik.getSelectionModel().getSelectedItem();
        String beskrivelse = txaBeskrivelse.getText().trim();

        if (navn.length() < 1) {
            createErrAlert("OBS! der skal udfyldes et navn",(Stage) this.getScene().getWindow());
            return;
        }

        if (beskrivelse.length() < 1) {
            createErrAlert("OBS! der skal udfyldes en beskrivelse",(Stage) this.getScene().getWindow());
            return;
        }

        if (metrik == null) {
            createErrAlert("OBS! der er ikke valgt en metrik",(Stage) this.getScene().getWindow());
            return;
        }

        if (produktkategori != null) {
            Controller.updateProduktkategori(navn, beskrivelse, metrik, produktkategori);
        } else {
            Controller.createProduktkategori(navn,beskrivelse,metrik);
        }

        this.hide();
    }

    private void cancelAction() {
        this.hide();
    }

    private void initControls() {
        if (produktkategori != null) {
            txfnavn.setText(produktkategori.getNavn());
            txaBeskrivelse.setText(produktkategori.getBeskrivelse());
            cbMetrik.setValue(produktkategori.getMetrik());
        }
    }
}
