package com.mekongfarm.controller;

import com.mekongfarm.service.BaoCaoLaiLoService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller cho Báo cáo Lãi/Lỗ
 */
public class LaiLoController implements Initializable {

    @FXML
    private DatePicker dpTuNgay;
    @FXML
    private DatePicker dpDenNgay;

    @FXML
    private Label lblDoanhThu;
    @FXML
    private Label lblChiPhi;
    @FXML
    private Label lblLaiLo;
    @FXML
    private Label lblTyLe;

    @FXML
    private BarChart<String, Number> chartThang;
    @FXML
    private PieChart chartSanPham;

    private BaoCaoLaiLoService laiLoService = new BaoCaoLaiLoService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Mặc định: tháng hiện tại
        dpTuNgay.setValue(LocalDate.now().withDayOfMonth(1));
        dpDenNgay.setValue(LocalDate.now());

        tinhToan();
        loadChartThang();
    }

    @FXML
    private void tinhToan() {
        LocalDate tuNgay = dpTuNgay.getValue();
        LocalDate denNgay = dpDenNgay.getValue();

        if (tuNgay == null || denNgay == null)
            return;

        Map<String, Double> ketQua = laiLoService.tinhLaiLo(tuNgay, denNgay);

        double doanhThu = ketQua.get("doanhThu");
        double chiPhi = ketQua.get("chiPhi");
        double laiLo = ketQua.get("laiLo");
        double tyLe = ketQua.get("tyLeLai");

        lblDoanhThu.setText(String.format("%,.0f VNĐ", doanhThu));
        lblChiPhi.setText(String.format("%,.0f VNĐ", chiPhi));
        lblLaiLo.setText(String.format("%,.0f VNĐ", laiLo));
        lblLaiLo.setStyle(laiLo >= 0 ? "-fx-text-fill: #16a34a;" : "-fx-text-fill: #dc2626;");
        lblTyLe.setText(String.format("%.1f%%", tyLe));

        loadChartSanPham(tuNgay, denNgay);
    }

    private void loadChartThang() {
        chartThang.getData().clear();
        int nam = LocalDate.now().getYear();

        Map<String, Double> data = laiLoService.laiLoTheoThang(nam);

        XYChart.Series<String, Number> seriesLai = new XYChart.Series<>();
        seriesLai.setName("Lợi nhuận " + nam);

        String[] thang = { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" };
        for (String t : thang) {
            seriesLai.getData().add(new XYChart.Data<>(t, data.getOrDefault(t, 0.0)));
        }

        chartThang.getData().add(seriesLai);
    }

    private void loadChartSanPham(LocalDate tuNgay, LocalDate denNgay) {
        chartSanPham.getData().clear();

        Map<String, Map<String, Double>> data = laiLoService.laiLoTheoSanPham(tuNgay, denNgay);

        for (Map.Entry<String, Map<String, Double>> entry : data.entrySet()) {
            double laiLo = entry.getValue().get("laiLo");
            if (laiLo > 0) {
                chartSanPham.getData().add(new PieChart.Data(entry.getKey(), laiLo));
            }
        }
    }

    @FXML
    private void xemThangNay() {
        dpTuNgay.setValue(LocalDate.now().withDayOfMonth(1));
        dpDenNgay.setValue(LocalDate.now());
        tinhToan();
    }

    @FXML
    private void xemThangTruoc() {
        LocalDate thangTruoc = LocalDate.now().minusMonths(1);
        dpTuNgay.setValue(thangTruoc.withDayOfMonth(1));
        dpDenNgay.setValue(thangTruoc.withDayOfMonth(thangTruoc.lengthOfMonth()));
        tinhToan();
    }

    @FXML
    private void xemNamNay() {
        dpTuNgay.setValue(LocalDate.now().withDayOfYear(1));
        dpDenNgay.setValue(LocalDate.now());
        tinhToan();
    }
}
