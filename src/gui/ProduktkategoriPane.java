package gui;

import application.controller.Controller;
import application.model.Produkt;
import application.model.Produktkategori;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.util.Optional;

import static gui.MainApp.createAlert;
import static gui.MainApp.createErrAlert;


public class ProduktkategoriPane extends GridPane {
    private Label lblProduktkategorier, lblProdukter;
    private ListView<Produktkategori> lwProduktkategorier;
    private ListView<Produkt> lwProdukter;
    private Button btnOpret, btnSlet, btnUpdate, btnOpretProdukt, btnSletProdukt, btnUpdateProdukt;
    private VBox vb1, vb2;
    private HBox hb1;

    public ProduktkategoriPane() {
        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(10);
        this.setGridLinesVisible(false);

        lblProduktkategorier = new Label("Produktkategorier");
        lblProdukter = new Label("Produkter");
        lwProduktkategorier = new ListView<>();
        lwProduktkategorier.getItems().addAll(Controller.getProduktkategorier());
        lwProdukter = new ListView<>();

        btnOpret = new Button("Opret");
        btnOpret.setOnAction(event -> this.createAction());
        btnOpret.setMaxWidth(Double.MAX_VALUE);

        btnUpdate = new Button("Update");
        btnUpdate.setOnAction(event -> this.updateAction());
        btnUpdate.setMaxWidth(Double.MAX_VALUE);

        btnSlet = new Button("Slet");
        btnSlet.setOnAction(event -> this.deleteAction());
        btnSlet.setMaxWidth(Double.MAX_VALUE);

        btnOpretProdukt = new Button("Opret");
        btnOpretProdukt.setOnAction(event -> this.createProduktAction());
        btnOpretProdukt.setMaxWidth(Double.MAX_VALUE);

        btnUpdateProdukt = new Button("Update");
        btnUpdateProdukt.setOnAction(event -> this.updateProduktAction());
        btnUpdateProdukt.setMaxWidth(Double.MAX_VALUE);

        btnSletProdukt = new Button("Slet");
        btnSletProdukt.setOnAction(event -> this.deleteProduktAction());
        btnSletProdukt.setMaxWidth(Double.MAX_VALUE);

        vb1 = new VBox(10);
        vb1.getChildren().addAll(lblProduktkategorier,lwProduktkategorier,btnOpret,btnUpdate,btnSlet);

        vb2 = new VBox(10);
        vb2.getChildren().addAll(lblProdukter,lwProdukter,btnOpretProdukt,btnUpdateProdukt,btnSletProdukt);

        hb1 = new HBox(50);
        this.add(hb1,0,0);
        hb1.getChildren().addAll(vb1,vb2);

        ChangeListener<Produktkategori> pkListener = (op, oldObj, newObj) -> this.selectedProduktkategoriChanged();
        lwProduktkategorier.getSelectionModel().selectedItemProperty().addListener(pkListener);

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
            updateControls();
        } else {
            createErrAlert("Ups! Der er ikke valgt en produktkategori", (Stage)this.getScene().getWindow());
        }
    }

    private void deleteAction() {
        Produktkategori pk = lwProduktkategorier.getSelectionModel().getSelectedItem();
           if (pk != null) {
               Alert alert = createAlert("Slet produktkategori", "Er du sikker på denne handling?", (Stage) this.getScene().getWindow());

               Optional<ButtonType> result = alert.showAndWait();

               if (result.isPresent() && result.get() == ButtonType.OK) {
                   //Controller.deleteProduktkategori(pk);
                   try {
                       Controller.deleteProduktkategori(pk);
                       updateControls();
                   } catch (Exception e) {
                       //e.printStackTrace();
                       TODO://Spørg Peter!
                       //lblError.setText(e.getMessage());
                       createErrAlert("OBS! denne produktkategori indeholder produkter og kan ikke slettes", (Stage)this.getScene().getWindow());
                   }
               }
           } else {
               createErrAlert("Ups der skete en fejl - ingen produktkategori er valgt", (Stage) this.getScene().getWindow());
           }
    }

    private void createProduktAction() {
            OpretProduktWindow win = new OpretProduktWindow("Opret produkt");
            win.showAndWait();

            updateControls();
    }

    private void updateProduktAction() {
        Produktkategori pk = lwProduktkategorier.getSelectionModel().getSelectedItem();
        Produkt p = lwProdukter.getSelectionModel().getSelectedItem();

        if(pk != null) {
            if (p != null) {
                OpretProduktWindow win = new OpretProduktWindow("Rediger produkt",pk, p);
                win.showAndWait();

                updateSelectedControls();
            } else {
                createErrAlert("Ups! Der er ikke valgt et produkt", (Stage) this.getScene().getWindow());
            }
        } else {
            createErrAlert("Ups! Der er ikke valgt en produktkategori", (Stage) this.getScene().getWindow());
        }
    }

    private void deleteProduktAction() {
        Produktkategori pk = lwProduktkategorier.getSelectionModel().getSelectedItem();
        Produkt p = lwProdukter.getSelectionModel().getSelectedItem();

        if (pk != null && p != null) {
           Alert alert = createAlert("Slet produkt", "Er du sikker på denne handling?",(Stage) this.getScene().getWindow());

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Controller.deleteprodukt(p);
                updateSelectedControls();
            }
        } else {
            createErrAlert("Ups der skete en fejl - intet produkt er valgt",(Stage) this.getScene().getWindow());
        }
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
        lwProduktkategorier.getItems().addAll(Controller.getProduktkategorier());
    }
}
