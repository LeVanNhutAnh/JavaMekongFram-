package com.mekongfarm.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model Đơn nhập hàng từ Nhà cung cấp
 */
public class DonNhap {

    private int maDonNhap;
    private String maDon;
    private int maNCC;
    private String tenNCC;
    private int maNguoiDung;
    private String tenNguoiDung;
    private LocalDateTime ngayNhap;
    private double tongTien;
    private double giamGia;
    private double thanhTien;
    private String trangThai;
    private boolean daNhapKho;
    private String ghiChu;

    // Constructors
    public DonNhap() {
        this.ngayNhap = LocalDateTime.now();
        this.trangThai = "cho_duyet";
        this.daNhapKho = false;
    }

    // Getters and Setters
    public int getMaDonNhap() {
        return maDonNhap;
    }

    public void setMaDonNhap(int maDonNhap) {
        this.maDonNhap = maDonNhap;
    }

    public String getMaDon() {
        return maDon;
    }

    public void setMaDon(String maDon) {
        this.maDon = maDon;
    }

    public int getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(int maNCC) {
        this.maNCC = maNCC;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
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

    public LocalDateTime getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDateTime ngayNhap) {
        this.ngayNhap = ngayNhap;
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

    public boolean isDaNhapKho() {
        return daNhapKho;
    }

    public void setDaNhapKho(boolean daNhapKho) {
        this.daNhapKho = daNhapKho;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    // Display methods
    public String getNgayNhapFormat() {
        if (ngayNhap == null)
            return "";
        return ngayNhap.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getThanhTienFormat() {
        return String.format("%,.0f VNĐ", thanhTien);
    }

    public String getTrangThaiHienThi() {
        if (trangThai == null)
            return "Chờ duyệt";
        if (trangThai.equals("cho_duyet"))
            return "Chờ duyệt";
        if (trangThai.equals("da_duyet"))
            return "Đã duyệt";
        if (trangThai.equals("da_nhap"))
            return "Đã nhập kho";
        if (trangThai.equals("da_huy"))
            return "Đã hủy";
        return trangThai;
    }

    @Override
    public String toString() {
        return maDon + " - " + tenNCC;
    }
}
