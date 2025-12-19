package com.mekongfarm.dao;

import com.mekongfarm.model.Kho;
import com.mekongfarm.config.CauHinhDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho Kho hàng
 */
public class KhoDAO {

    private final Connection conn;

    public KhoDAO() {
        this.conn = CauHinhDatabase.getInstance().getConnection();
        taoTable();
    }

    private void taoTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS Kho (
                        maKho INTEGER PRIMARY KEY AUTOINCREMENT,
                        tenKho TEXT NOT NULL,
                        diaChi TEXT,
                        nguoiQuanLy TEXT,
                        dienThoai TEXT,
                        sucChua REAL,
                        hoatDong INTEGER DEFAULT 1
                    )
                """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Lỗi tạo bảng Kho: " + e.getMessage());
        }
    }

    public boolean them(Kho kho) {
        String sql = "INSERT INTO Kho (tenKho, diaChi, nguoiQuanLy, dienThoai, sucChua, hoatDong) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kho.getTenKho());
            pstmt.setString(2, kho.getDiaChi());
            pstmt.setString(3, kho.getNguoiQuanLy());
            pstmt.setString(4, kho.getDienThoai());
            pstmt.setDouble(5, kho.getSucChua());
            pstmt.setInt(6, kho.isHoatDong() ? 1 : 0);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm kho: " + e.getMessage());
            return false;
        }
    }

    public boolean capNhat(Kho kho) {
        String sql = "UPDATE Kho SET tenKho=?, diaChi=?, nguoiQuanLy=?, dienThoai=?, sucChua=?, hoatDong=? WHERE maKho=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kho.getTenKho());
            pstmt.setString(2, kho.getDiaChi());
            pstmt.setString(3, kho.getNguoiQuanLy());
            pstmt.setString(4, kho.getDienThoai());
            pstmt.setDouble(5, kho.getSucChua());
            pstmt.setInt(6, kho.isHoatDong() ? 1 : 0);
            pstmt.setInt(7, kho.getMaKho());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật kho: " + e.getMessage());
            return false;
        }
    }

    public boolean xoa(int maKho) {
        String sql = "DELETE FROM Kho WHERE maKho=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maKho);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa kho: " + e.getMessage());
            return false;
        }
    }

    public List<Kho> layTatCa() {
        List<Kho> list = new ArrayList<>();
        String sql = "SELECT * FROM Kho ORDER BY tenKho";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách kho: " + e.getMessage());
        }
        return list;
    }

    public List<Kho> layHoatDong() {
        List<Kho> list = new ArrayList<>();
        String sql = "SELECT * FROM Kho WHERE hoatDong = 1 ORDER BY tenKho";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy kho hoạt động: " + e.getMessage());
        }
        return list;
    }

    public Kho timTheoId(int maKho) {
        String sql = "SELECT * FROM Kho WHERE maKho=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maKho);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm kho: " + e.getMessage());
        }
        return null;
    }

    private Kho mapRow(ResultSet rs) throws SQLException {
        Kho kho = new Kho();
        kho.setMaKho(rs.getInt("maKho"));
        kho.setTenKho(rs.getString("tenKho"));
        kho.setDiaChi(rs.getString("diaChi"));
        kho.setNguoiQuanLy(rs.getString("nguoiQuanLy"));
        kho.setDienThoai(rs.getString("dienThoai"));
        kho.setSucChua(rs.getDouble("sucChua"));
        kho.setHoatDong(rs.getInt("hoatDong") == 1);
        return kho;
    }
}
