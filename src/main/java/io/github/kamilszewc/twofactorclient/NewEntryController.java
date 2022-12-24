package io.github.kamilszewc.twofactorclient;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NewEntryController implements Initializable {

    @FXML
    private TextField serviceNameTextField;

    @FXML
    private TextField userNameTextField;

    @FXML
    private TextField issuerTextField;

    @FXML
    private TextField secretTextField;

    @FXML
    private Button addButton;

    @FXML
    protected void onAddButtonClick() {
        Entry entry = Entry.builder()
                .serviceName(serviceNameTextField.getText())
                .userName(userNameTextField.getText())
                .secret(secretTextField.getText())
                .issuer(issuerTextField.getText())
                .build();
        EntryStorage.entriesList.add(entry);

        Stage stage = (Stage)addButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}