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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button infoButton;

    @FXML
    private ListView<Entry> entriesListView = new ListView<>();

    public static Entry selectedEntry = null;

    final static Clipboard clipboard = Clipboard.getSystemClipboard();

    @FXML
    protected void onAddButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("new-entry-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 300, 300);
            Stage stage = new Stage();
            stage.setTitle("Add new entry");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    protected void onDeleteButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("delete-entry-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 250, 100);
            Stage stage = new Stage();
            stage.setTitle("Delete entry");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    protected void onInfoButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("info-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 500);
            Stage stage = new Stage();
            stage.setTitle("Info");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        EntryStorage entryStorage = null;
        try {
            entryStorage = new EntryStorage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        deleteButton.setDisable(true);

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
                            try {
                                setText(entry.getServiceName() + " (" + entry.getIssuer() + ")\n"
                                        + "code: " + entry.getCode());
                            } catch (NoSuchAlgorithmException e) {
                                throw new RuntimeException(e);
                            } catch (InvalidKeyException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                };
            }
        });
        entriesListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                selectedEntry = entriesListView.getSelectionModel().getSelectedItem();

                if (event.getClickCount() == 2) {
                    final ClipboardContent content = new ClipboardContent();
                    try {
                        content.putString(selectedEntry.getCode());
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    } catch (InvalidKeyException e) {
                        throw new RuntimeException(e);
                    }
                    clipboard.setContent(content);
                }

                deleteButton.setDisable(false);
            }
        });

        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(1000);
                    entriesListView.refresh();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

}