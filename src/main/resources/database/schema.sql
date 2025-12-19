-- =============================================
-- MEKONG FARM MANAGEMENT SYSTEM
-- Hệ thống Quản lý Nông sản Đồng bằng sông Cửu Long
-- =============================================

-- Bảng Người dùng
CREATE TABLE IF NOT EXISTS nguoi_dung (
    ma_nguoi_dung INTEGER PRIMARY KEY AUTOINCREMENT,
    ten_dang_nhap TEXT UNIQUE NOT NULL,
    mat_khau TEXT NOT NULL,
    ho_ten TEXT NOT NULL,
    vai_tro TEXT DEFAULT 'nhan_vien',
    ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP,
    trang_thai INTEGER DEFAULT 1
);

-- Bảng Tỉnh thành (13 tỉnh ĐBSCL)
CREATE TABLE IF NOT EXISTS tinh_thanh (
    ma_tinh INTEGER PRIMARY KEY AUTOINCREMENT,
    ten_tinh TEXT NOT NULL,
    vung_mien TEXT DEFAULT 'ĐBSCL'
);

-- Bảng Loại sản phẩm
CREATE TABLE IF NOT EXISTS loai_san_pham (
    ma_loai INTEGER PRIMARY KEY AUTOINCREMENT,
    ten_loai TEXT NOT NULL,
    mo_ta TEXT
);

-- Bảng Nhà cung cấp (nếu chưa có)
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

-- Bảng Sản phẩm (thêm ma_ncc)
CREATE TABLE IF NOT EXISTS san_pham (
    ma_san_pham INTEGER PRIMARY KEY AUTOINCREMENT,
    ma_sp TEXT UNIQUE NOT NULL,
    ten_san_pham TEXT NOT NULL,
    ma_loai INTEGER,
    ma_tinh INTEGER,
    ma_ncc INTEGER,
    don_gia REAL NOT NULL,
    so_luong_ton INTEGER DEFAULT 0,
    don_vi_tinh TEXT DEFAULT 'kg',
    mo_ta TEXT,
    hinh_anh TEXT,
    ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP,
    trang_thai INTEGER DEFAULT 1,
    FOREIGN KEY (ma_loai) REFERENCES loai_san_pham(ma_loai),
    FOREIGN KEY (ma_tinh) REFERENCES tinh_thanh(ma_tinh),
    FOREIGN KEY (ma_ncc) REFERENCES NhaCungCap(maNCC)
);

-- Bảng Khách hàng
CREATE TABLE IF NOT EXISTS khach_hang (
    ma_khach_hang INTEGER PRIMARY KEY AUTOINCREMENT,
    ma_kh TEXT UNIQUE NOT NULL,
    ho_ten TEXT NOT NULL,
    dia_chi TEXT,
    so_dien_thoai TEXT,
    email TEXT,
    ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Đơn hàng (bán)
CREATE TABLE IF NOT EXISTS don_hang (
    ma_don_hang INTEGER PRIMARY KEY AUTOINCREMENT,
    ma_dh TEXT UNIQUE NOT NULL,
    ma_khach_hang INTEGER,
    ma_nguoi_dung INTEGER,
    ngay_dat DATETIME DEFAULT CURRENT_TIMESTAMP,
    tong_tien REAL DEFAULT 0,
    giam_gia REAL DEFAULT 0,
    thanh_tien REAL DEFAULT 0,
    trang_thai TEXT DEFAULT 'cho_xu_ly',
    ghi_chu TEXT,
    FOREIGN KEY (ma_khach_hang) REFERENCES khach_hang(ma_khach_hang),
    FOREIGN KEY (ma_nguoi_dung) REFERENCES nguoi_dung(ma_nguoi_dung)
);

-- Bảng Chi tiết đơn hàng
CREATE TABLE IF NOT EXISTS chi_tiet_don_hang (
    ma_chi_tiet INTEGER PRIMARY KEY AUTOINCREMENT,
    ma_don_hang INTEGER,
    ma_san_pham INTEGER,
    so_luong INTEGER NOT NULL,
    don_gia REAL NOT NULL,
    thanh_tien REAL NOT NULL,
    FOREIGN KEY (ma_don_hang) REFERENCES don_hang(ma_don_hang),
    FOREIGN KEY (ma_san_pham) REFERENCES san_pham(ma_san_pham)
);

-- Bảng Đơn nhập hàng (từ NCC)
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

-- Bảng Chi tiết đơn nhập
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

-- Bảng Phiếu nhập kho
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

-- Bảng Truy xuất nguồn gốc
CREATE TABLE IF NOT EXISTS truy_xuat_nguon_goc (
    ma_truy_xuat INTEGER PRIMARY KEY AUTOINCREMENT,
    ma_san_pham INTEGER,
    so_lo TEXT,
    ngay_thu_hoach DATE,
    dia_chi_san_xuat TEXT,
    ten_nong_dan TEXT,
    chung_nhan TEXT,
    ngay_san_xuat DATE,
    han_su_dung DATE,
    ma_qr TEXT,
    FOREIGN KEY (ma_san_pham) REFERENCES san_pham(ma_san_pham)
);

-- Indexes để tối ưu tìm kiếm
CREATE INDEX IF NOT EXISTS idx_san_pham_ten ON san_pham(ten_san_pham);
CREATE INDEX IF NOT EXISTS idx_san_pham_tinh ON san_pham(ma_tinh);
CREATE INDEX IF NOT EXISTS idx_san_pham_loai ON san_pham(ma_loai);
CREATE INDEX IF NOT EXISTS idx_san_pham_ncc ON san_pham(ma_ncc);
CREATE INDEX IF NOT EXISTS idx_khach_hang_ten ON khach_hang(ho_ten);
CREATE INDEX IF NOT EXISTS idx_don_hang_ngay ON don_hang(ngay_dat);
CREATE INDEX IF NOT EXISTS idx_don_hang_khach ON don_hang(ma_khach_hang);
CREATE INDEX IF NOT EXISTS idx_don_nhap_ngay ON don_nhap(ngay_nhap);
CREATE INDEX IF NOT EXISTS idx_don_nhap_ncc ON don_nhap(ma_ncc);

-- Bảng Báo giá từ NCC
CREATE TABLE IF NOT EXISTS bao_gia (
    ma_bao_gia INTEGER PRIMARY KEY AUTOINCREMENT,
    ma_san_pham INTEGER NOT NULL,
    ma_ncc INTEGER NOT NULL,
    don_gia REAL NOT NULL,
    so_luong_toi_thieu INTEGER DEFAULT 0,
    thoi_gian_giao INTEGER DEFAULT 0, -- Số ngày giao hàng
    dieu_kien TEXT,
    ngay_bao_gia DATE DEFAULT CURRENT_DATE,
    han_hieu_luc DATE,
    trang_thai TEXT DEFAULT 'hieu_luc', -- 'hieu_luc', 'het_han', 'da_chon'
    ghi_chu TEXT,
    FOREIGN KEY (ma_san_pham) REFERENCES san_pham(ma_san_pham),
    FOREIGN KEY (ma_ncc) REFERENCES NhaCungCap(maNCC)
);

CREATE INDEX IF NOT EXISTS idx_bao_gia_sp ON bao_gia(ma_san_pham);
CREATE INDEX IF NOT EXISTS idx_bao_gia_ncc ON bao_gia(ma_ncc);
