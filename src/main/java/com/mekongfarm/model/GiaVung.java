package com.mekongfarm.model;

import java.time.LocalDate;

/**
 * Model đại diện cho Giá theo vùng/tỉnh
 */
public class GiaVung {
    private int id;
    private int maSP;
    private String tenSanPham; // để hiển thị
    private int maTinh;
    private String tenTinh; // để hiển thị
    private double giaRieng;
    private LocalDate ngayApDung;
    private String ghiChu;

    public GiaVung() {
        this.ngayApDung = LocalDate.now();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
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

    public double getGiaRieng() {
        return giaRieng;
    }

    public void setGiaRieng(double giaRieng) {
        this.giaRieng = giaRieng;
    }

    public LocalDate getNgayApDung() {
        return ngayApDung;
    }

    public void setNgayApDung(LocalDate ngayApDung) {
        this.ngayApDung = ngayApDung;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getGiaFormat() {
        return String.format("%,.0f VNĐ", giaRieng);
    }
}
