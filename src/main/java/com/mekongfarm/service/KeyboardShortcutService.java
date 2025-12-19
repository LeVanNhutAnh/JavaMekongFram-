package com.mekongfarm.service;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import java.util.HashMap;
import java.util.Map;

/**
 * Keyboard Shortcuts Service
 * Provides global keyboard shortcuts for the application
 */
public class KeyboardShortcutService {

    private static final Map<String, Runnable> shortcuts = new HashMap<>();

    /**
     * Đăng ký shortcuts cho scene
     */
    public static void registerShortcuts(Scene scene) {
        scene.getAccelerators().clear();

        // F5 - Refresh
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F5),
                () -> executeShortcut("refresh"));

        // Ctrl+S - Save
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
                () -> executeShortcut("save"));

        // Ctrl+N - New
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN),
                () -> executeShortcut("new"));

        // Ctrl+F - Find/Search
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN),
                () -> executeShortcut("search"));

        // Ctrl+P - Print
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN),
                () -> executeShortcut("print"));

        // Ctrl+E - Export
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN),
                () -> executeShortcut("export"));

        // Escape - Cancel/Close
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.ESCAPE),
                () -> executeShortcut("cancel"));

        // Ctrl+D - Dark mode toggle
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN),
                () -> {
                    ThemeManager.setScene(scene);
                    ThemeManager.toggleTheme();
                    NotificationService.info("🌙 Đã đổi chế độ Dark/Light");
                });

        System.out.println("⌨️ Keyboard shortcuts registered");
    }

    /**
     * Đăng ký action cho shortcut
     */
    public static void onShortcut(String action, Runnable handler) {
        shortcuts.put(action, handler);
    }

    /**
     * Xóa handler
     */
    public static void removeShortcut(String action) {
        shortcuts.remove(action);
    }

    /**
     * Execute shortcut action
     */
    private static void executeShortcut(String action) {
        Runnable handler = shortcuts.get(action);
        if (handler != null) {
            try {
                handler.run();
            } catch (Exception e) {
                System.err.println("Shortcut error: " + e.getMessage());
            }
        }
    }

    /**
     * Lấy danh sách shortcuts
     */
    public static String getShortcutHelp() {
        return """
                ⌨️ PHÍM TẮT:

                F5          - Làm mới
                Ctrl+S      - Lưu
                Ctrl+N      - Tạo mới
                Ctrl+F      - Tìm kiếm
                Ctrl+P      - In
                Ctrl+E      - Xuất file
                Ctrl+D      - Dark mode
                Escape      - Hủy/Đóng
                """;
    }
}
