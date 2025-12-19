package com.mekongfarm.util;

import java.io.*;
import java.util.Properties;

/**
 * Utility để lưu/load cấu hình người dùng
 */
public class ConfigUtil {

    private static final String CONFIG_FILE = "config/user.properties";
    private static Properties props = new Properties();

    static {
        load();
    }

    /**
     * Load cấu hình từ file
     */
    public static void load() {
        File file = new File(CONFIG_FILE);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
            } catch (IOException e) {
                System.err.println("Không thể load config: " + e.getMessage());
            }
        }
    }

    /**
     * Lưu cấu hình ra file
     */
    public static void save() {
        File file = new File(CONFIG_FILE);
        file.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            props.store(fos, "Mekong Farm User Configuration");
        } catch (IOException e) {
            System.err.println("Không thể lưu config: " + e.getMessage());
        }
    }

    /**
     * Lấy giá trị string
     */
    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    /**
     * Lấy giá trị boolean
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = props.getProperty(key);
        if (value == null)
            return defaultValue;
        return Boolean.parseBoolean(value);
    }

    /**
     * Đặt giá trị
     */
    public static void set(String key, String value) {
        props.setProperty(key, value);
        save();
    }

    /**
     * Đặt giá trị boolean
     */
    public static void setBoolean(String key, boolean value) {
        props.setProperty(key, String.valueOf(value));
        save();
    }

    // Các key cấu hình
    public static final String DARK_MODE = "app.darkMode";
    public static final String FONT_SIZE = "app.fontSize";
    public static final String LAST_USER = "app.lastUser";
}
