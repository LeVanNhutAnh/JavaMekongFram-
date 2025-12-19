package com.mekongfarm.model;

import java.time.LocalDate;

/**
 * Model đại diện cho Báo giá từ nhà cung cấp
 */
public class BaoGia {
    private int maBaoGia;
    private int maSanPham;
    private String tenSanPham;
    private int maNCC;
    private String tenNCC;
    private double donGia;
    private int soLuongToiThieu;
    private int thoiGianGiao; // Số ngày giao hàng
    private String dieuKien;
    private LocalDate ngayBaoGia;
    private LocalDate hanHieuLuc;
    private String trangThai; // "hieu_luc", "het_han", "da_chon"
    private String ghiChu;

    public BaoGia() {
        this.ngayBaoGia = LocalDate.now();
        this.hanHieuLuc = LocalDate.now().plusDays(30); // Mặc định 30 ngày
        this.trangThai = "hieu_luc";
    }

    // Getters and Setters
    public int getMaBaoGia() {
        return maBaoGia;
    }

    public void setMaBaoGia(int maBaoGia) {
        this.maBaoGia = maBaoGia;
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

    public int getSoLuongToiThieu() {
        return soLuongToiThieu;
    }

    public void setSoLuongToiThieu(int soLuongToiThieu) {
        this.soLuongToiThieu = soLuongToiThieu;
    }

    public int getThoiGianGiao() {
        return thoiGianGiao;
    }

    public void setThoiGianGiao(int thoiGianGiao) {
        this.thoiGianGiao = thoiGianGiao;
    }

    public String getDieuKien() {
        return dieuKien;
    }

    public void setDieuKien(String dieuKien) {
        this.dieuKien = dieuKien;
    }

    public LocalDate getNgayBaoGia() {
        return ngayBaoGia;
    }

    public void setNgayBaoGia(LocalDate ngayBaoGia) {
        this.ngayBaoGia = ngayBaoGia;
    }

    public LocalDate getHanHieuLuc() {
        return hanHieuLuc;
    }

    public void setHanHieuLuc(LocalDate hanHieuLuc) {
        this.hanHieuLuc = hanHieuLuc;
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

    // Kiểm tra đã hết hạn chưa
    public boolean daHetHan() {
        return hanHieuLuc != null && LocalDate.now().isAfter(hanHieuLuc);
    }

    // Lấy số ngày còn hiệu lực
    public long soNgayConHieuLuc() {
        if (hanHieuLuc == null || daHetHan())
            return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), hanHieuLuc);
    }

    @Override
    public String toString() {
        return String.format("BG-%06d - %s - %s - %.0f VND", maBaoGia, tenSanPham, tenNCC, donGia);
    }
}
