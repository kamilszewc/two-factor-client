package com.computinglaboratory.twofactorclient.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class HelpController implements Initializable {

    @FXML
    public TitledPane licensePane;

    @FXML
    public TitledPane passwordChangePane;

    @FXML
    public Accordion accordion;

    @FXML
    private Label versionLabel;

    @FXML
    private TextArea licenseTextArea;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label infoLabel;

    @FXML
    protected void onChangePasswordButtonClick() {
        if (!currentPasswordField.getText().equals(MainController.entryStorage.getPassword())) {
            infoLabel.setText("Wrong password");
            infoLabel.setVisible(true);
            return;
        }

        if (newPasswordField.getText().isBlank()) {
            infoLabel.setText("New password is empty");
            infoLabel.setVisible(true);
            return;
        }

        if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
            infoLabel.setText("Passwords are not the same");
            infoLabel.setVisible(true);
            return;
        }

        MainController.entryStorage.setPassword(newPasswordField.getText());
        try {
            MainController.entryStorage.writeEntriesToDisk();
            infoLabel.setText("New password was set");
            infoLabel.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            Platform.exit();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String version = getVersion().orElse("unknown");

        versionLabel.setText("Two Factor Client - version " + version);

        var inputStream = getClass().getClassLoader().getResourceAsStream("LICENSE");
        var inputReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputReader);
        String license = bufferedReader
                .lines()
                .reduce("", (a, b) -> a + b + "\n");
        licenseTextArea.setText(license);
        licenseTextArea.setWrapText(true);

        this.accordion.expandedPaneProperty().addListener(((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                Stage stage = (Stage) this.accordion.getScene().getWindow();
                stage.sizeToScene();
            });
        }));
    }

    private Optional<String> getVersion() {
       InputStream inputStream = getClass().getClassLoader().getResourceAsStream("version.txt");
       var inputStreamReader = new InputStreamReader(inputStream);
       BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
       try {
           return Optional.of(bufferedReader.readLine());
       } catch (Exception ex) {
            return Optional.empty();
       }
    }
}