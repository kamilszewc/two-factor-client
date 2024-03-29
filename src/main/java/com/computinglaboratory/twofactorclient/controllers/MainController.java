package com.computinglaboratory.twofactorclient.controllers;

import com.computinglaboratory.twofactorclient.Application;
import com.computinglaboratory.twofactorclient.Entry;
import com.computinglaboratory.twofactorclient.EntryStorage;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
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
    private Button helpButton;

    @FXML
    private ListView<Entry> entriesListView = new ListView<>();

    public static Entry selectedEntry = null;

    final static Clipboard clipboard = Clipboard.getSystemClipboard();

    final static EntryStorage entryStorage = new EntryStorage();

    @FXML
    protected void onAddButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("new-entry-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 300, 270);
            scene.getStylesheets().add(Application.class.getResource("styles/style.css").toExternalForm());
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
            Scene scene = new Scene(fxmlLoader.load(), 260, 100);
            scene.getStylesheets().add(Application.class.getResource("styles/style.css").toExternalForm());
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
    protected void onHelpButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("help-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(Application.class.getResource("styles/style.css").toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("Help");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        try {
            if (entryStorage.isFileCreated()) {
                try {
                    entryStorage.readEntriesFromDisk();

                } catch (Exception ex) {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("password-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 230, 100);
                    scene.getStylesheets().add(Application.class.getResource("styles/style.css").toExternalForm());
                    Stage stage = new Stage();
                    stage.setTitle("Type password");
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.setAlwaysOnTop(true);
                    stage.setOnCloseRequest((event) -> Platform.exit());
                    stage.show();
                }
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("first-password-view.fxml"));
                //Scene scene = new Scene(fxmlLoader.load());
                Scene scene = new Scene(fxmlLoader.load());
                scene.getStylesheets().add(Application.class.getResource("styles/style.css").toExternalForm());
                Stage stage = new Stage();
                stage.sizeToScene();
                stage.setTitle("Initial config");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setAlwaysOnTop(true);
                stage.setOnCloseRequest((event) -> Platform.exit());
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        deleteButton.setDisable(true);

        ObservableList<Entry> entriesList = entryStorage.getEntries();
        entriesListView.setItems(entriesList);
        entriesListView.setCellFactory(new Callback<>() {
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
                                        + "code: " + entry.getCode() + " (" + entry.getRemainingCodeValidity() + " sec)");
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
        entriesListView.setOnMouseClicked(event -> {

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
        });

        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(1000);
                    entriesListView.refresh();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

}