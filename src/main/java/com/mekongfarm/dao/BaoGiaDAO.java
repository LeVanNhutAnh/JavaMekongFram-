package com.mekongfarm.dao;

import com.mekongfarm.model.BaoGia;
import com.mekongfarm.config.CauHinhDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho Báo giá
 */
public class BaoGiaDAO {

    private final Connection conn;

    public BaoGiaDAO() {
        this.conn = CauHinhDatabase.getInstance().getConnection();
        taoTable();
    }

    private void taoTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS bao_gia (
                        ma_bao_gia INTEGER PRIMARY KEY AUTOINCREMENT,
                        ma_san_pham INTEGER NOT NULL,
                        ma_ncc INTEGER NOT NULL,
                        don_gia REAL NOT NULL,
                        so_luong_toi_thieu INTEGER DEFAULT 0,
                        thoi_gian_giao INTEGER DEFAULT 0,
                        dieu_kien TEXT,
                        ngay_bao_gia DATE DEFAULT CURRENT_DATE,
                        han_hieu_luc DATE,
                        trang_thai TEXT DEFAULT 'hieu_luc',
                        ghi_chu TEXT,
                        FOREIGN KEY (ma_san_pham) REFERENCES san_pham(ma_san_pham),
                        FOREIGN KEY (ma_ncc) REFERENCES NhaCungCap(maNCC)
                    )
                """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Lỗi tạo bảng bao_gia: " + e.getMessage());
        }
    }

    public int them(BaoGia bg) {
        String sql = "INSERT INTO bao_gia (ma_san_pham, ma_ncc, don_gia, so_luong_toi_thieu, thoi_gian_giao, dieu_kien, ngay_bao_gia, han_hieu_luc, trang_thai, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, bg.getMaSanPham());
            pstmt.setInt(2, bg.getMaNCC());
            pstmt.setDouble(3, bg.getDonGia());
            pstmt.setInt(4, bg.getSoLuongToiThieu());
            pstmt.setInt(5, bg.getThoiGianGiao());
            pstmt.setString(6, bg.getDieuKien());
            pstmt.setString(7, bg.getNgayBaoGia().toString());
            pstmt.setString(8, bg.getHanHieuLuc() != null ? bg.getHanHieuLuc().toString() : null);
            pstmt.setString(9, bg.getTrangThai());
            pstmt.setString(10, bg.getGhiChu());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi thêm báo giá: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public boolean capNhatTrangThai(int maBaoGia, String trangThai) {
        String sql = "UPDATE bao_gia SET trang_thai=? WHERE ma_bao_gia=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, trangThai);
            pstmt.setInt(2, maBaoGia);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật trạng thái báo giá: " + e.getMessage());
            return false;
        }
    }

    public List<BaoGia> layTatCa() {
        List<BaoGia> list = new ArrayList<>();
        String sql = """
                    SELECT bg.*, sp.ten_san_pham, ncc.tenNCC
                    FROM bao_gia bg
                    LEFT JOIN san_pham sp ON bg.ma_san_pham = sp.ma_san_pham
                    LEFT JOIN NhaCungCap ncc ON bg.ma_ncc = ncc.maNCC
                    ORDER BY bg.ngay_bao_gia DESC
                """;
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách báo giá: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<BaoGia> layConHieuLuc() {
        List<BaoGia> list = new ArrayList<>();
        String sql = """
                    SELECT bg.*, sp.ten_san_pham, ncc.tenNCC
                    FROM bao_gia bg
                    LEFT JOIN san_pham sp ON bg.ma_san_pham = sp.ma_san_pham
                    LEFT JOIN NhaCungCap ncc ON bg.ma_ncc = ncc.maNCC
                    WHERE bg.han_hieu_luc >= ? AND bg.trang_thai = 'hieu_luc'
                    ORDER BY bg.han_hieu_luc
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, LocalDate.now().toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy báo giá còn hiệu lực: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public BaoGia timTheoId(int maBaoGia) {
        String sql = """
                    SELECT bg.*, sp.ten_san_pham, ncc.tenNCC
                    FROM bao_gia bg
                    LEFT JOIN san_pham sp ON bg.ma_san_pham = sp.ma_san_pham
                    LEFT JOIN NhaCungCap ncc ON bg.ma_ncc = ncc.maNCC
                    WHERE bg.ma_bao_gia = ?
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maBaoGia);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm báo giá: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private BaoGia mapRow(ResultSet rs) throws SQLException {
        BaoGia bg = new BaoGia();
        bg.setMaBaoGia(rs.getInt("ma_bao_gia"));
        bg.setMaSanPham(rs.getInt("ma_san_pham"));
        bg.setTenSanPham(rs.getString("ten_san_pham"));
        bg.setMaNCC(rs.getInt("ma_ncc"));
        bg.setTenNCC(rs.getString("tenNCC"));
        bg.setDonGia(rs.getDouble("don_gia"));
        bg.setSoLuongToiThieu(rs.getInt("so_luong_toi_thieu"));
        bg.setThoiGianGiao(rs.getInt("thoi_gian_giao"));
        bg.setDieuKien(rs.getString("dieu_kien"));
        String ngayBaoGia = rs.getString("ngay_bao_gia");
        if (ngayBaoGia != null)
            bg.setNgayBaoGia(LocalDate.parse(ngayBaoGia));
        String hanHieuLuc = rs.getString("han_hieu_luc");
        if (hanHieuLuc != null)
            bg.setHanHieuLuc(LocalDate.parse(hanHieuLuc));
        bg.setTrangThai(rs.getString("trang_thai"));
        bg.setGhiChu(rs.getString("ghi_chu"));
        return bg;
    }
}
