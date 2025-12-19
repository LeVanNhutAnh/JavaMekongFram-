package com.mekongfarm;

import com.mekongfarm.config.CauHinhDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Khởi tạo database
        CauHinhDatabase.getInstance().khoiTaoDatabase();

        // Load màn hình đăng nhập
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/DangNhap.fxml"));

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        // Apply saved dark mode preference
        com.mekongfarm.controller.CaiDatController.loadAndApplyDarkMode(scene);

        primaryStage.setTitle("🌾 Quản Lý Nông Sản ĐBSCL");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    @Override
    public void stop() {
        CauHinhDatabase.getInstance().dongKetNoi();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
