package com.mekongfarm.controller;

import com.mekongfarm.dao.ThongKeDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.chart.*;
import java.util.Map;

public class DashboardController {

    @FXML
    private Label lblTongSP, lblTongKH, lblTongDH, lblDoanhThu;
    @FXML
    private PieChart chartLoai;
    @FXML
    private BarChart<String, Number> chartTinh;

    private final ThongKeDAO thongKeDAO = new ThongKeDAO();

    @FXML
    public void initialize() {
        taiDuLieuThongKe();
    }

    private void taiDuLieuThongKe() {
        lblTongSP.setText(String.valueOf(thongKeDAO.demTongSanPham()));
        lblTongKH.setText(String.valueOf(thongKeDAO.demTongKhachHang()));
        lblTongDH.setText(String.valueOf(thongKeDAO.demTongDonHang()));
        lblDoanhThu.setText(String.format("%,.0f VNĐ", thongKeDAO.tinhTongDoanhThu()));

        // Pie chart
        chartLoai.getData().clear();
        Map<String, Integer> duLieu = thongKeDAO.thongKeSanPhamTheoLoai();
        for (Map.Entry<String, Integer> entry : duLieu.entrySet()) {
            chartLoai.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        // Bar chart
        chartTinh.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Số sản phẩm");
        Map<String, Integer> duLieuTinh = thongKeDAO.thongKeSanPhamTheoTinh();
        int count = 0;
        for (Map.Entry<String, Integer> entry : duLieuTinh.entrySet()) {
            if (count++ >= 6)
                break;
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        chartTinh.getData().add(series);
    }
}
