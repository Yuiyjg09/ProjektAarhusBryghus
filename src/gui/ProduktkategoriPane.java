package gui;

import application.controller.Controller;
import application.model.Produkt;
import application.model.Produktkategori;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


import java.util.Optional;


public class ProduktkategoriPane extends GridPane {
    private Label lblError;
    private ListView<Produktkategori> lvwProduktkategorier;
    private ListView<Produkt> lvwProdukter;

    public ProduktkategoriPane() {
        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(10);
        this.setGridLinesVisible(false);

        Label lblProduktkategorier = new Label("Produktkategorier");
        this.add(lblProduktkategorier,0,0);

        Label lblProdukter = new Label("Produkter");
        this.add(lblProdukter, 1, 0);

        lblError = new Label();
        this.add(lblError, 0,5,2,1);

        lvwProduktkategorier = new ListView<>();
        this.add(lvwProduktkategorier,0,1);

        lvwProduktkategorier.getItems().addAll(Controller.getProduktkategorier());


        lvwProdukter = new ListView<>();
        this.add(lvwProdukter,1,1);

        Button btnOpret = new Button("Opret");
        this.add(btnOpret, 0,2);
        btnOpret.setOnAction(event -> this.createAction());
        btnOpret.setMaxWidth(Double.MAX_VALUE);

        Button btnUpdate = new Button("Update");
        this.add(btnUpdate,0,3);
        btnUpdate.setOnAction(event -> this.updateAction());
        btnUpdate.setMaxWidth(Double.MAX_VALUE);

        Button btnSlet = new Button("Slet");
        this.add(btnSlet,0,4);
        btnSlet.setOnAction(event -> this.deleteAction());
        btnSlet.setMaxWidth(Double.MAX_VALUE);

        Button btnOpretProdukt = new Button("Opret");
        this.add(btnOpretProdukt, 1,2);
        btnOpretProdukt.setOnAction(event -> this.createProduktAction());
        btnOpretProdukt.setMaxWidth(Double.MAX_VALUE);

        Button btnUpdateProdukt = new Button("Update");
        this.add(btnUpdateProdukt,1,3);
        btnUpdateProdukt.setOnAction(event -> this.updateProduktAction());
        btnUpdateProdukt.setMaxWidth(Double.MAX_VALUE);

        Button btnSletProdukt = new Button("Slet");
        this.add(btnSletProdukt,1,4);
        btnSletProdukt.setOnAction(event -> this.deleteProduktAction());
        btnSletProdukt.setMaxWidth(Double.MAX_VALUE);

        ChangeListener<Produktkategori> l1 = (op, oldProduktkategori, newProduktkategori) -> this.selectedProduktkategoriChanged();
        lvwProduktkategorier.getSelectionModel().selectedItemProperty().addListener(l1);

        this.updateControls();
    }

    private void selectedProduktkategoriChanged() {
        lvwProdukter.getItems().clear();
        Produktkategori pk = lvwProduktkategorier.getSelectionModel().getSelectedItem();
        if (pk != null) {
            lvwProdukter.getItems().addAll(pk.getProdukter());
        }

    }

    private void createAction() {
        OpretProduktkategoriWindow win = new OpretProduktkategoriWindow("Opret produktkategori");
        win.showAndWait();


        updateControls();
    }

    private void updateAction() {
        Produktkategori pk = lvwProduktkategorier.getSelectionModel().getSelectedItem();

        if(pk != null) {
            OpretProduktkategoriWindow win = new OpretProduktkategoriWindow("Rediger produktkategori",pk);
            win.showAndWait();
        } else {
            lblError.setText("Der er ikke valgt en produktkategori");
        }
    }

    private void deleteAction() {
        Produktkategori pk = lvwProduktkategorier.getSelectionModel().getSelectedItem();
           if (pk != null) {
               Alert alert = new Alert(Alert.AlertType.INFORMATION);

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

           }
    }

    private void createProduktAction() {
            OpretProduktWindow win = new OpretProduktWindow("Opret produkt");
            win.showAndWait();


            updateSelectedControls();
    }

    private void updateProduktAction() {
        Produktkategori pk = lvwProduktkategorier.getSelectionModel().getSelectedItem();
        Produkt p = lvwProdukter.getSelectionModel().getSelectedItem();

        if(pk != null && p != null) {
            OpretProduktWindow win = new OpretProduktWindow("Rediger produkt",pk, p);
            win.showAndWait();
            updateSelectedControls();
        } else {
            lblError.setText("Der er ikke valgt en produktkategori eller produkt");
        }
    }

    private void deleteProduktAction() {
        Produktkategori pk = lvwProduktkategorier.getSelectionModel().getSelectedItem();
        Produkt p = lvwProdukter.getSelectionModel().getSelectedItem();

        if (pk != null && p != null) {
           Alert alert = new Alert(Alert.AlertType.INFORMATION);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Controller.deleteprodukt(p);
                updateSelectedControls();
            }
        } else {

        }
    }



    private void updateSelectedControls() {
        int selectedIndex = lvwProduktkategorier.getSelectionModel().getSelectedIndex();
        lvwProduktkategorier.getSelectionModel().select(selectedIndex);
        Produktkategori pk = lvwProduktkategorier.getSelectionModel().getSelectedItem();
        lvwProdukter.getItems().setAll(pk.getProdukter());
    }

    public void updateControls() {
        lvwProduktkategorier.getItems().setAll(Controller.getProduktkategorier());


    }
}
