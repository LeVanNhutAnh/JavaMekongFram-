package com.mekongfarm.model;

/**
 * Model Loại sản phẩm
 */
public class LoaiSanPham {

    private int maLoai;
    private String tenLoai;
    private String moTa;

    // Constructors
    public LoaiSanPham() {
    }

    public LoaiSanPham(int maLoai, String tenLoai) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
    }

    // Getters và Setters
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

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return tenLoai;
    }
}
