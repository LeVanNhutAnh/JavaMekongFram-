package com.mekongfarm.controller;

import com.mekongfarm.dao.KhoDAO;
import com.mekongfarm.model.Kho;
import com.mekongfarm.util.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller cho Quản lý Kho
 */
public class KhoController implements Initializable {

    @FXML
    private TableView<Kho> tableKho;
    @FXML
    private TableColumn<Kho, String> colTen;
    @FXML
    private TableColumn<Kho, String> colDiaChi;
    @FXML
    private TableColumn<Kho, String> colNguoiQL;
    @FXML
    private TableColumn<Kho, Double> colSucChua;
    @FXML
    private TableColumn<Kho, String> colTrangThai;

    @FXML
    private TextField txtTen;
    @FXML
    private TextField txtDiaChi;
    @FXML
    private TextField txtNguoiQL;
    @FXML
    private TextField txtDienThoai;
    @FXML
    private TextField txtSucChua;
    @FXML
    private CheckBox chkHoatDong;

    private KhoDAO khoDAO = new KhoDAO();
    private Kho selectedKho = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadData();
    }

    private void setupTable() {
        colTen.setCellValueFactory(new PropertyValueFactory<>("tenKho"));
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        colNguoiQL.setCellValueFactory(new PropertyValueFactory<>("nguoiQuanLy"));
        colSucChua.setCellValueFactory(new PropertyValueFactory<>("sucChua"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThaiHoatDong"));

        tableKho.getSelectionModel().selectedItemProperty().addListener((obs, old, kho) -> {
            if (kho != null) {
                selectedKho = kho;
                hienThiForm(kho);
            }
        });
    }

    private void loadData() {
        tableKho.setItems(FXCollections.observableArrayList(khoDAO.layTatCa()));
    }

    private void hienThiForm(Kho kho) {
        txtTen.setText(kho.getTenKho());
        txtDiaChi.setText(kho.getDiaChi());
        txtNguoiQL.setText(kho.getNguoiQuanLy());
        txtDienThoai.setText(kho.getDienThoai());
        txtSucChua.setText(String.valueOf(kho.getSucChua()));
        chkHoatDong.setSelected(kho.isHoatDong());
    }

    @FXML
    private void themMoi() {
        xoaForm();
        selectedKho = null;
    }

    @FXML
    private void luu() {
        if (txtTen.getText().isEmpty()) {
            thongBao("Vui lòng nhập tên kho!");
            return;
        }

        Kho kho = selectedKho != null ? selectedKho : new Kho();
        kho.setTenKho(txtTen.getText());
        kho.setDiaChi(txtDiaChi.getText());
        kho.setNguoiQuanLy(txtNguoiQL.getText());
        kho.setDienThoai(txtDienThoai.getText());
        try {
            kho.setSucChua(Double.parseDouble(txtSucChua.getText()));
        } catch (NumberFormatException e) {
            kho.setSucChua(0);
        }
        kho.setHoatDong(chkHoatDong.isSelected());

        boolean result;
        if (selectedKho != null) {
            result = khoDAO.capNhat(kho);
        } else {
            result = khoDAO.them(kho);
        }

        if (result) {
            thongBao("✅ Đã lưu kho!");
            loadData();
            xoaForm();
        }
    }

    @FXML
    private void xoa() {
        if (selectedKho == null)
            return;

        if (DialogUtil.confirm("Xác nhận xóa", "Bạn có chắc muốn xóa kho này?")) {
            khoDAO.xoa(selectedKho.getMaKho());
            loadData();
            xoaForm();
        }
    }

    @FXML
    private void lamMoi() {
        loadData();
    }

    private void xoaForm() {
        selectedKho = null;
        txtTen.clear();
        txtDiaChi.clear();
        txtNguoiQL.clear();
        txtDienThoai.clear();
        txtSucChua.clear();
        chkHoatDong.setSelected(true);
    }

    private void thongBao(String msg) {
        DialogUtil.showSuccess("Thông báo", msg);
    }
}
