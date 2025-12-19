-- Thêm dữ liệu mẫu truy xuất nguồn gốc nếu chưa có
INSERT OR IGNORE INTO truy_xuat_nguon_goc (ma_san_pham, so_lo, ngay_thu_hoach, dia_chi_san_xuat, ten_nong_dan, chung_nhan, ngay_san_xuat, han_su_dung) 
VALUES 
(1, 'LO-ST25-001', '2024-11-15', 'Xã Tân Hưng, Huyện Long Phú, Sóc Trăng', 'Hồ Quang Cua', 'VietGAP, GlobalGAP', '2024-11-20', '2025-11-20'),
(2, 'LO-XOAI-001', '2024-12-01', 'Xã Hòa Lộc, Huyện Cái Bè, Tiền Giang', 'Nguyễn Văn Thành', 'VietGAP', '2024-12-02', '2024-12-15'),
(3, 'LO-TOMSU-001', '2024-12-10', 'Huyện Năm Căn, Cà Mau', 'Công ty TNHH Tôm Cà Mau', 'ASC, GlobalGAP', '2024-12-11', '2025-03-11');
