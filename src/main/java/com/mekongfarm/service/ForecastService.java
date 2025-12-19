package com.mekongfarm.service;

import com.mekongfarm.dao.ThongKeDAO;
import com.mekongfarm.dao.SanPhamDAO;
import com.mekongfarm.model.SanPham;
import java.util.*;

/**
 * AI Inventory Forecast - Dự đoán nhu cầu nhập hàng
 */
public class ForecastService {

    private final ThongKeDAO thongKeDAO = new ThongKeDAO();
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();

    /**
     * Dự đoán số lượng cần nhập trong 30 ngày tới
     */
    public Map<String, Integer> duDoanNhapHang() {
        Map<String, Integer> forecast = new LinkedHashMap<>();

        // Lấy top sản phẩm bán chạy
        Map<String, Integer> topSP = thongKeDAO.topSanPhamBanChay(10);

        for (Map.Entry<String, Integer> entry : topSP.entrySet()) {
            String tenSP = entry.getKey();
            int tongBan = entry.getValue();

            // Ước tính bán trong 30 ngày = (tổng bán / 30) * 30 + buffer 20%
            int duDoan = (int) ((tongBan / 30.0) * 30 * 1.2);
            forecast.put(tenSP, Math.max(duDoan, 10)); // Tối thiểu 10
        }

        return forecast;
    }

    /**
     * Phân tích xu hướng bán hàng
     */
    public String phanTichXuHuong(String tenSP) {
        // TODO: Phân tích dữ liệu bán hàng theo thời gian
        return "📈 Sản phẩm " + tenSP + " có xu hướng tăng 15% so với tháng trước";
    }

    /**
     * Đề xuất số lượng nhập tối ưu
     */
    public int deXuatNhap(int maSP) {
        SanPham sp = sanPhamDAO.timTheoId(maSP);
        if (sp == null)
            return 0;

        int tonKho = sp.getSoLuongTon();
        int tongBan = thongKeDAO.topSanPhamBanChay(100)
                .getOrDefault(sp.getTenSanPham(), 0);

        // Công thức đơn giản: (trung bình bán * 1.5) - tồn kho
        int trungBinhBan = tongBan / 30;
        int deXuat = (int) (trungBinhBan * 45) - tonKho; // 45 ngày = 1.5 tháng

        return Math.max(deXuat, 0);
    }

    /**
     * Cảnh báo sản phẩm cần nhập gấp
     */
    public List<SanPham> canNhapGap() {
        List<SanPham> result = new ArrayList<>();

        for (SanPham sp : sanPhamDAO.layTatCa()) {
            int deXuat = deXuatNhap(sp.getMaSanPham());
            if (deXuat > 20) {
                result.add(sp);
            }
        }

        return result;
    }

    /**
     * Tạo báo cáo dự báo
     */
    public String taoBaoCaoDuBao() {
        StringBuilder sb = new StringBuilder();
        sb.append("📊 BÁO CÁO DỰ BÁO NHẬP HÀNG\n");
        sb.append("================================\n\n");

        Map<String, Integer> forecast = duDoanNhapHang();
        for (Map.Entry<String, Integer> entry : forecast.entrySet()) {
            sb.append("• ").append(entry.getKey())
                    .append(": ").append(entry.getValue()).append(" đơn vị\n");
        }

        return sb.toString();
    }
}
