package com.mekongfarm.config;

import com.mekongfarm.util.AppLogger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Cấu hình kết nối cơ sở dữ liệu SQLite
 * Quản lý kết nối và khởi tạo database
 */
public class CauHinhDatabase {

    private static final String DB_URL = "jdbc:sqlite:mekongfarm.db";
    private static CauHinhDatabase instance;
    private Connection connection;

    private CauHinhDatabase() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            AppLogger.info("Kết nối database thành công!");
        } catch (ClassNotFoundException | SQLException e) {
            AppLogger.error("Lỗi kết nối database", e);
        }
    }

    /**
     * Singleton pattern - lấy instance duy nhất
     */
    public static synchronized CauHinhDatabase getInstance() {
        if (instance == null) {
            instance = new CauHinhDatabase();
        }
        return instance;
    }

    /**
     * Lấy kết nối database
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            AppLogger.error("Lỗi lấy kết nối", e);
        }
        return connection;
    }

    /**
     * Khởi tạo database
     */
    public void khoiTaoDatabase() {
        try {
            // Tạo bảng ngay trong code để tránh lỗi resource
            taoTableTrucTiep();
            AppLogger.info("Tạo bảng trực tiếp thành công!");

            // Skip schema.sql vì tables đã được tạo bởi các DAOs
            // String schema = docFileResource("/database/schema.sql");
            // if (!schema.isEmpty()) {
            //     thucThiSQL(schema);
            // }
            System.out.println("✓ Khởi tạo schema thành công!");

            // Thêm dữ liệu mẫu nếu chưa có
            if (kiemTraDuLieuTrong()) {
                themDuLieuMauTrucTiep();
                // Load dữ liệu mẫu đầy đủ
                String duLieuMauDayDu = docFileResource("/database/du_lieu_mau_day_du.sql");
                if (!duLieuMauDayDu.isEmpty()) {
                    thucThiSQL(duLieuMauDayDu);
                    AppLogger.info("Thêm dữ liệu mẫu đầy đủ thành công!");
                } else {
                    // Fallback to basic data
                    String duLieuMau = docFileResource("/database/du_lieu_mau.sql");
                    if (!duLieuMau.isEmpty()) {
                        thucThiSQL(duLieuMau);
                    }
                }
                System.out.println("✓ Thêm dữ liệu mẫu thành công!");
            } else {
                // Luôn check và thêm data truy xuất nếu table trống
                themDuLieuTruyXuatNeuChua();
            }
        } catch (Exception e) {
                AppLogger.error("Lỗi khởi tạo database", e);
        }
    }

    /**
     * Tạo các bảng cơ bản trực tiếp trong code
     */
    private void taoTableTrucTiep() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS nguoi_dung (ma_nguoi_dung INTEGER PRIMARY KEY AUTOINCREMENT, ten_dang_nhap TEXT UNIQUE, mat_khau TEXT, ho_ten TEXT, vai_tro TEXT DEFAULT 'nhan_vien', ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP, trang_thai INTEGER DEFAULT 1)");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS tinh_thanh (ma_tinh INTEGER PRIMARY KEY AUTOINCREMENT, ten_tinh TEXT, vung_mien TEXT)");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS loai_san_pham (ma_loai INTEGER PRIMARY KEY AUTOINCREMENT, ten_loai TEXT, mo_ta TEXT)");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS NhaCungCap (maNCC INTEGER PRIMARY KEY AUTOINCREMENT, tenNCC TEXT NOT NULL, dienThoai TEXT, diaChi TEXT, email TEXT, loaiSanPham TEXT, danhGia INTEGER DEFAULT 5, ghiChu TEXT, ngayTao DATETIME DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS san_pham (ma_san_pham INTEGER PRIMARY KEY AUTOINCREMENT, ma_sp TEXT UNIQUE, ten_san_pham TEXT, ma_loai INTEGER, ma_tinh INTEGER, ma_ncc INTEGER, don_gia REAL, so_luong_ton INTEGER DEFAULT 0, don_vi_tinh TEXT DEFAULT 'kg', mo_ta TEXT, hinh_anh TEXT, ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP, trang_thai INTEGER DEFAULT 1)");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS khach_hang (ma_khach_hang INTEGER PRIMARY KEY AUTOINCREMENT, ma_kh TEXT UNIQUE, ho_ten TEXT, dia_chi TEXT, so_dien_thoai TEXT, email TEXT, loai_khach TEXT DEFAULT 'le', ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS don_hang (ma_don_hang INTEGER PRIMARY KEY AUTOINCREMENT, ma_dh TEXT UNIQUE, ma_khach_hang INTEGER, ma_nguoi_dung INTEGER, ngay_dat DATETIME DEFAULT CURRENT_TIMESTAMP, tong_tien REAL DEFAULT 0, giam_gia REAL DEFAULT 0, thanh_tien REAL DEFAULT 0, trang_thai TEXT DEFAULT 'cho_xu_ly', ghi_chu TEXT)");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS chi_tiet_don_hang (ma_chi_tiet INTEGER PRIMARY KEY AUTOINCREMENT, ma_don_hang INTEGER, ma_san_pham INTEGER, so_luong INTEGER, don_gia REAL, thanh_tien REAL)");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS don_nhap (ma_don_nhap INTEGER PRIMARY KEY AUTOINCREMENT, ma_don TEXT UNIQUE NOT NULL, ma_ncc INTEGER NOT NULL, ma_nguoi_dung INTEGER, ngay_nhap DATETIME DEFAULT CURRENT_TIMESTAMP, tong_tien REAL DEFAULT 0, giam_gia REAL DEFAULT 0, thanh_tien REAL DEFAULT 0, trang_thai TEXT DEFAULT 'cho_duyet', da_nhap_kho INTEGER DEFAULT 0, ghi_chu TEXT)");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS chi_tiet_don_nhap (ma_chi_tiet INTEGER PRIMARY KEY AUTOINCREMENT, ma_don_nhap INTEGER NOT NULL, ma_san_pham INTEGER NOT NULL, so_luong INTEGER NOT NULL, don_gia REAL NOT NULL, thanh_tien REAL NOT NULL)");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS CongNo (maCongNo INTEGER PRIMARY KEY AUTOINCREMENT, loaiCongNo TEXT DEFAULT 'phai_thu', maKH INTEGER, maNCC INTEGER, maDH INTEGER, soTien REAL, ngayPhatSinh TEXT, hanThanhToan TEXT, trangThai TEXT DEFAULT 'Chưa thanh toán', daThanhToan REAL DEFAULT 0, ghiChu TEXT)");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS truy_xuat_nguon_goc (ma_truy_xuat INTEGER PRIMARY KEY AUTOINCREMENT, ma_san_pham INTEGER, so_lo TEXT, ngay_thu_hoach DATE, dia_chi_san_xuat TEXT, ten_nong_dan TEXT, chung_nhan TEXT, ngay_san_xuat DATE, han_su_dung DATE, ma_qr TEXT)");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS bao_gia (ma_bao_gia INTEGER PRIMARY KEY AUTOINCREMENT, ma_san_pham INTEGER NOT NULL, ma_ncc INTEGER NOT NULL, don_gia REAL NOT NULL, so_luong_toi_thieu INTEGER DEFAULT 0, thoi_gian_giao INTEGER DEFAULT 0, dieu_kien TEXT, ngay_bao_gia DATE DEFAULT CURRENT_DATE, han_hieu_luc DATE, trang_thai TEXT DEFAULT 'hieu_luc', ghi_chu TEXT)");
            AppLogger.info("Tạo bảng trực tiếp thành công!");
        } catch (SQLException e) {
            AppLogger.error("Lỗi tạo bảng", e);
        }
    }

    /**
     * Thêm user admin và dữ liệu cơ bản
     */
    private void themDuLieuMauTrucTiep() {
        try (Statement stmt = connection.createStatement()) {
            // Hash password với BCrypt
            String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw("admin123", org.mindrot.jbcrypt.BCrypt.gensalt());
            stmt.execute("INSERT OR IGNORE INTO nguoi_dung (ten_dang_nhap, mat_khau, ho_ten, vai_tro) VALUES ('admin', '" + hashedPassword + "', 'Quản trị viên', 'quan_tri')");
            stmt.execute("INSERT OR IGNORE INTO nguoi_dung (ten_dang_nhap, mat_khau, ho_ten, vai_tro) VALUES ('nhanvien01', '123456', 'Nguyễn Văn An', 'nhan_vien')");
            
            // 13 tỉnh ĐBSCL
            stmt.execute("INSERT OR IGNORE INTO tinh_thanh (ten_tinh, vung_mien) VALUES ('Long An', 'ĐBSCL'), ('Tiền Giang', 'ĐBSCL'), ('Bến Tre', 'ĐBSCL'), ('Trà Vinh', 'ĐBSCL'), ('Vĩnh Long', 'ĐBSCL'), ('Đồng Tháp', 'ĐBSCL'), ('An Giang', 'ĐBSCL'), ('Kiên Giang', 'ĐBSCL'), ('Hậu Giang', 'ĐBSCL'), ('Sóc Trăng', 'ĐBSCL'), ('Bạc Liêu', 'ĐBSCL'), ('Cà Mau', 'ĐBSCL'), ('Cần Thơ', 'ĐBSCL')");
            
            // Loại sản phẩm
            stmt.execute("INSERT OR IGNORE INTO loai_san_pham (ten_loai, mo_ta) VALUES ('Lúa gạo', 'Các loại gạo'), ('Trái cây', 'Trái cây tươi'), ('Thủy sản', 'Cá tôm'), ('Rau củ', 'Rau củ quả'), ('Đặc sản', 'Đặc sản địa phương')");
            
            // Nhà cung cấp (10 NCC)
            stmt.execute("INSERT OR IGNORE INTO NhaCungCap (maNCC, tenNCC, dienThoai, diaChi, loaiSanPham, danhGia) VALUES " +
                    "(1, 'Công ty Nông Sản Sóc Trăng', '0292-3821456', 'Sóc Trăng', 'Lúa gạo', 5), " +
                    "(2, 'HTX Xoài Tiền Giang', '0273-3955123', 'Tiền Giang', 'Trái cây', 5), " +
                    "(3, 'Công ty Vĩnh Hoàn', '0277-3825888', 'Đồng Tháp', 'Thủy sản', 5), " +
                    "(4, 'HTX Tôm Cà Mau', '0290-3831234', 'Cà Mau', 'Thủy sản', 4), " +
                    "(5, 'Nước Mắm Phú Quốc', '0297-3846789', 'Kiên Giang', 'Đặc sản', 5)");
            
            // Sản phẩm (20 sản phẩm)
            stmt.execute("INSERT OR IGNORE INTO san_pham (ma_sp, ten_san_pham, ma_loai, ma_tinh, don_gia, so_luong_ton, don_vi_tinh, mo_ta) VALUES " +
                    "('SP001', 'Gạo ST25', 1, 10, 45000, 500, 'kg', 'Gạo ngon nhất thế giới'), " +
                    "('SP002', 'Gạo Jasmine', 1, 7, 25000, 800, 'kg', 'Gạo thơm Jasmine An Giang'), " +
                    "('SP003', 'Gạo Nàng Hoa', 1, 6, 28000, 600, 'kg', 'Gạo Nàng Hoa Đồng Tháp'), " +
                    "('SP004', 'Xoài cát Hòa Lộc', 2, 2, 65000, 200, 'kg', 'Đặc sản Tiền Giang'), " +
                    "('SP005', 'Sầu riêng Ri6', 2, 2, 120000, 150, 'kg', 'Sầu riêng Tiền Giang'), " +
                    "('SP006', 'Bưởi da xanh', 2, 3, 35000, 300, 'kg', 'Bưởi Bến Tre'), " +
                    "('SP007', 'Bưởi Năm Roi', 2, 5, 30000, 400, 'kg', 'Bưởi Vĩnh Long'), " +
                    "('SP008', 'Thanh long ruột đỏ', 2, 1, 25000, 350, 'kg', 'Thanh long Long An'), " +
                    "('SP015', 'Cá tra fillet', 3, 6, 85000, 400, 'kg', 'Cá tra Đồng Tháp'), " +
                    "('SP016', 'Tôm sú', 3, 12, 250000, 100, 'kg', 'Tôm sú Cà Mau')");
            
            // Khách hàng (10 khách hàng)
            stmt.execute("INSERT OR IGNORE INTO khach_hang (ma_kh, ho_ten, dia_chi, so_dien_thoai, email, loai_khach) VALUES " +
                    "('KH001', 'Nguyễn Thị Mai', '123 Nguyễn Huệ, Q.1, TP.HCM', '0901234567', 'mai@email.com', 'le'), " +
                    "('KH002', 'Trần Văn Hùng', '456 Lê Lợi, Q.3, TP.HCM', '0912345678', 'hung@email.com', 'si'), " +
                    "('KH003', 'Lê Thị Hồng', '789 CMT8, Cần Thơ', '0923456789', 'hong@email.com', 'le'), " +
                    "('KH004', 'Phạm Văn Đức', '321 Đường 30/4, Vĩnh Long', '0934567890', 'duc@email.com', 'si'), " +
                    "('KH005', 'Huỳnh Thị Lan', '654 Trần Hưng Đạo, Mỹ Tho', '0945678901', 'lan@email.com', 'le')");
            
            // Đơn hàng (5 đơn hàng)
            stmt.execute("INSERT OR IGNORE INTO don_hang (ma_dh, ma_khach_hang, ma_nguoi_dung, ngay_dat, tong_tien, thanh_tien, trang_thai) VALUES " +
                    "('DH001', 1, 1, '2024-11-18 08:30:00', 500000, 500000, 'da_giao'), " +
                    "('DH002', 2, 1, '2024-11-19 09:15:00', 750000, 750000, 'da_giao'), " +
                    "('DH003', 3, 1, '2024-11-20 14:20:00', 1200000, 1200000, 'da_giao'), " +
                    "('DH004', 4, 1, '2024-12-15 10:45:00', 2500000, 2500000, 'dang_giao'), " +
                    "('DH005', 5, 1, '2024-12-20 11:00:00', 850000, 850000, 'cho_xu_ly')");
            
            // Chi tiết đơn hàng
            stmt.execute("INSERT OR IGNORE INTO chi_tiet_don_hang (ma_don_hang, ma_san_pham, so_luong, don_gia, thanh_tien) VALUES " +
                    "(1, 1, 10, 45000, 450000), (1, 8, 2, 25000, 50000), " +
                    "(2, 4, 5, 65000, 325000), (2, 6, 10, 35000, 350000), " +
                    "(3, 5, 10, 120000, 1200000), " +
                    "(4, 2, 50, 25000, 1250000), (4, 3, 30, 28000, 840000), " +
                    "(5, 15, 10, 85000, 850000)");
            
            // Đơn nhập
            stmt.execute("INSERT OR IGNORE INTO don_nhap (ma_don_nhap, ma_don, ma_ncc, ma_nguoi_dung, ngay_nhap, tong_tien, trang_thai) VALUES " +
                    "(1, 'DN001', 1, 1, '2024-11-15 07:00:00', 22500000, 'hoan_thanh'), " +
                    "(2, 'DN002', 2, 1, '2024-11-20 08:30:00', 13000000, 'hoan_thanh'), " +
                    "(3, 'DN003', 3, 1, '2024-12-01 09:00:00', 34000000, 'hoan_thanh')");
            
            // Chi tiết đơn nhập
            stmt.execute("INSERT OR IGNORE INTO chi_tiet_don_nhap (ma_don_nhap, ma_san_pham, so_luong, don_gia, thanh_tien) VALUES " +
                    "(1, 1, 500, 45000, 22500000), " +
                    "(2, 4, 200, 65000, 13000000), " +
                    "(3, 15, 400, 85000, 34000000)");
            
            // Công nợ
            stmt.execute("INSERT OR IGNORE INTO CongNo (loaiCongNo, maKH, maNCC, soTien, ngayPhatSinh, hanThanhToan, trangThai, daThanhToan) VALUES " +
                    "('phai_thu', 2, NULL, 750000, '2024-11-19 09:15:00', '2024-12-19 23:59:59', 'Chưa thanh toán', 0), " +
                    "('phai_thu', 4, NULL, 2500000, '2024-12-15 10:45:00', '2025-01-15 23:59:59', 'Chưa thanh toán', 0), " +
                    "('phai_tra', NULL, 1, 22500000, '2024-11-15 07:00:00', '2024-12-15 23:59:59', 'Đã thanh toán', 22500000), " +
                    "('phai_tra', NULL, 3, 34000000, '2024-12-01 09:00:00', '2025-01-01 23:59:59', 'Chưa thanh toán', 0)");
            
            // Truy xuất nguồn gốc - INSERT OR REPLACE để đảm bảo có dữ liệu
            stmt.execute("INSERT OR REPLACE INTO truy_xuat_nguon_goc (ma_truy_xuat, ma_san_pham, so_lo, ngay_thu_hoach, dia_chi_san_xuat, ten_nong_dan, chung_nhan, ngay_san_xuat, han_su_dung) VALUES " +
                    "(1, 1, 'LO-ST25-001', '2024-11-15', 'Xã Tân Hưng, Huyện Long Phú, Sóc Trăng', 'Hồ Quang Cua', 'VietGAP, GlobalGAP', '2024-11-20', '2025-11-20'), " +
                    "(2, 4, 'LO-XOAI-001', '2024-12-01', 'Xã Hòa Lộc, Huyện Cái Bè, Tiền Giang', 'Nguyễn Văn Thành', 'VietGAP', '2024-12-02', '2024-12-15'), " +
                    "(3, 15, 'LO-CATRA-001', '2024-12-10', 'Huyện Hồng Ngự, Đồng Tháp', 'Công ty Vĩnh Hoàn', 'ASC, GlobalGAP', '2024-12-11', '2025-06-11')");
            
            // Verify data inserted
            ResultSet checkRs = stmt.executeQuery("SELECT COUNT(*) FROM truy_xuat_nguon_goc");
            if (checkRs.next()) {
                AppLogger.info("Thêm dữ liệu mẫu thành công! Tổng truy xuất: " + checkRs.getInt(1));
            }
        } catch (SQLException e) {
            AppLogger.error("Lỗi thêm dữ liệu", e);
        }
    }

    /**
     * Đọc file SQL từ resources
     */
    private String docFileResource(String duongDan) {
        try {
            InputStream is = getClass().getResourceAsStream(duongDan);
            if (is == null) {
                System.err.println("✗ Không tìm thấy resource: " + duongDan);
                // Thử đường dẫn khác
                is = CauHinhDatabase.class.getClassLoader().getResourceAsStream(duongDan.substring(1));
            }
            if (is == null) {
                System.err.println("✗ Vẫn không tìm thấy resource!");
                return "";
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String content = reader.lines().collect(Collectors.joining("\n"));
                System.out.println("✓ Đọc file " + duongDan + " (" + content.length() + " bytes)");
                return content;
            }
        } catch (Exception e) {
            System.err.println("✗ Lỗi đọc file: " + duongDan + " - " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Thực thi câu lệnh SQL
     */
    private void thucThiSQL(String sql) {
        if (sql == null || sql.isEmpty()) {
            System.err.println("✗ SQL rỗng!");
            return;
        }
        try (Statement stmt = connection.createStatement()) {
            // Tách các câu lệnh SQL (bỏ qua ; trong string)
            String[] cacCauLenh = sql.split(";(?=(?:[^']*'[^']*')*[^']*$)");
            int count = 0;
            for (String cauLenh : cacCauLenh) {
                String cauLenhTrim = cauLenh.trim();
                // Bỏ qua dòng trống và comment
                if (!cauLenhTrim.isEmpty() && !cauLenhTrim.startsWith("--")) {
                    try {
                        stmt.execute(cauLenhTrim);
                        count++;
                    } catch (SQLException ex) {
                        // Ignore duplicate errors when re-running
                        if (!ex.getMessage().contains("already exists") &&
                                !ex.getMessage().contains("UNIQUE constraint failed")) {
                            System.err.println("SQL Error: " + ex.getMessage());
                        }
                    }
                }
            }
            System.out.println("✓ Đã thực thi " + count + " câu lệnh SQL");
        } catch (SQLException e) {
            System.err.println("✗ Lỗi thực thi SQL: " + e.getMessage());
        }
    }

    /**
     * Kiểm tra database có dữ liệu chưa
     */
    private boolean kiemTraDuLieuTrong() {
        try (Statement stmt = connection.createStatement()) {
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM nguoi_dung");
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            return true; // Nếu lỗi thì coi như chưa có dữ liệu
        }
        return true;
    }

    private void themDuLieuTruyXuatNeuChua() {
        try (Statement stmt = connection.createStatement()) {
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM truy_xuat_nguon_goc");
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("▶ Đang thêm dữ liệu truy xuất...");
                stmt.execute(
                        "INSERT INTO truy_xuat_nguon_goc (ma_truy_xuat, ma_san_pham, so_lo, ngay_thu_hoach, dia_chi_san_xuat, ten_nong_dan, chung_nhan, ngay_san_xuat, han_su_dung) VALUES (1, 1, 'LO-ST25-001', '2024-11-15', 'Xã Tân Hưng, Huyện Long Phú, Sóc Trăng', 'Hồ Quang Cua', 'VietGAP, GlobalGAP', '2024-11-20', '2025-11-20')");
                stmt.execute(
                        "INSERT INTO truy_xuat_nguon_goc (ma_truy_xuat, ma_san_pham, so_lo, ngay_thu_hoach, dia_chi_san_xuat, ten_nong_dan, chung_nhan, ngay_san_xuat, han_su_dung) VALUES (2, 2, 'LO-XOAI-001', '2024-12-01', 'Xã Hòa Lộc, Huyện Cái Bè, Tiền Giang', 'Nguyễn Văn Thành', 'VietGAP', '2024-12-02', '2024-12-15')");
                stmt.execute(
                        "INSERT INTO truy_xuat_nguon_goc (ma_truy_xuat, ma_san_pham, so_lo, ngay_thu_hoach, dia_chi_san_xuat, ten_nong_dan, chung_nhan, ngay_san_xuat, han_su_dung) VALUES (3, 3, 'LO-TOMSU-001', '2024-12-10', 'Huyện Năm Căn, Cà Mau', 'Công ty TNHH Tôm Cà Mau', 'ASC, GlobalGAP', '2024-12-11', '2025-03-11')");
                rs = stmt.executeQuery("SELECT COUNT(*) FROM truy_xuat_nguon_goc");
                if (rs.next()) {
                    System.out.println("✓ Đã thêm " + rs.getInt(1) + " records truy xuất!");
                }
            } else {
                System.out.println("✓ Dữ liệu truy xuất đã tồn tại");
            }
        } catch (SQLException e) {
            System.err.println("✗ Lỗi thêm dữ liệu truy xuất: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Đóng kết nối database
     */
    public void dongKetNoi() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                AppLogger.info("Đóng kết nối database");
            }
        } catch (SQLException e) {
            AppLogger.error("Lỗi đóng kết nối", e);
        }
    }
}
