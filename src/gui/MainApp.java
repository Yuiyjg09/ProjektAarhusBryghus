package gui;

import application.controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
        this.initContent(pane);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    //-------------------------------------------

    private void initContent(BorderPane pane) {
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
        produktkategoriTab.setOnSelectionChanged(event ->produktkategoritab.updateControls());

        //-------------------------------------------
        //PrislistePane
        Tab prislisteTab = new Tab("Prislister");
        tabPane.getTabs().add(prislisteTab);

        PrislistePane prislistetab = new PrislistePane();
        prislisteTab.setContent(prislistetab);

        //-------------------------------------------
        //SalgPane
        Tab salgTab = new Tab("Salg");
        tabPane.getTabs().add(salgTab);

        SalgPane salgPane = new SalgPane();
        salgTab.setContent(salgPane);
    }

    //-------------------------------------------


}
