package com.mekongfarm.model;

/**
 * Model Chi tiết đơn hàng
 */
public class ChiTietDonHang {

    private int maChiTiet;
    private int maDonHang;
    private int maSanPham;
    private String tenSanPham;
    private String donViTinh;
    private int soLuong;
    private double donGia;
    private double thanhTien;

    // Constructors
    public ChiTietDonHang() {
    }

    public ChiTietDonHang(int maSanPham, int soLuong, double donGia) {
        this.maSanPham = maSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = soLuong * donGia;
    }

    public ChiTietDonHang(SanPham sanPham, int soLuong) {
        this.maSanPham = sanPham.getMaSanPham();
        this.tenSanPham = sanPham.getTenSanPham();
        this.donViTinh = sanPham.getDonViTinh();
        this.soLuong = soLuong;
        this.donGia = sanPham.getDonGia();
        this.thanhTien = soLuong * donGia;
    }

    // Getters và Setters
    public int getMaChiTiet() {
        return maChiTiet;
    }

    public void setMaChiTiet(int maChiTiet) {
        this.maChiTiet = maChiTiet;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
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

    /**
     * Tính thành tiền = số lượng × đơn giá
     */
    private void tinhThanhTien() {
        this.thanhTien = soLuong * donGia;
    }

    /**
     * Định dạng đơn giá
     */
    public String getDonGiaFormat() {
        return String.format("%,.0f", donGia);
    }

    /**
     * Định dạng thành tiền
     */
    public String getThanhTienFormat() {
        return String.format("%,.0f", thanhTien);
    }

    @Override
    public String toString() {
        return tenSanPham + " x " + soLuong;
    }
}
