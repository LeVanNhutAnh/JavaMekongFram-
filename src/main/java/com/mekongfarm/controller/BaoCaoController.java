package com.mekongfarm.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import java.io.IOException;

/**
 * Controller cho module Báo Cáo (gộp Thống kê + Lãi/Lỗ)
 */
public class BaoCaoController {

    @FXML
    private TabPane tabPane;
    @FXML
    private VBox tabThongKe;
    @FXML
    private VBox tabLaiLo;

    @FXML
    public void initialize() {
        loadTabThongKe();
        loadTabLaiLo();
    }

    /**
     * Load nội dung tab Thống kê từ ThongKe.fxml
     */
    private void loadTabThongKe() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ThongKe.fxml"));
            Parent content = loader.load();
            tabThongKe.getChildren().clear();
            tabThongKe.getChildren().add(content);
        } catch (IOException e) {
            System.err.println("Lỗi load tab Thống kê: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load nội dung tab Lãi/Lỗ từ LaiLo.fxml
     */
    private void loadTabLaiLo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LaiLo.fxml"));
            Parent content = loader.load();
            tabLaiLo.getChildren().clear();
            tabLaiLo.getChildren().add(content);
        } catch (IOException e) {
            System.err.println("Lỗi load tab Lãi/Lỗ: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
