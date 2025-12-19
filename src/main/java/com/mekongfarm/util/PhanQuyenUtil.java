package com.mekongfarm.util;

import com.mekongfarm.controller.DangNhapController;
import com.mekongfarm.model.NguoiDung;
import com.mekongfarm.model.VaiTro;

/**
 * Utility kiểm tra quyền truy cập của người dùng
 */
public class PhanQuyenUtil {

    /**
     * Lấy vai trò của người dùng hiện tại
     */
    public static VaiTro getVaiTro() {
        NguoiDung user = DangNhapController.nguoiDungHienTai;
        if (user == null)
            return VaiTro.KHACH_HANG;
        return user.getVaiTroEnum();
    }

    /**
     * Kiểm tra có phải admin không
     */
    public static boolean laAdmin() {
        return getVaiTro().laAdmin();
    }

    /**
     * Có quyền quản lý sản phẩm không (Admin)
     */
    public static boolean coQuyenSanPham() {
        return getVaiTro().coQuyenSanPham();
    }

    /**
     * Có quyền quản lý đơn hàng không (Admin, Nhân viên)
     */
    public static boolean coQuyenDonHang() {
        return getVaiTro().coQuyenDonHang();
    }

    /**
     * Có quyền xem thống kê không (Admin, Kế toán)
     */
    public static boolean coQuyenThongKe() {
        return getVaiTro().coQuyenThongKe();
    }

    /**
     * Có quyền quản lý user không (Admin)
     */
    public static boolean coQuyenQuanLyUser() {
        return getVaiTro().coQuyenQuanLyUser();
    }

    /**
     * Có quyền quản lý chung không (Admin, Nhân viên)
     */
    public static boolean coQuyenQuanLy() {
        return getVaiTro().coQuyenQuanLy();
    }

    /**
     * Có quyền quản lý NCC + Nhập hàng không (Admin, Nhân viên)
     */
    public static boolean coQuyenNhapHang() {
        return getVaiTro().coQuyenQuanLy();
    }

    /**
     * Có quyền xem lịch sử và cài đặt không (Admin)
     */
    public static boolean coQuyenHeThong() {
        return getVaiTro().laAdmin();
    }
}
