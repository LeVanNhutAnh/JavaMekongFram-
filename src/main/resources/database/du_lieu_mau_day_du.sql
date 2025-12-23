-- =============================================
-- DỮ LIỆU MẪU ĐẦY ĐỦ - MEKONG FARM SYSTEM
-- Cập nhật: 23/12/2025
-- =============================================

-- 1. NGƯỜI DÙNG (3 users)
INSERT OR IGNORE INTO nguoi_dung (ten_dang_nhap, mat_khau, ho_ten, vai_tro) VALUES
('nhanvien01', '123456', 'Nguyễn Văn An', 'nhan_vien'),
('nhanvien02', '123456', 'Trần Thị Bình', 'nhan_vien'),
('khachhang01', '123456', 'Lê Văn Cường', 'khach_hang');

-- 2. NHÀ CUNG CẤP (10 nhà cung cấp)
INSERT OR IGNORE INTO NhaCungCap (maNCC, tenNCC, dienThoai, diaChi, email, loaiSanPham, danhGia, ghiChu) VALUES
(1, 'Công ty TNHH Nông Sản Sóc Trăng', '0292-3821456', 'KCN Trần Đề, Sóc Trăng', 'contact@nongsan-st.vn', 'Lúa gạo', 5, 'Chuyên gạo ST25'),
(2, 'HTX Xoài Hòa Lộc Tiền Giang', '0273-3955123', 'Xã Hòa Lộc, Cái Bè, Tiền Giang', 'hoaloc@xoai.vn', 'Trái cây', 5, 'Xoài xuất khẩu'),
(3, 'Công ty CP Thủy Sản Vĩnh Hoàn', '0277-3825888', 'KCN Hồng Ngự, Đồng Tháp', 'info@vinhhoan.com', 'Thủy sản', 5, 'Cá tra fillet'),
(4, 'HTX Tôm Cà Mau', '0290-3831234', 'Huyện Năm Căn, Cà Mau', 'tomcamau@gmail.com', 'Thủy sản', 4, 'Tôm sú organic'),
(5, 'Công ty Nước Mắm Phú Quốc', '0297-3846789', 'Phú Quốc, Kiên Giang', 'nuocmam@phuquoc.vn', 'Đặc sản', 5, 'Nước mắm truyền thống'),
(6, 'Vườn Cây Trái Bến Tre', '0275-3822456', 'Xã Mỹ Lồng, Bến Tre', 'bentre@traicay.vn', 'Trái cây', 4, 'Dừa, bưởi, sầu riêng'),
(7, 'HTX Gạo An Giang', '0296-3855321', 'Châu Đốc, An Giang', 'gao@angiang.vn', 'Lúa gạo', 5, 'Gạo Jasmine'),
(8, 'Công ty Thanh Long Long An', '0272-3865432', 'Thị xã Kiến Tường, Long An', 'thanhlong@longan.vn', 'Trái cây', 4, 'Thanh long ruột đỏ'),
(9, 'Kẹo Dừa Bến Tre', '0275-3877654', 'Phường 8, TP Bến Tre', 'keodua@bentre.vn', 'Đặc sản', 4, 'Kẹo dừa handmade'),
(10, 'HTX Cá Lóc An Giang', '0296-3866543', 'Huyện Tân Châu, An Giang', 'caloc@angiang.vn', 'Thủy sản', 4, 'Cá lóc đồng');

