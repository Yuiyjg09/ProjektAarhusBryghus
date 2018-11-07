package gui;

import application.controller.Controller;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void init() {
        Controller.initStorage();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Aarhus Bryghus");
        BorderPane pane = new BorderPane();
        this.initContent(pane, stage);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    //-------------------------------------------

    private void initContent(BorderPane pane, Stage stage) {
        TabPane tabPane = new TabPane();
        this.initTabPane(tabPane);
        pane.setCenter(tabPane);
    }

    private void initTabPane(TabPane tabPane) {
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        //-------------------------------------------
        //ProduktkategoriPane
        Tab produktkategoriTab = new Tab("Produktkategorier");
        tabPane.getTabs().add(produktkategoriTab);

        ProduktkategoriPane produktkategoritab = new ProduktkategoriPane();
        produktkategoriTab.setContent(produktkategoritab);
        produktkategoriTab.setOnSelectionChanged(event -> produktkategoritab.updateControls());

        //-------------------------------------------
        //PrislistePane
        Tab prislisteTab = new Tab("Prislister");
        tabPane.getTabs().add(prislisteTab);

        PrislistePane prislistetab = new PrislistePane();
        prislisteTab.setContent(prislistetab);

        ChangeListener<Tab> tabPrislisteChangeListener = (oitem, olditem, newitem) -> prislistetab.updateControls();
        tabPane.getSelectionModel().selectedItemProperty().addListener(tabPrislisteChangeListener);

        //-------------------------------------------
        //SalgPane
        Tab salgTab = new Tab("Salg");
        tabPane.getTabs().add(salgTab);

        SalgPane salgPane = new SalgPane();
        salgTab.setContent(salgPane);

        ChangeListener<Tab> tabSalgChangeListener = (oitem1, olditem1, newitem1) -> salgPane.updateControls();
        tabPane.getSelectionModel().selectedItemProperty().addListener(tabSalgChangeListener);
        //-------------------------------------------
        //UdlejningsPane + Listener
        Tab udlejningsTab = new Tab("Udlejninger");
        tabPane.getTabs().add(udlejningsTab);

        Udlejningspane udlejningspane = new Udlejningspane();
        udlejningsTab.setContent(udlejningspane);

        ChangeListener<Tab> tabUdlejningChangeListener = (oitem2, olditem2, newitem2) -> udlejningspane.updateControls();
        tabPane.getSelectionModel().selectedItemProperty().addListener(tabUdlejningChangeListener);

        //-------------------------------------------
        //AdministrationsPane
        Tab adminTab = new Tab("Administration");
        tabPane.getTabs().add(adminTab);

        AdministrationsPane administrationsPane = new AdministrationsPane();
        adminTab.setContent(administrationsPane);

        ChangeListener<Tab> tabAdminChangeListener = (oitem3, olditem3, newitem3) -> administrationsPane.updateControls();
        tabPane.getSelectionModel().selectedItemProperty().addListener(tabAdminChangeListener);

    }

    //-------------------------------------------


}
