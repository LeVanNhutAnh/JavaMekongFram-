package com.mekongfarm.model;

import java.time.LocalDateTime;

/**
 * Model Người dùng - Quản lý thông tin đăng nhập
 */
public class NguoiDung {

    private int maNguoiDung;
    private String tenDangNhap;
    private String matKhau;
    private String hoTen;
    private String vaiTro;
    private LocalDateTime ngayTao;
    private boolean trangThai;

    // Constructors
    public NguoiDung() {
    }

    public NguoiDung(String tenDangNhap, String matKhau, String hoTen, String vaiTro) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.vaiTro = vaiTro;
        this.trangThai = true;
    }

    // Getters và Setters
    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public boolean laQuanTri() {
        return "quan_tri".equals(vaiTro) || "ADMIN".equals(vaiTro);
    }

    /**
     * Lấy VaiTro enum từ chuỗi vaiTro
     */
    public VaiTro getVaiTroEnum() {
        if (vaiTro == null)
            return VaiTro.KHACH_HANG;
        switch (vaiTro.toUpperCase()) {
            case "ADMIN":
            case "QUAN_TRI":
                return VaiTro.ADMIN;
            case "NHAN_VIEN":
                return VaiTro.NHAN_VIEN;
            case "KE_TOAN":
                return VaiTro.KE_TOAN;
            default:
                return VaiTro.KHACH_HANG;
        }
    }

    /**
     * Kiểm tra có quyền quản lý không
     */
    public boolean coQuyenQuanLy() {
        return getVaiTroEnum().coQuyenQuanLy();
    }

    @Override
    public String toString() {
        return hoTen + " (" + tenDangNhap + ")";
    }
}
