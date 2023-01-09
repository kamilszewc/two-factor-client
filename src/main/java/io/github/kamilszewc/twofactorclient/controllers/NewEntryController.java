package io.github.kamilszewc.twofactorclient.controllers;

import io.github.kamilszewc.Totp;
import io.github.kamilszewc.twofactorclient.Entry;
import io.github.kamilszewc.twofactorclient.EntryStorage;
import io.github.kamilszewc.twofactorclient.QrCodeScanner;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
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
    private ComboBox algorithmComboBox;

    @FXML
    private Button addButton;

    @FXML
    private Button scanScreenButton;

    @FXML
    private Label errorMessageLabel;

    @FXML
    protected void onAddButtonClick() {
        Entry entry = Entry.builder()
                .serviceName(serviceNameTextField.getText())
                .userName(userNameTextField.getText())
                .secret(secretTextField.getText())
                .issuer(issuerTextField.getText())
                .build();

        // Validate entry
        if (entry.getSecret().isBlank()) {
            errorMessageLabel.setText("Secret can not be empty");
            return;
        }

        if (entry.getServiceName().isBlank()) {
            errorMessageLabel.setText("Service name can not be empty");
            return;
        }

        if (entry.getIssuer().isBlank()) {
            errorMessageLabel.setText("Issuer field can not be empty");
            return;
        }

        EntryStorage.entriesList.add(entry);

        Stage stage = (Stage)addButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onScanScreenButtonClick() {
        QrCodeScanner qrCodeScanner = new QrCodeScanner();
        try {
            Optional<Entry> entry = qrCodeScanner.scanScreen();

            if (entry.isPresent()) {
                serviceNameTextField.setText(entry.get().getServiceName());
                userNameTextField.setText(entry.get().getUserName());
                issuerTextField.setText(entry.get().getIssuer());
                secretTextField.setText(entry.get().getSecret());
                String algorithm = entry.get().getAlgorithm();
                if (algorithm.equals(Totp.HashFunction.HMACSHA1.toString())) {
                    algorithmComboBox.getSelectionModel().select(Totp.HashFunction.HMACSHA1.ordinal());
                } else if (algorithm.equals(Totp.HashFunction.HMACSHA256.toString())) {
                    algorithmComboBox.getSelectionModel().select(Totp.HashFunction.HMACSHA256.ordinal());
                } else if (algorithm.equals(Totp.HashFunction.HMACSHA512.toString())) {
                    algorithmComboBox.getSelectionModel().select(Totp.HashFunction.HMACSHA512.ordinal());
                }
            } else {

                errorMessageLabel.setText("Could not find any QR code");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        algorithmComboBox.setItems(FXCollections.observableArrayList(Totp.HashFunction.values()));
        algorithmComboBox.getSelectionModel().selectFirst();
    }
}