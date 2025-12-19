package com.mekongfarm.service;

import com.mekongfarm.model.SanPham;
import java.util.List;
import java.util.Map;

/**
 * Interface cho tích hợp sàn TMĐT
 */
public interface EcommerceService {

    /**
     * Kết nối với sàn
     */
    boolean ketNoi(String apiKey, String secretKey);

    /**
     * Đăng sản phẩm lên sàn
     */
    boolean dangSanPham(SanPham sp);

    /**
     * Cập nhật tồn kho trên sàn
     */
    boolean capNhatTonKho(int maSP, int soLuong);

    /**
     * Lấy danh sách đơn hàng từ sàn
     */
    List<Map<String, Object>> layDonHang();

    /**
     * Đồng bộ tồn kho
     */
    boolean dongBoTonKho();

    /**
     * Kiểm tra trạng thái kết nối
     */
    boolean isConnected();

    /**
     * Lấy tên sàn
     */
    String getTenSan();
}
