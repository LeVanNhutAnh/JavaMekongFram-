package com.mekongfarm.controller;

import com.mekongfarm.model.LogHoatDong;
import com.mekongfarm.service.LogService;
import com.mekongfarm.util.DialogUtil;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LichSuController {

    @FXML
    private ComboBox<String> cboLoai;
    @FXML
    private TextField txtTimKiem;
    @FXML
    private TableView<LogHoatDong> tableLog;
    @FXML
    private Label lblTongLog;
    @FXML
    private Label lblTongHoatDong;
    @FXML
    private Label lblThemMoi;
    @FXML
    private Label lblCapNhat;
    @FXML
    private Label lblHomNay;

    private ObservableList<LogHoatDong> danhSach = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cboLoai.getItems().addAll("Tất cả", "DANG_NHAP", "DANG_XUAT", "THEM", "CAP_NHAT", "XOA", "XUAT_FILE");
        cboLoai.setValue("Tất cả");

        cboLoai.setOnAction(e -> locDuLieu());
        txtTimKiem.textProperty().addListener((obs, old, val) -> locDuLieu());

        lamMoi();
    }

    @FXML
    private void lamMoi() {
        List<LogHoatDong> logs = LogService.layLichSu(500);
        danhSach.setAll(logs);
        tableLog.setItems(danhSach);
        lblTongLog.setText("Tổng: " + logs.size() + " hoạt động");
        
        // Cập nhật statistics
        capNhatThongKe(logs);
    }
    
    private void capNhatThongKe(List<LogHoatDong> logs) {
        lblTongHoatDong.setText(String.valueOf(logs.size()));
        
        long themMoi = logs.stream().filter(l -> "THEM".equals(l.getLoaiHoatDong())).count();
        lblThemMoi.setText(String.valueOf(themMoi));
        
        long capNhat = logs.stream().filter(l -> "CAP_NHAT".equals(l.getLoaiHoatDong())).count();
        lblCapNhat.setText(String.valueOf(capNhat));
        
        long homNay = logs.stream()
            .filter(l -> l.getThoiGian() != null && 
                l.getThoiGian().toLocalDate().equals(java.time.LocalDate.now()))
            .count();
        lblHomNay.setText(String.valueOf(homNay));
    }

    private void locDuLieu() {
        String loai = cboLoai.getValue();
        String keyword = txtTimKiem.getText().toLowerCase();

        List<LogHoatDong> all = LogService.layLichSu(500);
        ObservableList<LogHoatDong> filtered = FXCollections.observableArrayList();

        for (LogHoatDong log : all) {
            boolean matchLoai = "Tất cả".equals(loai) || loai.equals(log.getLoaiHoatDong());
            boolean matchKeyword = keyword.isEmpty() ||
                    (log.getMoTa() != null && log.getMoTa().toLowerCase().contains(keyword)) ||
                    (log.getTenNguoiDung() != null && log.getTenNguoiDung().toLowerCase().contains(keyword));

            if (matchLoai && matchKeyword) {
                filtered.add(log);
            }
        }

        tableLog.setItems(filtered);
        lblTongLog.setText("Tổng: " + filtered.size() + " hoạt động");
    }

    @FXML
    private void xuatExcel() {
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle("Xuất lịch sử ra Excel");
            fc.setInitialFileName("LichSu_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx");
            fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            
            File file = fc.showSaveDialog(tableLog.getScene().getWindow());
            if (file != null) {
                new com.mekongfarm.service.ExcelExportService().xuatLichSuExcel(
                    new java.util.ArrayList<>(danhSach), file);
                DialogUtil.showSuccess("Thành công", "Đã xuất lịch sử ra Excel!");
                LogService.logXuatFile("Excel", file.getName());
            }
        } catch (Exception e) {
            DialogUtil.showError("Lỗi", "Không thể xuất Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void xuatPDF() {
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle("Xuất lịch sử ra PDF");
            fc.setInitialFileName("LichSu_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf");
            fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            
            File file = fc.showSaveDialog(tableLog.getScene().getWindow());
            if (file != null) {
                new com.mekongfarm.service.PDFExportService().xuatLichSuPDF(
                    new java.util.ArrayList<>(danhSach), file);
                DialogUtil.showSuccess("Thành công", "Đã xuất lịch sử ra PDF!");
                LogService.logXuatFile("PDF", file.getName());
            }
        } catch (Exception e) {
            DialogUtil.showError("Lỗi", "Không thể xuất PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
