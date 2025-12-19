package com.mekongfarm.util;

import java.sql.*;

public class FixTruyXuatData {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite driver không tìm thấy!");
            return;
        }
        
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:mekongfarm.db")) {
            System.out.println("✓ Kết nối database thành công!");
            
            // Check hiện tại
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM truy_xuat_nguon_goc");
                if (rs.next()) {
                    System.out.println("Số bản ghi hiện tại: " + rs.getInt(1));
                }
            }
            
            // Insert data
            String sql = "INSERT OR REPLACE INTO truy_xuat_nguon_goc " +
                "(ma_truy_xuat, ma_san_pham, so_lo, ngay_thu_hoach, dia_chi_san_xuat, ten_nong_dan, chung_nhan, ngay_san_xuat, han_su_dung) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Record 1: Gạo ST25
                pstmt.setInt(1, 1);
                pstmt.setInt(2, 1);
                pstmt.setString(3, "LO-ST25-001");
                pstmt.setString(4, "2024-11-15");
                pstmt.setString(5, "Xã Tân Hưng, Huyện Long Phú, Sóc Trăng");
                pstmt.setString(6, "Hồ Quang Cua");
                pstmt.setString(7, "VietGAP, GlobalGAP");
                pstmt.setString(8, "2024-11-20");
                pstmt.setString(9, "2025-11-20");
                pstmt.executeUpdate();
                System.out.println("✓ Đã thêm truy xuất cho SP001 (Gạo ST25)");
                
                // Record 2: Xoài
                pstmt.setInt(1, 2);
                pstmt.setInt(2, 2);
                pstmt.setString(3, "LO-XOAI-001");
                pstmt.setString(4, "2024-12-01");
                pstmt.setString(5, "Xã Hòa Lộc, Huyện Cái Bè, Tiền Giang");
                pstmt.setString(6, "Nguyễn Văn Thành");
                pstmt.setString(7, "VietGAP");
                pstmt.setString(8, "2024-12-02");
                pstmt.setString(9, "2024-12-15");
                pstmt.executeUpdate();
                System.out.println("✓ Đã thêm truy xuất cho SP002 (Xoài)");
                
                // Record 3: Tôm sú
                pstmt.setInt(1, 3);
                pstmt.setInt(2, 3);
                pstmt.setString(3, "LO-TOMSU-001");
                pstmt.setString(4, "2024-12-10");
                pstmt.setString(5, "Huyện Năm Căn, Cà Mau");
                pstmt.setString(6, "Công ty TNHH Tôm Cà Mau");
                pstmt.setString(7, "ASC, GlobalGAP");
                pstmt.setString(8, "2024-12-11");
                pstmt.setString(9, "2025-03-11");
                pstmt.executeUpdate();
                System.out.println("✓ Đã thêm truy xuất cho SP003 (Tôm sú)");
            }
            
            // Verify
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM truy_xuat_nguon_goc");
                if (rs.next()) {
                    System.out.println("\n✓ Tổng số bản ghi sau khi thêm: " + rs.getInt(1));
                }
                
                rs = stmt.executeQuery(
                    "SELECT tx.so_lo, sp.ten_san_pham FROM truy_xuat_nguon_goc tx " +
                    "LEFT JOIN san_pham sp ON tx.ma_san_pham = sp.ma_san_pham");
                System.out.println("\nDanh sách truy xuất:");
                while (rs.next()) {
                    System.out.println("  - " + rs.getString("ten_san_pham") + " (" + rs.getString("so_lo") + ")");
                }
            }
            
            System.out.println("\n✅ HOÀN THÀNH! Hãy thử tra cứu lại SP001");
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
