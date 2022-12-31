package io.github.kamilszewc.twofactorclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PasswordController implements Initializable {
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button okButton;

    @FXML
    protected void onOkButtonClick(ActionEvent actionEvent) {
        MainController.entryStorage.setPassword(passwordField.getText());
        try {
            MainController.entryStorage.readEntriesFromDisk();
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
        } catch (Exception ex) {
            System.out.println("Wrong password");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}