-- 3. KHÁCH HÀNG (15 khách hàng)
INSERT OR IGNORE INTO khach_hang (ma_kh, ho_ten, dia_chi, so_dien_thoai, email, loai_khach) VALUES
('KH001', 'Nguyễn Thị Mai', '123 Nguyễn Huệ, Q.1, TP.HCM', '0901234567', 'mai.nguyen@email.com', 'le'),
('KH002', 'Trần Văn Hùng', '456 Lê Lợi, Q.3, TP.HCM', '0912345678', 'hung.tran@email.com', 'si'),
('KH003', 'Lê Thị Hồng', '789 CMT8, Ninh Kiều, Cần Thơ', '0923456789', 'hong.le@email.com', 'le'),
('KH004', 'Phạm Văn Đức', '321 Đường 30/4, Vĩnh Long', '0934567890', 'duc.pham@email.com', 'si'),
('KH005', 'Huỳnh Thị Lan', '654 Trần Hưng Đạo, Mỹ Tho', '0945678901', 'lan.huynh@email.com', 'le'),
('KH006', 'Võ Văn Tùng', '111 Hai Bà Trưng, Q.1, TP.HCM', '0956789012', 'tung.vo@email.com', 'si'),
('KH007', 'Đặng Thị Oanh', '222 Trần Phú, Nha Trang', '0967890123', 'oanh.dang@email.com', 'le'),
('KH008', 'Ngô Văn Phát', '333 Lý Thường Kiệt, Đà Nẵng', '0978901234', 'phat.ngo@email.com', 'si'),
('KH009', 'Bùi Thị Thu', '444 Nguyễn Trãi, Hà Nội', '0989012345', 'thu.bui@email.com', 'le'),
('KH010', 'Dương Văn Sơn', '555 Lê Duẩn, Huế', '0990123456', 'son.duong@email.com', 'si'),
('KH011', 'Siêu Thị CoopMart', '789 Nguyễn Văn Linh, Q.7, TP.HCM', '0281234567', 'coopmart@email.com', 'si'),
('KH012', 'Nhà Hàng Năm Sao', '999 Đồng Khởi, Q.1, TP.HCM', '0282345678', 'namsa@email.com', 'si'),
('KH013', 'Chợ Đầu Mối Bình Điền', 'Quận 8, TP.HCM', '0283456789', 'binhdien@email.com', 'si'),
('KH014', 'Công Ty XNK Nông Sản', '123 Võ Văn Tần, Q.3, TP.HCM', '0284567890', 'xnk@email.com', 'si'),
('KH015', 'Siêu Thị Lotte Mart', '469 Nguyễn Hữu Thọ, Q.7, TP.HCM', '0285678901', 'lotte@email.com', 'si');

-- 4. ĐƠN NHẬP (10 đơn nhập từ NCC)
INSERT OR IGNORE INTO don_nhap (ma_don_nhap, ma_don, ma_ncc, ma_nguoi_dung, ngay_nhap, tong_tien, trang_thai, ghi_chu) VALUES
(1, 'DN001', 1, 1, '2024-11-15', 22500000, 'hoan_thanh', 'Nhập gạo ST25 - lô 1'),
(2, 'DN002', 2, 1, '2024-11-20', 13000000, 'hoan_thanh', 'Nhập xoài Hòa Lộc'),
(3, 'DN003', 3, 1, '2024-12-01', 34000000, 'hoan_thanh', 'Nhập cá tra fillet'),
(4, 'DN004', 4, 2, '2024-12-05', 25000000, 'hoan_thanh', 'Nhập tôm sú'),
(5, 'DN005', 5, 1, '2024-12-08', 36000000, 'hoan_thanh', 'Nhập nước mắm Phú Quốc'),
(6, 'DN006', 6, 2, '2024-12-10', 18000000, 'hoan_thanh', 'Nhập trái cây Bến Tre'),
(7, 'DN007', 7, 1, '2024-12-12', 20000000, 'hoan_thanh', 'Nhập gạo Jasmine'),
(8, 'DN008', 8, 2, '2024-12-15', 8750000, 'cho_duyet', 'Nhập thanh long'),
(9, 'DN009', 9, 1, '2024-12-18', 15000000, 'cho_duyet', 'Nhập kẹo dừa'),
(10, 'DN010', 10, 2, '2024-12-20', 14000000, 'dang_nhap', 'Nhập cá lóc');

-- 5. CHI TIẾT ĐƠN NHẬP
INSERT OR IGNORE INTO chi_tiet_don_nhap (ma_don_nhap, ma_san_pham, so_luong, don_gia, thanh_tien) VALUES
-- Đơn 1: Gạo ST25
(1, 1, 500, 45000, 22500000),
-- Đơn 2: Xoài
(2, 4, 200, 65000, 13000000),
-- Đơn 3: Cá tra
(3, 15, 400, 85000, 34000000),
-- Đơn 4: Tôm sú
(4, 16, 100, 250000, 25000000),
-- Đơn 5: Nước mắm
(5, 20, 300, 120000, 36000000),
-- Đơn 6: Mix trái cây
(6, 6, 300, 35000, 10500000),
(6, 9, 500, 15000, 7500000),
-- Đơn 7: Gạo Jasmine
(7, 2, 800, 25000, 20000000),
-- Đơn 8: Thanh long
(8, 8, 350, 25000, 8750000),
-- Đơn 9: Kẹo dừa
(9, 22, 250, 60000, 15000000),
-- Đơn 10: Cá lóc
(10, 19, 200, 70000, 14000000);

