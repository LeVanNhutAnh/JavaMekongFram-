package com.mekongfarm.model;

/**
 * Model Chi tiết đơn nhập hàng
 */
public class ChiTietDonNhap {

    private int maChiTiet;
    private int maDonNhap;
    private int maSanPham;
    private String tenSanPham;
    private String donViTinh;
    private int soLuong;
    private double donGia;
    private double thanhTien;

    // Constructors
    public ChiTietDonNhap() {
    }

    public ChiTietDonNhap(int maSanPham, String tenSanPham, int soLuong, double donGia) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = soLuong * donGia;
    }

    // Getters and Setters
    public int getMaChiTiet() {
        return maChiTiet;
    }

    public void setMaChiTiet(int maChiTiet) {
        this.maChiTiet = maChiTiet;
    }

    public int getMaDonNhap() {
        return maDonNhap;
    }

    public void setMaDonNhap(int maDonNhap) {
        this.maDonNhap = maDonNhap;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
        tinhThanhTien();
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
        tinhThanhTien();
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    private void tinhThanhTien() {
        this.thanhTien = this.soLuong * this.donGia;
    }

    // Display methods
    public String getDonGiaFormat() {
        return String.format("%,.0f", donGia);
    }

    public String getThanhTienFormat() {
        return String.format("%,.0f", thanhTien);
    }
}
