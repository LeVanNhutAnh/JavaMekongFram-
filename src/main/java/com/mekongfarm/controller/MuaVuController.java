package com.mekongfarm.controller;

import com.mekongfarm.dao.MuaVuDAO;
import com.mekongfarm.model.MuaVu;
import com.mekongfarm.util.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller cho Quản lý Mùa vụ
 */
public class MuaVuController implements Initializable {

    @FXML
    private TableView<MuaVu> tableMuaVu;
    @FXML
    private TableColumn<MuaVu, String> colTen;
    @FXML
    private TableColumn<MuaVu, LocalDate> colBatDau;
    @FXML
    private TableColumn<MuaVu, LocalDate> colKetThuc;
    @FXML
    private TableColumn<MuaVu, String> colTrangThai;
    @FXML
    private TableColumn<MuaVu, Double> colDuBao;

    @FXML
    private TextField txtTen;
    @FXML
    private DatePicker dpBatDau;
    @FXML
    private DatePicker dpKetThuc;
    @FXML
    private TextField txtDuBao;
    @FXML
    private TextArea txtMoTa;

    @FXML
    private Label lblDangDienRa;

    private MuaVuDAO muaVuDAO = new MuaVuDAO();
    private MuaVu selectedMuaVu = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadData();
        capNhatThongKe();
    }

    private void setupTable() {
        colTen.setCellValueFactory(new PropertyValueFactory<>("tenMuaVu"));
        colBatDau.setCellValueFactory(new PropertyValueFactory<>("ngayBatDau"));
        colKetThuc.setCellValueFactory(new PropertyValueFactory<>("ngayKetThuc"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        colDuBao.setCellValueFactory(new PropertyValueFactory<>("duBaoSanLuong"));

        // Màu theo trạng thái
        tableMuaVu.setRowFactory(tv -> new TableRow<MuaVu>() {
            @Override
            protected void updateItem(MuaVu item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if ("Đang diễn ra".equals(item.getTrangThai())) {
                    setStyle("-fx-background-color: #c8e6c9;");
                } else if ("Sắp tới".equals(item.getTrangThai())) {
                    setStyle("-fx-background-color: #fff9c4;");
                } else {
                    setStyle("");
                }
            }
        });

        tableMuaVu.getSelectionModel().selectedItemProperty().addListener((obs, old, mv) -> {
            if (mv != null) {
                selectedMuaVu = mv;
                hienThiForm(mv);
            }
        });
    }

    private void loadData() {
        tableMuaVu.setItems(FXCollections.observableArrayList(muaVuDAO.layTatCa()));
    }

    private void capNhatThongKe() {
        int dangDienRa = muaVuDAO.layDangDienRa().size();
        lblDangDienRa.setText("🌾 Mùa vụ đang diễn ra: " + dangDienRa);
    }

    private void hienThiForm(MuaVu mv) {
        txtTen.setText(mv.getTenMuaVu());
        dpBatDau.setValue(mv.getNgayBatDau());
        dpKetThuc.setValue(mv.getNgayKetThuc());
        txtDuBao.setText(String.valueOf(mv.getDuBaoSanLuong()));
        txtMoTa.setText(mv.getMoTa());
    }

    @FXML
    private void themMoi() {
        xoaForm();
        selectedMuaVu = null;
        txtTen.requestFocus();
    }

    @FXML
    private void luu() {
        if (txtTen.getText().isEmpty()) {
            thongBao("Vui lòng nhập tên mùa vụ!");
            return;
        }

        MuaVu mv = selectedMuaVu != null ? selectedMuaVu : new MuaVu();
        mv.setTenMuaVu(txtTen.getText());
        mv.setNgayBatDau(dpBatDau.getValue());
        mv.setNgayKetThuc(dpKetThuc.getValue());
        try {
            mv.setDuBaoSanLuong(Double.parseDouble(txtDuBao.getText()));
        } catch (NumberFormatException e) {
            mv.setDuBaoSanLuong(0);
        }
        mv.setMoTa(txtMoTa.getText());

        boolean result;
        if (selectedMuaVu != null) {
            result = muaVuDAO.capNhat(mv);
        } else {
            result = muaVuDAO.them(mv);
        }

        if (result) {
            thongBao("✅ Đã lưu mùa vụ!");
            loadData();
            capNhatThongKe();
            xoaForm();
        }
    }

    @FXML
    private void xoa() {
        if (selectedMuaVu == null)
            return;

        if (DialogUtil.confirm("Xác nhận xóa", "Bạn có chắc muốn xóa mùa vụ này?")) {
            muaVuDAO.xoa(selectedMuaVu.getMaMuaVu());
            loadData();
            capNhatThongKe();
            xoaForm();
        }
    }

    @FXML
    private void lamMoi() {
        loadData();
        capNhatThongKe();
    }

    private void xoaForm() {
        selectedMuaVu = null;
        txtTen.clear();
        dpBatDau.setValue(null);
        dpKetThuc.setValue(null);
        txtDuBao.clear();
        txtMoTa.clear();
    }

    private void thongBao(String msg) {
        DialogUtil.showSuccess("Thông báo", msg);
    }
}
