package com.mekongfarm.util;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Utility class cho loading indicators
 */
public class LoadingUtil {
    
    private static Stage loadingStage;
    
    /**
     * Show loading dialog với message
     */
    public static void showLoading(String message) {
        Platform.runLater(() -> {
            if (loadingStage != null && loadingStage.isShowing()) {
                return; // Already showing
            }
            
            loadingStage = new Stage();
            loadingStage.initStyle(StageStyle.UNDECORATED);
            loadingStage.initModality(Modality.APPLICATION_MODAL);
            
            ProgressIndicator progressIndicator = new ProgressIndicator();
            progressIndicator.setStyle("-fx-progress-color: #16a34a;");
            
            Label label = new Label(message);
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #1e293b; -fx-font-weight: bold;");
            
            VBox vbox = new VBox(15, progressIndicator, label);
            vbox.setAlignment(Pos.CENTER);
            vbox.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-background-radius: 10; " +
                         "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");
            
            StackPane root = new StackPane(vbox);
            root.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
            
            Scene scene = new Scene(root, 300, 150);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            
            loadingStage.setScene(scene);
            loadingStage.show();
        });
    }
    
    /**
     * Hide loading dialog
     */
    public static void hideLoading() {
        Platform.runLater(() -> {
            if (loadingStage != null && loadingStage.isShowing()) {
                loadingStage.close();
                loadingStage = null;
            }
        });
    }
    
    /**
     * Set cursor to waiting
     */
    public static void setWaitCursor(Scene scene) {
        Platform.runLater(() -> {
            if (scene != null) {
                scene.setCursor(Cursor.WAIT);
            }
        });
    }
    
    /**
     * Reset cursor to default
     */
    public static void setDefaultCursor(Scene scene) {
        Platform.runLater(() -> {
            if (scene != null) {
                scene.setCursor(Cursor.DEFAULT);
            }
        });
    }
    
    /**
     * Disable button với loading text
     */
    public static void disableButton(Button button, String loadingText) {
        Platform.runLater(() -> {
            button.setDisable(true);
            button.setUserData(button.getText()); // Save original text
            button.setText(loadingText);
        });
    }
    
    /**
     * Enable button và restore text
     */
    public static void enableButton(Button button) {
        Platform.runLater(() -> {
            button.setDisable(false);
            Object originalText = button.getUserData();
            if (originalText != null) {
                button.setText(originalText.toString());
            }
        });
    }
    
    /**
     * Run task with loading indicator
     */
    public static void runWithLoading(String message, Runnable task) {
        new Thread(() -> {
            try {
                showLoading(message);
                task.run();
            } finally {
                hideLoading();
            }
        }).start();
    }
    
    /**
     * Run task with loading và callback
     */
    public static void runWithLoading(String message, Runnable task, Runnable onComplete) {
        new Thread(() -> {
            try {
                showLoading(message);
                task.run();
                Platform.runLater(onComplete);
            } finally {
                hideLoading();
            }
        }).start();
    }
}
