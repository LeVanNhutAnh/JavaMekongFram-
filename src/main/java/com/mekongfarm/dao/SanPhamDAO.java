package com.mekongfarm.dao;

import com.mekongfarm.config.CauHinhDatabase;
import com.mekongfarm.model.SanPham;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO Sản phẩm - Truy xuất dữ liệu nông sản
 */
public class SanPhamDAO {

    private final Connection conn;

    public SanPhamDAO() {
        this.conn = CauHinhDatabase.getInstance().getConnection();
    }

    /**
     * Lấy tất cả sản phẩm còn hoạt động
     */
    public List<SanPham> layTatCa() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT sp.*, lsp.ten_loai, tt.ten_tinh, ncc.tenNCC " +
                "FROM san_pham sp " +
                "LEFT JOIN loai_san_pham lsp ON sp.ma_loai = lsp.ma_loai " +
                "LEFT JOIN tinh_thanh tt ON sp.ma_tinh = tt.ma_tinh " +
                "LEFT JOIN NhaCungCap ncc ON sp.ma_ncc = ncc.maNCC " +
                "WHERE sp.trang_thai = 1 " +
                "ORDER BY sp.ten_san_pham";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapSanPham(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách sản phẩm: " + e.getMessage());
        }
        return list;
    }

    /**
     * Tìm theo ID
     */
    public SanPham timTheoId(int id) {
        String sql = "SELECT sp.*, lsp.ten_loai, tt.ten_tinh " +
                "FROM san_pham sp " +
                "LEFT JOIN loai_san_pham lsp ON sp.ma_loai = lsp.ma_loai " +
                "LEFT JOIN tinh_thanh tt ON sp.ma_tinh = tt.ma_tinh " +
                "WHERE sp.ma_san_pham = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapSanPham(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm sản phẩm: " + e.getMessage());
        }
        return null;
    }

    /**
     * Tìm theo mã sản phẩm
     */
    public SanPham timTheoMaSP(String maSP) {
        String sql = "SELECT sp.*, lsp.ten_loai, tt.ten_tinh " +
                "FROM san_pham sp " +
                "LEFT JOIN loai_san_pham lsp ON sp.ma_loai = lsp.ma_loai " +
                "LEFT JOIN tinh_thanh tt ON sp.ma_tinh = tt.ma_tinh " +
                "WHERE sp.ma_sp = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maSP);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapSanPham(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm sản phẩm: " + e.getMessage());
        }
        return null;
    }

    /**
     * Tìm kiếm theo tên
     */
    public List<SanPham> timTheoTen(String tuKhoa) {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT sp.*, lsp.ten_loai, tt.ten_tinh " +
                "FROM san_pham sp " +
                "LEFT JOIN loai_san_pham lsp ON sp.ma_loại = lsp.ma_loai " +
                "LEFT JOIN tinh_thanh tt ON sp.ma_tinh = tt.ma_tinh " +
                "WHERE sp.trang_thai = 1 AND sp.ten_san_pham LIKE ? " +
                "ORDER BY sp.ten_san_pham";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + tuKhoa + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapSanPham(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm kiếm sản phẩm: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lọc theo loại sản phẩm
     */
    public List<SanPham> locTheoLoai(int maLoai) {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT sp.*, lsp.ten_loai, tt.ten_tinh " +
                "FROM san_pham sp " +
                "LEFT JOIN loai_san_pham lsp ON sp.ma_loai = lsp.ma_loai " +
                "LEFT JOIN tinh_thanh tt ON sp.ma_tinh = tt.ma_tinh " +
                "WHERE sp.trang_thai = 1 AND sp.ma_loai = ? " +
                "ORDER BY sp.ten_san_pham";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maLoai);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapSanPham(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lọc sản phẩm: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lọc theo tỉnh
     */
    public List<SanPham> locTheoTinh(int maTinh) {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT sp.*, lsp.ten_loai, tt.ten_tinh " +
                "FROM san_pham sp " +
                "LEFT JOIN loai_san_pham lsp ON sp.ma_loai = lsp.ma_loai " +
                "LEFT JOIN tinh_thanh tt ON sp.ma_tinh = tt.ma_tinh " +
                "WHERE sp.trang_thai = 1 AND sp.ma_tinh = ? " +
                "ORDER BY sp.ten_san_pham";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maTinh);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapSanPham(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lọc sản phẩm: " + e.getMessage());
        }
        return list;
    }

    /**
     * Thêm sản phẩm mới
     */
    public boolean them(SanPham sp) {
        String sql = "INSERT INTO san_pham (ma_sp, ten_san_pham, ma_loai, ma_tinh, ma_ncc, don_gia, so_luong_ton, don_vi_tinh, mo_ta, hinh_anh) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sp.getMaSP());
            pstmt.setString(2, sp.getTenSanPham());
            pstmt.setInt(3, sp.getMaLoai());
            pstmt.setInt(4, sp.getMaTinh());
            if (sp.getMaNCC() > 0) {
                pstmt.setInt(5, sp.getMaNCC());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }
            pstmt.setDouble(6, sp.getDonGia());
            pstmt.setInt(7, sp.getSoLuongTon());
            pstmt.setString(8, sp.getDonViTinh());
            pstmt.setString(9, sp.getMoTa());
            pstmt.setString(10, sp.getHinhAnh());

            System.out.println("DEBUG: Thêm SP - maSP=" + sp.getMaSP() + ", ten=" + sp.getTenSanPham() +
                    ", maLoai=" + sp.getMaLoai() + ", maTinh=" + sp.getMaTinh());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                // Get last insert ID using SQLite function
                try (Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                    if (rs.next()) {
                        sp.setMaSanPham(rs.getInt(1));
                    }
                }
                System.out.println("DEBUG: Thêm SP thành công, ID=" + sp.getMaSanPham());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi thêm sản phẩm: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật sản phẩm
     */
    public boolean capNhat(SanPham sp) {
        String sql = "UPDATE san_pham SET " +
                "ten_san_pham = ?, ma_loai = ?, ma_tinh = ?, ma_ncc = ?, don_gia = ?, " +
                "so_luong_ton = ?, don_vi_tinh = ?, mo_ta = ?, hinh_anh = ? " +
                "WHERE ma_san_pham = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sp.getTenSanPham());
            pstmt.setInt(2, sp.getMaLoai());
            pstmt.setInt(3, sp.getMaTinh());
            if (sp.getMaNCC() > 0) {
                pstmt.setInt(4, sp.getMaNCC());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }
            pstmt.setDouble(5, sp.getDonGia());
            pstmt.setInt(6, sp.getSoLuongTon());
            pstmt.setString(7, sp.getDonViTinh());
            pstmt.setString(8, sp.getMoTa());
            pstmt.setString(9, sp.getHinhAnh());
            pstmt.setInt(10, sp.getMaSanPham());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật sản phẩm: " + e.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật số lượng tồn
     */
    public boolean capNhatSoLuong(int maSanPham, int soLuongMoi) {
        String sql = "UPDATE san_pham SET so_luong_ton = ? WHERE ma_san_pham = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, soLuongMoi);
            pstmt.setInt(2, maSanPham);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật số lượng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Giảm số lượng tồn khi bán
     */
    public boolean giamSoLuong(int maSanPham, int soLuongGiam) {
        String sql = "UPDATE san_pham SET so_luong_ton = so_luong_ton - ? WHERE ma_san_pham = ? AND so_luong_ton >= ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, soLuongGiam);
            pstmt.setInt(2, maSanPham);
            pstmt.setInt(3, soLuongGiam);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi giảm số lượng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật tồn kho - Tăng số lượng khi nhập hàng từ NCC
     */
    public boolean capNhatTonKho(int maSanPham, int soLuongThem) {
        String sql = "UPDATE san_pham SET so_luong_ton = so_luong_ton + ? WHERE ma_san_pham = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, soLuongThem);
            pstmt.setInt(2, maSanPham);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật tồn kho: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa sản phẩm (soft delete)
     */
    public boolean xoa(int maSanPham) {
        String sql = "UPDATE san_pham SET trang_thai = 0 WHERE ma_san_pham = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maSanPham);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa sản phẩm: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy danh sách sản phẩm đã xóa (thùng rác)
     */
    public List<SanPham> layDaXoa() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT sp.*, lsp.ten_loai, tt.ten_tinh " +
                "FROM san_pham sp " +
                "LEFT JOIN loai_san_pham lsp ON sp.ma_loai = lsp.ma_loai " +
                "LEFT JOIN tinh_thanh tt ON sp.ma_tinh = tt.ma_tinh " +
                "WHERE sp.trang_thai = 0 " +
                "ORDER BY sp.ten_san_pham";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapSanPham(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách đã xóa: " + e.getMessage());
        }
        return list;
    }

    /**
     * Khôi phục sản phẩm từ thùng rác
     */
    public boolean khoiPhuc(int maSanPham) {
        String sql = "UPDATE san_pham SET trang_thai = 1 WHERE ma_san_pham = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maSanPham);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khôi phục sản phẩm: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa vĩnh viễn sản phẩm (hard delete)
     */
    public boolean xoaVinhVien(int maSanPham) {
        String sql = "DELETE FROM san_pham WHERE ma_san_pham = ? AND trang_thai = 0";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maSanPham);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa vĩnh viễn: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy mã sản phẩm tự động - đảm bảo không trùng
     */
    public String layMaSPTiepTheo() {
        String sql = "SELECT MAX(CAST(SUBSTR(ma_sp, 3) AS INTEGER)) AS max_ma FROM san_pham WHERE ma_sp LIKE 'SP%'";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int maxMa = rs.getInt("max_ma");
                String newCode = String.format("SP%03d", maxMa + 1);

                while (kiemTraMaSPTonTai(newCode)) {
                    maxMa++;
                    newCode = String.format("SP%03d", maxMa + 1);
                }
                return newCode;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy mã SP: " + e.getMessage());
        }
        return "SP001";
    }

    /**
     * Kiểm tra mã SP đã tồn tại chưa
     */
    public boolean kiemTraMaSPTonTai(String maSP) {
        String sql = "SELECT COUNT(*) FROM san_pham WHERE ma_sp = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maSP);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra mã SP: " + e.getMessage());
        }
        return false;
    }

    /**
     * Đếm sản phẩm theo loại
     */
    public int demTheoLoai(int maLoai) {
        String sql = "SELECT COUNT(*) FROM san_pham WHERE ma_loai = ? AND trang_thai = 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maLoai);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đếm sản phẩm: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Map ResultSet sang SanPham
     */
    private SanPham mapSanPham(ResultSet rs) throws SQLException {
        SanPham sp = new SanPham();
        sp.setMaSanPham(rs.getInt("ma_san_pham"));
        sp.setMaSP(rs.getString("ma_sp"));
        sp.setTenSanPham(rs.getString("ten_san_pham"));
        sp.setMaLoai(rs.getInt("ma_loai"));
        sp.setTenLoai(rs.getString("ten_loai"));
        sp.setMaTinh(rs.getInt("ma_tinh"));
        sp.setTenTinh(rs.getString("ten_tinh"));

        // Nhà cung cấp
        try {
            sp.setMaNCC(rs.getInt("ma_ncc"));
            sp.setTenNCC(rs.getString("tenNCC"));
        } catch (SQLException e) {
            // Column không tồn tại - bỏ qua
        }

        sp.setDonGia(rs.getDouble("don_gia"));
        sp.setSoLuongTon(rs.getInt("so_luong_ton"));
        sp.setDonViTinh(rs.getString("don_vi_tinh"));
        sp.setMoTa(rs.getString("mo_ta"));
        sp.setHinhAnh(rs.getString("hinh_anh"));
        sp.setTrangThai(rs.getInt("trang_thai") == 1);
        return sp;
    }
}
