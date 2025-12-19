package com.mekongfarm.model;

import java.time.LocalDateTime;

/**
 * Model Log hoạt động
 */
public class LogHoatDong {

    private int id;
    private int maNguoiDung;
    private String tenNguoiDung;
    private String loaiHoatDong; // DANG_NHAP, THEM, SUA, XOA, XUAT_FILE, etc.
    private String moTa;
    private String bangLienQuan; // san_pham, don_hang, khach_hang, etc.
    private int maDoiTuong;
    private LocalDateTime thoiGian;
    private String diaChi_IP;

    public LogHoatDong() {
        this.thoiGian = LocalDateTime.now();
    }

    public LogHoatDong(int maNguoiDung, String loaiHoatDong, String moTa) {
        this.maNguoiDung = maNguoiDung;
        this.loaiHoatDong = loaiHoatDong;
        this.moTa = moTa;
        this.thoiGian = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getLoaiHoatDong() {
        return loaiHoatDong;
    }

    public void setLoaiHoatDong(String loaiHoatDong) {
        this.loaiHoatDong = loaiHoatDong;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getBangLienQuan() {
        return bangLienQuan;
    }

    public void setBangLienQuan(String bangLienQuan) {
        this.bangLienQuan = bangLienQuan;
    }

    public int getMaDoiTuong() {
        return maDoiTuong;
    }

    public void setMaDoiTuong(int maDoiTuong) {
        this.maDoiTuong = maDoiTuong;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getThoiGianFormat() {
        return thoiGian != null ? thoiGian.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                : "";
    }

    public String getDiaChi_IP() {
        return diaChi_IP;
    }

    public void setDiaChi_IP(String diaChi_IP) {
        this.diaChi_IP = diaChi_IP;
    }
}
