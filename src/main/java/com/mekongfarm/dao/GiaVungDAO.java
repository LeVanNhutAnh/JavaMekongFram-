package com.mekongfarm.dao;

import com.mekongfarm.model.GiaVung;
import com.mekongfarm.config.CauHinhDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho Giá theo vùng
 */
public class GiaVungDAO {

    private final Connection conn;

    public GiaVungDAO() {
        this.conn = CauHinhDatabase.getInstance().getConnection();
        taoTable();
    }

    private void taoTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS GiaVung (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        maSP INTEGER,
                        maTinh INTEGER,
                        giaRieng REAL,
                        ngayApDung TEXT,
                        ghiChu TEXT,
                        FOREIGN KEY (maSP) REFERENCES SanPham(maSP)
                    )
                """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Lỗi tạo bảng GiaVung: " + e.getMessage());
        }
    }

    public boolean them(GiaVung gv) {
        String sql = "INSERT INTO GiaVung (maSP, maTinh, giaRieng, ngayApDung, ghiChu) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gv.getMaSP());
            pstmt.setInt(2, gv.getMaTinh());
            pstmt.setDouble(3, gv.getGiaRieng());
            pstmt.setString(4, gv.getNgayApDung() != null ? gv.getNgayApDung().toString() : null);
            pstmt.setString(5, gv.getGhiChu());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm giá vùng: " + e.getMessage());
            return false;
        }
    }

    public boolean capNhat(GiaVung gv) {
        String sql = "UPDATE GiaVung SET maSP=?, maTinh=?, giaRieng=?, ngayApDung=?, ghiChu=? WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gv.getMaSP());
            pstmt.setInt(2, gv.getMaTinh());
            pstmt.setDouble(3, gv.getGiaRieng());
            pstmt.setString(4, gv.getNgayApDung() != null ? gv.getNgayApDung().toString() : null);
            pstmt.setString(5, gv.getGhiChu());
            pstmt.setInt(6, gv.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật giá vùng: " + e.getMessage());
            return false;
        }
    }

    public boolean xoa(int id) {
        String sql = "DELETE FROM GiaVung WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa giá vùng: " + e.getMessage());
            return false;
        }
    }

    public List<GiaVung> layTatCa() {
        List<GiaVung> list = new ArrayList<>();
        String sql = """
                    SELECT gv.*, sp.tenSanPham, tt.tenTinh
                    FROM GiaVung gv
                    LEFT JOIN SanPham sp ON gv.maSP = sp.maSP
                    LEFT JOIN TinhThanh tt ON gv.maTinh = tt.maTinh
                    ORDER BY sp.tenSanPham, tt.tenTinh
                """;
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách giá vùng: " + e.getMessage());
        }
        return list;
    }

    public List<GiaVung> layTheoSanPham(int maSP) {
        List<GiaVung> list = new ArrayList<>();
        String sql = """
                    SELECT gv.*, sp.tenSanPham, tt.tenTinh
                    FROM GiaVung gv
                    LEFT JOIN SanPham sp ON gv.maSP = sp.maSP
                    LEFT JOIN TinhThanh tt ON gv.maTinh = tt.maTinh
                    WHERE gv.maSP = ?
                    ORDER BY tt.tenTinh
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maSP);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy giá theo SP: " + e.getMessage());
        }
        return list;
    }

    public double layGia(int maSP, int maTinh) {
        String sql = "SELECT giaRieng FROM GiaVung WHERE maSP=? AND maTinh=? ORDER BY ngayApDung DESC LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maSP);
            pstmt.setInt(2, maTinh);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("giaRieng");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy giá: " + e.getMessage());
        }
        return 0;
    }

    private GiaVung mapRow(ResultSet rs) throws SQLException {
        GiaVung gv = new GiaVung();
        gv.setId(rs.getInt("id"));
        gv.setMaSP(rs.getInt("maSP"));
        gv.setMaTinh(rs.getInt("maTinh"));
        gv.setGiaRieng(rs.getDouble("giaRieng"));
        String ngayApDung = rs.getString("ngayApDung");
        if (ngayApDung != null)
            gv.setNgayApDung(LocalDate.parse(ngayApDung));
        gv.setGhiChu(rs.getString("ghiChu"));
        try {
            gv.setTenSanPham(rs.getString("tenSanPham"));
            gv.setTenTinh(rs.getString("tenTinh"));
        } catch (SQLException e) {
            /* ignore */ }
        return gv;
    }
}
