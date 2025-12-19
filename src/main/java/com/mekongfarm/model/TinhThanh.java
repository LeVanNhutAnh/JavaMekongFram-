package com.mekongfarm.model;

/**
 * Model Tỉnh thành - 13 tỉnh ĐBSCL
 */
public class TinhThanh {

    private int maTinh;
    private String tenTinh;
    private String vungMien;

    // Constructors
    public TinhThanh() {
    }

    public TinhThanh(int maTinh, String tenTinh) {
        this.maTinh = maTinh;
        this.tenTinh = tenTinh;
        this.vungMien = "ĐBSCL";
    }

    // Getters và Setters
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

    public String getVungMien() {
        return vungMien;
    }

    public void setVungMien(String vungMien) {
        this.vungMien = vungMien;
    }

    @Override
    public String toString() {
        return tenTinh;
    }
}
