-- =============================================
-- DỮ LIỆU MẪU - NÔNG SẢN ĐBSCL
-- =============================================

-- Người dùng mặc định (admin/admin123)
INSERT INTO nguoi_dung (ten_dang_nhap, mat_khau, ho_ten, vai_tro) VALUES
('admin', 'admin123', 'Quản trị viên', 'quan_tri'),
('nhanvien01', '123456', 'Nguyễn Văn An', 'nhan_vien');

-- 13 Tỉnh thành ĐBSCL
INSERT INTO tinh_thanh (ten_tinh, vung_mien) VALUES
('Long An', 'ĐBSCL'),
('Tiền Giang', 'ĐBSCL'),
('Bến Tre', 'ĐBSCL'),
('Trà Vinh', 'ĐBSCL'),
('Vĩnh Long', 'ĐBSCL'),
('Đồng Tháp', 'ĐBSCL'),
('An Giang', 'ĐBSCL'),
('Kiên Giang', 'ĐBSCL'),
('Hậu Giang', 'ĐBSCL'),
('Sóc Trăng', 'ĐBSCL'),
('Bạc Liêu', 'ĐBSCL'),
('Cà Mau', 'ĐBSCL'),
('Cần Thơ', 'ĐBSCL');

-- Loại sản phẩm
INSERT INTO loai_san_pham (ten_loai, mo_ta) VALUES
('Lúa gạo', 'Các loại gạo, lúa'),
('Trái cây', 'Các loại trái cây tươi'),
('Thủy sản', 'Cá, tôm, cua và các loại hải sản'),
('Rau củ', 'Các loại rau củ quả'),
('Đặc sản', 'Các sản phẩm đặc sản địa phương');

-- Sản phẩm mẫu
INSERT INTO san_pham (ma_sp, ten_san_pham, ma_loai, ma_tinh, don_gia, so_luong_ton, don_vi_tinh, mo_ta) VALUES
-- Lúa gạo
('SP001', 'Gạo ST25', 1, 10, 45000, 500, 'kg', 'Gạo ST25 Sóc Trăng - Gạo ngon nhất thế giới'),
('SP002', 'Gạo Jasmine', 1, 7, 25000, 800, 'kg', 'Gạo thơm Jasmine An Giang'),
('SP003', 'Gạo Nàng Hoa', 1, 6, 28000, 600, 'kg', 'Gạo Nàng Hoa Đồng Tháp'),

-- Trái cây
('SP004', 'Xoài cát Hòa Lộc', 2, 2, 65000, 200, 'kg', 'Xoài cát Hòa Lộc Tiền Giang - Đặc sản'),
('SP005', 'Sầu riêng Ri6', 2, 2, 120000, 150, 'kg', 'Sầu riêng Ri6 Tiền Giang'),
('SP006', 'Bưởi da xanh', 2, 3, 35000, 300, 'kg', 'Bưởi da xanh Bến Tre'),
('SP007', 'Bưởi Năm Roi', 2, 5, 30000, 400, 'kg', 'Bưởi Năm Roi Vĩnh Long'),
('SP008', 'Thanh long ruột đỏ', 2, 1, 25000, 350, 'kg', 'Thanh long ruột đỏ Long An'),
('SP009', 'Dừa xiêm', 2, 3, 15000, 500, 'trái', 'Dừa xiêm Bến Tre'),
('SP010', 'Khóm Cầu Đúc', 2, 9, 20000, 250, 'kg', 'Khóm (thơm) Cầu Đúc Hậu Giang'),
('SP011', 'Mít Thái', 2, 1, 40000, 180, 'kg', 'Mít Thái Long An'),
('SP012', 'Chôm chôm', 2, 3, 35000, 220, 'kg', 'Chôm chôm Bến Tre'),
('SP013', 'Vú sữa Lò Rèn', 2, 2, 55000, 120, 'kg', 'Vú sữa Lò Rèn Tiền Giang'),
('SP014', 'Măng cụt', 2, 3, 80000, 100, 'kg', 'Măng cụt Bến Tre'),

