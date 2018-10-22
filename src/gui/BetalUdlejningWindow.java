package gui;

import application.model.Produkt;
import application.model.Udlejning;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class BetalUdlejningWindow extends Stage {
    private ListView<Produkt> lvwProdukterInUdlejning, lvwProdukterToRemove;

    public BetalUdlejningWindow(Udlejning udlejning) {
        GridPane pane = new GridPane();
        pane.setGridLinesVisible(false);
        Scene scene = new Scene(pane);
        this.initContent(pane);
        this.setScene(scene);
    }

    private void initContent(GridPane pane) {

    }
}
