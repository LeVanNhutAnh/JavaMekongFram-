package com.mekongfarm.model;

/**
 * Định nghĩa các vai trò người dùng
 */
public enum VaiTro {
    ADMIN("Quản trị viên"),
    NHAN_VIEN("Nhân viên"),
    KE_TOAN("Kế toán"),
    KHACH_HANG("Khách hàng");

    private final String tenHienThi;

    VaiTro(String tenHienThi) {
        this.tenHienThi = tenHienThi;
    }

    public String getTenHienThi() {
        return tenHienThi;
    }

    /**
     * Kiểm tra xem vai trò có quyền quản lý không (Admin và Nhân viên)
     */
    public boolean coQuyenQuanLy() {
        return this == ADMIN || this == NHAN_VIEN;
    }

    /**
     * Kiểm tra xem vai trò có quyền admin không
     */
    public boolean laAdmin() {
        return this == ADMIN;
    }

    /**
     * Kiểm tra xem có quyền xem thống kê không (Admin và Kế toán)
     */
    public boolean coQuyenThongKe() {
        return this == ADMIN || this == KE_TOAN;
    }

    /**
     * Kiểm tra xem có quyền quản lý đơn hàng không (Admin, Nhân viên)
     */
    public boolean coQuyenDonHang() {
        return this == ADMIN || this == NHAN_VIEN;
    }

    /**
     * Kiểm tra xem có quyền quản lý sản phẩm không (chỉ Admin)
     */
    public boolean coQuyenSanPham() {
        return this == ADMIN;
    }

    /**
     * Kiểm tra xem có quyền quản lý user không (chỉ Admin)
     */
    public boolean coQuyenQuanLyUser() {
        return this == ADMIN;
    }

    @Override
    public String toString() {
        return tenHienThi;
    }
}
