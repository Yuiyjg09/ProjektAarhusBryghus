package gui;

        import application.controller.Controller;
        import application.model.GaveAeske;
        import application.model.Pris;
        import application.model.Produkt;
        import application.model.Produktkategori;
        import javafx.beans.value.ChangeListener;
        import javafx.geometry.Insets;
        import javafx.scene.control.*;
        import javafx.scene.layout.GridPane;
        import javafx.scene.layout.HBox;

        import java.util.Optional;


class ProduktkategoriPane extends GridPane {
    private ListView<Produktkategori> lvwProduktkategorier;
    private ListView<Produkt> lvwProdukter;
    private ListView<GaveAeske> lvwGaveAeske;
    private GaveAeske gaveAesket;

    ProduktkategoriPane() {
        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(10);
        this.setGridLinesVisible(false);

        //Labels
        Label lblProduktkategorier = new Label("Produktkategorier:");
        this.add(lblProduktkategorier,0,0);

        Label lblProdukter = new Label("Produkter (i den valgte produktkategori):");
        this.add(lblProdukter, 1, 0);

        lvwProduktkategorier = new ListView<>();
        this.add(lvwProduktkategorier,0,1);
        lvwProduktkategorier.getItems().setAll(Controller.getProduktkategorier());

        lvwProdukter = new ListView<>();
        this.add(lvwProdukter,1,1);

        //GaveAeske listview
        Label lblGaveAesker = new Label("GaveAesker:");
        this.add(lblGaveAesker,2,0);

        lvwGaveAeske = new ListView<>();
        this.add(lvwGaveAeske,2,1);
        lvwGaveAeske.getItems().setAll(Controller.getGaveAesker());

        ChangeListener<GaveAeske> gaveAeskeChangeListener = (oitem2, olditem2, newitem2) -> this.selectedGaveAeskeChanged();
        lvwGaveAeske.getSelectionModel().selectedItemProperty().addListener(gaveAeskeChangeListener);

        //Produktkategori buttons & HBox
        HBox produktkategoriHBox = new HBox();
        this.add(produktkategoriHBox, 0, 2);

        Button btnOpret = new Button("Opret Produktkategori");
        produktkategoriHBox.getChildren().add(btnOpret);
        btnOpret.setOnAction(event -> this.createAction());

        Button btnUpdate = new Button("Rediger Produktkategori");
        produktkategoriHBox.getChildren().add(btnUpdate);
        btnUpdate.setOnAction(event -> this.updateAction());

        Button btnSlet = new Button("Slet Produktkategori");
        this.add(btnSlet,0,3);
        btnSlet.setOnAction(event -> this.deleteAction());

        //Produkt buttons & HBox
        HBox produktHBox = new HBox();
        this.add(produktHBox,1,2);

        Button btnOpretProdukt = new Button("Opret Produkt");
        produktHBox.getChildren().add(btnOpretProdukt);
        btnOpretProdukt.setOnAction(event -> this.createProduktAction());

        Button btnUpdateProdukt = new Button("Rediger Produkt");
        produktHBox.getChildren().add(btnUpdateProdukt);
        btnUpdateProdukt.setOnAction(event -> this.updateProduktAction());

        Button btnSletProdukt = new Button("Slet Produkt");
        produktHBox.getChildren().add(btnSletProdukt);
        btnSletProdukt.setOnAction(event -> this.deleteProduktAction());

        //GaveAeske buttons
        HBox hBox = new HBox();
        this.add(hBox,2,2);

        Button btnOpretGaveAeske = new Button("Opret Gaveæske");
        hBox.getChildren().add(btnOpretGaveAeske);
        btnOpretGaveAeske.setOnAction(event -> this.createGaveAeskeAction());

        Button btnSletGaveAeske = new Button("Slet GaveAeske");
        hBox.getChildren().add(btnSletGaveAeske);
        btnSletGaveAeske.setOnAction(event -> this.deleteGaveAeskeAction());

        ChangeListener<Produktkategori> l1 = (op, oldProduktkategori, newProduktkategori) -> this.selectedProduktkategoriChanged();
        lvwProduktkategorier.getSelectionModel().selectedItemProperty().addListener(l1);

        this.updateControls();
    }

