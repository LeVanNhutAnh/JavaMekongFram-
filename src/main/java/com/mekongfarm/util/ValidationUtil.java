package com.mekongfarm.util;

import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import java.util.regex.Pattern;

/**
 * Utility class cho validation input
 */
public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^(0|\\+84)(\\d{9,10})$"
    );
    
    /**
     * Validate số dương (không âm, không 0)
     */
    public static boolean isPositiveNumber(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        try {
            double value = Double.parseDouble(text.trim().replace(",", ""));
            return value > 0 && value <= Double.MAX_VALUE / 2; // Tránh overflow
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate số không âm (>= 0)
     */
    public static boolean isNonNegativeNumber(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        try {
            double value = Double.parseDouble(text.trim().replace(",", ""));
            return value >= 0 && value <= Double.MAX_VALUE / 2;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate integer dương
     */
    public static boolean isPositiveInteger(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        try {
            int value = Integer.parseInt(text.trim().replace(",", ""));
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate integer không âm
     */
    public static boolean isNonNegativeInteger(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        try {
            int value = Integer.parseInt(text.trim().replace(",", ""));
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate email
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return true; // Email optional
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validate số điện thoại Việt Nam
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return true; // Phone optional
        }
        String cleanPhone = phone.trim().replaceAll("[\\s-()]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }
    
    /**
     * Validate text không rỗng
     */
    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }
    
    /**
     * Validate text có độ dài hợp lệ
     */
    public static boolean hasValidLength(String text, int minLength, int maxLength) {
        if (text == null) return false;
        int length = text.trim().length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * Parse double từ string, xử lý dấu phẩy
     */
    public static double parseDouble(String text) throws NumberFormatException {
        if (text == null || text.trim().isEmpty()) {
            throw new NumberFormatException("Giá trị rỗng");
        }
        return Double.parseDouble(text.trim().replace(",", ""));
    }
    
    /**
     * Parse integer từ string
     */
    public static int parseInt(String text) throws NumberFormatException {
        if (text == null || text.trim().isEmpty()) {
            throw new NumberFormatException("Giá trị rỗng");
        }
        return Integer.parseInt(text.trim().replace(",", ""));
    }
    
    /**
     * Set style lỗi cho TextField
     */
    public static void setErrorStyle(TextField field) {
        field.setStyle("-fx-border-color: #dc2626; -fx-border-width: 2px;");
    }

    /**
     * Set style lỗi cho Control (ComboBox, DatePicker, etc.)
     */
    public static void setErrorStyle(Control control) {
        control.setStyle("-fx-border-color: #dc2626; -fx-border-width: 2px;");
    }
    
    /**
     * Remove style lỗi
     */
    public static void clearErrorStyle(TextField field) {
        field.setStyle("");
    }

    /**
     * Remove style lỗi cho Control
     */
    public static void clearErrorStyle(Control control) {
        control.setStyle("");
    }
    
    /**
     * Format số tiền VNĐ
     */
    public static String formatCurrency(double amount) {
        return String.format("%,.0f VNĐ", amount);
    }
    
    /**
     * Format số với dấu phẩy
     */
    public static String formatNumber(double number) {
        return String.format("%,.0f", number);
    }
}