-- 6. ĐƠN HÀNG (20 đơn hàng bán)
INSERT OR IGNORE INTO don_hang (ma_dh, ma_khach_hang, ma_nguoi_dung, ngay_dat, tong_tien, thanh_tien, trang_thai, ghi_chu) VALUES
('DH001', 1, 1, '2024-11-18', 500000, 500000, 'da_giao', 'Giao nhanh'),
('DH002', 2, 1, '2024-11-19', 750000, 750000, 'da_giao', 'Khách VIP'),
('DH003', 3, 2, '2024-11-20', 1200000, 1200000, 'da_giao', ''),
('DH004', 4, 1, '2024-11-25', 2500000, 2500000, 'da_giao', 'Đơn sỉ lớn'),
('DH005', 5, 2, '2024-12-01', 850000, 850000, 'da_giao', ''),
('DH006', 11, 1, '2024-12-03', 15000000, 15000000, 'da_giao', 'CoopMart - Đơn tháng 12'),
('DH007', 6, 2, '2024-12-05', 3200000, 3200000, 'da_giao', ''),
('DH008', 12, 1, '2024-12-08', 8500000, 8500000, 'da_giao', 'Nhà hàng Năm Sao'),
('DH009', 7, 2, '2024-12-10', 680000, 680000, 'dang_giao', 'Giao Nha Trang'),
('DH010', 13, 1, '2024-12-12', 25000000, 25000000, 'dang_giao', 'Chợ Bình Điền - Đơn lớn'),
('DH011', 8, 1, '2024-12-15', 4500000, 4500000, 'dang_giao', 'Giao Đà Nẵng'),
('DH012', 9, 2, '2024-12-16', 920000, 920000, 'dang_giao', 'Giao Hà Nội'),
('DH013', 14, 1, '2024-12-18', 12000000, 12000000, 'cho_xu_ly', 'Công ty XNK'),
('DH014', 10, 2, '2024-12-19', 3800000, 3800000, 'cho_xu_ly', ''),
('DH015', 15, 1, '2024-12-20', 18000000, 18000000, 'cho_xu_ly', 'Lotte Mart'),
('DH016', 1, 2, '2024-12-21', 650000, 650000, 'cho_xu_ly', ''),
('DH017', 3, 1, '2024-12-21', 1150000, 1150000, 'cho_xu_ly', ''),
('DH018', 5, 2, '2024-12-22', 890000, 890000, 'cho_xu_ly', ''),
('DH019', 7, 1, '2024-12-22', 1200000, 1200000, 'cho_xu_ly', ''),
('DH020', 2, 2, '2024-12-23', 2800000, 2800000, 'cho_xu_ly', 'Đơn mới nhất');

-- 7. CHI TIẾT ĐƠN HÀNG
INSERT OR IGNORE INTO chi_tiet_don_hang (ma_don_hang, ma_san_pham, so_luong, don_gia, thanh_tien) VALUES
-- DH001
(1, 1, 10, 45000, 450000),
(1, 8, 2, 25000, 50000),
-- DH002
(2, 4, 5, 65000, 325000),
(2, 6, 10, 35000, 350000),
(2, 9, 5, 15000, 75000),
-- DH003  
(3, 5, 10, 120000, 1200000),
-- DH004
(4, 2, 50, 25000, 1250000),
(4, 3, 30, 28000, 840000),
(4, 8, 10, 25000, 250000),
(4, 10, 8, 20000, 160000),
-- DH005
(5, 15, 10, 85000, 850000),
-- DH006 (CoopMart)
(6, 1, 100, 45000, 4500000),
(6, 2, 200, 25000, 5000000),
(6, 15, 50, 85000, 4250000),
(6, 20, 10, 120000, 1200000),
-- DH007
(7, 4, 30, 65000, 1950000),
(7, 5, 10, 120000, 1200000),
(7, 8, 2, 25000, 50000),
-- DH008 (Nhà hàng)
(8, 15, 40, 85000, 3400000),
(8, 16, 10, 250000, 2500000),
(8, 17, 15, 180000, 2700000),
-- DH009
(9, 6, 10, 35000, 350000),
(9, 7, 10, 30000, 300000),
(9, 12, 1, 35000, 35000),
-- DH010 (Chợ Bình Điền)
(10, 1, 200, 45000, 9000000),
(10, 2, 300, 25000, 7500000),
(10, 4, 50, 65000, 3250000),
(10, 15, 60, 85000, 5100000),
-- DH011
(11, 5, 20, 120000, 2400000),
(11, 13, 30, 55000, 1650000),
(11, 14, 5, 80000, 400000),
-- DH012
(12, 20, 5, 120000, 600000),
(12, 21, 5, 45000, 225000),
(12, 22, 2, 60000, 120000),
-- DH013 (XNK)
(13, 1, 150, 45000, 6750000),
(13, 4, 80, 65000, 5200000),
-- DH014
(14, 16, 10, 250000, 2500000),
(14, 17, 7, 180000, 1260000),
-- DH015 (Lotte)
(15, 1, 100, 45000, 4500000),
(15, 2, 200, 25000, 5000000),
(15, 4, 50, 65000, 3250000),
(15, 15, 60, 85000, 5100000),
-- DH016-020 (các đơn nhỏ)
(16, 1, 10, 45000, 450000),
(16, 8, 8, 25000, 200000),
(17, 5, 8, 120000, 960000),
(17, 11, 5, 40000, 200000),
(18, 6, 15, 35000, 525000),
(18, 7, 10, 30000, 300000),
(19, 15, 12, 85000, 1020000),
(19, 19, 3, 70000, 210000),
(20, 16, 8, 250000, 2000000),
(20, 17, 5, 180000, 900000);

