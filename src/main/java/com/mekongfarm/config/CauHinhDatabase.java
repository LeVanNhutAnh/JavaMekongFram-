package com.mekongfarm.config;

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
            System.out.println("✓ Kết nối database thành công!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("✗ Lỗi kết nối database: " + e.getMessage());
            e.printStackTrace();
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
            System.err.println("✗ Lỗi lấy kết nối: " + e.getMessage());
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

            // Thử đọc từ file resource
            String schema = docFileResource("/database/schema.sql");
            if (!schema.isEmpty()) {
                thucThiSQL(schema);
            }
            System.out.println("✓ Khởi tạo schema thành công!");

            // Thêm dữ liệu mẫu nếu chưa có
            if (kiemTraDuLieuTrong()) {
                themDuLieuMauTrucTiep();
                String duLieuMau = docFileResource("/database/du_lieu_mau.sql");
                if (!duLieuMau.isEmpty()) {
                    thucThiSQL(duLieuMau);
                }
                System.out.println("✓ Thêm dữ liệu mẫu thành công!");
            } else {
                // Luôn check và thêm data truy xuất nếu table trống
                themDuLieuTruyXuatNeuChua();
            }
        } catch (Exception e) {
            System.err.println("✗ Lỗi khởi tạo database: " + e.getMessage());
            e.printStackTrace();
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
                    "CREATE TABLE IF NOT EXISTS san_pham (ma_san_pham INTEGER PRIMARY KEY AUTOINCREMENT, ma_sp TEXT UNIQUE, ten_san_pham TEXT, ma_loai INTEGER, ma_tinh INTEGER, ma_ncc INTEGER, don_gia REAL, so_luong_ton INTEGER DEFAULT 0, don_vi_tinh TEXT DEFAULT 'kg', mo_ta TEXT, hinh_anh TEXT, ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP, trang_thai INTEGER DEFAULT 1)");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS khach_hang (ma_khach_hang INTEGER PRIMARY KEY AUTOINCREMENT, ma_kh TEXT UNIQUE, ho_ten TEXT, dia_chi TEXT, so_dien_thoai TEXT, email TEXT, ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS don_hang (ma_don_hang INTEGER PRIMARY KEY AUTOINCREMENT, ma_dh TEXT UNIQUE, ma_khach_hang INTEGER, ma_nguoi_dung INTEGER, ngay_dat DATETIME DEFAULT CURRENT_TIMESTAMP, tong_tien REAL DEFAULT 0, giam_gia REAL DEFAULT 0, thanh_tien REAL DEFAULT 0, trang_thai TEXT DEFAULT 'cho_xu_ly', ghi_chu TEXT)");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS chi_tiet_don_hang (ma_chi_tiet INTEGER PRIMARY KEY AUTOINCREMENT, ma_don_hang INTEGER, ma_san_pham INTEGER, so_luong INTEGER, don_gia REAL, thanh_tien REAL)");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS truy_xuat_nguon_goc (ma_truy_xuat INTEGER PRIMARY KEY AUTOINCREMENT, ma_san_pham INTEGER, so_lo TEXT, ngay_thu_hoach DATE, dia_chi_san_xuat TEXT, ten_nong_dan TEXT, chung_nhan TEXT, ngay_san_xuat DATE, han_su_dung DATE, ma_qr TEXT)");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS bao_gia (ma_bao_gia INTEGER PRIMARY KEY AUTOINCREMENT, ma_san_pham INTEGER NOT NULL, ma_ncc INTEGER NOT NULL, don_gia REAL NOT NULL, so_luong_toi_thieu INTEGER DEFAULT 0, thoi_gian_giao INTEGER DEFAULT 0, dieu_kien TEXT, ngay_bao_gia DATE DEFAULT CURRENT_DATE, han_hieu_luc DATE, trang_thai TEXT DEFAULT 'hieu_luc', ghi_chu TEXT, FOREIGN KEY (ma_san_pham) REFERENCES san_pham(ma_san_pham), FOREIGN KEY (ma_ncc) REFERENCES NhaCungCap(maNCC))");
            System.out.println("✓ Tạo bảng trực tiếp thành công!");
        } catch (SQLException e) {
            System.err.println("Lỗi tạo bảng: " + e.getMessage());
        }
    }

    /**
     * Thêm user admin và dữ liệu cơ bản
     */
    private void themDuLieuMauTrucTiep() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(
                    "INSERT OR IGNORE INTO nguoi_dung (ten_dang_nhap, mat_khau, ho_ten, vai_tro) VALUES ('admin', 'admin123', 'Quản trị viên', 'quan_tri')");
            stmt.execute(
                    "INSERT OR IGNORE INTO tinh_thanh (ten_tinh, vung_mien) VALUES ('Long An', 'ĐBSCL'), ('Tiền Giang', 'ĐBSCL'), ('Bến Tre', 'ĐBSCL'), ('Trà Vinh', 'ĐBSCL'), ('Vĩnh Long', 'ĐBSCL'), ('Đồng Tháp', 'ĐBSCL'), ('An Giang', 'ĐBSCL'), ('Kiên Giang', 'ĐBSCL'), ('Hậu Giang', 'ĐBSCL'), ('Sóc Trăng', 'ĐBSCL'), ('Bạc Liêu', 'ĐBSCL'), ('Cà Mau', 'ĐBSCL'), ('Cần Thơ', 'ĐBSCL')");
            stmt.execute(
                    "INSERT OR IGNORE INTO loai_san_pham (ten_loai, mo_ta) VALUES ('Lúa gạo', 'Các loại gạo'), ('Trái cây', 'Trái cây tươi'), ('Thủy sản', 'Cá tôm'), ('Rau củ', 'Rau củ quả'), ('Đặc sản', 'Đặc sản địa phương')");
            stmt.execute(
                    "INSERT OR IGNORE INTO san_pham (ma_sp, ten_san_pham, ma_loai, ma_tinh, don_gia, so_luong_ton, don_vi_tinh, mo_ta) VALUES ('SP001', 'Gạo ST25', 1, 10, 45000, 500, 'kg', 'Gạo ngon nhất thế giới')");
            stmt.execute(
                    "INSERT OR IGNORE INTO san_pham (ma_sp, ten_san_pham, ma_loai, ma_tinh, don_gia, so_luong_ton, don_vi_tinh, mo_ta) VALUES ('SP002', 'Xoài cát Hòa Lộc', 2, 2, 65000, 200, 'kg', 'Đặc sản Tiền Giang')");
            stmt.execute(
                    "INSERT OR IGNORE INTO san_pham (ma_sp, ten_san_pham, ma_loai, ma_tinh, don_gia, so_luong_ton, don_vi_tinh, mo_ta) VALUES ('SP003', 'Tôm sú', 3, 12, 250000, 100, 'kg', 'Tôm sú Cà Mau')");
            // Thêm dữ liệu truy xuất nguồn gốc - INSERT OR REPLACE để đảm bảo có dữ liệu
            stmt.execute(
                    "INSERT OR REPLACE INTO truy_xuat_nguon_goc (ma_truy_xuat, ma_san_pham, so_lo, ngay_thu_hoach, dia_chi_san_xuat, ten_nong_dan, chung_nhan, ngay_san_xuat, han_su_dung) VALUES (1, 1, 'LO-ST25-001', '2024-11-15', 'Xã Tân Hưng, Huyện Long Phú, Sóc Trăng', 'Hồ Quang Cua', 'VietGAP, GlobalGAP', '2024-11-20', '2025-11-20')");
            stmt.execute(
                    "INSERT OR REPLACE INTO truy_xuat_nguon_goc (ma_truy_xuat, ma_san_pham, so_lo, ngay_thu_hoach, dia_chi_san_xuat, ten_nong_dan, chung_nhan, ngay_san_xuat, han_su_dung) VALUES (2, 2, 'LO-XOAI-001', '2024-12-01', 'Xã Hòa Lộc, Huyện Cái Bè, Tiền Giang', 'Nguyễn Văn Thành', 'VietGAP', '2024-12-02', '2024-12-15')");
            stmt.execute(
                    "INSERT OR REPLACE INTO truy_xuat_nguon_goc (ma_truy_xuat, ma_san_pham, so_lo, ngay_thu_hoach, dia_chi_san_xuat, ten_nong_dan, chung_nhan, ngay_san_xuat, han_su_dung) VALUES (3, 3, 'LO-TOMSU-001', '2024-12-10', 'Huyện Năm Căn, Cà Mau', 'Công ty TNHH Tôm Cà Mau', 'ASC, GlobalGAP', '2024-12-11', '2025-03-11')");
            
            // Verify data inserted
            ResultSet checkRs = stmt.executeQuery("SELECT COUNT(*) FROM truy_xuat_nguon_goc");
            if (checkRs.next()) {
                System.out.println("✓ Thêm dữ liệu mẫu thành công! Tổng truy xuất: " + checkRs.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi thêm dữ liệu: " + e.getMessage());
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
                System.out.println("✓ Đã đóng kết nối database");
            }
        } catch (SQLException e) {
            System.err.println("✗ Lỗi đóng kết nối: " + e.getMessage());
        }
    }
}
