package com.mekongfarm.dao;

import com.mekongfarm.model.NhaCungCap;
import com.mekongfarm.config.CauHinhDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho Nhà cung cấp
 */
public class NhaCungCapDAO {

    private final Connection conn;

    public NhaCungCapDAO() {
        this.conn = CauHinhDatabase.getInstance().getConnection();
        taoTable();
    }

    private void taoTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS NhaCungCap (
                        maNCC INTEGER PRIMARY KEY AUTOINCREMENT,
                        tenNCC TEXT NOT NULL,
                        dienThoai TEXT,
                        diaChi TEXT,
                        email TEXT,
                        loaiSanPham TEXT,
                        danhGia INTEGER DEFAULT 5,
                        ghiChu TEXT
                    )
                """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Lỗi tạo bảng NhaCungCap: " + e.getMessage());
        }
    }

    public boolean them(NhaCungCap ncc) {
        String sql = "INSERT INTO NhaCungCap (tenNCC, dienThoai, diaChi, email, loaiSanPham, danhGia, ghiChu) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ncc.getTenNCC());
            pstmt.setString(2, ncc.getDienThoai());
            pstmt.setString(3, ncc.getDiaChi());
            pstmt.setString(4, ncc.getEmail());
            pstmt.setString(5, ncc.getLoaiSanPham());
            pstmt.setInt(6, ncc.getDanhGia());
            pstmt.setString(7, ncc.getGhiChu());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm NCC: " + e.getMessage());
            return false;
        }
    }

    public boolean capNhat(NhaCungCap ncc) {
        String sql = "UPDATE NhaCungCap SET tenNCC=?, dienThoai=?, diaChi=?, email=?, loaiSanPham=?, danhGia=?, ghiChu=? WHERE maNCC=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ncc.getTenNCC());
            pstmt.setString(2, ncc.getDienThoai());
            pstmt.setString(3, ncc.getDiaChi());
            pstmt.setString(4, ncc.getEmail());
            pstmt.setString(5, ncc.getLoaiSanPham());
            pstmt.setInt(6, ncc.getDanhGia());
            pstmt.setString(7, ncc.getGhiChu());
            pstmt.setInt(8, ncc.getMaNCC());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật NCC: " + e.getMessage());
            return false;
        }
    }

    public boolean xoa(int maNCC) {
        String sql = "DELETE FROM NhaCungCap WHERE maNCC=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maNCC);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa NCC: " + e.getMessage());
            return false;
        }
    }

    public List<NhaCungCap> layTatCa() {
        List<NhaCungCap> list = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap ORDER BY tenNCC";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách NCC: " + e.getMessage());
        }
        return list;
    }

    public NhaCungCap timTheoId(int maNCC) {
        String sql = "SELECT * FROM NhaCungCap WHERE maNCC=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maNCC);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm NCC: " + e.getMessage());
        }
        return null;
    }

    public List<NhaCungCap> timKiem(String keyword) {
        List<NhaCungCap> list = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap WHERE tenNCC LIKE ? OR loaiSanPham LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm kiếm NCC: " + e.getMessage());
        }
        return list;
    }

    private NhaCungCap mapRow(ResultSet rs) throws SQLException {
        NhaCungCap ncc = new NhaCungCap();
        ncc.setMaNCC(rs.getInt("maNCC"));
        ncc.setTenNCC(rs.getString("tenNCC"));
        ncc.setDienThoai(rs.getString("dienThoai"));
        ncc.setDiaChi(rs.getString("diaChi"));
        ncc.setEmail(rs.getString("email"));
        ncc.setLoaiSanPham(rs.getString("loaiSanPham"));
        ncc.setDanhGia(rs.getInt("danhGia"));
        ncc.setGhiChu(rs.getString("ghiChu"));
        return ncc;
    }
}
