package com.mekongfarm.controller;

import com.mekongfarm.dao.ThongKeDAO;
import com.mekongfarm.util.*;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import java.time.Year;
import java.util.Map;
import java.io.File;

public class ThongKeController {
    @FXML
    private ComboBox<Integer> cboNam;
    @FXML
    private BarChart<String, Number> chartDoanhThu, chartTinh, chartTopSP;
    @FXML
    private PieChart chartLoai;
    @FXML
    private Label lblTongSP, lblTongKH, lblTongDH, lblDoanhThu;

    private final ThongKeDAO dao = new ThongKeDAO();

    @FXML
    public void initialize() {
        int namHienTai = Year.now().getValue();
        for (int i = namHienTai; i >= namHienTai - 5; i--)
            cboNam.getItems().add(i);
        cboNam.setValue(namHienTai);
        capNhatThongKe();
        xemThongKe();
    }

    private void capNhatThongKe() {
        if (lblTongSP != null)
            lblTongSP.setText(String.valueOf(dao.demTongSanPham()));
        if (lblTongKH != null)
            lblTongKH.setText(String.valueOf(dao.demTongKhachHang()));
        if (lblTongDH != null)
            lblTongDH.setText(String.valueOf(dao.demTongDonHang()));
        if (lblDoanhThu != null)
            lblDoanhThu.setText(String.format("%,.0f VNĐ", dao.tinhTongDoanhThu()));
    }

    @FXML
    public void xemThongKe() {
        int nam = cboNam.getValue();

        // Chart doanh thu theo tháng
        chartDoanhThu.getData().clear();
        XYChart.Series<String, Number> seriesDT = new XYChart.Series<>();
        seriesDT.setName("Doanh thu " + nam);
        Map<String, Double> dtThang = dao.thongKeDoanhThuTheoThang(nam);
        for (Map.Entry<String, Double> e : dtThang.entrySet()) {
            seriesDT.getData().add(new XYChart.Data<>(e.getKey().replace("Tháng ", "T"), e.getValue()));
        }
        chartDoanhThu.getData().add(seriesDT);

        // Chart top sản phẩm bán chạy
        chartTopSP.getData().clear();
        XYChart.Series<String, Number> seriesTop = new XYChart.Series<>();
        seriesTop.setName("Top bán chạy");
        Map<String, Integer> topSP = dao.topSanPhamBanChay(5);
        for (Map.Entry<String, Integer> e : topSP.entrySet()) {
            String tenRut = e.getKey().length() > 15 ? e.getKey().substring(0, 12) + "..." : e.getKey();
            seriesTop.getData().add(new XYChart.Data<>(tenRut, e.getValue()));
        }
        chartTopSP.getData().add(seriesTop);

        // Chart sản phẩm theo loại
        chartLoai.getData().clear();
        Map<String, Integer> spLoai = dao.thongKeSanPhamTheoLoai();
        for (Map.Entry<String, Integer> e : spLoai.entrySet()) {
            chartLoai.getData().add(new PieChart.Data(e.getKey() + " (" + e.getValue() + ")", e.getValue()));
        }

        // Chart sản phẩm theo tỉnh
        chartTinh.getData().clear();
        XYChart.Series<String, Number> seriesTinh = new XYChart.Series<>();
        seriesTinh.setName("Số sản phẩm");
        Map<String, Integer> spTinh = dao.thongKeSanPhamTheoTinh();
        for (Map.Entry<String, Integer> e : spTinh.entrySet()) {
            seriesTinh.getData().add(new XYChart.Data<>(e.getKey(), e.getValue()));
        }
        chartTinh.getData().add(seriesTinh);
    }

    @FXML
    private void xuatPDF() {
        try {
            javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
            fc.setTitle("Lưu báo cáo PDF");
            fc.setInitialFileName("bao_cao_thong_ke.pdf");
            fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("PDF", "*.pdf"));
            File file = fc.showSaveDialog(chartDoanhThu.getScene().getWindow());
            if (file != null) {
                LoadingUtil.showLoading("Đang xuất báo cáo PDF...");
                new Thread(() -> {
                    try {
                        new com.mekongfarm.service.PDFExportService().xuatBaoCaoThongKe(file);
                        LoadingUtil.hideLoading();
                        javafx.application.Platform.runLater(() ->
                            DialogUtil.showSuccess("Xuất file thành công", "Đã xuất báo cáo PDF: " + file.getName()));
                    } catch (Exception ex) {
                        LoadingUtil.hideLoading();
                        javafx.application.Platform.runLater(() ->
                            DialogUtil.showError("Lỗi xuất PDF", "Không thể xuất báo cáo!", ex.getMessage()));
                    }
                }).start();
            }
        } catch (Exception e) {
            DialogUtil.showError("Lỗi", e.getMessage());
        }
    }

    @FXML
    private void xuatExcel() {
        try {
            javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
            fc.setTitle("Lưu báo cáo Excel");
            fc.setInitialFileName("bao_cao_thong_ke.xlsx");
            fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Excel", "*.xlsx"));
            File file = fc.showSaveDialog(chartDoanhThu.getScene().getWindow());
            if (file != null) {
                DialogUtil.showWarning("Tính năng đang phát triển",
                    "Tính năng xuất Excel thống kê đang được phát triển.\n" +
                    "Hiện tại bạn có thể xuất PDF.");
            }
        } catch (Exception e) {
            DialogUtil.showError("Lỗi", e.getMessage());
        }
    }
}
