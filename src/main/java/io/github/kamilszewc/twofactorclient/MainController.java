package io.github.kamilszewc.twofactorclient;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button addButton;

    @FXML
    private ListView<Entry> entriesListView = new ListView<>();

    final static Clipboard clipboard = Clipboard.getSystemClipboard();

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
        ObservableList<Entry> entriesList = entryStorage.getEntries();
        entriesListView.setItems(entriesList);
        entriesListView.setCellFactory(new Callback<ListView<Entry>, ListCell<Entry>>() {
            @Override
            public ListCell<Entry> call(ListView<Entry> param) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(Entry entry, boolean empty) {
                        super.updateItem(entry, empty);
                        if (empty || entry == null) {
                            setText(null);
                        } else {
                            setText(entry.getServiceName() + " (" + entry.getIssuer() + ")\n"
                                    + "code: " + entry.getCode());
                        }
                    }
                };
            }
        });
        entriesListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    Entry entry = entriesListView.getSelectionModel().getSelectedItem();
                    System.out.println(entry);

                    final ClipboardContent content = new ClipboardContent();
                    content.putString(String.valueOf(entry.getCode()));
                    clipboard.setContent(content);
                }
            }
        });

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