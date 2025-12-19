package com.mekongfarm.service;

import com.mekongfarm.model.SanPham;
import java.util.*;

/**
 * Tích hợp Lazada (Placeholder - cần API key thực)
 */
public class LazadaIntegration implements EcommerceService {

    private String apiKey;
    private String secretKey;
    private boolean connected = false;

    @Override
    public boolean ketNoi(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;

        // TODO: Gọi API Lazada thực tế
        // https://open.lazada.com/apps/doc/api?path=/seller/get

        if (apiKey != null && !apiKey.isEmpty()) {
            this.connected = true;
            System.out.println("🛍️ [Lazada] Đã kết nối (mock)");
            return true;
        }
        return false;
    }

    @Override
    public boolean dangSanPham(SanPham sp) {
        if (!connected)
            return false;

        // TODO: Gọi API Lazada để đăng sản phẩm
        // https://open.lazada.com/apps/doc/api?path=/product/create

        System.out.println("🛍️ [Lazada] Đăng sản phẩm: " + sp.getTenSanPham());
        return true;
    }

    @Override
    public boolean capNhatTonKho(int maSP, int soLuong) {
        if (!connected)
            return false;

        // TODO: Gọi API Lazada cập nhật stock
        // https://open.lazada.com/apps/doc/api?path=/product/stock/sellable/update

        System.out.println("🛍️ [Lazada] Cập nhật tồn kho SP " + maSP + ": " + soLuong);
        return true;
    }

    @Override
    public List<Map<String, Object>> layDonHang() {
        List<Map<String, Object>> orders = new ArrayList<>();

        if (!connected)
            return orders;

        // TODO: Gọi API Lazada lấy đơn hàng
        // https://open.lazada.com/apps/doc/api?path=/orders/get

        // Mock data
        Map<String, Object> mockOrder = new HashMap<>();
        mockOrder.put("orderId", "LAZADA789012");
        mockOrder.put("customerName", "Khách Lazada");
        mockOrder.put("total", 750000.0);
        mockOrder.put("status", "Chờ xử lý");
        orders.add(mockOrder);

        return orders;
    }

    @Override
    public boolean dongBoTonKho() {
        if (!connected)
            return false;

        System.out.println("🛍️ [Lazada] Đồng bộ tồn kho...");
        // TODO: Lấy tất cả sản phẩm và đồng bộ với Lazada
        return true;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public String getTenSan() {
        return "Lazada";
    }
}
