package com.mekongfarm.dao;

import com.mekongfarm.model.MuaVu;
import com.mekongfarm.config.CauHinhDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho Mùa vụ
 */
public class MuaVuDAO {

    private final Connection conn;

    public MuaVuDAO() {
        this.conn = CauHinhDatabase.getInstance().getConnection();
        taoTable();
    }

    private void taoTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS MuaVu (
                        maMuaVu INTEGER PRIMARY KEY AUTOINCREMENT,
                        tenMuaVu TEXT NOT NULL,
                        ngayBatDau TEXT,
                        ngayKetThuc TEXT,
                        sanPhamLienQuan TEXT,
                        duBaoSanLuong REAL,
                        moTa TEXT,
                        trangThai TEXT
                    )
                """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Lỗi tạo bảng MuaVu: " + e.getMessage());
        }
    }

    public boolean them(MuaVu mv) {
        mv.capNhatTrangThai();
        String sql = "INSERT INTO MuaVu (tenMuaVu, ngayBatDau, ngayKetThuc, sanPhamLienQuan, duBaoSanLuong, moTa, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mv.getTenMuaVu());
            pstmt.setString(2, mv.getNgayBatDau() != null ? mv.getNgayBatDau().toString() : null);
            pstmt.setString(3, mv.getNgayKetThuc() != null ? mv.getNgayKetThuc().toString() : null);
            pstmt.setString(4, mv.getSanPhamLienQuan());
            pstmt.setDouble(5, mv.getDuBaoSanLuong());
            pstmt.setString(6, mv.getMoTa());
            pstmt.setString(7, mv.getTrangThai());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm mùa vụ: " + e.getMessage());
            return false;
        }
    }

    public boolean capNhat(MuaVu mv) {
        mv.capNhatTrangThai();
        String sql = "UPDATE MuaVu SET tenMuaVu=?, ngayBatDau=?, ngayKetThuc=?, sanPhamLienQuan=?, duBaoSanLuong=?, moTa=?, trangThai=? WHERE maMuaVu=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mv.getTenMuaVu());
            pstmt.setString(2, mv.getNgayBatDau() != null ? mv.getNgayBatDau().toString() : null);
            pstmt.setString(3, mv.getNgayKetThuc() != null ? mv.getNgayKetThuc().toString() : null);
            pstmt.setString(4, mv.getSanPhamLienQuan());
            pstmt.setDouble(5, mv.getDuBaoSanLuong());
            pstmt.setString(6, mv.getMoTa());
            pstmt.setString(7, mv.getTrangThai());
            pstmt.setInt(8, mv.getMaMuaVu());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật mùa vụ: " + e.getMessage());
            return false;
        }
    }

    public boolean xoa(int maMuaVu) {
        String sql = "DELETE FROM MuaVu WHERE maMuaVu=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maMuaVu);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa mùa vụ: " + e.getMessage());
            return false;
        }
    }

    public List<MuaVu> layTatCa() {
        List<MuaVu> list = new ArrayList<>();
        String sql = "SELECT * FROM MuaVu ORDER BY ngayBatDau DESC";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách mùa vụ: " + e.getMessage());
        }
        return list;
    }

    public List<MuaVu> layDangDienRa() {
        List<MuaVu> list = new ArrayList<>();
        String today = LocalDate.now().toString();
        String sql = "SELECT * FROM MuaVu WHERE ngayBatDau <= ? AND ngayKetThuc >= ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, today);
            pstmt.setString(2, today);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy mùa vụ đang diễn ra: " + e.getMessage());
        }
        return list;
    }

    private MuaVu mapRow(ResultSet rs) throws SQLException {
        MuaVu mv = new MuaVu();
        mv.setMaMuaVu(rs.getInt("maMuaVu"));
        mv.setTenMuaVu(rs.getString("tenMuaVu"));
        String ngayBatDau = rs.getString("ngayBatDau");
        if (ngayBatDau != null)
            mv.setNgayBatDau(LocalDate.parse(ngayBatDau));
        String ngayKetThuc = rs.getString("ngayKetThuc");
        if (ngayKetThuc != null)
            mv.setNgayKetThuc(LocalDate.parse(ngayKetThuc));
        mv.setSanPhamLienQuan(rs.getString("sanPhamLienQuan"));
        mv.setDuBaoSanLuong(rs.getDouble("duBaoSanLuong"));
        mv.setMoTa(rs.getString("moTa"));
        mv.setTrangThai(rs.getString("trangThai"));
        mv.capNhatTrangThai();
        return mv;
    }
}
