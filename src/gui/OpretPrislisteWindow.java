package gui;

import application.model.Produkt;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class OpretPrislisteWindow extends GridPane {
    private Label lblNavn;
    private Label lblDatoStart;
    private Label lblDatoSlut;
    private TextArea txaBeskrivelse;
    private ListView<Produkt> lvwProdukter, lvwTilfoejet;
    private TextField txfPris;

    public OpretPrislisteWindow() {
        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(10);
        this.setGridLinesVisible(false);


    }
}
