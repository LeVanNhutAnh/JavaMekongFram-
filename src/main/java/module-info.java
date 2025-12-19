module com.mekongfram {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;
    requires transitive java.sql;
    requires java.desktop;
    requires java.net.http;
    requires jdk.httpserver;
    requires com.google.gson;
    requires org.xerial.sqlitejdbc;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires jbcrypt;

    // PDF Export (iText)
    requires kernel;
    requires layout;

    // Excel Export (Apache POI)
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.mekongfarm to javafx.fxml;
    opens com.mekongfarm.controller to javafx.fxml;
    opens com.mekongfarm.model to javafx.base, javafx.fxml, com.google.gson;

    exports com.mekongfarm;
    exports com.mekongfarm.controller;
    exports com.mekongfarm.model;
    exports com.mekongfarm.dao;
    exports com.mekongfarm.config;
    exports com.mekongfarm.service;
    exports com.mekongfarm.api;
    exports com.mekongfarm.util;
}
