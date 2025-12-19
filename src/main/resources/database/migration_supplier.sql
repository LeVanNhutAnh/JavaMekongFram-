-- =============================================
-- MIGRATION SCRIPT: Supplier Integration
-- Mục đích: Thêm tính năng Nhà cung cấp vào DB hiện có
-- Cách dùng: Paste vào SQLite DB Browser và Execute
-- =============================================

-- Bước 1: Backup dữ liệu quan trọng (optional, SQLite auto-backup)
-- CREATE TABLE san_pham_backup AS SELECT * FROM san_pham;

-- Bước 2: Tạo bảng NhaCungCap nếu chưa có
CREATE TABLE IF NOT EXISTS NhaCungCap (
    maNCC INTEGER PRIMARY KEY AUTOINCREMENT,
    tenNCC TEXT NOT NULL,
    dienThoai TEXT,
    diaChi TEXT,
    email TEXT,
    loaiSanPham TEXT,
    danhGia INTEGER DEFAULT 5,
    ghiChu TEXT,
    ngayTao DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Bước 3: Thêm cột ma_ncc vào san_pham
-- SQLite không hỗ trợ ALTER ADD COLUMN với FOREIGN KEY trực tiếp
-- Kiểm tra xem cột đã tồn tại chưa
-- Nếu lỗi "duplicate column", bỏ qua và chạy tiếp

ALTER TABLE san_pham ADD COLUMN ma_ncc INTEGER;

-- Bước 4: Tạo các bảng mới cho Đơn nhập hàng
CREATE TABLE IF NOT EXISTS don_nhap (
    ma_don_nhap INTEGER PRIMARY KEY AUTOINCREMENT,
    ma_don TEXT UNIQUE NOT NULL,
    ma_ncc INTEGER NOT NULL,
    ma_nguoi_dung INTEGER,
    ngay_nhap DATETIME DEFAULT CURRENT_TIMESTAMP,
    tong_tien REAL DEFAULT 0,
    giam_gia REAL DEFAULT 0,
    thanh_tien REAL DEFAULT 0,
    trang_thai TEXT DEFAULT 'cho_duyet',
    da_nhap_kho INTEGER DEFAULT 0,
    ghi_chu TEXT,
    FOREIGN KEY (ma_ncc) REFERENCES NhaCungCap(maNCC),
    FOREIGN KEY (ma_nguoi_dung) REFERENCES nguoi_dung(ma_nguoi_dung)
);

CREATE TABLE IF NOT EXISTS chi_tiet_don_nhap (
    ma_chi_tiet INTEGER PRIMARY KEY AUTOINCREMENT,
    ma_don_nhap INTEGER NOT NULL,
    ma_san_pham INTEGER NOT NULL,
    so_luong INTEGER NOT NULL,
    don_gia REAL NOT NULL,
    thanh_tien REAL NOT NULL,
    FOREIGN KEY (ma_don_nhap) REFERENCES don_nhap(ma_don_nhap),
    FOREIGN KEY (ma_san_pham) REFERENCES san_pham(ma_san_pham)
);

CREATE TABLE IF NOT EXISTS phieu_nhap_kho (
    ma_phieu INTEGER PRIMARY KEY AUTOINCREMENT,
    ma_phieu_text TEXT UNIQUE NOT NULL,
    ma_don_nhap INTEGER,
    ma_ncc INTEGER NOT NULL,
    ma_nguoi_dung INTEGER,
    ngay_nhap DATETIME DEFAULT CURRENT_TIMESTAMP,
    tong_so_luong INTEGER DEFAULT 0,
    ghi_chu TEXT,
    FOREIGN KEY (ma_don_nhap) REFERENCES don_nhap(ma_don_nhap),
    FOREIGN KEY (ma_ncc) REFERENCES NhaCungCap(maNCC),
    FOREIGN KEY (ma_nguoi_dung) REFERENCES nguoi_dung(ma_nguoi_dung)
);

-- Bước 5: Cập nhật bảng CongNo
-- Thêm cột loaiCongNo và maNCC
ALTER TABLE CongNo ADD COLUMN loaiCongNo TEXT DEFAULT 'phai_thu';
ALTER TABLE CongNo ADD COLUMN maNCC INTEGER;

-- Bước 6: Tạo indexes cho performance
CREATE INDEX IF NOT EXISTS idx_san_pham_ncc ON san_pham(ma_ncc);
CREATE INDEX IF NOT EXISTS idx_don_nhap_ngay ON don_nhap(ngay_nhap);
CREATE INDEX IF NOT EXISTS idx_don_nhap_ncc ON don_nhap(ma_ncc);

-- Bước 7: Insert dữ liệu mẫu NCC (optional)
INSERT OR IGNORE INTO NhaCungCap (maNCC, tenNCC, dienThoai, loaiSanPham, danhGia) VALUES
(1, 'Công ty Gạo Sài Gòn', '0901234567', 'Lúa gạo', 5),
(2, 'HTX Trái cây Cần Thơ', '0912345678', 'Trái cây', 4),
(3, 'Trại thủy sản An Giang', '0923456789', 'Thủy sản', 5);

-- =============================================
-- KẾT THÚC MIGRATION
-- Kiểm tra: SELECT * FROM san_pham LIMIT 1;
-- Nếu có lỗi "duplicate column", ignore và app vẫn chạy OK
-- =============================================
