package io.github.kamilszewc.twofactorclient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button addButton;

    @FXML
    private ListView<Entry> entriesListView = new ListView<>();

    static public ObservableList<Entry> entriesList = FXCollections.observableArrayList();

    @FXML
    protected void onAddButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("new-entry-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 300, 300);
            Stage stage = new Stage();
            stage.setTitle("Add new entry");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EntryStorage entryStorage = new EntryStorage();
        entriesList.addAll(entryStorage.getAllEntries());
        entriesListView.setItems(entriesList);

        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(10000);
                    entriesListView.refresh();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}