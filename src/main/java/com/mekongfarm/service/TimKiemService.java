package com.mekongfarm.service;

import com.mekongfarm.dao.*;
import com.mekongfarm.model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service tìm kiếm toàn cục - tìm SP, KH, ĐH từ một ô tìm kiếm
 */
public class TimKiemService {

    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final DonHangDAO donHangDAO = new DonHangDAO();
    private final NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAO();

    /**
     * Kết quả tìm kiếm toàn cục
     */
    public static class KetQuaTimKiem {
        private String loai; // SP, KH, DH, NCC
        private String tieuDe;
        private String moTa;
        private int maDoiTuong;
        private String fxmlPath; // Đường dẫn FXML để điều hướng

        public KetQuaTimKiem(String loai, String tieuDe, String moTa, int maDoiTuong, String fxmlPath) {
            this.loai = loai;
            this.tieuDe = tieuDe;
            this.moTa = moTa;
            this.maDoiTuong = maDoiTuong;
            this.fxmlPath = fxmlPath;
        }

        // Getters
        public String getLoai() {
            return loai;
        }

        public String getTieuDe() {
            return tieuDe;
        }

        public String getMoTa() {
            return moTa;
        }

        public int getMaDoiTuong() {
            return maDoiTuong;
        }

        public String getFxmlPath() {
            return fxmlPath;
        }

        public String getIcon() {
            return switch (loai) {
                case "SP" -> "📦";
                case "KH" -> "👤";
                case "DH" -> "🛒";
                case "NCC" -> "🏭";
                default -> "📄";
            };
        }

        @Override
        public String toString() {
            return getIcon() + " " + tieuDe + " - " + moTa;
        }
    }

    /**
     * Tìm kiếm toàn cục
     * 
     * @param tuKhoa Từ khóa tìm kiếm
     * @return Danh sách kết quả (giới hạn 20)
     */
    public List<KetQuaTimKiem> timKiem(String tuKhoa) {
        List<KetQuaTimKiem> ketQua = new ArrayList<>();

        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return ketQua;
        }

        String keyword = tuKhoa.trim().toLowerCase();

        // Tìm sản phẩm
        for (SanPham sp : sanPhamDAO.layTatCa()) {
            if (phuHop(sp.getTenSanPham(), keyword) ||
                    phuHop(sp.getMaSP(), keyword)) {
                ketQua.add(new KetQuaTimKiem(
                        "SP",
                        sp.getTenSanPham(),
                        "Mã: " + sp.getMaSP() + " | Giá: " + String.format("%,.0f VNĐ", sp.getDonGia()),
                        sp.getMaSanPham(),
                        "/fxml/SanPham.fxml"));
            }
            if (ketQua.size() >= 20)
                break;
        }

        // Tìm khách hàng
        for (KhachHang kh : khachHangDAO.layTatCa()) {
            if (phuHop(kh.getHoTen(), keyword) ||
                    phuHop(kh.getSoDienThoai(), keyword) ||
                    phuHop(kh.getMaKH(), keyword)) {
                ketQua.add(new KetQuaTimKiem(
                        "KH",
                        kh.getHoTen(),
                        "Mã: " + kh.getMaKH() + " | SĐT: "
                                + (kh.getSoDienThoai() != null ? kh.getSoDienThoai() : "N/A"),
                        kh.getMaKhachHang(),
                        "/fxml/KhachHang.fxml"));
            }
            if (ketQua.size() >= 20)
                break;
        }

        // Tìm đơn hàng
        for (DonHang dh : donHangDAO.layTatCa()) {
            if (phuHop(dh.getMaDH(), keyword) ||
                    phuHop(dh.getTenKhachHang(), keyword)) {
                ketQua.add(new KetQuaTimKiem(
                        "DH",
                        "Đơn hàng: " + dh.getMaDH(),
                        "KH: " + dh.getTenKhachHang() + " | " + String.format("%,.0f VNĐ", dh.getThanhTien()),
                        dh.getMaDonHang(),
                        "/fxml/DonHang.fxml"));
            }
            if (ketQua.size() >= 20)
                break;
        }

        // Tìm nhà cung cấp
        for (NhaCungCap ncc : nhaCungCapDAO.layTatCa()) {
            if (phuHop(ncc.getTenNCC(), keyword) ||
                    phuHop(ncc.getDienThoai(), keyword)) {
                ketQua.add(new KetQuaTimKiem(
                        "NCC",
                        ncc.getTenNCC(),
                        "ĐT: " + (ncc.getDienThoai() != null ? ncc.getDienThoai() : "N/A"),
                        ncc.getMaNCC(),
                        "/fxml/NhaCungCap.fxml"));
            }
            if (ketQua.size() >= 20)
                break;
        }

        return ketQua;
    }

    private boolean phuHop(String text, String keyword) {
        if (text == null)
            return false;
        return text.toLowerCase().contains(keyword);
    }
}