-- 8. CÔNG NỢ (15 bản ghi)
INSERT OR IGNORE INTO CongNo (loaiCongNo, maKH, maNCC, maDH, soTien, ngayPhatSinh, hanThanhToan, trangThai, daThanhToan, ghiChu) VALUES
-- Phải thu từ khách hàng
('phai_thu', 2, NULL, 2, 750000, '2024-11-19', '2024-12-19', 'Chưa thanh toán', 0, 'KH Trần Văn Hùng - DH002'),
('phai_thu', 4, NULL, 4, 2500000, '2024-11-25', '2024-12-25', 'Đã thanh toán một phần', 1000000, 'Đơn sỉ - còn nợ 1.5tr'),
('phai_thu', 11, NULL, 6, 15000000, '2024-12-03', '2025-01-03', 'Đã thanh toán một phần', 10000000, 'CoopMart - còn 5tr'),
('phai_thu', 12, NULL, 8, 8500000, '2024-12-08', '2025-01-08', 'Chưa thanh toán', 0, 'Nhà hàng Năm Sao'),
('phai_thu', 13, NULL, 10, 25000000, '2024-12-12', '2025-01-12', 'Đã thanh toán một phần', 15000000, 'Chợ Bình Điền - còn 10tr'),
('phai_thu', 14, NULL, 13, 12000000, '2024-12-18', '2025-01-18', 'Chưa thanh toán', 0, 'Công ty XNK'),
('phai_thu', 15, NULL, 15, 18000000, '2024-12-20', '2025-01-20', 'Chưa thanh toán', 0, 'Lotte Mart'),
('phai_thu', 8, NULL, 11, 4500000, '2024-12-15', '2025-01-15', 'Chưa thanh toán', 0, 'Giao Đà Nẵng'),
-- Phải trả cho NCC
('phai_tra', NULL, 1, NULL, 22500000, '2024-11-15', '2024-12-15', 'Đã thanh toán', 22500000, 'Gạo ST25 - Đã thanh toán'),
('phai_tra', NULL, 3, NULL, 34000000, '2024-12-01', '2025-01-01', 'Đã thanh toán một phần', 20000000, 'Cá tra - còn 14tr'),
('phai_tra', NULL, 4, NULL, 25000000, '2024-12-05', '2025-01-05', 'Chưa thanh toán', 0, 'Tôm sú - chưa thanh toán'),
('phai_tra', NULL, 5, NULL, 36000000, '2024-12-08', '2025-01-08', 'Đã thanh toán một phần', 18000000, 'Nước mắm - còn 18tr'),
('phai_tra', NULL, 7, NULL, 20000000, '2024-12-12', '2025-01-12', 'Chưa thanh toán', 0, 'Gạo Jasmine'),
('phai_tra', NULL, 9, NULL, 15000000, '2024-12-18', '2025-01-18', 'Chưa thanh toán', 0, 'Kẹo dừa'),
('phai_tra', NULL, 10, NULL, 14000000, '2024-12-20', '2025-01-20', 'Chưa thanh toán', 0, 'Cá lóc');

