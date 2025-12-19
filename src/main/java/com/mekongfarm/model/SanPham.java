package com.mekongfarm.model;

import java.time.LocalDateTime;

/**
 * Model Sản phẩm - Nông sản ĐBSCL
 */
public class SanPham {

    private int maSanPham;
    private String maSP;
    private String tenSanPham;
    private int maLoai;
    private String tenLoai;
    private int maTinh;
    private String tenTinh;
    private int maNCC; // Mã nhà cung cấp
    private String tenNCC; // Tên nhà cung cấp
    private double donGia;
    private int soLuongTon;
    private String donViTinh;
    private String moTa;
    private String hinhAnh;
    private LocalDateTime ngayTao;
    private boolean trangThai;

    // Constructors
    public SanPham() {
    }

    public SanPham(String maSP, String tenSanPham, int maLoai, int maTinh,
            double donGia, int soLuongTon, String donViTinh) {
        this.maSP = maSP;
        this.tenSanPham = tenSanPham;
        this.maLoai = maLoai;
        this.maTinh = maTinh;
        this.donGia = donGia;
        this.soLuongTon = soLuongTon;
        this.donViTinh = donViTinh;
        this.trangThai = true;
    }

    // Getters và Setters
    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public int getMaTinh() {
        return maTinh;
    }

    public void setMaTinh(int maTinh) {
        this.maTinh = maTinh;
    }

    public String getTenTinh() {
        return tenTinh;
    }

    public void setTenTinh(String tenTinh) {
        this.tenTinh = tenTinh;
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

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    /**
     * Định dạng giá tiền theo format Việt Nam
     */
    public String getDonGiaFormat() {
        return String.format("%,.0f VNĐ", donGia);
    }

    /**
     * Hiển thị số lượng với đơn vị
     */
    public String getSoLuongHienThi() {
        return soLuongTon + " " + donViTinh;
    }

    @Override
    public String toString() {
        return tenSanPham + " - " + getDonGiaFormat();
    }
}
