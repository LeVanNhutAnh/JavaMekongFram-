package com.mekongfarm.service;

import com.mekongfarm.config.CauHinhDatabase;
import com.mekongfarm.model.SanPham;
import java.sql.*;
import java.util.*;

/**
 * Service quản lý tồn kho
 * - Cảnh báo sản phẩm sắp hết
 * - Gửi email thông báo cho admin
 */
public class TonKhoService {

    private final Connection conn;
    private static final int NGUONG_CANH_BAO = 10; // Số lượng tồn kho tối thiểu

    public TonKhoService() {
        this.conn = CauHinhDatabase.getInstance().getConnection();
    }

    /**
     * Lấy danh sách sản phẩm sắp hết hàng
     */
    public List<SanPham> getSanPhamSapHet() {
        return getSanPhamSapHet(NGUONG_CANH_BAO);
    }

    /**
     * Lấy danh sách sản phẩm có tồn kho dưới ngưỡng
     */
    public List<SanPham> getSanPhamSapHet(int nguong) {
        List<SanPham> list = new ArrayList<>();
        String sql = """
                SELECT sp.*, lsp.ten_loai, tt.ten_tinh
                FROM san_pham sp
                LEFT JOIN loai_san_pham lsp ON sp.ma_loai = lsp.ma_loai
                LEFT JOIN tinh_thanh tt ON sp.ma_tinh = tt.ma_tinh
                WHERE sp.trang_thai = 1 AND sp.so_luong_ton <= ?
                ORDER BY sp.so_luong_ton ASC
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, nguong);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSanPham(rs.getInt("ma_san_pham"));
                sp.setMaSP(rs.getString("ma_sp"));
                sp.setTenSanPham(rs.getString("ten_san_pham"));
                sp.setMaLoai(rs.getInt("ma_loai"));
                sp.setTenLoai(rs.getString("ten_loai"));
                sp.setMaTinh(rs.getInt("ma_tinh"));
                sp.setTenTinh(rs.getString("ten_tinh"));
                sp.setDonGia(rs.getDouble("don_gia"));
                sp.setSoLuongTon(rs.getInt("so_luong_ton"));
                sp.setDonViTinh(rs.getString("don_vi_tinh"));
                list.add(sp);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy sản phẩm sắp hết: " + e.getMessage());
        }
        return list;
    }

    /**
     * Đếm số sản phẩm sắp hết hàng
     */
    public int demSanPhamSapHet() {
        return demSanPhamSapHet(NGUONG_CANH_BAO);
    }

    /**
     * Đếm số sản phẩm có tồn kho dưới ngưỡng
     */
    public int demSanPhamSapHet(int nguong) {
        String sql = "SELECT COUNT(*) FROM san_pham WHERE trang_thai = 1 AND so_luong_ton <= ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, nguong);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đếm sản phẩm sắp hết: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Tạo nội dung email cảnh báo tồn kho
     */
    public String taoNoiDungEmailCanhBao() {
        List<SanPham> sanPhamSapHet = getSanPhamSapHet();
        if (sanPhamSapHet.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("🔔 CẢNH BÁO TỒN KHO - MEKONG FARM\n\n");
        sb.append("Các sản phẩm sau đây có số lượng tồn kho thấp (dưới ").append(NGUONG_CANH_BAO).append(" ").append("đơn vị):\n\n");

        for (SanPham sp : sanPhamSapHet) {
            sb.append("• ").append(sp.getMaSP()).append(" - ").append(sp.getTenSanPham())
                    .append(": còn ").append(sp.getSoLuongTon()).append(" ").append(sp.getDonViTinh())
                    .append("\n");
        }

        sb.append("\nVui lòng kiểm tra và bổ sung hàng kịp thời.\n");
        sb.append("\n---\n");
        sb.append("Hệ thống Quản lý Nông sản ĐBSCL - Mekong Farm");

        return sb.toString();
    }

    /**
     * Gửi email cảnh báo tồn kho cho admin
     */
    public boolean guiEmailCanhBao(String emailAdmin) {
        String noiDung = taoNoiDungEmailCanhBao();
        if (noiDung == null) {
            return false; // Không có sản phẩm cần cảnh báo
        }

        try {
            EmailService emailService = new EmailService();
            return emailService.guiEmail(
                    emailAdmin,
                    "🔔 [Mekong Farm] Cảnh báo tồn kho thấp",
                    noiDung
            );
        } catch (Exception e) {
            System.err.println("Lỗi gửi email cảnh báo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Kiểm tra và tự động gửi cảnh báo nếu cần
     */
    public int kiemTraVaCanhBao(String emailAdmin) {
        int soSanPhamSapHet = demSanPhamSapHet();
        if (soSanPhamSapHet > 0 && emailAdmin != null && !emailAdmin.isEmpty()) {
            guiEmailCanhBao(emailAdmin);
        }
        return soSanPhamSapHet;
    }
}
