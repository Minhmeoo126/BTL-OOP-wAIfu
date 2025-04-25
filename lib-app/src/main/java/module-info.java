module com.example.libapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    opens com.example.libapp to javafx.fxml;
    exports com.example.libapp;
    exports com.example.libapp.controllers;
    exports com.example.libapp.persistence;
    opens com.example.libapp.controllers to javafx.fxml;
    opens com.example.libapp.model to javafx.base;
    exports com.example.libapp.model;
}