package com.mekongfarm.service;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Notification Center - Popup thông báo trong app
 */
public class NotificationService {

    private static Stage primaryStage;
    private static final List<Notification> notifications = new ArrayList<>();
    private static final int MAX_NOTIFICATIONS = 50;

    public enum NotificationType {
        INFO, SUCCESS, WARNING, ERROR
    }

    public static class Notification {
        public String title;
        public String message;
        public NotificationType type;
        public LocalDateTime time;
        public boolean read;

        public Notification(String title, String message, NotificationType type) {
            this.title = title;
            this.message = message;
            this.type = type;
            this.time = LocalDateTime.now();
            this.read = false;
        }

        public String getTimeFormat() {
            return time.format(DateTimeFormatter.ofPattern("HH:mm dd/MM"));
        }
    }

    /**
     * Khởi tạo với stage chính
     */
    public static void init(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Hiển thị thông báo popup
     */
    public static void show(String title, String message, NotificationType type) {
        // Add to history
        Notification notif = new Notification(title, message, type);
        notifications.add(0, notif);
        if (notifications.size() > MAX_NOTIFICATIONS) {
            notifications.remove(notifications.size() - 1);
        }

        // Show popup
        Platform.runLater(() -> showPopup(notif));
    }

    /**
     * Thông báo nhanh
     */
    public static void info(String message) {
        show("Thông báo", message, NotificationType.INFO);
    }

    public static void success(String message) {
        show("Thành công", message, NotificationType.SUCCESS);
    }

    public static void warning(String message) {
        show("Cảnh báo", message, NotificationType.WARNING);
    }

    public static void error(String message) {
        show("Lỗi", message, NotificationType.ERROR);
    }

    /**
     * Lấy danh sách thông báo
     */
    public static List<Notification> getAll() {
        return new ArrayList<>(notifications);
    }

    /**
     * Đếm thông báo chưa đọc
     */
    public static int countUnread() {
        return (int) notifications.stream().filter(n -> !n.read).count();
    }

    /**
     * Đánh dấu tất cả đã đọc
     */
    public static void markAllRead() {
        notifications.forEach(n -> n.read = true);
    }

    private static void showPopup(Notification notif) {
        if (primaryStage == null)
            return;

        Popup popup = new Popup();

        String icon = switch (notif.type) {
            case SUCCESS -> "✅";
            case WARNING -> "⚠️";
            case ERROR -> "❌";
            default -> "ℹ️";
        };

        String bgColor = switch (notif.type) {
            case SUCCESS -> "#4CAF50";
            case WARNING -> "#FF9800";
            case ERROR -> "#f44336";
            default -> "#2196F3";
        };

        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle("-fx-background-color: " + bgColor + "; -fx-padding: 15; -fx-background-radius: 8;");

        Label lblTitle = new Label(icon + " " + notif.title);
        lblTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14;");

        Label lblMsg = new Label(notif.message);
        lblMsg.setStyle("-fx-text-fill: white; -fx-font-size: 12;");
        lblMsg.setWrapText(true);
        lblMsg.setMaxWidth(250);

        box.getChildren().addAll(lblTitle, lblMsg);
        popup.getContent().add(box);

        // Position at top-right
        popup.show(primaryStage,
                primaryStage.getX() + primaryStage.getWidth() - 300,
                primaryStage.getY() + 50);

        // Auto hide after 4 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(e -> popup.hide());
        delay.play();
    }
}
