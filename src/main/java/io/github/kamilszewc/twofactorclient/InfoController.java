package io.github.kamilszewc.twofactorclient;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class InfoController implements Initializable {

    @FXML
    private Label versionLabel;

    @FXML
    private TextArea licenseTextArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String version = getClass().getPackage().getImplementationVersion();
        if (version == null) {
            versionLabel.setText("unknown");
        } else {
            versionLabel.setText(version);
        }

        var inputStream = getClass().getClassLoader().getResourceAsStream("LICENSE");
        var inputReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputReader);
        String license = bufferedReader
                .lines()
                .reduce("", (a, b) -> a + b + "\n");
        licenseTextArea.setText(license);
        licenseTextArea.setWrapText(true);
    }
}