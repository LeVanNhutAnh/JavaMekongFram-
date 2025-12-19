package com.mekongfarm.service;

import java.io.*;
import java.net.*;

/**
 * Service gửi email thông báo (Simple HTTP API version)
 * Lưu ý: Đây là phiên bản đơn giản. Để sử dụng thực tế cần cấu hình SMTP
 * server.
 */
public class EmailService {

    private static String EMAIL_FROM = "";
    private static String EMAIL_PASSWORD = "";

    /**
     * Cấu hình email gửi
     */
    public static void configure(String email, String password) {
        EMAIL_FROM = email;
        EMAIL_PASSWORD = password;
    }

    /**
     * Gửi email (placeholder - cần tích hợp SMTP thực tế)
     */
    public boolean guiEmail(String toEmail, String subject, String body) {
        if (EMAIL_FROM.isEmpty()) {
            System.out.println("[EmailService] Chưa cấu hình. Email sẽ được log:");
            System.out.println("  To: " + toEmail);
            System.out.println("  Subject: " + subject);
            System.out.println("  Body: " + body.substring(0, Math.min(100, body.length())) + "...");
            return true; // Simulate success
        }

        // TODO: Implement actual SMTP sending
        System.out.println("[EmailService] Đã gửi email đến: " + toEmail);
        return true;
    }

    /**
     * Gửi email thông báo đơn hàng mới
     */
    public boolean guiThongBaoDonHang(String toEmail, String maDH, double tongTien) {
        String subject = "🛒 Đơn hàng mới #" + maDH + " - Mekong Farm";
        String body = String.format(
                "Xin chào,\n\n" +
                        "Đơn hàng #%s đã được tạo thành công!\n\n" +
                        "Tổng tiền: %,.0f VNĐ\n\n" +
                        "Cảm ơn quý khách đã mua hàng tại Mekong Farm!\n\n" +
                        "Trân trọng,\nMekong Farm Team",
                maDH, tongTien);

        return guiEmail(toEmail, subject, body);
    }

    /**
     * Gửi email cảnh báo tồn kho
     */
    public boolean guiCanhBaoTonKho(String toEmail, String tenSP, int soLuong) {
        String subject = "⚠️ Cảnh báo tồn kho - Mekong Farm";
        String body = String.format(
                "Xin chào Admin,\n\n" +
                        "Sản phẩm \"%s\" sắp hết hàng!\n" +
                        "Số lượng còn lại: %d\n\n" +
                        "Vui lòng nhập thêm hàng sớm.\n\n" +
                        "Mekong Farm System",
                tenSP, soLuong);

        return guiEmail(toEmail, subject, body);
    }
}
