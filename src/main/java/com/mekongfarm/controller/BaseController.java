package com.mekongfarm.controller;

import com.mekongfarm.util.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Base Controller với các method dùng chung
 * Các controller khác có thể extend hoặc sử dụng các static methods
 */
public abstract class BaseController {
    
    /**
     * Hiển thị thông báo thành công
     */
    protected void showSuccess(String message) {
        DialogUtil.showSuccess("Thành công", message);
    }
    
    /**
     * Hiển thị thông báo lỗi
     */
    protected void showError(String message) {
        DialogUtil.showError("Lỗi", message);
    }
    
    /**
     * Hiển thị thông báo lỗi chi tiết
     */
    protected void showError(String message, String details) {
        DialogUtil.showError("Lỗi", message, details);
    }
    
    /**
     * Hiển thị cảnh báo
     */
    protected void showWarning(String message) {
        DialogUtil.showWarning("Cảnh báo", message);
    }
    
    /**
     * Xác nhận hành động
     */
    protected boolean confirm(String message) {
        return DialogUtil.confirm("Xác nhận", message);
    }
    
    /**
     * Xác nhận xóa
     */
    protected boolean confirmDelete(String itemType, String itemName) {
        return DialogUtil.confirmDelete(itemType, itemName);
    }
    
    /**
     * Validate text không rỗng với error style
     */
    protected boolean validateNotEmpty(javafx.scene.control.TextField field, String fieldName) {
        if (!ValidationUtil.isNotEmpty(field.getText())) {
            ValidationUtil.setErrorStyle(field);
            DialogUtil.showError("Lỗi nhập liệu", "Vui lòng nhập " + fieldName + "!");
            return false;
        }
        ValidationUtil.clearErrorStyle(field);
        return true;
    }
    
    /**
     * Validate số dương
     */
    protected boolean validatePositiveNumber(javafx.scene.control.TextField field, String fieldName) {
        if (!ValidationUtil.isPositiveNumber(field.getText())) {
            ValidationUtil.setErrorStyle(field);
            DialogUtil.showError("Lỗi " + fieldName,
                fieldName + " không hợp lệ!\n" +
                "• Phải là số dương\n" +
                "• Không được âm hoặc 0");
            return false;
        }
        ValidationUtil.clearErrorStyle(field);
        return true;
    }
    
    /**
     * Validate email
     */
    protected boolean validateEmail(javafx.scene.control.TextField field) {
        if (!ValidationUtil.isValidEmail(field.getText())) {
            ValidationUtil.setErrorStyle(field);
            DialogUtil.showError("Lỗi email",
                "Email không hợp lệ!\n" +
                "• Ví dụ: user@example.com\n" +
                "• Hoặc để trống");
            return false;
        }
        ValidationUtil.clearErrorStyle(field);
        return true;
    }
    
    /**
     * Validate số điện thoại
     */
    protected boolean validatePhone(javafx.scene.control.TextField field) {
        if (!ValidationUtil.isValidPhone(field.getText())) {
            ValidationUtil.setErrorStyle(field);
            DialogUtil.showError("Lỗi số điện thoại",
                "Số điện thoại không hợp lệ!\n" +
                "• Ví dụ: 0912345678\n" +
                "• Hoặc để trống");
            return false;
        }
        ValidationUtil.clearErrorStyle(field);
        return true;
    }
    
    /**
     * Show loading
     */
    protected void showLoading(String message) {
        LoadingUtil.showLoading(message);
    }
    
    /**
     * Hide loading
     */
    protected void hideLoading() {
        LoadingUtil.hideLoading();
    }
    
    /**
     * Format currency
     */
    protected String formatCurrency(double amount) {
        return ValidationUtil.formatCurrency(amount);
    }
    
    /**
     * Legacy methods for backward compatibility
     */
    protected void thongBao(String msg) {
        DialogUtil.showSuccess("Thông báo", msg);
    }
    
    protected boolean xacNhan(String msg) {
        return DialogUtil.confirm("Xác nhận", msg);
    }
}
