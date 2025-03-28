module com.example.libapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires jbcrypt;

    opens com.example.libapp to javafx.fxml;
    exports com.example.libapp;
    exports com.example.libapp.controllers;
    opens com.example.libapp.controllers to javafx.fxml;
}