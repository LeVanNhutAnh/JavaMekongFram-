package com.mekongfarm.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * Quản lý cấu hình ứng dụng
 */
public class AppConfig {

    private static final Properties properties = new Properties();
    private static boolean loaded = false;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try (InputStream is = AppConfig.class.getResourceAsStream("/config.properties")) {
            if (is != null) {
                properties.load(is);
                loaded = true;
                System.out.println("✓ Đã tải cấu hình ứng dụng");
            }
        } catch (Exception e) {
            System.err.println("✗ Lỗi tải config: " + e.getMessage());
        }
    }

    public static String getGeminiApiKey() {
        return properties.getProperty("gemini.api.key", "");
    }

    public static int getStockWarningThreshold() {
        try {
            return Integer.parseInt(properties.getProperty("stock.warning.threshold", "10"));
        } catch (NumberFormatException e) {
            return 10;
        }
    }

    public static String getAppName() {
        return properties.getProperty("app.name", "Mekong Farm");
    }

    public static String getAppVersion() {
        return properties.getProperty("app.version", "1.0.0");
    }
}
