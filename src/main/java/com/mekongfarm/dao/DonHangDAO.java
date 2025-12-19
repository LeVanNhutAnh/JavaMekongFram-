package com.mekongfarm.dao;

import com.mekongfarm.config.CauHinhDatabase;
import com.mekongfarm.model.DonHang;
import com.mekongfarm.model.ChiTietDonHang;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO Đơn hàng
 */
public class DonHangDAO {

    private final Connection conn;

    public DonHangDAO() {
        this.conn = CauHinhDatabase.getInstance().getConnection();
    }

    /**
     * Lấy tất cả đơn hàng
     */
    public List<DonHang> layTatCa() {
        List<DonHang> list = new ArrayList<>();
        String sql = """
                SELECT dh.*, kh.ho_ten AS ten_khach_hang, nd.ho_ten AS ten_nguoi_dung
                FROM don_hang dh
                LEFT JOIN khach_hang kh ON dh.ma_khach_hang = kh.ma_khach_hang
                LEFT JOIN nguoi_dung nd ON dh.ma_nguoi_dung = nd.ma_nguoi_dung
                ORDER BY dh.ngay_dat DESC
                """;
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapDonHang(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách đơn hàng: " + e.getMessage());
        }
        return list;
    }

    /**
     * Tìm theo ID
     */
    public DonHang timTheoId(int id) {
        String sql = """
                SELECT dh.*, kh.ho_ten AS ten_khach_hang, nd.ho_ten AS ten_nguoi_dung
                FROM don_hang dh
                LEFT JOIN khach_hang kh ON dh.ma_khach_hang = kh.ma_khach_hang
                LEFT JOIN nguoi_dung nd ON dh.ma_nguoi_dung = nd.ma_nguoi_dung
                WHERE dh.ma_don_hang = ?
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                DonHang dh = mapDonHang(rs);
                dh.setChiTietList(layChiTiet(id));
                return dh;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm đơn hàng: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lọc theo ngày
     */
    public List<DonHang> locTheoNgay(LocalDateTime tuNgay, LocalDateTime denNgay) {
        List<DonHang> list = new ArrayList<>();
        String sql = """
                SELECT dh.*, kh.ho_ten AS ten_khach_hang, nd.ho_ten AS ten_nguoi_dung
                FROM don_hang dh
                LEFT JOIN khach_hang kh ON dh.ma_khach_hang = kh.ma_khach_hang
                LEFT JOIN nguoi_dung nd ON dh.ma_nguoi_dung = nd.ma_nguoi_dung
                WHERE dh.ngay_dat BETWEEN ? AND ?
                ORDER BY dh.ngay_dat DESC
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(tuNgay));
            pstmt.setTimestamp(2, Timestamp.valueOf(denNgay));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapDonHang(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lọc đơn hàng: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lọc theo trạng thái
     */
    public List<DonHang> locTheoTrangThai(String trangThai) {
        List<DonHang> list = new ArrayList<>();
        String sql = """
                SELECT dh.*, kh.ho_ten AS ten_khach_hang, nd.ho_ten AS ten_nguoi_dung
                FROM don_hang dh
                LEFT JOIN khach_hang kh ON dh.ma_khach_hang = kh.ma_khach_hang
                LEFT JOIN nguoi_dung nd ON dh.ma_nguoi_dung = nd.ma_nguoi_dung
                WHERE dh.trang_thai = ?
                ORDER BY dh.ngay_dat DESC
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, trangThai);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapDonHang(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lọc đơn hàng: " + e.getMessage());
        }
        return list;
    }

    /**
     * Thêm đơn hàng mới
     */
    public boolean them(DonHang dh) {
        String sqlDonHang = "INSERT INTO don_hang (ma_dh, ma_khach_hang, ma_nguoi_dung, tong_tien, giam_gia, thanh_tien, trang_thai, ghi_chu) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlChiTiet = "INSERT INTO chi_tiet_don_hang (ma_don_hang, ma_san_pham, so_luong, don_gia, thanh_tien) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            // Thêm đơn hàng
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDonHang)) {
                pstmt.setString(1, dh.getMaDH());
                pstmt.setInt(2, dh.getMaKhachHang());
                pstmt.setInt(3, dh.getMaNguoiDung());
                pstmt.setDouble(4, dh.getTongTien());
                pstmt.setDouble(5, dh.getGiamGia());
                pstmt.setDouble(6, dh.getThanhTien());
                pstmt.setString(7, dh.getTrangThai());
                pstmt.setString(8, dh.getGhiChu());

                System.out.println("SQL DonHang: " + sqlDonHang.replace("\n", " ").trim());
                System.out.println("Executing INSERT don_hang: maDH=" + dh.getMaDH() + ", maKH=" + dh.getMaKhachHang()
                        + ", maND=" + dh.getMaNguoiDung() +
                        ", tong=" + dh.getTongTien() + ", giam=" + dh.getGiamGia() + ", thanh=" + dh.getThanhTien()
                        + ", trangThai=" + dh.getTrangThai());

                pstmt.executeUpdate();

                // Get last insert ID using SQLite function
                try (Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                    if (rs.next()) {
                        dh.setMaDonHang(rs.getInt(1));
                    }
                }
            }

            // Thêm chi tiết đơn hàng
            try (PreparedStatement pstmt = conn.prepareStatement(sqlChiTiet)) {
                for (ChiTietDonHang ct : dh.getChiTietList()) {
                    pstmt.setInt(1, dh.getMaDonHang());
                    pstmt.setInt(2, ct.getMaSanPham());
                    pstmt.setInt(3, ct.getSoLuong());
                    pstmt.setDouble(4, ct.getDonGia());
                    pstmt.setDouble(5, ct.getThanhTien());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Lỗi rollback: " + ex.getMessage());
            }
            System.err.println("Lỗi thêm đơn hàng: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("FAILED ORDER CONTEXT: maDH=" + dh.getMaDH() + ", maKH=" + dh.getMaKhachHang()
                    + ", maND=" + dh.getMaNguoiDung() +
                    ", items=" + (dh.getChiTietList() != null ? dh.getChiTietList().size() : 0));
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Lỗi reset auto commit: " + e.getMessage());
            }
        }
        return false;
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    public boolean capNhatTrangThai(int maDonHang, String trangThai) {
        String sql = "UPDATE don_hang SET trang_thai = ? WHERE ma_don_hang = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, trangThai);
            pstmt.setInt(2, maDonHang);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật trạng thái: " + e.getMessage());
        }
        return false;
    }

    /**
     * Hủy đơn hàng
     */
    public boolean huyDonHang(int maDonHang) {
        return capNhatTrangThai(maDonHang, "da_huy");
    }

    /**
     * Lấy chi tiết đơn hàng
     */
    public List<ChiTietDonHang> layChiTiet(int maDonHang) {
        List<ChiTietDonHang> list = new ArrayList<>();
        String sql = """
                SELECT ct.*, sp.ten_san_pham, sp.don_vi_tinh
                FROM chi_tiet_don_hang ct
                LEFT JOIN san_pham sp ON ct.ma_san_pham = sp.ma_san_pham
                WHERE ct.ma_don_hang = ?
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maDonHang);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ChiTietDonHang ct = new ChiTietDonHang();
                ct.setMaChiTiet(rs.getInt("ma_chi_tiet"));
                ct.setMaDonHang(rs.getInt("ma_don_hang"));
                ct.setMaSanPham(rs.getInt("ma_san_pham"));
                ct.setTenSanPham(rs.getString("ten_san_pham"));
                ct.setDonViTinh(rs.getString("don_vi_tinh"));
                ct.setSoLuong(rs.getInt("so_luong"));
                ct.setDonGia(rs.getDouble("don_gia"));
                ct.setThanhTien(rs.getDouble("thanh_tien"));
                list.add(ct);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy chi tiết đơn hàng: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lấy mã đơn hàng tự động - đảm bảo không trùng
     */
    public String layMaDHTiepTheo() {
        String sql = "SELECT MAX(CAST(SUBSTR(ma_dh, 3) AS INTEGER)) AS max_ma FROM don_hang WHERE ma_dh LIKE 'DH%'";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int maxMa = rs.getInt("max_ma");
                String newCode = String.format("DH%03d", maxMa + 1);

                while (kiemTraMaDHTonTai(newCode)) {
                    maxMa++;
                    newCode = String.format("DH%03d", maxMa + 1);
                }
                return newCode;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy mã DH: " + e.getMessage());
        }
        return "DH001";
    }

