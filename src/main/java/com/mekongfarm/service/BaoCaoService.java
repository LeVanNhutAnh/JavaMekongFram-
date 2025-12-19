package com.mekongfarm.service;

import com.mekongfarm.config.CauHinhDatabase;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Service báo cáo nâng cao
 * - Báo cáo theo khoảng thời gian
 * - So sánh doanh thu theo tháng/quý/năm
 * - Top sản phẩm bán chạy
 */
public class BaoCaoService {

    private final Connection conn;

    public BaoCaoService() {
        this.conn = CauHinhDatabase.getInstance().getConnection();
    }

    /**
     * Lấy doanh thu theo khoảng thời gian
     */
    public double getDoanhThuTheoThoiGian(LocalDate tuNgay, LocalDate denNgay) {
        String sql = """
                SELECT COALESCE(SUM(thanh_tien), 0) as tong
                FROM don_hang
                WHERE DATE(ngay_dat) BETWEEN ? AND ?
                AND trang_thai != 'huy'
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tuNgay.toString());
            pstmt.setString(2, denNgay.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("tong");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy doanh thu: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Lấy doanh thu theo tháng trong năm
     */
    public Map<Integer, Double> getDoanhThuTheoThang(int nam) {
        Map<Integer, Double> result = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            result.put(i, 0.0);
        }

        String sql = """
                SELECT strftime('%m', ngay_dat) as thang, SUM(thanh_tien) as tong
                FROM don_hang
                WHERE strftime('%Y', ngay_dat) = ?
                AND trang_thai != 'huy'
                GROUP BY strftime('%m', ngay_dat)
                ORDER BY thang
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, String.valueOf(nam));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int thang = Integer.parseInt(rs.getString("thang"));
                double tong = rs.getDouble("tong");
                result.put(thang, tong);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy doanh thu theo tháng: " + e.getMessage());
        }
        return result;
    }

    /**
     * Lấy doanh thu theo quý
     */
    public Map<Integer, Double> getDoanhThuTheoQuy(int nam) {
        Map<Integer, Double> result = new LinkedHashMap<>();
        for (int i = 1; i <= 4; i++) {
            result.put(i, 0.0);
        }

        String sql = """
                SELECT 
                    CASE 
                        WHEN CAST(strftime('%m', ngay_dat) AS INTEGER) BETWEEN 1 AND 3 THEN 1
                        WHEN CAST(strftime('%m', ngay_dat) AS INTEGER) BETWEEN 4 AND 6 THEN 2
                        WHEN CAST(strftime('%m', ngay_dat) AS INTEGER) BETWEEN 7 AND 9 THEN 3
                        ELSE 4
                    END as quy,
                    SUM(thanh_tien) as tong
                FROM don_hang
                WHERE strftime('%Y', ngay_dat) = ?
                AND trang_thai != 'huy'
                GROUP BY quy
                ORDER BY quy
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, String.valueOf(nam));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int quy = rs.getInt("quy");
                double tong = rs.getDouble("tong");
                result.put(quy, tong);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy doanh thu theo quý: " + e.getMessage());
        }
        return result;
    }

    /**
     * So sánh doanh thu 2 năm
     */
    public Map<String, Double> soSanhDoanhThuNam(int nam1, int nam2) {
        Map<String, Double> result = new LinkedHashMap<>();
        result.put("Năm " + nam1, getDoanhThuTheoThoiGian(
                LocalDate.of(nam1, 1, 1), LocalDate.of(nam1, 12, 31)));
        result.put("Năm " + nam2, getDoanhThuTheoThoiGian(
                LocalDate.of(nam2, 1, 1), LocalDate.of(nam2, 12, 31)));
        return result;
    }

    /**
     * Top sản phẩm bán chạy
     */
    public List<Map<String, Object>> getTopSanPhamBanChay(int limit) {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = """
                SELECT sp.ten_san_pham, sp.ma_sp, 
                       SUM(ct.so_luong) as tong_ban,
                       SUM(ct.thanh_tien) as doanh_thu
                FROM chi_tiet_don_hang ct
                JOIN san_pham sp ON ct.ma_san_pham = sp.ma_san_pham
                JOIN don_hang dh ON ct.ma_don_hang = dh.ma_don_hang
                WHERE dh.trang_thai != 'huy'
                GROUP BY sp.ma_san_pham
                ORDER BY tong_ban DESC
                LIMIT ?
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("maSP", rs.getString("ma_sp"));
                item.put("tenSanPham", rs.getString("ten_san_pham"));
                item.put("tongBan", rs.getInt("tong_ban"));
                item.put("doanhThu", rs.getDouble("doanh_thu"));
                result.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy top sản phẩm: " + e.getMessage());
        }
        return result;
    }

    /**
     * Số đơn hàng theo trạng thái
     */
    public Map<String, Integer> getSoDonHangTheoTrangThai() {
        Map<String, Integer> result = new LinkedHashMap<>();
        String sql = """
                SELECT trang_thai, COUNT(*) as so_luong
                FROM don_hang
                GROUP BY trang_thai
                """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String trangThai = rs.getString("trang_thai");
                int soLuong = rs.getInt("so_luong");
                result.put(trangThai, soLuong);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy số đơn hàng: " + e.getMessage());
        }
        return result;
    }

    /**
     * Thống kê khách hàng mua nhiều nhất
     */
    public List<Map<String, Object>> getTopKhachHang(int limit) {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = """
                SELECT kh.ho_ten, kh.ma_kh,
                       COUNT(dh.ma_don_hang) as so_don,
                       SUM(dh.thanh_tien) as tong_chi
                FROM khach_hang kh
                JOIN don_hang dh ON kh.ma_khach_hang = dh.ma_khach_hang
                WHERE dh.trang_thai != 'huy'
                GROUP BY kh.ma_khach_hang
                ORDER BY tong_chi DESC
                LIMIT ?
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("maKH", rs.getString("ma_kh"));
                item.put("hoTen", rs.getString("ho_ten"));
                item.put("soDon", rs.getInt("so_don"));
                item.put("tongChi", rs.getDouble("tong_chi"));
                result.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy top khách hàng: " + e.getMessage());
        }
        return result;
    }
}
