package com.mekongfarm.model;

import java.time.LocalDate;

/**
 * Model Truy xuất nguồn gốc
 */
public class TruyXuatNguonGoc {

    private int maTruyXuat;
    private int maSanPham;
    private String tenSanPham;
    private String soLo;
    private LocalDate ngayThuHoach;
    private String diaChiSanXuat;
    private String tenNongDan;
    private String chungNhan;
    private LocalDate ngaySanXuat;
    private LocalDate hanSuDung;
    private String maQR;
    private int maNCC; // FK to NhaCungCap
    private String tenNCC; // Tên nhà cung cấp (để hiển thị)

    // Constructors
    public TruyXuatNguonGoc() {
    }

    // Getters và Setters
    public int getMaTruyXuat() {
        return maTruyXuat;
    }

    public void setMaTruyXuat(int maTruyXuat) {
        this.maTruyXuat = maTruyXuat;
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

    public String getSoLo() {
        return soLo;
    }

    public void setSoLo(String soLo) {
        this.soLo = soLo;
    }

    public LocalDate getNgayThuHoach() {
        return ngayThuHoach;
    }

    public void setNgayThuHoach(LocalDate ngayThuHoach) {
        this.ngayThuHoach = ngayThuHoach;
    }

    public String getDiaChiSanXuat() {
        return diaChiSanXuat;
    }

    public void setDiaChiSanXuat(String diaChiSanXuat) {
        this.diaChiSanXuat = diaChiSanXuat;
    }

    public String getTenNongDan() {
        return tenNongDan;
    }

    public void setTenNongDan(String tenNongDan) {
        this.tenNongDan = tenNongDan;
    }

    public String getChungNhan() {
        return chungNhan;
    }

    public void setChungNhan(String chungNhan) {
        this.chungNhan = chungNhan;
    }

    public LocalDate getNgaySanXuat() {
        return ngaySanXuat;
    }

    public void setNgaySanXuat(LocalDate ngaySanXuat) {
        this.ngaySanXuat = ngaySanXuat;
    }

    public LocalDate getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(LocalDate hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

    public String getMaQR() {
        return maQR;
    }

    public void setMaQR(String maQR) {
        this.maQR = maQR;
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

    /**
     * Kiểm tra còn hạn sử dụng không
     */
    public boolean conHanSuDung() {
        if (hanSuDung == null)
            return true;
        return LocalDate.now().isBefore(hanSuDung);
    }

    /**
     * Lấy danh sách chứng nhận
     */
    public String[] getDanhSachChungNhan() {
        if (chungNhan == null || chungNhan.isEmpty()) {
            return new String[0];
        }
        return chungNhan.split(",\\s*");
    }

    @Override
    public String toString() {
        return soLo + " - " + tenSanPham;
    }
}
