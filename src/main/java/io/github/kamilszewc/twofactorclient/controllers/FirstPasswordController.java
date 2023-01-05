package io.github.kamilszewc.twofactorclient.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class FirstPasswordController implements Initializable {
    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button okButton;

    @FXML
    private Label infoLabel;

    @FXML
    protected void onOkButtonClick(ActionEvent actionEvent) {

        if (passwordField.getText().isBlank()) {
            infoLabel.setText("New password is empty");
            infoLabel.setVisible(true);
            return;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            infoLabel.setText("Passwords are not the same");
            infoLabel.setVisible(true);
            return;
        }

        MainController.entryStorage.setPassword(passwordField.getText());
        try {
            MainController.entryStorage.readEntriesFromDisk();
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}