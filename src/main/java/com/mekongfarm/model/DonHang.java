package com.mekongfarm.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Model Đơn hàng
 */
public class DonHang {

    private int maDonHang;
    private String maDH;
    private int maKhachHang;
    private String tenKhachHang;
    private int maNguoiDung;
    private String tenNguoiDung;
    private LocalDateTime ngayDat;
    private double tongTien;
    private double giamGia;
    private double thanhTien;
    private String trangThai;
    private String ghiChu;

    private List<ChiTietDonHang> chiTietList;

    // Constructors
    public DonHang() {
        this.chiTietList = new ArrayList<>();
    }

    public DonHang(String maDH, int maKhachHang, int maNguoiDung) {
        this.maDH = maDH;
        this.maKhachHang = maKhachHang;
        this.maNguoiDung = maNguoiDung;
        this.ngayDat = LocalDateTime.now();
        this.trangThai = "cho_xu_ly";
        this.chiTietList = new ArrayList<>();
    }

    // Getters và Setters
    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getMaDH() {
        return maDH;
    }

    public void setMaDH(String maDH) {
        this.maDH = maDH;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public LocalDateTime getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(LocalDateTime ngayDat) {
        this.ngayDat = ngayDat;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public double getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(double giamGia) {
        this.giamGia = giamGia;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public List<ChiTietDonHang> getChiTietList() {
        return chiTietList;
    }

    public void setChiTietList(List<ChiTietDonHang> chiTietList) {
        this.chiTietList = chiTietList;
    }

    /**
     * Thêm chi tiết vào đơn hàng
     */
    public void themChiTiet(ChiTietDonHang chiTiet) {
        chiTietList.add(chiTiet);
        tinhTongTien();
    }

    /**
     * Xóa chi tiết khỏi đơn hàng
     */
    public void xoaChiTiet(ChiTietDonHang chiTiet) {
        chiTietList.remove(chiTiet);
        tinhTongTien();
    }

    /**
     * Tính tổng tiền tự động
     */
    public void tinhTongTien() {
        tongTien = chiTietList.stream()
                .mapToDouble(ChiTietDonHang::getThanhTien)
                .sum();
        thanhTien = tongTien - giamGia;
    }

    /**
     * Lấy trạng thái hiển thị
     */
    public String getTrangThaiHienThi() {
        if (trangThai == null)
            return "";
        if ("cho_xu_ly".equals(trangThai))
            return "Chờ xử lý";
        if ("dang_giao".equals(trangThai))
            return "Đang giao";
        if ("da_giao".equals(trangThai))
            return "Đã giao";
        if ("da_huy".equals(trangThai))
            return "Đã hủy";
        return trangThai; // Fallback
    }

    /**
     * Định dạng ngày
     */
    public String getNgayDatFormat() {
        if (ngayDat == null)
            return "";
        return ngayDat.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    /**
     * Định dạng thành tiền
     */
    public String getThanhTienFormat() {
        return String.format("%,.0f VNĐ", thanhTien);
    }

    @Override
    public String toString() {
        return maDH + " - " + getTrangThaiHienThi();
    }
}
