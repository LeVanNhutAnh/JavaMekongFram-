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
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller cho Quản lý Giá theo vùng
 */
public class GiaVungController implements Initializable {

    @FXML
    private TableView<GiaVung> tableGiaVung;
    @FXML
    private TableColumn<GiaVung, String> colSanPham;
    @FXML
    private TableColumn<GiaVung, String> colTinh;
    @FXML
    private TableColumn<GiaVung, String> colGia;
    @FXML
    private TableColumn<GiaVung, LocalDate> colNgay;

    @FXML
    private ComboBox<SanPham> cboSanPham;
    @FXML
    private ComboBox<TinhThanh> cboTinh;
    @FXML
    private TextField txtGia;
    @FXML
    private DatePicker dpNgay;
    @FXML
    private TextArea txtGhiChu;

    private GiaVungDAO giaVungDAO = new GiaVungDAO();
    private SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private DanhMucDAO danhMucDAO = new DanhMucDAO();
    private GiaVung selectedGiaVung = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        setupForm();
        loadData();
    }

    private void setupTable() {
        colSanPham.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
        colTinh.setCellValueFactory(new PropertyValueFactory<>("tenTinh"));
        colGia.setCellValueFactory(new PropertyValueFactory<>("giaFormat"));
        colNgay.setCellValueFactory(new PropertyValueFactory<>("ngayApDung"));

        tableGiaVung.getSelectionModel().selectedItemProperty().addListener((obs, old, gv) -> {
            if (gv != null) {
                selectedGiaVung = gv;
                hienThiForm(gv);
            }
        });
    }

    private void setupForm() {
        // Load sản phẩm
        List<SanPham> dsSanPham = sanPhamDAO.layTatCa();
        cboSanPham.setItems(FXCollections.observableArrayList(dsSanPham));

        // Load tỉnh thành
        List<TinhThanh> dsTinh = danhMucDAO.layTatCaTinh();
        cboTinh.setItems(FXCollections.observableArrayList(dsTinh));

        dpNgay.setValue(LocalDate.now());
    }

    private void loadData() {
        tableGiaVung.setItems(FXCollections.observableArrayList(giaVungDAO.layTatCa()));
    }

    private void hienThiForm(GiaVung gv) {
        // Find and select product
        for (SanPham sp : cboSanPham.getItems()) {
            if (sp.getMaSanPham() == gv.getMaSP()) {
                cboSanPham.setValue(sp);
                break;
            }
        }
        // Find and select province
        for (TinhThanh tt : cboTinh.getItems()) {
            if (tt.getMaTinh() == gv.getMaTinh()) {
                cboTinh.setValue(tt);
                break;
            }
        }
        txtGia.setText(String.valueOf(gv.getGiaRieng()));
        dpNgay.setValue(gv.getNgayApDung());
        txtGhiChu.setText(gv.getGhiChu());
    }

    @FXML
    private void themMoi() {
        xoaForm();
        selectedGiaVung = null;
    }

    @FXML
    private void luu() {
        if (cboSanPham.getValue() == null || cboTinh.getValue() == null) {
            thongBao("Vui lòng chọn sản phẩm và tỉnh!");
            return;
        }
        if (txtGia.getText().isEmpty()) {
            thongBao("Vui lòng nhập giá!");
            return;
        }

        GiaVung gv = selectedGiaVung != null ? selectedGiaVung : new GiaVung();
        gv.setMaSP(cboSanPham.getValue().getMaSanPham());
        gv.setMaTinh(cboTinh.getValue().getMaTinh());
        try {
            gv.setGiaRieng(Double.parseDouble(txtGia.getText().replace(",", "")));
        } catch (NumberFormatException e) {
            thongBao("Giá không hợp lệ!");
            return;
        }
        gv.setNgayApDung(dpNgay.getValue());
        gv.setGhiChu(txtGhiChu.getText());

        boolean result;
        if (selectedGiaVung != null) {
            result = giaVungDAO.capNhat(gv);
        } else {
            result = giaVungDAO.them(gv);
        }

        if (result) {
            thongBao("✅ Đã lưu giá vùng!");
            loadData();
            xoaForm();
        }
    }

    @FXML
    private void xoa() {
        if (selectedGiaVung == null)
            return;

        if (DialogUtil.confirm("Xác nhận xóa", "Bạn có chắc muốn xóa giá này?")) {
            giaVungDAO.xoa(selectedGiaVung.getId());
            loadData();
            xoaForm();
        }
    }

    @FXML
    private void lamMoi() {
        loadData();
    }

    private void xoaForm() {
        selectedGiaVung = null;
        cboSanPham.setValue(null);
        cboTinh.setValue(null);
        txtGia.clear();
        dpNgay.setValue(LocalDate.now());
        txtGhiChu.clear();
    }

    private void thongBao(String msg) {
        DialogUtil.showSuccess("Thông báo", msg);
    }
}
