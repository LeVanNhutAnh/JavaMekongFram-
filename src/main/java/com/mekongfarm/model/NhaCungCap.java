package com.mekongfarm.model;

/**
 * Model đại diện cho Nhà cung cấp
 */
public class NhaCungCap {
    private int maNCC;
    private String tenNCC;
    private String dienThoai;
    private String diaChi;
    private String email;
    private String loaiSanPham;
    private int danhGia; // 1-5 sao
    private String ghiChu;

    public NhaCungCap() {
    }

    public NhaCungCap(String tenNCC, String dienThoai, String diaChi) {
        this.tenNCC = tenNCC;
        this.dienThoai = dienThoai;
        this.diaChi = diaChi;
        this.danhGia = 5;
    }

    // Getters and Setters
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

    public String getDienThoai() {
        return dienThoai;
    }

    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(String loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public int getDanhGia() {
        return danhGia;
    }

    public void setDanhGia(int danhGia) {
        this.danhGia = danhGia;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    // Hiển thị đánh giá sao
    public String getDanhGiaSao() {
        return "⭐".repeat(Math.max(0, danhGia));
    }

    @Override
    public String toString() {
        return tenNCC;
    }
}
