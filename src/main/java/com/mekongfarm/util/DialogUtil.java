package com.mekongfarm.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

/**
 * Utility class cho dialogs và confirmations
 */
public class DialogUtil {

    /**
     * Hiển thị thông báo lỗi chi tiết
     */
    public static void showError(String title, String message, String details) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("❌ " + title);
        alert.setHeaderText(message);
        alert.setContentText(details);
        alert.showAndWait();
    }

    /**
     * Hiển thị thông báo lỗi đơn giản
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("❌ " + title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Hiển thị thông báo thành công
     */
    public static void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("✅ " + title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Hiển thị thông tin
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ℹ️ " + title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Hiển thị cảnh báo
     */
    public static void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("⚠️ " + title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Xác nhận hành động (Yes/No)
     */
    public static boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("❓ " + title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Xác nhận xóa với chi tiết
     */
    public static boolean confirmDelete(String itemType, String itemName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("🗑️ Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc muốn xóa " + itemType + " này?");
        alert.setContentText(
                "• " + itemType + ": " + itemName + "\n" +
                        "• Hành động này có thể hoàn tác\n" +
                        "• Dữ liệu sẽ được chuyển sang trạng thái không hoạt động");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Xác nhận xóa vĩnh viễn (hard delete)
     */
    public static boolean confirmPermanentDelete(String itemType, String itemName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("⚠️ Xóa vĩnh viễn");
        alert.setHeaderText("CẢNH BÁO: Không thể hoàn tác!");
        alert.setContentText(
                "Bạn có CHẮC CHẮN muốn xóa vĩnh viễn?\n\n" +
                        "• " + itemType + ": " + itemName + "\n" +
                        "• Dữ liệu sẽ BỊ XÓA HOÀN TOÀN\n" +
                        "• KHÔNG THỂ KHÔI PHỤC\n\n" +
                        "Gõ 'XOA' để xác nhận:");

        // Custom buttons
        ButtonType confirmButton = new ButtonType("Xóa vĩnh viễn");
        ButtonType cancelButton = new ButtonType("Hủy");
        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == confirmButton;
    }

    /**
     * Prompt nhập text
     */
    public static Optional<String> promptText(String title, String message, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);
        return dialog.showAndWait();
    }

    /**
     * Xác nhận với unsaved changes
     */
    public static boolean confirmUnsavedChanges() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("⚠️ Thay đổi chưa lưu");
        alert.setHeaderText("Bạn có thay đổi chưa được lưu");
        alert.setContentText("Bạn muốn:\n• Lưu thay đổi\n• Bỏ qua thay đổi\n• Tiếp tục chỉnh sửa");

        ButtonType saveButton = new ButtonType("Lưu");
        ButtonType discardButton = new ButtonType("Bỏ qua");
        ButtonType cancelButton = new ButtonType("Hủy");
        alert.getButtonTypes().setAll(saveButton, discardButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == saveButton;
    }
}
