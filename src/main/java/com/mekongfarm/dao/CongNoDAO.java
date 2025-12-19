package com.mekongfarm.dao;

import com.mekongfarm.model.CongNo;
import com.mekongfarm.config.CauHinhDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho Công nợ khách hàng
 */
public class CongNoDAO {

    private final Connection conn;

    public CongNoDAO() {
        this.conn = CauHinhDatabase.getInstance().getConnection();
        taoTable();
    }

    private void taoTable() {
        String sql = "CREATE TABLE IF NOT EXISTS CongNo (" +
                "maCongNo INTEGER PRIMARY KEY AUTOINCREMENT," +
                "loaiCongNo TEXT DEFAULT 'phai_thu'," +
                "maKH INTEGER," +
                "maNCC INTEGER," +
                "maDH INTEGER," +
                "soTien REAL," +
                "ngayPhatSinh TEXT," +
                "hanThanhToan TEXT," +
                "trangThai TEXT DEFAULT 'Chưa thanh toán'," +
                "daThanhToan REAL DEFAULT 0," +
                "ghiChu TEXT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Lỗi tạo bảng CongNo: " + e.getMessage());
        }
    }

    public boolean them(CongNo cn) {
        String sql = "INSERT INTO CongNo (loaiCongNo, maKH, maNCC, maDH, soTien, ngayPhatSinh, hanThanhToan, trangThai, daThanhToan, ghiChu) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cn.getLoaiCongNo());
            pstmt.setInt(2, cn.getMaKH());
            pstmt.setInt(3, cn.getMaNCC());
            pstmt.setInt(4, cn.getMaDH());
            pstmt.setDouble(5, cn.getSoTien());
            pstmt.setString(6, cn.getNgayPhatSinh().toString());
            pstmt.setString(7, cn.getHanThanhToan() != null ? cn.getHanThanhToan().toString() : null);
            pstmt.setString(8, cn.getTrangThai());
            pstmt.setDouble(9, cn.getDaThanhToan());
            pstmt.setString(10, cn.getGhiChu());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm công nợ: " + e.getMessage());
            return false;
        }
    }

    public boolean thanhToan(int maCongNo, double soTien) {
        String sql = "UPDATE CongNo SET daThanhToan = daThanhToan + ?, trangThai = CASE WHEN daThanhToan + ? >= soTien THEN 'Đã thanh toán' ELSE 'Đã thanh toán một phần' END WHERE maCongNo = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, soTien);
            pstmt.setDouble(2, soTien);
            pstmt.setInt(3, maCongNo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thanh toán công nợ: " + e.getMessage());
            return false;
        }
    }

    public List<CongNo> layTatCa() {
        List<CongNo> list = new ArrayList<>();
        String sql = "SELECT cn.*, kh.ho_ten as tenKhachHang, ncc.tenNCC " +
                "FROM CongNo cn " +
                "LEFT JOIN khach_hang kh ON cn.maKH = kh.ma_khach_hang " +
                "LEFT JOIN NhaCungCap ncc ON cn.maNCC = ncc.maNCC " +
                "ORDER BY cn.ngayPhatSinh DESC";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách công nợ: " + e.getMessage());
        }
        return list;
    }

    public List<CongNo> layChuaThanhToan() {
        List<CongNo> list = new ArrayList<>();
        String sql = "SELECT cn.*, kh.ho_ten as tenKhachHang " +
                "FROM CongNo cn " +
                "LEFT JOIN khach_hang kh ON cn.maKH = kh.ma_khach_hang " +
                "WHERE cn.trangThai != 'Đã thanh toán' " +
                "ORDER BY cn.hanThanhToan";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy công nợ chưa thanh toán: " + e.getMessage());
        }
        return list;
    }

    public List<CongNo> layQuaHan() {
        List<CongNo> list = new ArrayList<>();
        String sql = "SELECT cn.*, kh.ho_ten as tenKhachHang " +
                "FROM CongNo cn " +
                "LEFT JOIN khach_hang kh ON cn.maKH = kh.ma_khach_hang " +
                "WHERE cn.trangThai != 'Đã thanh toán' AND cn.hanThanhToan < ? " +
                "ORDER BY cn.hanThanhToan";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, LocalDate.now().toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy công nợ quá hạn: " + e.getMessage());
        }
        return list;
    }

    public double tongCongNo() {
        String sql = "SELECT SUM(soTien - daThanhToan) FROM CongNo WHERE trangThai != 'Đã thanh toán'";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tính tổng công nợ: " + e.getMessage());
        }
        return 0;
    }

    public List<CongNo> layTheoLoai(String loaiCongNo) {
        List<CongNo> list = new ArrayList<>();
        String sql = "SELECT cn.*, kh.ho_ten as tenKhachHang, ncc.tenNCC " +
                "FROM CongNo cn " +
                "LEFT JOIN khach_hang kh ON cn.maKH = kh.ma_khach_hang " +
                "LEFT JOIN NhaCungCap ncc ON cn.maNCC = ncc.maNCC " +
                "WHERE cn.loaiCongNo = ? " +
                "ORDER BY cn.ngayPhatSinh DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loaiCongNo);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy công nợ theo loại: " + e.getMessage());
        }
        return list;
    }

    // Lấy công nợ phải trả theo NCC
    public List<CongNo> layTheoNCC(int maNCC) {
        List<CongNo> list = new ArrayList<>();
        String sql = "SELECT cn.*, ncc.tenNCC " +
                "FROM CongNo cn " +
                "LEFT JOIN NhaCungCap ncc ON cn.maNCC = ncc.maNCC " +
                "WHERE cn.loaiCongNo = 'phai_tra' AND cn.maNCC = ? " +
                "ORDER BY cn.ngayPhatSinh DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maNCC);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy công nợ NCC: " + e.getMessage());
        }
        return list;
    }

    // Tổng phải trả NCC
    public double tongPhaiTra() {
        String sql = "SELECT SUM(soTien - daThanhToan) FROM CongNo WHERE loaiCongNo = 'phai_tra' AND trangThai != 'Đã thanh toán'";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tính tổng phải trả: " + e.getMessage());
        }
        return 0;
    }

    private CongNo mapRow(ResultSet rs) throws SQLException {
        CongNo cn = new CongNo();
        cn.setMaCongNo(rs.getInt("maCongNo"));
        try {
            cn.setLoaiCongNo(rs.getString("loaiCongNo"));
        } catch (Exception e) {
        }
        cn.setMaKH(rs.getInt("maKH"));
        cn.setMaNCC(rs.getInt("maNCC"));
        cn.setMaDH(rs.getInt("maDH"));
        cn.setSoTien(rs.getDouble("soTien"));
        String ngayPhatSinh = rs.getString("ngayPhatSinh");
        if (ngayPhatSinh != null)
            cn.setNgayPhatSinh(LocalDate.parse(ngayPhatSinh));
        String hanThanhToan = rs.getString("hanThanhToan");
        if (hanThanhToan != null)
            cn.setHanThanhToan(LocalDate.parse(hanThanhToan));
        cn.setTrangThai(rs.getString("trangThai"));
        cn.setDaThanhToan(rs.getDouble("daThanhToan"));
        cn.setGhiChu(rs.getString("ghiChu"));
        try {
            cn.setTenKhachHang(rs.getString("tenKhachHang"));
            cn.setTenNCC(rs.getString("tenNCC"));
        } catch (SQLException e) {
            /* ignore */ }
        return cn;
    }
}
