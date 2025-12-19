package com.mekongfarm.model;

import java.time.LocalDate;

/**
 * Model đại diện cho Mùa vụ
 */
public class MuaVu {
    private int maMuaVu;
    private String tenMuaVu;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String sanPhamLienQuan; // danh sách mã SP, phân cách bởi dấu phẩy
    private double duBaoSanLuong; // kg
    private String moTa;
    private String trangThai; // "Sắp tới", "Đang diễn ra", "Đã kết thúc"

    public MuaVu() {
    }

    public MuaVu(String tenMuaVu, LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        this.tenMuaVu = tenMuaVu;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.capNhatTrangThai();
    }

    // Getters and Setters
    public int getMaMuaVu() {
        return maMuaVu;
    }

    public void setMaMuaVu(int maMuaVu) {
        this.maMuaVu = maMuaVu;
    }

    public String getTenMuaVu() {
        return tenMuaVu;
    }

    public void setTenMuaVu(String tenMuaVu) {
        this.tenMuaVu = tenMuaVu;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getSanPhamLienQuan() {
        return sanPhamLienQuan;
    }

    public void setSanPhamLienQuan(String sanPhamLienQuan) {
        this.sanPhamLienQuan = sanPhamLienQuan;
    }

    public double getDuBaoSanLuong() {
        return duBaoSanLuong;
    }

    public void setDuBaoSanLuong(double duBaoSanLuong) {
        this.duBaoSanLuong = duBaoSanLuong;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    // Tự động cập nhật trạng thái dựa trên ngày
    public void capNhatTrangThai() {
        LocalDate today = LocalDate.now();
        if (ngayBatDau != null && ngayKetThuc != null) {
            if (today.isBefore(ngayBatDau)) {
                this.trangThai = "Sắp tới";
            } else if (today.isAfter(ngayKetThuc)) {
                this.trangThai = "Đã kết thúc";
            } else {
                this.trangThai = "Đang diễn ra";
            }
        }
    }

    // Số ngày còn lại của mùa vụ
    public long getSoNgayConLai() {
        if (ngayKetThuc != null) {
            return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), ngayKetThuc);
        }
        return 0;
    }

    @Override
    public String toString() {
        return tenMuaVu;
    }
}
