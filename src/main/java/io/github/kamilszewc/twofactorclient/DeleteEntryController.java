package io.github.kamilszewc.twofactorclient;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteEntryController implements Initializable {

    @FXML
    private TextField deleteTextField;

    @FXML
    private Button deleteButton;

    @FXML
    protected void onDeleteButtonClick() {
        Entry selectedEntry = MainController.selectedEntry;
        EntryStorage.entriesList.remove(selectedEntry);
        Stage stage = (Stage)deleteButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deleteButton.setDisable(true);
        deleteTextField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (deleteTextField.getText().equals("delete")) {
                    deleteButton.setDisable(false);
                } else {
                    deleteButton.setDisable(true);
                }
            }
        });
    }
}