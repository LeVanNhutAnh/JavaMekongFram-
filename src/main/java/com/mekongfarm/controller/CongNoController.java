package com.mekongfarm.controller;

import com.mekongfarm.dao.*;
import com.mekongfarm.model.*;
import com.mekongfarm.util.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller cho Quản lý Công nợ
 */
public class CongNoController implements Initializable {

    @FXML
    private TableView<CongNo> tableCongNo;
    @FXML
    private TableView<CongNo> tablePhaiTra; // Tab phải trả NCC
    @FXML
    private TableColumn<CongNo, String> colKhachHang;
    @FXML
    private TableColumn<CongNo, String> colSoTien;
    @FXML
    private TableColumn<CongNo, String> colConNo;
    @FXML
    private TableColumn<CongNo, LocalDate> colHanTT;
    @FXML
    private TableColumn<CongNo, String> colTrangThai;

    @FXML
    private Label lblTongNo;
    @FXML
    private Label lblQuaHan;
    @FXML
    private Label lblDaThanhToan;
    @FXML
    private Label lblSoKhachNo;
    @FXML
    private Label lblSoKhoan;
    @FXML
    private ComboBox<String> cboFilter;

    @FXML
    private TextField txtThanhToan;

    private CongNoDAO congNoDAO = new CongNoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        setupFilter();
        loadData();
        capNhatThongKe();
    }

    private void setupTable() {
        colKhachHang.setCellValueFactory(new PropertyValueFactory<>("tenKhachHang"));
        colSoTien.setCellValueFactory(new PropertyValueFactory<>("soTienFormat"));
        colConNo.setCellValueFactory(new PropertyValueFactory<>("conNoFormat"));
        colHanTT.setCellValueFactory(new PropertyValueFactory<>("hanThanhToan"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        // Highlight dòng quá hạn
        tableCongNo.setRowFactory(tv -> new TableRow<CongNo>() {
            @Override
            protected void updateItem(CongNo item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if (item.isQuaHan()) {
                    setStyle("-fx-background-color: #ffcdd2;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void setupFilter() {
        cboFilter.setItems(FXCollections.observableArrayList(
                "Tất cả", "Chưa thanh toán", "Quá hạn", "Đã thanh toán"));
        cboFilter.setValue("Tất cả");
        cboFilter.setOnAction(e -> locDuLieu());
    }

    private void loadData() {
        // Load phải thu (khách hàng)
        tableCongNo.setItems(FXCollections.observableArrayList(congNoDAO.layTheoLoai("phai_thu")));
        // Load phải trả (NCC)
        if (tablePhaiTra != null) {
            tablePhaiTra.setItems(FXCollections.observableArrayList(congNoDAO.layTheoLoai("phai_tra")));
        }
    }

    private void locDuLieu() {
        String filter = cboFilter.getValue();
        switch (filter) {
            case "Chưa thanh toán":
                tableCongNo.setItems(FXCollections.observableArrayList(congNoDAO.layChuaThanhToan()));
                break;
            case "Quá hạn":
                tableCongNo.setItems(FXCollections.observableArrayList(congNoDAO.layQuaHan()));
                break;
            default:
                loadData();
        }
    }

    private void capNhatThongKe() {
        // Lấy dữ liệu
        var tatCa = congNoDAO.layTheoLoai("phai_thu");
        var quaHan = congNoDAO.layQuaHan();
        var daThanhToan = tatCa.stream()
                .filter(cn -> "Đã thanh toán".equals(cn.getTrangThai()))
                .toList();
        
        // Tính tổng công nợ (còn nợ)
        double tongNo = tatCa.stream()
                .filter(cn -> !"Đã thanh toán".equals(cn.getTrangThai()))
                .mapToDouble(CongNo::getConNo)
                .sum();
        
        // Tính tổng đã thanh toán
        double daThanhToanTong = daThanhToan.stream()
                .mapToDouble(CongNo::getSoTien)
                .sum();
        
        // Đếm số khách nợ (distinct khách hàng chưa thanh toán hết)
        long soKhachNo = tatCa.stream()
                .filter(cn -> !"Đã thanh toán".equals(cn.getTrangThai()))
                .map(CongNo::getMaKH)
                .distinct()
                .count();
        
        // Cập nhật UI
        if (lblSoKhoan != null)
            lblSoKhoan.setText(tatCa.size() + " khoản nợ");
        if (lblTongNo != null)
            lblTongNo.setText(String.format("%,.0f VNĐ", tongNo));
        if (lblQuaHan != null)
            lblQuaHan.setText(String.valueOf(quaHan.size()));
        if (lblDaThanhToan != null)
            lblDaThanhToan.setText(String.format("%,.0f VNĐ", daThanhToanTong));
        if (lblSoKhachNo != null)
            lblSoKhachNo.setText(String.valueOf(soKhachNo));
    }

    @FXML
    private void thanhToan() {
        CongNo selected = tableCongNo.getSelectionModel().getSelectedItem();
        if (selected == null) {
            thongBao("Vui lòng chọn khoản nợ cần thanh toán!");
            return;
        }

        try {
            double soTien = Double.parseDouble(txtThanhToan.getText());
            if (soTien <= 0) {
                thongBao("Số tiền phải lớn hơn 0!");
                return;
            }

            if (congNoDAO.thanhToan(selected.getMaCongNo(), soTien)) {
                thongBao("✅ Đã ghi nhận thanh toán: " + String.format("%,.0f VNĐ", soTien));
                loadData();
                capNhatThongKe();
                txtThanhToan.clear();
            }
        } catch (NumberFormatException e) {
            thongBao("Vui lòng nhập số tiền hợp lệ!");
        }
    }

    @FXML
    private void nhacNo() {
        CongNo selected = tableCongNo.getSelectionModel().getSelectedItem();
        if (selected == null) {
            thongBao("Vui lòng chọn khoản nợ!");
            return;
        }

        // TODO: Gửi SMS/Email nhắc nợ
        thongBao("📩 Đã gửi nhắc nợ đến: " + selected.getTenKhachHang() +
                "\nSố tiền: " + selected.getConNoFormat());
    }

    @FXML
    private void lamMoi() {
        loadData();
        capNhatThongKe();
        cboFilter.setValue("Tất cả");
    }

    private void thongBao(String msg) {
        DialogUtil.showSuccess("Thông báo", msg);
    }
}
