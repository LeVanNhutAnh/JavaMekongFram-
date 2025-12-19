package com.mekongfarm.service;

import com.mekongfarm.config.AppConfig;
import javafx.scene.Scene;
import java.io.*;
import java.util.Properties;

/**
 * Quản lý theme (Light/Dark mode)
 */
public class ThemeManager {

    private static final String THEME_CONFIG = "theme.properties";
    private static boolean isDarkMode = false;
    private static Scene currentScene;

    static {
        loadThemePreference();
    }

    /**
     * Đặt scene hiện tại để thay đổi theme
     */
    public static void setScene(Scene scene) {
        currentScene = scene;
        applyTheme();
    }

    /**
     * Toggle giữa dark và light mode
     */
    public static void toggleTheme() {
        isDarkMode = !isDarkMode;
        applyTheme();
        saveThemePreference();
    }

    /**
     * Bật dark mode
     */
    public static void setDarkMode(boolean dark) {
        isDarkMode = dark;
        applyTheme();
        saveThemePreference();
    }

    /**
     * Kiểm tra có đang dark mode không
     */
    public static boolean isDarkMode() {
        return isDarkMode;
    }

    /**
     * Áp dụng theme cho scene
     */
    private static void applyTheme() {
        if (currentScene == null)
            return;

        String darkThemePath = "/css/dark-theme.css";

        if (isDarkMode) {
            // Thêm class root-dark
            currentScene.getRoot().getStyleClass().add("root-dark");

            // Thêm CSS dark theme nếu chưa có
            String darkCss = ThemeManager.class.getResource(darkThemePath).toExternalForm();
            if (!currentScene.getStylesheets().contains(darkCss)) {
                currentScene.getStylesheets().add(darkCss);
            }
        } else {
            // Xóa class root-dark
            currentScene.getRoot().getStyleClass().remove("root-dark");

            // Xóa CSS dark theme
            String darkCss = ThemeManager.class.getResource(darkThemePath).toExternalForm();
            currentScene.getStylesheets().remove(darkCss);
        }
    }

    /**
     * Load preference từ file
     */
    private static void loadThemePreference() {
        try (FileInputStream fis = new FileInputStream(THEME_CONFIG)) {
            Properties props = new Properties();
            props.load(fis);
            isDarkMode = Boolean.parseBoolean(props.getProperty("darkMode", "false"));
        } catch (Exception e) {
            isDarkMode = false;
        }
    }

    /**
     * Lưu preference vào file
     */
    private static void saveThemePreference() {
        try (FileOutputStream fos = new FileOutputStream(THEME_CONFIG)) {
            Properties props = new Properties();
            props.setProperty("darkMode", String.valueOf(isDarkMode));
            props.store(fos, "Theme preferences");
        } catch (Exception e) {
            System.err.println("Không thể lưu theme: " + e.getMessage());
        }
    }
}
