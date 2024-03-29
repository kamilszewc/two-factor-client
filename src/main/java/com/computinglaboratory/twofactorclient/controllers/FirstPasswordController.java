package com.computinglaboratory.twofactorclient.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class FirstPasswordController implements Initializable {
    @FXML
    private Accordion accordion;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button okButton;

    @FXML
    private Button noPasswordOkButton;

    @FXML
    private TitledPane withPasswordTitledPane;

    @FXML
    private TitledPane noPasswordTitledPane;

    @FXML
    private Label infoLabel;

    @FXML
    protected void onNoPasswordOkButtonClick(ActionEvent actionEvent) {
        System.out.println(withPasswordTitledPane.isExpanded());
        System.out.println(noPasswordTitledPane.isExpanded());

        MainController.entryStorage.setPassword("");
        try {
            MainController.entryStorage.readEntriesFromDisk();
            Stage stage = (Stage) noPasswordOkButton.getScene().getWindow();
            stage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    protected void onOkButtonClick(ActionEvent actionEvent) {

        if (passwordField.getText().isBlank()) {
            infoLabel.setText("Password is empty");
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
        this.accordion.setExpandedPane(withPasswordTitledPane);
        this.accordion.expandedPaneProperty().addListener(((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                Stage stage = (Stage) this.accordion.getScene().getWindow();
                stage.sizeToScene();
            });
        }));
    }

}