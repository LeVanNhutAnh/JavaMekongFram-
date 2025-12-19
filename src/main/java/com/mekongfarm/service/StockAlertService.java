package com.mekongfarm.service;

import com.mekongfarm.dao.SanPhamDAO;
import com.mekongfarm.model.SanPham;
import javafx.scene.control.Alert;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service cảnh báo tồn kho
 */
public class StockAlertService {

    private final SanPhamDAO dao = new SanPhamDAO();
    private static final int DEFAULT_THRESHOLD = 10;

    /**
     * Lấy danh sách sản phẩm sắp hết hàng
     */
    public List<SanPham> laySanPhamSapHet() {
        int threshold = com.mekongfarm.config.AppConfig.getStockWarningThreshold();
        return dao.layTatCa().stream()
                .filter(sp -> sp.getSoLuongTon() <= threshold && sp.getSoLuongTon() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách sản phẩm đã hết hàng
     */
    public List<SanPham> laySanPhamHetHang() {
        return dao.layTatCa().stream()
                .filter(sp -> sp.getSoLuongTon() == 0)
                .collect(Collectors.toList());
    }

    /**
     * Hiển thị popup cảnh báo
     */
    public void hienThiCanhBao() {
        List<SanPham> sapHet = laySanPhamSapHet();
        List<SanPham> hetHang = laySanPhamHetHang();

        if (sapHet.isEmpty() && hetHang.isEmpty()) {
            return;
        }

        StringBuilder msg = new StringBuilder();

        if (!hetHang.isEmpty()) {
            msg.append("🔴 SẢN PHẨM HẾT HÀNG:\n");
            for (SanPham sp : hetHang) {
                msg.append("  • ").append(sp.getTenSanPham()).append("\n");
            }
            msg.append("\n");
        }

        if (!sapHet.isEmpty()) {
            msg.append("🟠 SẢN PHẨM SẮP HẾT:\n");
            for (SanPham sp : sapHet) {
                msg.append("  • ").append(sp.getTenSanPham())
                        .append(" (còn ").append(sp.getSoLuongTon()).append(")\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cảnh báo tồn kho");
        alert.setHeaderText("⚠️ Cần nhập thêm hàng!");
        alert.setContentText(msg.toString());
        alert.showAndWait();
    }

    /**
     * Đếm số sản phẩm cần cảnh báo
     */
    public int demCanCanhBao() {
        return laySanPhamSapHet().size() + laySanPhamHetHang().size();
    }
}
