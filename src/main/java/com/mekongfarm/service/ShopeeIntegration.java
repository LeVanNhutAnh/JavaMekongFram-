package com.mekongfarm.service;

import com.mekongfarm.model.SanPham;
import java.util.*;

/**
 * Tích hợp Shopee (Placeholder - cần API key thực)
 */
public class ShopeeIntegration implements EcommerceService {

    private String apiKey;
    private String secretKey;
    private boolean connected = false;

    @Override
    public boolean ketNoi(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;

        // TODO: Gọi API Shopee thực tế
        // https://open.shopee.com/documents/v2/v2.shop.get_shop_info

        if (apiKey != null && !apiKey.isEmpty()) {
            this.connected = true;
            System.out.println("🛒 [Shopee] Đã kết nối (mock)");
            return true;
        }
        return false;
    }

    @Override
    public boolean dangSanPham(SanPham sp) {
        if (!connected)
            return false;

        // TODO: Gọi API Shopee để đăng sản phẩm
        // https://open.shopee.com/documents/v2/v2.product.add_item

        System.out.println("🛒 [Shopee] Đăng sản phẩm: " + sp.getTenSanPham());
        return true;
    }

    @Override
    public boolean capNhatTonKho(int maSP, int soLuong) {
        if (!connected)
            return false;

        // TODO: Gọi API Shopee cập nhật stock
        // https://open.shopee.com/documents/v2/v2.product.update_stock

        System.out.println("🛒 [Shopee] Cập nhật tồn kho SP " + maSP + ": " + soLuong);
        return true;
    }

    @Override
    public List<Map<String, Object>> layDonHang() {
        List<Map<String, Object>> orders = new ArrayList<>();

        if (!connected)
            return orders;

        // TODO: Gọi API Shopee lấy đơn hàng
        // https://open.shopee.com/documents/v2/v2.order.get_order_list

        // Mock data
        Map<String, Object> mockOrder = new HashMap<>();
        mockOrder.put("orderId", "SHOPEE123456");
        mockOrder.put("customerName", "Khách Shopee");
        mockOrder.put("total", 500000.0);
        mockOrder.put("status", "Chờ xử lý");
        orders.add(mockOrder);

        return orders;
    }

    @Override
    public boolean dongBoTonKho() {
        if (!connected)
            return false;

        System.out.println("🛒 [Shopee] Đồng bộ tồn kho...");
        // TODO: Lấy tất cả sản phẩm và đồng bộ với Shopee
        return true;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public String getTenSan() {
        return "Shopee";
    }
}