-- Thủy sản
('SP015', 'Cá tra fillet', 3, 6, 85000, 400, 'kg', 'Cá tra fillet xuất khẩu Đồng Tháp'),
('SP016', 'Tôm sú', 3, 12, 250000, 100, 'kg', 'Tôm sú Cà Mau'),
('SP017', 'Tôm thẻ chân trắng', 3, 11, 180000, 150, 'kg', 'Tôm thẻ Bạc Liêu'),
('SP018', 'Cua biển', 3, 12, 350000, 80, 'kg', 'Cua biển Cà Mau'),
('SP019', 'Cá lóc', 3, 7, 70000, 200, 'kg', 'Cá lóc đồng An Giang'),

-- Đặc sản
('SP020', 'Nước mắm Phú Quốc', 5, 8, 120000, 300, 'chai', 'Nước mắm nhĩ Phú Quốc'),
('SP021', 'Bánh tráng Mỹ Lồng', 5, 3, 45000, 200, 'kg', 'Bánh tráng dừa Mỹ Lồng Bến Tre'),
('SP022', 'Kẹo dừa Bến Tre', 5, 3, 60000, 250, 'kg', 'Kẹo dừa thương hiệu Bến Tre'),
('SP023', 'Mắm cá linh', 5, 7, 80000, 100, 'hũ', 'Mắm cá linh An Giang'),
('SP024', 'Bánh pía Sóc Trăng', 5, 10, 55000, 180, 'hộp', 'Bánh pía đặc sản Sóc Trăng');

-- Khách hàng mẫu
INSERT INTO khach_hang (ma_kh, ho_ten, dia_chi, so_dien_thoai, email) VALUES
('KH001', 'Nguyễn Thị Mai', '123 Đường Nguyễn Huệ, Q.1, TP.HCM', '0901234567', 'mai.nguyen@email.com'),
('KH002', 'Trần Văn Hùng', '456 Đường Lê Lợi, Q.3, TP.HCM', '0912345678', 'hung.tran@email.com'),
('KH003', 'Lê Thị Hồng', '789 Đường Cách Mạng Tháng 8, Cần Thơ', '0923456789', 'hong.le@email.com'),
('KH004', 'Phạm Văn Đức', '321 Đường 30/4, Vĩnh Long', '0934567890', 'duc.pham@email.com'),
('KH005', 'Huỳnh Thị Lan', '654 Đường Trần Hưng Đạo, Mỹ Tho', '0945678901', 'lan.huynh@email.com');

-- Đơn hàng mẫu
INSERT INTO don_hang (ma_dh, ma_khach_hang, ma_nguoi_dung, tong_tien, thanh_tien, trang_thai) VALUES
('DH001', 1, 1, 500000, 500000, 'da_giao'),
('DH002', 2, 1, 750000, 750000, 'dang_giao'),
('DH003', 3, 2, 1200000, 1200000, 'cho_xu_ly');

-- Chi tiết đơn hàng
INSERT INTO chi_tiet_don_hang (ma_don_hang, ma_san_pham, so_luong, don_gia, thanh_tien) VALUES
(1, 1, 10, 45000, 450000),
(1, 4, 1, 50000, 50000),
(2, 15, 5, 85000, 425000),
(2, 16, 2, 162500, 325000),
(3, 5, 10, 120000, 1200000);

-- Truy xuất nguồn gốc mẫu
INSERT INTO truy_xuat_nguon_goc (ma_san_pham, so_lo, ngay_thu_hoach, dia_chi_san_xuat, ten_nong_dan, chung_nhan, ngay_san_xuat, han_su_dung) VALUES
(1, 'LO-ST25-001', '2024-11-15', 'Xã Tân Hưng, Huyện Long Phú, Sóc Trăng', 'Hồ Quang Cua', 'VietGAP, GlobalGAP', '2024-11-20', '2025-11-20'),
(4, 'LO-XOAI-001', '2024-12-01', 'Xã Hòa Lộc, Huyện Cái Bè, Tiền Giang', 'Nguyễn Văn Thành', 'VietGAP', '2024-12-02', '2024-12-15'),
(15, 'LO-CATRA-001', '2024-12-10', 'Huyện Hồng Ngự, Đồng Tháp', 'Công ty TNHH Vĩnh Hoàn', 'ASC, GlobalGAP, BAP', '2024-12-11', '2025-06-11');
