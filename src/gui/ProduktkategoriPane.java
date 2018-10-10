package gui;

import application.controller.Controller;
import application.model.Produkt;
import application.model.Produktkategori;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import storage.Storage;

import java.util.Optional;


public class ProduktkategoriPane extends GridPane {
    private Label lblProduktkategorier, lblProdukter, lblError;
    private ListView<Produktkategori> lwProduktkategorier;
    private ListView<Produkt> lwProdukter;
    private Button btnOpret, btnSlet, btnUpdate, btnOpretProdukt, btnSletProdukt, btnUpdateProdukt;

    public ProduktkategoriPane() {
        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(10);
        this.setGridLinesVisible(false);

        lblProduktkategorier = new Label("Produktkategorier");
        this.add(lblProduktkategorier,0,0);

        lblProdukter = new Label("Produkter");
        this.add(lblProdukter, 1, 0);

        lblError = new Label();
        this.add(lblError, 0,5,2,1);

        lwProduktkategorier = new ListView<>();
        this.add(lwProduktkategorier,0,1);
        //lwProduktkategorier.getItems().addAll(Storage.getProduktkategorier());

        lwProdukter = new ListView<>();
        this.add(lwProdukter,1,1);

        btnOpret = new Button("Opret");
        this.add(btnOpret, 0,2);
        btnOpret.setOnAction(event -> this.createAction());
        btnOpret.setMaxWidth(Double.MAX_VALUE);

        btnUpdate = new Button("Update");
        this.add(btnUpdate,0,3);
        btnUpdate.setOnAction(event -> this.updateAction());
        btnUpdate.setMaxWidth(Double.MAX_VALUE);

        btnSlet = new Button("Slet");
        this.add(btnSlet,0,4);
        btnSlet.setOnAction(event -> this.deleteAction());
        btnSlet.setMaxWidth(Double.MAX_VALUE);

        btnOpretProdukt = new Button("Opret");
        this.add(btnOpretProdukt, 1,2);
        btnOpretProdukt.setOnAction(event -> this.createProduktAction());
        btnOpretProdukt.setMaxWidth(Double.MAX_VALUE);

        btnUpdateProdukt = new Button("Update");
        this.add(btnUpdateProdukt,1,3);
        btnUpdateProdukt.setOnAction(event -> this.updateProduktAction());
        btnUpdateProdukt.setMaxWidth(Double.MAX_VALUE);

        btnSletProdukt = new Button("Slet");
        this.add(btnSletProdukt,1,4);
        btnSletProdukt.setOnAction(event -> this.deleteProduktAction());
        btnSletProdukt.setMaxWidth(Double.MAX_VALUE);

        ChangeListener<Produktkategori> l1 = (op, oldProduktkategori, newProduktkategori) -> this.selectedProduktkategoriChanged();
        lwProduktkategorier.getSelectionModel().selectedItemProperty().addListener(l1);

        this.updateControls();
    }

    private void selectedProduktkategoriChanged() {
        lwProdukter.getItems().clear();
        Produktkategori pk = lwProduktkategorier.getSelectionModel().getSelectedItem();
        if (pk != null) {
            lwProdukter.getItems().addAll(pk.getProdukter());
        }

    }

    private void createAction() {
        OpretProduktkategoriWindow win = new OpretProduktkategoriWindow("Opret produktkategori");
        win.showAndWait();
        updateControls();
    }

    private void updateAction() {
        Produktkategori pk = lwProduktkategorier.getSelectionModel().getSelectedItem();

        if(pk != null) {
            OpretProduktkategoriWindow win = new OpretProduktkategoriWindow("Rediger produktkategori",pk);
            win.showAndWait();
        } else {
            lblError.setText("Der er ikke valgt en produktkategori");
        }
    }

    private void deleteAction() {
        Produktkategori pk = lwProduktkategorier.getSelectionModel().getSelectedItem();
           if (pk != null) {
               Alert alert = createAlert("Slet produktkategori", "Er du sikker på denne handling?");

               Optional<ButtonType> result = alert.showAndWait();

               if (result.isPresent() && result.get() == ButtonType.OK) {
                   try {
                       Controller.deleteProduktkategori(pk);
                   } catch (Exception e) {
                       //e.printStackTrace();
                       TODO://Spørg Peter!
                       lblError.setText(e.getMessage());
                   }
               }
           } else {
               Alert errAlert = createErrAlert("Ups der skete en fejl - ingen produktkategori er valgt");
           }
    }

    private void createProduktAction() {
            OpretProduktWindow win = new OpretProduktWindow("Opret produkt");
            win.showAndWait();
            updateSelectedControls();
    }

    private void updateProduktAction() {
        Produktkategori pk = lwProduktkategorier.getSelectionModel().getSelectedItem();
        Produkt p = lwProdukter.getSelectionModel().getSelectedItem();

        if(pk != null && p != null) {
            OpretProduktWindow win = new OpretProduktWindow("Rediger produkt",pk, p);
            win.showAndWait();
            updateSelectedControls();
        } else {
            lblError.setText("Der er ikke valgt en produktkategori eller produkt");
        }
    }

    private void deleteProduktAction() {
        Produktkategori pk = lwProduktkategorier.getSelectionModel().getSelectedItem();
        Produkt p = lwProdukter.getSelectionModel().getSelectedItem();

        if (pk != null && p != null) {
           Alert alert = createAlert("Slet produkt", "Er du sikker på denne handling?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Controller.deleteprodukt(p);
                updateSelectedControls();
            }
        } else {
            Alert errAlert = createErrAlert("Ups der skete en fejl - intet produkt er valgt");
        }
    }

    public Alert createAlert(String title, String headerText) {
        Stage owner = (Stage) this.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.initOwner(owner);
        alert.setHeaderText(headerText);

        return alert;
    }

    public Alert createErrAlert(String headerText) {
        Stage owner = (Stage) this.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(headerText);
        alert.initOwner(owner);
        alert.showAndWait();

        return alert;
    }

    private void updateSelectedControls() {
        int selectedIndex = lwProduktkategorier.getSelectionModel().getSelectedIndex();
        lwProduktkategorier.getSelectionModel().select(selectedIndex);
        Produktkategori pk = lwProduktkategorier.getSelectionModel().getSelectedItem();
        lwProdukter.getItems().clear();
        lwProdukter.getItems().addAll(pk.getProdukter());
    }

    public void updateControls() {
        lwProduktkategorier.getItems().clear();
        lwProduktkategorier.getItems().addAll(Storage.getProduktkategorier());
    }
}
