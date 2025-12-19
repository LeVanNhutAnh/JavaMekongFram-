package com.mekongfarm.dao;

import com.mekongfarm.config.CauHinhDatabase;
import com.mekongfarm.model.NguoiDung;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO Người dùng - Truy xuất dữ liệu người dùng
 */
public class NguoiDungDAO {

    private final Connection conn;

    public NguoiDungDAO() {
        this.conn = CauHinhDatabase.getInstance().getConnection();
    }

    /**
     * Kiểm tra đăng nhập với BCrypt
     */
    public NguoiDung dangNhap(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM nguoi_dung WHERE ten_dang_nhap = ? AND trang_thai = 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenDangNhap);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("mat_khau");
                // Kiểm tra nếu password chưa hash (legacy data)
                if (!hashedPassword.startsWith("$2a$") && !hashedPassword.startsWith("$2b$")) {
                    // Plain text password - so sánh trực tiếp (legacy support)
                    if (matKhau.equals(hashedPassword)) {
                        return mapNguoiDung(rs);
                    }
                } else {
                    // BCrypt hashed password
                    if (BCrypt.checkpw(matKhau, hashedPassword)) {
                        return mapNguoiDung(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đăng nhập: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy tất cả người dùng
     */
    public List<NguoiDung> layTatCa() {
        List<NguoiDung> list = new ArrayList<>();
        String sql = "SELECT * FROM nguoi_dung ORDER BY ho_ten";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapNguoiDung(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách người dùng: " + e.getMessage());
        }
        return list;
    }

    /**
     * Tìm theo ID
     */
    public NguoiDung timTheoId(int id) {
        String sql = "SELECT * FROM nguoi_dung WHERE ma_nguoi_dung = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapNguoiDung(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm người dùng: " + e.getMessage());
        }
        return null;
    }

    /**
     * Thêm người dùng mới với BCrypt hash
     */
    public boolean them(NguoiDung nguoiDung) {
        String sql = "INSERT INTO nguoi_dung (ten_dang_nhap, mat_khau, ho_ten, vai_tro) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nguoiDung.getTenDangNhap());
            // Hash password với BCrypt
            String hashedPassword = BCrypt.hashpw(nguoiDung.getMatKhau(), BCrypt.gensalt());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, nguoiDung.getHoTen());
            pstmt.setString(4, nguoiDung.getVaiTro());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm người dùng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật người dùng
     */
    public boolean capNhat(NguoiDung nguoiDung) {
        String sql = "UPDATE nguoi_dung SET ho_ten = ?, vai_tro = ?, trang_thai = ? WHERE ma_nguoi_dung = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nguoiDung.getHoTen());
            pstmt.setString(2, nguoiDung.getVaiTro());
            pstmt.setBoolean(3, nguoiDung.isTrangThai());
            pstmt.setInt(4, nguoiDung.getMaNguoiDung());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật người dùng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Đổi mật khẩu với BCrypt hash
     */
    public boolean doiMatKhau(int id, String matKhauMoi) {
        String sql = "UPDATE nguoi_dung SET mat_khau = ? WHERE ma_nguoi_dung = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Hash password mới với BCrypt
            String hashedPassword = BCrypt.hashpw(matKhauMoi, BCrypt.gensalt());
            pstmt.setString(1, hashedPassword);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi đổi mật khẩu: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa người dùng (soft delete)
     */
    public boolean xoa(int id) {
        String sql = "UPDATE nguoi_dung SET trang_thai = 0 WHERE ma_nguoi_dung = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa người dùng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Map ResultSet sang NguoiDung
     */
    private NguoiDung mapNguoiDung(ResultSet rs) throws SQLException {
        NguoiDung nd = new NguoiDung();
        nd.setMaNguoiDung(rs.getInt("ma_nguoi_dung"));
        nd.setTenDangNhap(rs.getString("ten_dang_nhap"));
        nd.setMatKhau(rs.getString("mat_khau"));
        nd.setHoTen(rs.getString("ho_ten"));
        nd.setVaiTro(rs.getString("vai_tro"));
        nd.setTrangThai(rs.getInt("trang_thai") == 1);
        return nd;
    }
}
