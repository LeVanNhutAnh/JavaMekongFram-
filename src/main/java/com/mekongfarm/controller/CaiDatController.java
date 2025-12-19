package com.mekongfarm.controller;

import com.mekongfarm.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CaiDatController {

    @FXML
    private ToggleButton btnDarkMode;
    @FXML
    private ListView<String> listBackup;
    @FXML
    private Label lblDbPath, lblJavaVersion;
    @FXML
    private ComboBox<String> cboFontSize;

    private static boolean isDarkMode = false;
    private static final String DARK_CSS = "/css/styles-dark.css";

    @FXML
    public void initialize() {
        // Load saved dark mode preference
        isDarkMode = ConfigUtil.getBoolean(ConfigUtil.DARK_MODE, false);
        updateDarkModeButton();

        // Load backup list
        taiDanhSachBackup();

        // DB path
        if (lblDbPath != null) {
            lblDbPath.setText(new File("mekongfarm.db").getAbsolutePath());
        }

        // Java version
        if (lblJavaVersion != null) {
            lblJavaVersion.setText(System.getProperty("java.version"));
        }

        // Font size options
        if (cboFontSize != null) {
            cboFontSize.getItems().addAll("Nhỏ", "Mặc định", "Lớn");
            cboFontSize.setValue("Mặc định");
        }

        // Apply saved dark mode on startup
        if (isDarkMode && btnDarkMode != null && btnDarkMode.getScene() != null) {
            applyDarkMode(true);
        }
    }

    @FXML
    private void toggleDarkMode() {
        isDarkMode = !isDarkMode;

        // Lưu preference
        ConfigUtil.setBoolean(ConfigUtil.DARK_MODE, isDarkMode);

        applyDarkMode(isDarkMode);
        updateDarkModeButton();

        if (isDarkMode) {
            DialogUtil.showSuccess("Dark Mode", "✅ Đã bật Dark Mode!\nCài đặt đã được lưu.");
        } else {
            DialogUtil.showSuccess("Light Mode", "☀️ Đã tắt Dark Mode!\nCài đặt đã được lưu.");
        }
    }

    private void applyDarkMode(boolean dark) {
        try {
            if (btnDarkMode == null || btnDarkMode.getScene() == null)
                return;

            javafx.scene.Scene scene = btnDarkMode.getScene();
            String darkCssPath = getClass().getResource(DARK_CSS).toExternalForm();

            if (dark) {
                if (!scene.getStylesheets().contains(darkCssPath)) {
                    scene.getStylesheets().add(darkCssPath);
                }
            } else {
                scene.getStylesheets().remove(darkCssPath);
            }
        } catch (Exception e) {
            System.err.println("Lỗi apply dark mode: " + e.getMessage());
        }
    }

    private void updateDarkModeButton() {
        if (btnDarkMode != null) {
            btnDarkMode.setText(isDarkMode ? "🌙 Bật" : "☀️ Tắt");
            btnDarkMode.setSelected(isDarkMode);
        }
    }

    /**
     * Kiểm tra trạng thái Dark Mode hiện tại (static để dùng từ nơi khác)
     */
    public static boolean isDarkModeEnabled() {
        return isDarkMode;
    }

    /**
     * Load và apply dark mode từ config (gọi từ Main khi khởi động)
     */
    public static void loadAndApplyDarkMode(javafx.scene.Scene scene) {
        isDarkMode = ConfigUtil.getBoolean(ConfigUtil.DARK_MODE, false);
        if (isDarkMode) {
            try {
                String darkCssPath = CaiDatController.class.getResource(DARK_CSS).toExternalForm();
                if (!scene.getStylesheets().contains(darkCssPath)) {
                    scene.getStylesheets().add(darkCssPath);
                }
            } catch (Exception e) {
                System.err.println("Lỗi load dark mode: " + e.getMessage());
            }
        }
    }

    @FXML
    private void saoLuuDB() {
        try {
            // Tạo thư mục backup nếu chưa có
            File backupDir = new File("backups");
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }

            // Copy database file
            File dbFile = new File("mekongfarm.db");
            if (!dbFile.exists()) {
                DialogUtil.showError("Lỗi", "Không tìm thấy file database!");
                return;
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String backupName = "backup_" + timestamp + ".db";
            File backupFile = new File(backupDir, backupName);

            Files.copy(dbFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            DialogUtil.showSuccess("Sao lưu thành công",
                    "✅ Đã sao lưu database!\n\nFile: " + backupName + "\nVị trí: " + backupFile.getAbsolutePath());

            taiDanhSachBackup();
        } catch (IOException e) {
            DialogUtil.showError("Lỗi sao lưu", "Không thể sao lưu database!\n" + e.getMessage());
        }
    }

    @FXML
    private void khoiPhucDB() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Chọn file backup để khôi phục");

        File backupDir = new File("backups");
        if (backupDir.exists()) {
            fc.setInitialDirectory(backupDir);
        }
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Database", "*.db"));

        File file = fc.showOpenDialog(btnDarkMode.getScene().getWindow());
        if (file != null) {
            if (DialogUtil.confirm("Xác nhận khôi phục",
                    "⚠️ Khôi phục sẽ GHI ĐÈ dữ liệu hiện tại!\n\n" +
                            "File backup: " + file.getName() + "\n\n" +
                            "Bạn có chắc chắn muốn khôi phục?")) {
                try {
                    File dbFile = new File("mekongfarm.db");
                    Files.copy(file.toPath(), dbFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    DialogUtil.showSuccess("Khôi phục thành công",
                            "✅ Đã khôi phục database từ backup!\n\n" +
                                    "⚠️ Vui lòng KHỞI ĐỘNG LẠI ứng dụng để áp dụng thay đổi.");
                } catch (IOException e) {
                    DialogUtil.showError("Lỗi khôi phục", "Không thể khôi phục: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void moThuMucBackup() {
        try {
            File backupDir = new File("backups");
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }
            java.awt.Desktop.getDesktop().open(backupDir);
        } catch (Exception e) {
            thongBao("❌ Không thể mở thư mục: " + e.getMessage());
        }
    }

    @FXML
    private void moGiaVung() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/GiaVung.fxml"));
            javafx.scene.Parent content = loader.load();

            javafx.stage.Stage dialog = new javafx.stage.Stage();
            dialog.setTitle("🗺️ Quản lý Giá Vùng");
            javafx.scene.Scene scene = new javafx.scene.Scene(content, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            dialog.setScene(scene);
            dialog.show();
        } catch (Exception e) {
            DialogUtil.showError("Lỗi", "Không thể mở màn hình Giá Vùng: " + e.getMessage());
        }
    }

    private void taiDanhSachBackup() {
        if (listBackup != null) {
            listBackup.getItems().clear();

            File backupDir = new File("backups");
            if (backupDir.exists() && backupDir.isDirectory()) {
                File[] files = backupDir.listFiles((dir, name) -> name.endsWith(".db"));
                if (files != null && files.length > 0) {
                    // Sort by modified time (newest first)
                    java.util.Arrays.sort(files, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
                    for (File f : files) {
                        listBackup.getItems().add("📁 " + f.getName());
                    }
                } else {
                    listBackup.getItems().add("Chưa có backup nào");
                }
            } else {
                listBackup.getItems().add("Thư mục backup chưa tồn tại");
            }
        }
    }

    private void thongBao(String msg) {
        DialogUtil.showSuccess("Thông báo", msg);
    }
}
