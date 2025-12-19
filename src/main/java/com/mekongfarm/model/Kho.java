package com.mekongfarm.model;

/**
 * Model đại diện cho Kho hàng
 */
public class Kho {
    private int maKho;
    private String tenKho;
    private String diaChi;
    private String nguoiQuanLy;
    private String dienThoai;
    private double sucChua; // kg hoặc đơn vị tính
    private boolean hoatDong;

    public Kho() {
        this.hoatDong = true;
    }

    public Kho(String tenKho, String diaChi) {
        this.tenKho = tenKho;
        this.diaChi = diaChi;
        this.hoatDong = true;
    }

    // Getters and Setters
    public int getMaKho() {
        return maKho;
    }

    public void setMaKho(int maKho) {
        this.maKho = maKho;
    }

    public String getTenKho() {
        return tenKho;
    }

    public void setTenKho(String tenKho) {
        this.tenKho = tenKho;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getNguoiQuanLy() {
        return nguoiQuanLy;
    }

    public void setNguoiQuanLy(String nguoiQuanLy) {
        this.nguoiQuanLy = nguoiQuanLy;
    }

    public String getDienThoai() {
        return dienThoai;
    }

    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }

    public double getSucChua() {
        return sucChua;
    }

    public void setSucChua(double sucChua) {
        this.sucChua = sucChua;
    }

    public boolean isHoatDong() {
        return hoatDong;
    }

    public void setHoatDong(boolean hoatDong) {
        this.hoatDong = hoatDong;
    }

    public String getTrangThaiHoatDong() {
        return hoatDong ? "Hoạt động" : "Tạm ngưng";
    }

    @Override
    public String toString() {
        return tenKho;
    }
}
