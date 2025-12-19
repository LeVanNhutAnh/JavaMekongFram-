package com.mekongfarm.dao;

import com.mekongfarm.config.CauHinhDatabase;
import java.sql.*;
import java.util.*;

public class ThongKeDAO {
    private final Connection conn;

    public ThongKeDAO() {
        this.conn = CauHinhDatabase.getInstance().getConnection();
    }

    public double tinhDoanhThuThang(int thang, int nam) {
        String sql = "SELECT COALESCE(SUM(thanh_tien), 0) FROM don_hang " +
                "WHERE strftime('%m', ngay_dat) = ? AND strftime('%Y', ngay_dat) = ? AND trang_thai != 'da_huy'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, String.format("%02d", thang));
            pstmt.setString(2, String.valueOf(nam));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
                return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return 0;
    }

    public Map<String, Double> thongKeDoanhThuTheoThang(int nam) {
        Map<String, Double> result = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            result.put("Tháng " + i, tinhDoanhThuThang(i, nam));
        }
        return result;
    }

    public Map<String, Integer> thongKeSanPhamTheoLoai() {
        Map<String, Integer> result = new LinkedHashMap<>();
        String sql = "SELECT lsp.ten_loai, COUNT(sp.ma_san_pham) as so_luong " +
                "FROM loai_san_pham lsp LEFT JOIN san_pham sp ON lsp.ma_loai = sp.ma_loai " +
                "WHERE sp.trang_thai = 1 GROUP BY lsp.ma_loai ORDER BY so_luong DESC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.put(rs.getString("ten_loai"), rs.getInt("so_luong"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return result;
    }

    public Map<String, Integer> thongKeSanPhamTheoTinh() {
        Map<String, Integer> result = new LinkedHashMap<>();
        String sql = "SELECT tt.ten_tinh, COUNT(sp.ma_san_pham) as so_luong " +
                "FROM tinh_thanh tt LEFT JOIN san_pham sp ON tt.ma_tinh = sp.ma_tinh " +
                "WHERE sp.trang_thai = 1 GROUP BY tt.ma_tinh ORDER BY so_luong DESC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.put(rs.getString("ten_tinh"), rs.getInt("so_luong"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return result;
    }

    public int demTongSanPham() {
        String sql = "SELECT COUNT(*) FROM san_pham WHERE trang_thai = 1";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
        }
        return 0;
    }

    public int demTongKhachHang() {
        String sql = "SELECT COUNT(*) FROM khach_hang";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
        }
        return 0;
    }

    public int demTongDonHang() {
        String sql = "SELECT COUNT(*) FROM don_hang";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
        }
        return 0;
    }

    public double tinhTongDoanhThu() {
        String sql = "SELECT COALESCE(SUM(thanh_tien), 0) FROM don_hang WHERE trang_thai != 'da_huy'";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                return rs.getDouble(1);
        } catch (SQLException e) {
        }
        return 0;
    }

    /**
     * Top sản phẩm bán chạy
     */
    public Map<String, Integer> topSanPhamBanChay(int limit) {
        Map<String, Integer> result = new LinkedHashMap<>();
        String sql = """
                SELECT sp.ten_san_pham, COALESCE(SUM(ctdh.so_luong), 0) as tong_ban
                FROM san_pham sp
                LEFT JOIN chi_tiet_don_hang ctdh ON sp.ma_san_pham = ctdh.ma_san_pham
                GROUP BY sp.ma_san_pham
                ORDER BY tong_ban DESC
                LIMIT ?
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("ten_san_pham"), rs.getInt("tong_ban"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return result;
    }

    /**
     * Doanh thu theo năm
     */
    public Map<Integer, Double> thongKeDoanhThuTheoNam() {
        Map<Integer, Double> result = new LinkedHashMap<>();
        String sql = """
                SELECT strftime('%Y', ngay_dat) as nam, SUM(thanh_tien) as doanh_thu
                FROM don_hang
                WHERE trang_thai != 'da_huy'
                GROUP BY strftime('%Y', ngay_dat)
                ORDER BY nam DESC
                """;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String namStr = rs.getString("nam");
                if (namStr != null) {
                    result.put(Integer.parseInt(namStr), rs.getDouble("doanh_thu"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return result;
    }
}
