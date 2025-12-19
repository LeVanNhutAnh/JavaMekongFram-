package com.mekongfarm.model;

import java.time.LocalDate;

/**
 * Model đại diện cho Công nợ (phải thu từ khách hàng, phải trả cho NCC)
 */
public class CongNo {
    private int maCongNo;
    private String loaiCongNo; // "phai_thu" hoặc "phai_tra"
    private int maKH; // Khách hàng (nếu loại = phai_thu)
    private String tenKhachHang; // để hiển thị
    private int maNCC; // Nhà cung cấp (nếu loại = phai_tra)
    private String tenNCC; // để hiển thị
    private int maDH; // đơn hàng liên quan
    private double soTien;
    private LocalDate ngayPhatSinh;
    private LocalDate hanThanhToan;
    private String trangThai; // "Chưa thanh toán", "Đã thanh toán một phần", "Đã thanh toán"
    private double daThanhToan;
    private String ghiChu;

    public CongNo() {
        this.trangThai = "Chưa thanh toán";
        this.ngayPhatSinh = LocalDate.now();
        this.daThanhToan = 0;
        this.loaiCongNo = "phai_thu"; // Default
    }

    // Getters and Setters
    public int getMaCongNo() {
        return maCongNo;
    }

    public void setMaCongNo(int maCongNo) {
        this.maCongNo = maCongNo;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public String getLoaiCongNo() {
        return loaiCongNo;
    }

    public void setLoaiCongNo(String loaiCongNo) {
        this.loaiCongNo = loaiCongNo;
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

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public int getMaDH() {
        return maDH;
    }

    public void setMaDH(int maDH) {
        this.maDH = maDH;
    }

    public double getSoTien() {
        return soTien;
    }

    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }

    public LocalDate getNgayPhatSinh() {
        return ngayPhatSinh;
    }

    public void setNgayPhatSinh(LocalDate ngayPhatSinh) {
        this.ngayPhatSinh = ngayPhatSinh;
    }

    public LocalDate getHanThanhToan() {
        return hanThanhToan;
    }

    public void setHanThanhToan(LocalDate hanThanhToan) {
        this.hanThanhToan = hanThanhToan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public double getDaThanhToan() {
        return daThanhToan;
    }

    public void setDaThanhToan(double daThanhToan) {
        this.daThanhToan = daThanhToan;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    // Tính số tiền còn nợ
    public double getConNo() {
        double conNo = soTien - daThanhToan;
        return Math.max(0, conNo); // Không cho phép số âm
    }

    // Kiểm tra quá hạn
    public boolean isQuaHan() {
        return hanThanhToan != null && LocalDate.now().isAfter(hanThanhToan)
                && !"Đã thanh toán".equals(trangThai);
    }

    // Format số tiền
    public String getSoTienFormat() {
        return String.format("%,.0f VNĐ", soTien);
    }

    public String getConNoFormat() {
        return String.format("%,.0f VNĐ", getConNo());
    }
}