    private void selectedProduktkategoriChanged() {
        Produktkategori pk = lvwProduktkategorier.getSelectionModel().getSelectedItem();
        if (pk != null) {
            lvwProdukter.getItems().setAll(pk.getProdukter());
        }
    }

    private void selectedGaveAeskeChanged() {
        gaveAesket = lvwGaveAeske.getSelectionModel().getSelectedItem();
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl - Ingen Produktkategori valgt");
            alert.setContentText("Ingen Produktkategori valgt - Vælg venligst en produktkategori");
            alert.showAndWait();
        }
    }

    private void deleteAction() {
        Produktkategori produktkategori = lvwProduktkategorier.getSelectionModel().getSelectedItem();
        if (produktkategori != null) {
            if (!produktkategori.getNavn().equals("Klippekort") && !produktkategori.getNavn().equals("Rundvisninger")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Er du sikker på at du vil slette produktkategorien?", ButtonType.YES, ButtonType.NO);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.YES) {
                    try {
                        Controller.deleteProduktkategori(produktkategori);
                        updateSelectedControls();
                    } catch (Exception e) {
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("Fejl - Produktkategorien har produkter i sig");
                        alert1.setContentText("Produktkategorien har produkter i sig - Fjern først produkterne fra produktkategorien for at slette den");
                        alert1.showAndWait();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Kan ikke slettes");
                alert.setContentText("Denne Produktkategori er uundværelig for systemet - Den kan derfor ikke slettes");
                alert.showAndWait();
            }
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl - Intet Produkt valgt");
            alert.setContentText("Intet Produkt valgt - Vælg venligst et produkt");
            alert.showAndWait();
        }
    }

    private void deleteProduktAction() {
        Produktkategori pk = lvwProduktkategorier.getSelectionModel().getSelectedItem();
        Produkt p = lvwProdukter.getSelectionModel().getSelectedItem();

        if (pk != null && p != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Er du sikker på, at du vil slette produktet?", ButtonType.YES, ButtonType.NO);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {
                Controller.deleteprodukt(p);
                updateSelectedControls();
            }
        }
    }

    private void createGaveAeskeAction() {
        OpretGaveAeskeWindow opretGaveAeskeWindow = new OpretGaveAeskeWindow();
        opretGaveAeskeWindow.showAndWait();

        updateSelectedControls();
    }

    private void deleteGaveAeskeAction() {
        if (gaveAesket != null) {
            for (Produkt produkt:
                    gaveAesket.getIndhold().keySet()) {
                produkt.setLagerAntal(produkt.getLagerAntal() + gaveAesket.getIndhold().get(produkt));
            }
            for (Pris pris:
                    gaveAesket.getPriser()) {
                Controller.getGaveAeskePrisliste().deletePris(pris);
            }

            Controller.deleteGaveAeske(gaveAesket);

            updateSelectedControls();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fejl - Ingen Gaveæske valgt");
            alert.setContentText("Ingen Gaveæske valgt - Vælg venligst en gaveæske for at fortsætte");
            alert.showAndWait();
        }
    }

    private void updateSelectedControls() {
        if (lvwProduktkategorier.getSelectionModel().getSelectedItem() != null) {
            int selectedIndex = lvwProduktkategorier.getSelectionModel().getSelectedIndex();
            lvwProduktkategorier.getItems().setAll(Controller.getProduktkategorier());
            lvwProduktkategorier.getSelectionModel().select(selectedIndex);
            Produktkategori pk = lvwProduktkategorier.getSelectionModel().getSelectedItem();
            lvwProdukter.getItems().setAll(pk.getProdukter());
        }
        int selectedIndex = lvwGaveAeske.getSelectionModel().getSelectedIndex();
        lvwGaveAeske.getItems().setAll(Controller.getGaveAesker());
        lvwGaveAeske.getSelectionModel().select(selectedIndex);
        gaveAesket = lvwGaveAeske.getSelectionModel().getSelectedItem();

    }

    void updateControls() {
        lvwProduktkategorier.getItems().setAll(Controller.getProduktkategorier());
    }
}