    /**
     * Kiểm tra mã DH đã tồn tại chưa
     */
    public boolean kiemTraMaDHTonTai(String maDH) {
        String sql = "SELECT COUNT(*) FROM don_hang WHERE ma_dh = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maDH);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra mã DH: " + e.getMessage());
        }
        return false;
    }

    /**
     * Map ResultSet sang DonHang
     */
    private DonHang mapDonHang(ResultSet rs) throws SQLException {
        DonHang dh = new DonHang();
        dh.setMaDonHang(rs.getInt("ma_don_hang"));
        dh.setMaDH(rs.getString("ma_dh"));
        dh.setMaKhachHang(rs.getInt("ma_khach_hang"));
        dh.setTenKhachHang(rs.getString("ten_khach_hang"));
        dh.setMaNguoiDung(rs.getInt("ma_nguoi_dung"));
        dh.setTenNguoiDung(rs.getString("ten_nguoi_dung"));

        Timestamp ngayDat = rs.getTimestamp("ngay_dat");
        if (ngayDat != null) {
            dh.setNgayDat(ngayDat.toLocalDateTime());
        }

        dh.setTongTien(rs.getDouble("tong_tien"));
        dh.setGiamGia(rs.getDouble("giam_gia"));
        dh.setThanhTien(rs.getDouble("thanh_tien"));
        
        // Đảm bảo trạng thái không null, mặc định "cho_xu_ly"
        String trangThai = rs.getString("trang_thai");
        dh.setTrangThai(trangThai != null ? trangThai : "cho_xu_ly");
        
        dh.setGhiChu(rs.getString("ghi_chu"));
        return dh;
    }
}
