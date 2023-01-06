package io.github.kamilszewc.twofactorclient.controllers;

import io.github.kamilszewc.twofactorclient.EntryStorage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import lombok.Getter;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;
import java.util.ResourceBundle;

public class HelpController implements Initializable {

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
    protected void onResetButtonClick() {
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

        versionLabel.setText(getVersion().orElse("unknown"));

        var inputStream = getClass().getClassLoader().getResourceAsStream("LICENSE");
        var inputReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputReader);
        String license = bufferedReader
                .lines()
                .reduce("", (a, b) -> a + b + "\n");
        licenseTextArea.setText(license);
        licenseTextArea.setWrapText(true);
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