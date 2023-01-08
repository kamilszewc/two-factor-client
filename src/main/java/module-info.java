module io.github.kamilszewc.twofactorclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires io.github.kamilszewc.totp;
    requires java.datatransfer;
    requires java.desktop;
    requires com.google.zxing;
    requires com.google.zxing.javase;

    opens io.github.kamilszewc.twofactorclient to javafx.fxml;
    exports io.github.kamilszewc.twofactorclient;
    exports io.github.kamilszewc.twofactorclient.controllers;
    opens io.github.kamilszewc.twofactorclient.controllers to javafx.fxml;
}