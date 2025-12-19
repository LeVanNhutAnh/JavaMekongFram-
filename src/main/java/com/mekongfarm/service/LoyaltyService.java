package com.mekongfarm.service;

import com.mekongfarm.dao.KhachHangDAO;
import com.mekongfarm.model.KhachHang;

/**
 * Customer Loyalty - Tích điểm, khuyến mãi
 */
public class LoyaltyService {

    private static final int POINTS_PER_10K = 1; // 1 điểm mỗi 10,000 VNĐ
    private static final double DISCOUNT_PER_POINT = 100; // 100 VNĐ mỗi điểm

    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    /**
     * Tính điểm từ đơn hàng
     */
    public int tinhDiem(double tongTien) {
        return (int) (tongTien / 10000) * POINTS_PER_10K;
    }

    /**
     * Cộng điểm cho khách hàng
     */
    public void congDiem(int maKH, double tongTienDonHang) {
        int diemMoi = tinhDiem(tongTienDonHang);
        // TODO: Cập nhật điểm trong database
        System.out.println("🎁 Cộng " + diemMoi + " điểm cho KH #" + maKH);
    }

    /**
     * Tính giảm giá từ điểm
     */
    public double tinhGiamGia(int diemSuDung) {
        return diemSuDung * DISCOUNT_PER_POINT;
    }

    /**
     * Xác định hạng thành viên
     */
    public String xacDinhHang(int tongDiem) {
        if (tongDiem >= 10000)
            return "💎 Kim cương";
        if (tongDiem >= 5000)
            return "🥇 Vàng";
        if (tongDiem >= 1000)
            return "🥈 Bạc";
        return "🥉 Đồng";
    }

    /**
     * Tính % giảm giá theo hạng
     */
    public double getPhanTramGiamGia(String hang) {
        return switch (hang) {
            case "💎 Kim cương" -> 0.10; // 10%
            case "🥇 Vàng" -> 0.07; // 7%
            case "🥈 Bạc" -> 0.05; // 5%
            default -> 0.02; // 2%
        };
    }

    /**
     * Lấy thông tin loyalty của khách hàng
     */
    public String getLoyaltyInfo(int tongDiem) {
        String hang = xacDinhHang(tongDiem);
        double giamGia = getPhanTramGiamGia(hang) * 100;
        return String.format("%s | %,d điểm | Giảm %.0f%%", hang, tongDiem, giamGia);
    }
}
