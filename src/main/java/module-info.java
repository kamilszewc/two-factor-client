module com.computinglaboratory.twofactorclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires com.computinglaboratory.totp;
    requires java.datatransfer;
    requires java.desktop;
    requires com.google.zxing;
    requires com.google.zxing.javase;

    opens com.computinglaboratory.twofactorclient to javafx.fxml;
    exports com.computinglaboratory.twofactorclient;
    exports com.computinglaboratory.twofactorclient.controllers;
    opens com.computinglaboratory.twofactorclient.controllers to javafx.fxml;
}