package com.mekongfarm.service;

import java.util.*;

/**
 * Quản lý đa ngôn ngữ
 */
public class LanguageManager {

    private static final String BUNDLE_NAME = "i18n.messages";
    private static ResourceBundle bundle;
    private static Locale currentLocale = new Locale("vi", "VN");

    static {
        loadBundle();
    }

    /**
     * Đổi ngôn ngữ
     */
    public static void setLanguage(String langCode) {
        switch (langCode.toLowerCase()) {
            case "en":
                currentLocale = new Locale("en", "US");
                break;
            case "vi":
            default:
                currentLocale = new Locale("vi", "VN");
        }
        loadBundle();
    }

    /**
     * Lấy text theo key
     */
    public static String get(String key) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * Lấy locale hiện tại
     */
    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    /**
     * Kiểm tra đang dùng tiếng Anh không
     */
    public static boolean isEnglish() {
        return "en".equals(currentLocale.getLanguage());
    }

    private static void loadBundle() {
        try {
            bundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
        } catch (Exception e) {
            System.err.println("Cannot load bundle: " + e.getMessage());
            bundle = ResourceBundle.getBundle(BUNDLE_NAME, new Locale("vi", "VN"));
        }
    }
}
