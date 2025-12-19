package com.mekongfarm.dao;

import com.mekongfarm.config.CauHinhDatabase;
import com.mekongfarm.model.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO Khách hàng
 */
public class KhachHangDAO {

    private final Connection conn;

    public KhachHangDAO() {
        this.conn = CauHinhDatabase.getInstance().getConnection();
    }

    /**
     * Lấy tất cả khách hàng
     */
    public List<KhachHang> layTatCa() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM khach_hang ORDER BY ho_ten";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapKhachHang(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách khách hàng: " + e.getMessage());
        }
        return list;
    }

    /**
     * Tìm theo ID
     */
    public KhachHang timTheoId(int id) {
        String sql = "SELECT * FROM khach_hang WHERE ma_khach_hang = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapKhachHang(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm khách hàng: " + e.getMessage());
        }
        return null;
    }

    /**
     * Tìm kiếm theo tên hoặc SĐT
     */
    public List<KhachHang> timKiem(String tuKhoa) {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM khach_hang WHERE ho_ten LIKE ? OR so_dien_thoai LIKE ? ORDER BY ho_ten";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + tuKhoa + "%");
            pstmt.setString(2, "%" + tuKhoa + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapKhachHang(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm kiếm khách hàng: " + e.getMessage());
        }
        return list;
    }

    /**
     * Thêm khách hàng mới
     */
    public boolean them(KhachHang kh) {
        String sql = "INSERT INTO khach_hang (ma_kh, ho_ten, dia_chi, so_dien_thoai, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kh.getMaKH());
            pstmt.setString(2, kh.getHoTen());
            pstmt.setString(3, kh.getDiaChi());
            pstmt.setString(4, kh.getSoDienThoai());
            pstmt.setString(5, kh.getEmail());

            System.out.println("SQL: " + sql);
            System.out.println("Executing INSERT khach_hang: maKH=" + kh.getMaKH() + ", hoTen=" + kh.getHoTen() +
                    ", sdt=" + kh.getSoDienThoai() + ", email=" + kh.getEmail());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                // Get last insert ID using SQLite function
                try (Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                    if (rs.next()) {
                        kh.setMaKhachHang(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi thêm khách hàng: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật khách hàng
     */
    public boolean capNhat(KhachHang kh) {
        String sql = "UPDATE khach_hang SET ho_ten = ?, dia_chi = ?, so_dien_thoai = ?, email = ? WHERE ma_khach_hang = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kh.getHoTen());
            pstmt.setString(2, kh.getDiaChi());
            pstmt.setString(3, kh.getSoDienThoai());
            pstmt.setString(4, kh.getEmail());
            pstmt.setInt(5, kh.getMaKhachHang());

            System.out.println("SQL: " + sql);
            System.out.println("Executing UPDATE khach_hang: id=" + kh.getMaKhachHang() + ", maKH=" + kh.getMaKH()
                    + ", hoTen=" + kh.getHoTen());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật khách hàng: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa khách hàng
     */
    public boolean xoa(int maKhachHang) {
        String sql = "DELETE FROM khach_hang WHERE ma_khach_hang = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maKhachHang);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa khách hàng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy mã khách hàng tự động - đảm bảo không trùng
     */
    public String layMaKHTiepTheo() {
        String sql = "SELECT MAX(CAST(SUBSTR(ma_kh, 3) AS INTEGER)) AS max_ma FROM khach_hang WHERE ma_kh LIKE 'KH%'";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int maxMa = rs.getInt("max_ma");
                String newCode = String.format("KH%03d", maxMa + 1);

                while (kiemTraMaKHTonTai(newCode)) {
                    maxMa++;
                    newCode = String.format("KH%03d", maxMa + 1);
                }
                return newCode;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy mã KH: " + e.getMessage());
        }
        return "KH001";
    }

    /**
     * Kiểm tra mã KH đã tồn tại chưa
     */
    public boolean kiemTraMaKHTonTai(String maKH) {
        String sql = "SELECT COUNT(*) FROM khach_hang WHERE ma_kh = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKH);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra mã KH: " + e.getMessage());
        }
        return false;
    }

    /**
     * Đếm tổng khách hàng
     */
    public int demTongSo() {
        String sql = "SELECT COUNT(*) FROM khach_hang";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đếm khách hàng: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Map ResultSet sang KhachHang
     */
    private KhachHang mapKhachHang(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();
        kh.setMaKhachHang(rs.getInt("ma_khach_hang"));
        kh.setMaKH(rs.getString("ma_kh"));
        kh.setHoTen(rs.getString("ho_ten"));
        kh.setDiaChi(rs.getString("dia_chi"));
        kh.setSoDienThoai(rs.getString("so_dien_thoai"));
        kh.setEmail(rs.getString("email"));
        return kh;
    }
}