-- 9. BÁO GIÁ (10 báo giá)
INSERT OR IGNORE INTO bao_gia (ma_bao_gia, ma_khach_hang, ma_nguoi_dung, ngay_bao_gia, tong_tien, trang_thai, ghi_chu) VALUES
('BG001', 1, 1, '2024-12-22', 850000, 'da_duyet', 'Báo giá cho KH Mai'),
('BG002', 2, 1, '2024-12-22', 1200000, 'da_duyet', 'Báo giá gạo + trái cây'),
('BG003', 11, 2, '2024-12-23', 20000000, 'cho_duyet', 'Báo giá CoopMart tháng 1/2025'),
('BG004', 12, 1, '2024-12-23', 9500000, 'cho_duyet', 'Báo giá Nhà hàng'),
('BG005', 14, 2, '2024-12-23', 15000000, 'dang_soan', 'Báo giá XNK - đang soạn'),
('BG006', 3, 1, '2024-12-23', 680000, 'da_duyet', 'Báo giá lẻ'),
('BG007', 15, 2, '2024-12-23', 22000000, 'dang_soan', 'Báo giá Lotte tháng 1'),
('BG008', 5, 1, '2024-12-23', 920000, 'da_duyet', 'Báo giá KH Lan'),
('BG009', 8, 2, '2024-12-23', 5200000, 'cho_duyet', 'Báo giá giao Đà Nẵng'),
('BG010', 13, 1, '2024-12-23', 28000000, 'dang_soan', 'Báo giá Chợ Bình Điền');

-- 10. CHI TIẾT BÁO GIÁ
INSERT OR IGNORE INTO chi_tiet_bao_gia (ma_bao_gia, ma_san_pham, so_luong, don_gia, thanh_tien) VALUES
-- BG001
(1, 1, 10, 45000, 450000),
(1, 4, 5, 65000, 325000),
(1, 9, 5, 15000, 75000),
-- BG002
(2, 2, 30, 25000, 750000),
(2, 8, 10, 25000, 250000),
(2, 10, 10, 20000, 200000),
-- BG003 (CoopMart)
(3, 1, 150, 45000, 6750000),
(3, 2, 250, 25000, 6250000),
(3, 15, 80, 85000, 6800000),
-- BG004 (Nhà hàng)
(4, 15, 50, 85000, 4250000),
(4, 16, 12, 250000, 3000000),
(4, 17, 14, 180000, 2520000),
-- BG005 (XNK)
(5, 1, 180, 45000, 8100000),
(5, 4, 100, 65000, 6500000),
-- BG006
(6, 6, 10, 35000, 350000),
(6, 7, 10, 30000, 300000),
(6, 12, 1, 35000, 35000),
-- BG007 (Lotte)
(7, 1, 120, 45000, 5400000),
(7, 2, 280, 25000, 7000000),
(7, 4, 80, 65000, 5200000),
(7, 15, 50, 85000, 4250000),
-- BG008
(8, 20, 5, 120000, 600000),
(8, 21, 5, 45000, 225000),
(8, 22, 2, 60000, 120000),
-- BG009
(9, 5, 25, 120000, 3000000),
(9, 13, 35, 55000, 1925000),
(9, 14, 4, 80000, 320000),
-- BG010 (Chợ Bình Điền)
(10, 1, 250, 45000, 11250000),
(10, 2, 350, 25000, 8750000),
(10, 4, 80, 65000, 5200000),
(10, 8, 120, 25000, 3000000);

