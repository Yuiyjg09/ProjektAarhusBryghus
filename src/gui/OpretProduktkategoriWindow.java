package gui;

import application.controller.Controller;
import application.model.Maalbar;
import application.model.Produktkategori;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

    private Label lblNavn, lblMetrik, lblBeksrivelse, lblError;
    private TextField txfnavn;
    private ComboBox cbMetrik;
    private TextArea txaBeskrivelse;
    private Button btnOk, btnCancel;

    private void initContent(GridPane pane) {
        pane.setPadding(new Insets(10));
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setGridLinesVisible(false);

        lblNavn = new Label("Angiv navn");
        pane.add(lblNavn, 0,0);

        lblMetrik = new Label("Vælg metrik");
        pane.add(lblMetrik, 0,2);

        lblBeksrivelse = new Label("Angiv beskrivelse");
        pane.add(lblBeksrivelse, 1,0);

        txfnavn = new TextField();
        pane.add(txfnavn, 0,1);

        cbMetrik = new ComboBox();
        pane.add(cbMetrik, 0,3);
        cbMetrik.getItems().setAll(Maalbar.values());

        txaBeskrivelse = new TextArea();
        pane.add(txaBeskrivelse, 1,1,1,4);

        lblError = new Label();
        pane.add(lblError,0,5,2,1);

        btnOk = new Button("Ok");
        pane.add(btnOk,0,6);
        btnOk.setOnAction(event -> this.okAction());
        btnOk.setMaxWidth(Double.MAX_VALUE);

        btnCancel = new Button("Cancel");
        pane.add(btnCancel,1,6);
        btnCancel.setOnAction(event -> this.cancelAction());
        btnCancel.setMaxWidth(Double.MAX_VALUE);

        this.initControls();
    }

    private void okAction() {
        String navn = txfnavn.getText().trim();
        Maalbar metrik = (Maalbar) cbMetrik.getSelectionModel().getSelectedItem();
        String beskrivelse = txaBeskrivelse.getText().trim();

        if (navn.length() < 1 || beskrivelse.length() < 1 || metrik == null) {
            lblError.setText("Udfyld venligst alle felter");
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
