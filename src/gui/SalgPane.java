package gui;

import application.model.Salg;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

public class SalgPane extends GridPane {
    private ListView<Salg> salgListView;
    private final FileChooser fileChooserSave = new FileChooser();

    public SalgPane() {
        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(10);
        this.setGridLinesVisible(false);


    }

    private void updateControls() {

    }
}
