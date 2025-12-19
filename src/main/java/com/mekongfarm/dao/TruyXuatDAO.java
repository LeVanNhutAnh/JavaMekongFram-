package com.mekongfarm.dao;

import com.mekongfarm.config.CauHinhDatabase;
import com.mekongfarm.model.TruyXuatNguonGoc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TruyXuatDAO {
    private final Connection conn;

    public TruyXuatDAO() {
        this.conn = CauHinhDatabase.getInstance().getConnection();
    }

    public TruyXuatNguonGoc timTheoSanPham(int maSanPham) {
        String sql = "SELECT tx.*, sp.ten_san_pham FROM truy_xuat_nguon_goc tx " +
                "LEFT JOIN san_pham sp ON tx.ma_san_pham = sp.ma_san_pham WHERE tx.ma_san_pham = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maSanPham);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapTruyXuat(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return null;
    }

    public TruyXuatNguonGoc timTheoSoLo(String soLo) {
        String sql = "SELECT tx.*, sp.ten_san_pham FROM truy_xuat_nguon_goc tx " +
                "LEFT JOIN san_pham sp ON tx.ma_san_pham = sp.ma_san_pham WHERE tx.so_lo = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, soLo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapTruyXuat(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return null;
    }

    public List<TruyXuatNguonGoc> layTatCa() {
        List<TruyXuatNguonGoc> list = new ArrayList<>();
        String sql = "SELECT tx.*, sp.ten_san_pham FROM truy_xuat_nguon_goc tx " +
                "LEFT JOIN san_pham sp ON tx.ma_san_pham = sp.ma_san_pham";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapTruyXuat(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return list;
    }

    public boolean them(TruyXuatNguonGoc tx) {
        String sql = "INSERT INTO truy_xuat_nguon_goc (ma_san_pham, so_lo, ngay_thu_hoach, " +
                "dia_chi_san_xuat, ten_nong_dan, chung_nhan, ngay_san_xuat, han_su_dung, ma_qr) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tx.getMaSanPham());
            pstmt.setString(2, tx.getSoLo());
            pstmt.setString(3, tx.getNgayThuHoach() != null ? tx.getNgayThuHoach().toString() : null);
            pstmt.setString(4, tx.getDiaChiSanXuat());
            pstmt.setString(5, tx.getTenNongDan());
            pstmt.setString(6, tx.getChungNhan());
            pstmt.setString(7, tx.getNgaySanXuat() != null ? tx.getNgaySanXuat().toString() : null);
            pstmt.setString(8, tx.getHanSuDung() != null ? tx.getHanSuDung().toString() : null);
            pstmt.setString(9, tx.getMaQR());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return false;
    }

    public boolean capNhat(TruyXuatNguonGoc tx) {
        String sql = "UPDATE truy_xuat_nguon_goc SET ma_san_pham = ?, so_lo = ?, ngay_thu_hoach = ?, " +
                "dia_chi_san_xuat = ?, ten_nong_dan = ?, chung_nhan = ?, ngay_san_xuat = ?, " +
                "han_su_dung = ?, ma_qr = ? WHERE ma_truy_xuat = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tx.getMaSanPham());
            pstmt.setString(2, tx.getSoLo());
            pstmt.setString(3, tx.getNgayThuHoach() != null ? tx.getNgayThuHoach().toString() : null);
            pstmt.setString(4, tx.getDiaChiSanXuat());
            pstmt.setString(5, tx.getTenNongDan());
            pstmt.setString(6, tx.getChungNhan());
            pstmt.setString(7, tx.getNgaySanXuat() != null ? tx.getNgaySanXuat().toString() : null);
            pstmt.setString(8, tx.getHanSuDung() != null ? tx.getHanSuDung().toString() : null);
            pstmt.setString(9, tx.getMaQR());
            pstmt.setInt(10, tx.getMaTruyXuat());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return false;
    }

    public boolean xoa(int maTruyXuat) {
        String sql = "DELETE FROM truy_xuat_nguon_goc WHERE ma_truy_xuat = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maTruyXuat);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return false;
    }

    public List<TruyXuatNguonGoc> timKiem(String tuKhoa) {
        List<TruyXuatNguonGoc> list = new ArrayList<>();
        String sql = "SELECT tx.*, sp.ten_san_pham FROM truy_xuat_nguon_goc tx " +
                "LEFT JOIN san_pham sp ON tx.ma_san_pham = sp.ma_san_pham " +
                "WHERE tx.so_lo LIKE ? OR tx.ten_nong_dan LIKE ? OR sp.ten_san_pham LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String pattern = "%" + tuKhoa + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            pstmt.setString(3, pattern);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapTruyXuat(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return list;
    }

    private TruyXuatNguonGoc mapTruyXuat(ResultSet rs) throws SQLException {
        TruyXuatNguonGoc tx = new TruyXuatNguonGoc();
        tx.setMaTruyXuat(rs.getInt("ma_truy_xuat"));
        tx.setMaSanPham(rs.getInt("ma_san_pham"));
        tx.setTenSanPham(rs.getString("ten_san_pham"));
        tx.setSoLo(rs.getString("so_lo"));
        
        // SQLite stores dates as TEXT, use getString() then parse
        String ngayTH = rs.getString("ngay_thu_hoach");
        if (ngayTH != null && !ngayTH.isEmpty()) {
            try {
                tx.setNgayThuHoach(java.time.LocalDate.parse(ngayTH));
            } catch (Exception e) {
                System.err.println("Lỗi parse ngay_thu_hoach: " + ngayTH);
            }
        }
        
        tx.setDiaChiSanXuat(rs.getString("dia_chi_san_xuat"));
        tx.setTenNongDan(rs.getString("ten_nong_dan"));
        tx.setChungNhan(rs.getString("chung_nhan"));
        
        String ngaySX = rs.getString("ngay_san_xuat");
        if (ngaySX != null && !ngaySX.isEmpty()) {
            try {
                tx.setNgaySanXuat(java.time.LocalDate.parse(ngaySX));
            } catch (Exception e) {
                System.err.println("Lỗi parse ngay_san_xuat: " + ngaySX);
            }
        }
        
        String hanSD = rs.getString("han_su_dung");
        if (hanSD != null && !hanSD.isEmpty()) {
            try {
                tx.setHanSuDung(java.time.LocalDate.parse(hanSD));
            } catch (Exception e) {
                System.err.println("Lỗi parse han_su_dung: " + hanSD);
            }
        }
        
        tx.setMaQR(rs.getString("ma_qr"));
        return tx;
    }
}
