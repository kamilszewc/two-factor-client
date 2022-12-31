package io.github.kamilszewc.twofactorclient;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("controllers/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 240, 450);
        stage.setTitle("Two Factor Client");
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("logo.jpg")));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setOnCloseRequest((event) -> Platform.exit());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}