-- 11. GIÁ VÙNG (giá theo từng tỉnh - 13 tỉnh ĐBSCL)
INSERT OR IGNORE INTO gia_vung (ma_tinh, ten_tinh, ma_san_pham, gia_ban, ngay_cap_nhat) VALUES
-- Gạo ST25
(10, 'Sóc Trăng', 1, 45000, '2024-12-01'),
(13, 'Cần Thơ', 1, 46000, '2024-12-01'),
-- Gạo Jasmine
(7, 'An Giang', 2, 25000, '2024-12-01'),
(13, 'Cần Thơ', 2, 26000, '2024-12-01'),
-- Xoài Hòa Lộc
(2, 'Tiền Giang', 4, 65000, '2024-12-01'),
(13, 'Cần Thơ', 4, 68000, '2024-12-01'),
-- Sầu riêng
(2, 'Tiền Giang', 5, 120000, '2024-12-01'),
(13, 'Cần Thơ', 5, 125000, '2024-12-01'),
-- Bưởi da xanh
(3, 'Bến Tre', 6, 35000, '2024-12-01'),
(13, 'Cần Thơ', 6, 37000, '2024-12-01'),
-- Cá tra
(6, 'Đồng Tháp', 15, 85000, '2024-12-01'),
(13, 'Cần Thơ', 15, 88000, '2024-12-01'),
-- Tôm sú
(12, 'Cà Mau', 16, 250000, '2024-12-01'),
(13, 'Cần Thơ', 16, 255000, '2024-12-01'),
-- Nước mắm
(8, 'Kiên Giang', 20, 120000, '2024-12-01'),
(13, 'Cần Thơ', 20, 125000, '2024-12-01');

-- 12. MÙA VỤ (10 mùa vụ)
INSERT OR IGNORE INTO mua_vu (ten_mua_vu, nam, ma_tinh, loai_cay_trong, dien_tich_ha, san_luong_tan, ngay_bat_dau, ngay_ket_thuc, trang_thai) VALUES
('Vụ Đông Xuân 2024', 2024, 10, 'Lúa ST25', 5000, 25000, '2024-01-15', '2024-05-15', 'hoan_thanh'),
('Vụ Hè Thu 2024', 2024, 10, 'Lúa ST25', 4500, 22000, '2024-06-01', '2024-09-30', 'hoan_thanh'),
('Vụ Xoài 2024', 2024, 2, 'Xoài Hòa Lộc', 3000, 45000, '2024-02-01', '2024-06-30', 'hoan_thanh'),
('Vụ Sầu Riêng 2024', 2024, 2, 'Sầu riêng Ri6', 2000, 30000, '2024-05-01', '2024-08-31', 'hoan_thanh'),
('Vụ Dừa 2024', 2024, 3, 'Dừa xiêm', 8000, 120000, '2024-01-01', '2024-12-31', 'dang_thu_hoach'),
('Vụ Tôm Sú Xuân 2024', 2024, 12, 'Tôm sú', 1500, 2250, '2024-02-01', '2024-06-30', 'hoan_thanh'),
('Vụ Tôm Sú Thu 2024', 2024, 12, 'Tôm sú', 1800, 2700, '2024-08-01', '2024-12-31', 'dang_thu_hoach'),
('Vụ Cá Tra 2024', 2024, 6, 'Cá tra', 3000, 45000, '2024-01-01', '2024-12-31', 'dang_thu_hoach'),
('Vụ Thanh Long 2024', 2024, 1, 'Thanh long', 1200, 18000, '2024-01-01', '2024-12-31', 'dang_thu_hoach'),
('Vụ Đông Xuân 2025', 2025, 7, 'Lúa Jasmine', 6000, 30000, '2024-12-15', '2025-04-30', 'sap_den');

-- 13. KHO (5 kho)
INSERT OR IGNORE INTO kho (ma_kho, ten_kho, dia_chi, dien_tich_m2, suc_chua_tan, nguoi_quan_ly, trang_thai) VALUES
('KHO01', 'Kho Trung Tâm Cần Thơ', 'KCN Trà Nóc, Cần Thơ', 5000, 1000, 'Nguyễn Văn Tâm', 'hoat_dong'),
('KHO02', 'Kho Lạnh Thủy Sản', 'Huyện Cái Răng, Cần Thơ', 2000, 500, 'Trần Thị Lan', 'hoat_dong'),
('KHO03', 'Kho Gạo Sóc Trăng', 'KCN Trần Đề, Sóc Trăng', 3000, 800, 'Lê Văn Hùng', 'hoat_dong'),
('KHO04', 'Kho Trái Cây Tiền Giang', 'Huyện Cái Bè, Tiền Giang', 1500, 300, 'Phạm Thị Mai', 'hoat_dong'),
('KHO05', 'Kho Tổng Long An', 'Thị xã Kiến Tường, Long An', 4000, 900, 'Võ Văn Đức', 'bao_tri');

