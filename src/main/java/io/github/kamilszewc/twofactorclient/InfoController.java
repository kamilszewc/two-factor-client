package io.github.kamilszewc.twofactorclient;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoController implements Initializable {

    @FXML
    private Label versionLabel;

    @FXML
    private TextArea licenseTextArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        versionLabel.setText("latest");
    }
}