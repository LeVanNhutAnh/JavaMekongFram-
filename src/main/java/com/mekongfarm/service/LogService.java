package com.mekongfarm.service;

import com.mekongfarm.dao.LogDAO;
import com.mekongfarm.model.LogHoatDong;
import java.util.List;

/**
 * Service ghi log hoạt động
 */
public class LogService {

    private static final LogDAO dao = new LogDAO();
    private static int currentUserId = 0;

    /**
     * Đặt user hiện tại
     */
    public static void setCurrentUser(int userId) {
        currentUserId = userId;
    }

    /**
     * Log đăng nhập
     */
    public static void logDangNhap(int userId, String tenUser) {
        LogHoatDong log = new LogHoatDong(userId, "DANG_NHAP", "Đăng nhập hệ thống: " + tenUser);
        dao.them(log);
    }

    /**
     * Log đăng xuất
     */
    public static void logDangXuat(int userId) {
        LogHoatDong log = new LogHoatDong(userId, "DANG_XUAT", "Đăng xuất hệ thống");
        dao.them(log);
    }

    /**
     * Log thêm mới
     */
    public static void logThem(String bang, int maDoiTuong, String moTa) {
        LogHoatDong log = new LogHoatDong(currentUserId, "THEM", moTa);
        log.setBangLienQuan(bang);
        log.setMaDoiTuong(maDoiTuong);
        dao.them(log);
    }

    /**
     * Log cập nhật
     */
    public static void logCapNhat(String bang, int maDoiTuong, String moTa) {
        LogHoatDong log = new LogHoatDong(currentUserId, "CAP_NHAT", moTa);
        log.setBangLienQuan(bang);
        log.setMaDoiTuong(maDoiTuong);
        dao.them(log);
    }

    /**
     * Log xóa
     */
    public static void logXoa(String bang, int maDoiTuong, String moTa) {
        LogHoatDong log = new LogHoatDong(currentUserId, "XOA", moTa);
        log.setBangLienQuan(bang);
        log.setMaDoiTuong(maDoiTuong);
        dao.them(log);
    }

    /**
     * Log xuất file
     */
    public static void logXuatFile(String loaiFile, String tenFile) {
        LogHoatDong log = new LogHoatDong(currentUserId, "XUAT_FILE", "Xuất " + loaiFile + ": " + tenFile);
        dao.them(log);
    }

    /**
     * Lấy lịch sử hoạt động
     */
    public static List<LogHoatDong> layLichSu(int limit) {
        return dao.layTatCa(limit);
    }

    /**
     * Lấy lịch sử theo user
     */
    public static List<LogHoatDong> layLichSuTheoUser(int userId, int limit) {
        return dao.layTheoUser(userId, limit);
    }
}