-- 14. TỒN KHO THEO KHO (chi tiết hàng tồn kho)
INSERT OR IGNORE INTO ton_kho (ma_kho, ma_san_pham, so_luong, ngay_cap_nhat) VALUES
-- Kho 01 (Trung tâm)
('KHO01', 1, 300, '2024-12-23'),
('KHO01', 2, 400, '2024-12-23'),
('KHO01', 15, 150, '2024-12-23'),
('KHO01', 20, 180, '2024-12-23'),
-- Kho 02 (Lạnh thủy sản)
('KHO02', 15, 100, '2024-12-23'),
('KHO02', 16, 50, '2024-12-23'),
('KHO02', 17, 80, '2024-12-23'),
('KHO02', 18, 40, '2024-12-23'),
('KHO02', 19, 120, '2024-12-23'),
-- Kho 03 (Gạo ST)
('KHO03', 1, 200, '2024-12-23'),
('KHO03', 2, 400, '2024-12-23'),
('KHO03', 3, 600, '2024-12-23'),
-- Kho 04 (Trái cây)
('KHO04', 4, 100, '2024-12-23'),
('KHO04', 5, 80, '2024-12-23'),
('KHO04', 6, 180, '2024-12-23'),
('KHO04', 7, 250, '2024-12-23'),
('KHO04', 8, 200, '2024-12-23'),
('KHO04', 9, 300, '2024-12-23'),
('KHO04', 10, 150, '2024-12-23'),
-- Kho 05
('KHO05', 1, 100, '2024-12-23'),
('KHO05', 8, 150, '2024-12-23'),
('KHO05', 20, 120, '2024-12-23');

-- 15. TRUY XUẤT NGUỒN GỐC (thêm dữ liệu cho nhiều sản phẩm)
INSERT OR REPLACE INTO truy_xuat_nguon_goc (ma_truy_xuat, ma_san_pham, so_lo, ngay_thu_hoach, dia_chi_san_xuat, ten_nong_dan, chung_nhan, ngay_san_xuat, han_su_dung) VALUES
(1, 1, 'LO-ST25-001', '2024-11-15', 'Xã Tân Hưng, Huyện Long Phú, Sóc Trăng', 'Hồ Quang Cua', 'VietGAP, GlobalGAP', '2024-11-20', '2025-11-20'),
(2, 4, 'LO-XOAI-001', '2024-12-01', 'Xã Hòa Lộc, Huyện Cái Bè, Tiền Giang', 'Nguyễn Văn Thành', 'VietGAP', '2024-12-02', '2024-12-15'),
(3, 15, 'LO-CATRA-001', '2024-12-10', 'Huyện Hồng Ngự, Đồng Tháp', 'Công ty TNHH Vĩnh Hoàn', 'ASC, GlobalGAP, BAP', '2024-12-11', '2025-06-11'),
(4, 16, 'LO-TOMSU-001', '2024-12-08', 'Huyện Năm Căn, Cà Mau', 'HTX Tôm Cà Mau', 'ASC, Organic', '2024-12-09', '2025-03-09'),
(5, 2, 'LO-JASMINE-001', '2024-12-05', 'Huyện Châu Đốc, An Giang', 'Nguyễn Văn Phát', 'VietGAP', '2024-12-08', '2025-12-08'),
(6, 5, 'LO-SAURIENG-001', '2024-11-25', 'Xã Hòa Lộc, Tiền Giang', 'Trần Văn Đức', 'VietGAP', '2024-11-26', '2024-12-26'),
(7, 6, 'LO-BUOI-001', '2024-12-01', 'Xã Phước Lập, Bến Tre', 'Lê Thị Mai', 'VietGAP', '2024-12-02', '2025-01-02'),
(8, 20, 'LO-NUOCMAM-001', '2024-11-01', 'Phú Quốc, Kiên Giang', 'Công ty Nước Mắm Phú Quốc', 'OCOP 5 sao', '2024-11-15', '2026-11-15'),
(9, 8, 'LO-THANHLONG-001', '2024-12-18', 'Thị xã Kiến Tường, Long An', 'Võ Văn Sơn', 'VietGAP', '2024-12-19', '2025-01-19'),
(10, 9, 'LO-DUA-001', '2024-12-15', 'Huyện Châu Thành, Bến Tre', 'Phạm Thị Lan', 'VietGAP', '2024-12-16', '2025-03-16');

-- =============================================
-- HOÀN TẤT DỮ LIỆU MẪU
-- Tổng: 15 bảng với đầy đủ dữ liệu test
-- =============================================
