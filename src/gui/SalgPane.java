package gui;

import application.model.Salg;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class SalgPane extends GridPane {
    private ListView<Salg> salgListView;
    private final FileChooser fileChooserSave = new FileChooser();

    public SalgPane() {
        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(10);
        this.setGridLinesVisible(false);

        Label lblSalg = new Label("Salg idag");
        this.add(lblSalg, 0, 0);

        salgListView = new ListView<>();
        this.add(salgListView, 0, 1);

    }

    private void updateControls() {

    }

    //TODO: Printing method for all Salg today
    private void printAction() {
        ArrayList<String> strings = new ArrayList<>();

    }

    private static void configureFileChooserSave(final FileChooser fileChooser, String name) {
        fileChooser.setTitle("Save File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName(name);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt")
        );
    }

    private void saveFileEvent(FileChooser fileChooserSave, ArrayList<String> toWrite) {
        configureFileChooserSave(fileChooserSave, ("Slutseddel - DATO: " + LocalDate.now().toString()));
        File currentFile = fileChooserSave.showSaveDialog(null);
        if (currentFile != null) {
            try {
                FileWriter fileWriter = new FileWriter(currentFile);
                for (String str:
                        toWrite) {
                    fileWriter.append(str).append(System.getProperty("line.separator"));
                }
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
