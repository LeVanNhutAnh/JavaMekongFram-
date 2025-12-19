package com.mekongfarm.service;

import com.mekongfarm.config.CauHinhDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Service tính toán Lãi/Lỗ
 */
public class BaoCaoLaiLoService {

    private final Connection conn;

    public BaoCaoLaiLoService() {
        this.conn = CauHinhDatabase.getInstance().getConnection();
    }

    /**
     * Tính lãi/lỗ theo khoảng thời gian
     */
    public Map<String, Double> tinhLaiLo(LocalDate tuNgay, LocalDate denNgay) {
        Map<String, Double> result = new HashMap<>();

        double doanhThu = tinhDoanhThu(tuNgay, denNgay);
        double chiPhi = tinhChiPhi(tuNgay, denNgay);
        double laiLo = doanhThu - chiPhi;
        double tyLeLai = doanhThu > 0 ? (laiLo / doanhThu) * 100 : 0;

        result.put("doanhThu", doanhThu);
        result.put("chiPhi", chiPhi);
        result.put("laiLo", laiLo);
        result.put("tyLeLai", tyLeLai);

        return result;
    }

    /**
     * Tính doanh thu từ đơn hàng
     */
    public double tinhDoanhThu(LocalDate tuNgay, LocalDate denNgay) {
        String sql = "SELECT COALESCE(SUM(thanhTien), 0) FROM DonHang WHERE ngayDat BETWEEN ? AND ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tuNgay.toString());
            pstmt.setString(2, denNgay.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tính doanh thu: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Tính chi phí nhập hàng (giả định 60% giá bán)
     * TODO: Cần bảng NhapHang thực tế để tính chính xác
     */
    public double tinhChiPhi(LocalDate tuNgay, LocalDate denNgay) {
        // Giả định chi phí = 60% doanh thu (margin 40%)
        return tinhDoanhThu(tuNgay, denNgay) * 0.6;
    }

    /**
     * Lãi/lỗ theo sản phẩm
     */
    public Map<String, Map<String, Double>> laiLoTheoSanPham(LocalDate tuNgay, LocalDate denNgay) {
        Map<String, Map<String, Double>> result = new HashMap<>();

        String sql = """
                    SELECT sp.tenSanPham,
                           SUM(ct.soLuong * ct.donGia) as doanhThu,
                           SUM(ct.soLuong) as soLuong
                    FROM ChiTietDonHang ct
                    JOIN SanPham sp ON ct.maSP = sp.maSP
                    JOIN DonHang dh ON ct.maDH = dh.maDH
                    WHERE dh.ngayDat BETWEEN ? AND ?
                    GROUP BY sp.maSP
                    ORDER BY doanhThu DESC
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tuNgay.toString());
            pstmt.setString(2, denNgay.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Double> spData = new HashMap<>();
                double doanhThu = rs.getDouble("doanhThu");
                double chiPhi = doanhThu * 0.6; // Giả định
                spData.put("doanhThu", doanhThu);
                spData.put("chiPhi", chiPhi);
                spData.put("laiLo", doanhThu - chiPhi);
                spData.put("soLuong", rs.getDouble("soLuong"));
                result.put(rs.getString("tenSanPham"), spData);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tính lãi lỗ theo SP: " + e.getMessage());
        }

        return result;
    }

    /**
     * Lãi/lỗ theo tháng (dùng cho biểu đồ)
     */
    public Map<String, Double> laiLoTheoThang(int nam) {
        Map<String, Double> result = new HashMap<>();
        String[] thang = { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" };

        for (int i = 1; i <= 12; i++) {
            LocalDate tuNgay = LocalDate.of(nam, i, 1);
            LocalDate denNgay = tuNgay.withDayOfMonth(tuNgay.lengthOfMonth());
            Map<String, Double> laiLo = tinhLaiLo(tuNgay, denNgay);
            result.put(thang[i - 1], laiLo.get("laiLo"));
        }

        return result;
    }
}
