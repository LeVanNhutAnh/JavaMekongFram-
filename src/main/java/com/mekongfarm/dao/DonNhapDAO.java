package com.mekongfarm.dao;

import com.mekongfarm.config.CauHinhDatabase;
import com.mekongfarm.model.DonNhap;
import com.mekongfarm.model.ChiTietDonNhap;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO xử lý Đơn nhập hàng từ Nhà cung cấp
 */
public class DonNhapDAO {

    private Connection conn;

    public DonNhapDAO() {
        conn = CauHinhDatabase.getInstance().getConnection();
        taoTableNeuChuaCo();
    }

    private void taoTableNeuChuaCo() {
        try (Statement stmt = conn.createStatement()) {
            // Bảng đơn nhập
            stmt.execute("CREATE TABLE IF NOT EXISTS don_nhap (" +
                    "ma_don_nhap INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ma_don TEXT UNIQUE NOT NULL," +
                    "ma_ncc INTEGER NOT NULL," +
                    "ma_nguoi_dung INTEGER," +
                    "ngay_nhap DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "tong_tien REAL DEFAULT 0," +
                    "giam_gia REAL DEFAULT 0," +
                    "thanh_tien REAL DEFAULT 0," +
                    "trang_thai TEXT DEFAULT 'cho_duyet'," +
                    "da_nhap_kho INTEGER DEFAULT 0," +
                    "ghi_chu TEXT)");

            // Bảng chi tiết đơn nhập
            stmt.execute("CREATE TABLE IF NOT EXISTS chi_tiet_don_nhap (" +
                    "ma_chi_tiet INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ma_don_nhap INTEGER NOT NULL," +
                    "ma_san_pham INTEGER NOT NULL," +
                    "so_luong INTEGER NOT NULL," +
                    "don_gia REAL NOT NULL," +
                    "thanh_tien REAL NOT NULL)");

        } catch (SQLException e) {
            System.err.println("Lỗi tạo bảng don_nhap: " + e.getMessage());
        }
    }

