module io.github.kamilszewc.twofactorclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

    opens io.github.kamilszewc.twofactorclient to javafx.fxml;
    exports io.github.kamilszewc.twofactorclient;
}