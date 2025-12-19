package com.mekongfarm.test;

import com.mekongfarm.config.CauHinhDatabase;
import com.mekongfarm.dao.DonHangDAO;
import com.mekongfarm.model.DonHang;
import java.util.List;

/**
 * Test kiểm tra đơn hàng
 */
public class TestDonHang {
    public static void main(String[] args) {
        System.out.println("=== KIỂM TRA ĐƠN HÀNG ===");
        
        // Khởi tạo database
        CauHinhDatabase db = CauHinhDatabase.getInstance();
        System.out.println("Kết nối database: " + (db.getConnection() != null ? "OK" : "FAILED"));
        
        // Kiểm tra dữ liệu đơn hàng
        DonHangDAO dao = new DonHangDAO();
        List<DonHang> danhSach = dao.layTatCa();
        
        System.out.println("\nTổng số đơn hàng: " + danhSach.size());
        
        if (danhSach.isEmpty()) {
            System.out.println("⚠️ KHÔNG CÓ ĐƠN HÀNG NÀO TRONG DATABASE!");
            System.out.println("Vui lòng chạy file du_lieu_mau.sql để thêm dữ liệu mẫu");
        } else {
            System.out.println("\n📋 Danh sách đơn hàng:");
            for (DonHang dh : danhSach) {
                System.out.println("- " + dh.getMaDH() + 
                    " | Khách: " + dh.getTenKhachHang() + 
                    " | Ngày: " + dh.getNgayDatFormat() +
                    " | Trạng thái: " + dh.getTrangThaiHienThi() +
                    " | Tổng: " + dh.getThanhTienFormat());
            }
        }
        
        System.out.println("\n=== HOÀN TẤT ===");
    }
}