    // Lấy mã đơn tiếp theo
    public String layMaDonTiepTheo() {
        String prefix = "DN";
        String sql = "SELECT ma_don FROM don_nhap WHERE ma_don LIKE ? ORDER BY ma_don DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String lastCode = rs.getString("ma_don");
                int num = Integer.parseInt(lastCode.substring(2)) + 1;
                return prefix + String.format("%03d", num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prefix + "001";
    }

    // Thêm đơn nhập mới
    public int them(DonNhap dn) {
        String sql = "INSERT INTO don_nhap (ma_don, ma_ncc, ma_nguoi_dung, ngay_nhap, tong_tien, giam_gia, thanh_tien, trang_thai, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dn.getMaDon());
            ps.setInt(2, dn.getMaNCC());
            ps.setInt(3, dn.getMaNguoiDung());
            ps.setString(4, dn.getNgayNhap().toString());
            ps.setDouble(5, dn.getTongTien());
            ps.setDouble(6, dn.getGiamGia());
            ps.setDouble(7, dn.getThanhTien());
            ps.setString(8, dn.getTrangThai());
            ps.setString(9, dn.getGhiChu());
            ps.executeUpdate();

            // Lấy ID vừa insert
            ResultSet rs = conn.createStatement().executeQuery("SELECT last_insert_rowid()");
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Thêm chi tiết đơn nhập
    public boolean themChiTiet(ChiTietDonNhap ct) {
        String sql = "INSERT INTO chi_tiet_don_nhap (ma_don_nhap, ma_san_pham, so_luong, don_gia, thanh_tien) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getMaDonNhap());
            ps.setInt(2, ct.getMaSanPham());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getDonGia());
            ps.setDouble(5, ct.getThanhTien());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật trạng thái đơn nhập
    public boolean capNhatTrangThai(int maDonNhap, String trangThai) {
        String sql = "UPDATE don_nhap SET trang_thai = ? WHERE ma_don_nhap = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, maDonNhap);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Đánh dấu đã nhập kho
    public boolean danhDauDaNhapKho(int maDonNhap) {
        String sql = "UPDATE don_nhap SET da_nhap_kho = 1, trang_thai = 'da_nhap' WHERE ma_don_nhap = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDonNhap);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy tất cả đơn nhập
    public List<DonNhap> layTatCa() {
        List<DonNhap> list = new ArrayList<>();
        String sql = "SELECT dn.*, ncc.tenNCC, nd.ho_ten as ten_nguoi_dung " +
                "FROM don_nhap dn " +
                "LEFT JOIN NhaCungCap ncc ON dn.ma_ncc = ncc.maNCC " +
                "LEFT JOIN nguoi_dung nd ON dn.ma_nguoi_dung = nd.ma_nguoi_dung " +
                "ORDER BY dn.ngay_nhap DESC";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy đơn nhập theo NCC
    public List<DonNhap> layTheoNCC(int maNCC) {
        List<DonNhap> list = new ArrayList<>();
        String sql = "SELECT dn.*, ncc.tenNCC, nd.ho_ten as ten_nguoi_dung " +
                "FROM don_nhap dn " +
                "LEFT JOIN NhaCungCap ncc ON dn.ma_ncc = ncc.maNCC " +
                "LEFT JOIN nguoi_dung nd ON dn.ma_nguoi_dung = nd.ma_nguoi_dung " +
                "WHERE dn.ma_ncc = ? " +
                "ORDER BY dn.ngay_nhap DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNCC);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy chi tiết đơn nhập
    public List<ChiTietDonNhap> layChiTiet(int maDonNhap) {
        List<ChiTietDonNhap> list = new ArrayList<>();
        String sql = "SELECT ct.*, sp.ten_san_pham, sp.don_vi_tinh " +
                "FROM chi_tiet_don_nhap ct " +
                "LEFT JOIN san_pham sp ON ct.ma_san_pham = sp.ma_san_pham " +
                "WHERE ct.ma_don_nhap = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDonNhap);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietDonNhap ct = new ChiTietDonNhap();
                ct.setMaChiTiet(rs.getInt("ma_chi_tiet"));
                ct.setMaDonNhap(rs.getInt("ma_don_nhap"));
                ct.setMaSanPham(rs.getInt("ma_san_pham"));
                ct.setTenSanPham(rs.getString("ten_san_pham"));
                ct.setDonViTinh(rs.getString("don_vi_tinh"));
                ct.setSoLuong(rs.getInt("so_luong"));
                ct.setDonGia(rs.getDouble("don_gia"));
                ct.setThanhTien(rs.getDouble("thanh_tien"));
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tính tổng tiền nhập theo NCC
    public double tongTienNhapTheoNCC(int maNCC) {
        String sql = "SELECT SUM(thanh_tien) as tong FROM don_nhap WHERE ma_ncc = ? AND trang_thai != 'da_huy'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNCC);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("tong");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Xóa đơn nhập
    public boolean xoa(int maDonNhap) {
        try {
            // Xóa chi tiết trước
            PreparedStatement ps1 = conn.prepareStatement("DELETE FROM chi_tiet_don_nhap WHERE ma_don_nhap = ?");
            ps1.setInt(1, maDonNhap);
            ps1.executeUpdate();

            // Xóa đơn
            PreparedStatement ps2 = conn.prepareStatement("DELETE FROM don_nhap WHERE ma_don_nhap = ?");
            ps2.setInt(1, maDonNhap);
            return ps2.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private DonNhap mapRow(ResultSet rs) throws SQLException {
        DonNhap dn = new DonNhap();
        dn.setMaDonNhap(rs.getInt("ma_don_nhap"));
        dn.setMaDon(rs.getString("ma_don"));
        dn.setMaNCC(rs.getInt("ma_ncc"));
        dn.setTenNCC(rs.getString("tenNCC"));
        dn.setMaNguoiDung(rs.getInt("ma_nguoi_dung"));
        try {
            dn.setTenNguoiDung(rs.getString("ten_nguoi_dung"));
        } catch (Exception e) {
        }

        String ngayStr = rs.getString("ngay_nhap");
        if (ngayStr != null && !ngayStr.isEmpty()) {
            try {
                dn.setNgayNhap(LocalDateTime.parse(ngayStr.replace(" ", "T")));
            } catch (Exception e) {
                dn.setNgayNhap(LocalDateTime.now());
            }
        }

        dn.setTongTien(rs.getDouble("tong_tien"));
        dn.setGiamGia(rs.getDouble("giam_gia"));
        dn.setThanhTien(rs.getDouble("thanh_tien"));
        dn.setTrangThai(rs.getString("trang_thai"));
        dn.setDaNhapKho(rs.getInt("da_nhap_kho") == 1);
        dn.setGhiChu(rs.getString("ghi_chu"));
        return dn;
    }
}
