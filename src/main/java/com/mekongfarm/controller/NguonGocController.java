package com.mekongfarm.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import java.io.IOException;

/**
 * Controller cho module Nguồn Gốc (gộp Tra cứu + Quản lý)
 */
public class NguonGocController {

    @FXML
    private TabPane tabPane;
    @FXML
    private VBox tabTraCuu;
    @FXML
    private VBox tabQuanLy;

    @FXML
    public void initialize() {
        loadTabTraCuu();
        loadTabQuanLy();
    }

    /**
     * Load nội dung tab Tra cứu từ TruyXuat.fxml
     */
    private void loadTabTraCuu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TruyXuat.fxml"));
            Parent content = loader.load();
            tabTraCuu.getChildren().clear();
            tabTraCuu.getChildren().add(content);
        } catch (IOException e) {
            System.err.println("Lỗi load tab Tra cứu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load nội dung tab Quản lý từ QuanLyTruyXuat.fxml
     */
    private void loadTabQuanLy() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/QuanLyTruyXuat.fxml"));
            Parent content = loader.load();
            tabQuanLy.getChildren().clear();
            tabQuanLy.getChildren().add(content);
        } catch (IOException e) {
            System.err.println("Lỗi load tab Quản lý: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
