package com.mekongfarm.controller;

import com.mekongfarm.dao.NhaCungCapDAO;
import com.mekongfarm.model.NhaCungCap;
import com.mekongfarm.util.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller cho Quản lý Nhà cung cấp
 */
public class NhaCungCapController implements Initializable {

    @FXML
    private TextField txtTimKiem;
    @FXML
    private TableView<NhaCungCap> tableNCC;
    @FXML
    private TableColumn<NhaCungCap, Integer> colMa;
    @FXML
    private TableColumn<NhaCungCap, String> colTen;
    @FXML
    private TableColumn<NhaCungCap, String> colDienThoai;
    @FXML
    private TableColumn<NhaCungCap, String> colLoaiSP;
    @FXML
    private TableColumn<NhaCungCap, String> colDanhGia;

    // Form fields
    @FXML
    private TextField txtTen;
    @FXML
    private TextField txtDienThoai;
    @FXML
    private TextField txtDiaChi;
    @FXML
    private TextField txtEmail;
    @FXML
    private ComboBox<String> cboLoaiSP;
    @FXML
    private Spinner<Integer> spnDanhGia;
    @FXML
    private TextArea txtGhiChu;

    private NhaCungCapDAO nccDAO = new NhaCungCapDAO();
    private NhaCungCap selectedNCC = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        setupForm();
        loadData();

        txtTimKiem.textProperty().addListener((obs, old, newVal) -> timKiem());
    }

    private void setupTable() {
        colMa.setCellValueFactory(new PropertyValueFactory<>("maNCC"));
        colTen.setCellValueFactory(new PropertyValueFactory<>("tenNCC"));
        colDienThoai.setCellValueFactory(new PropertyValueFactory<>("dienThoai"));
        colLoaiSP.setCellValueFactory(new PropertyValueFactory<>("loaiSanPham"));
        colDanhGia.setCellValueFactory(new PropertyValueFactory<>("danhGiaSao"));

        tableNCC.getSelectionModel().selectedItemProperty().addListener((obs, old, ncc) -> {
            if (ncc != null) {
                selectedNCC = ncc;
                hienThiForm(ncc);
            }
        });
    }

    private void setupForm() {
        cboLoaiSP.setItems(FXCollections.observableArrayList(
                "Lúa gạo", "Trái cây", "Thủy sản", "Rau củ", "Gia cầm", "Khác"));

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 5);
        spnDanhGia.setValueFactory(valueFactory);
    }

    private void loadData() {
        tableNCC.setItems(FXCollections.observableArrayList(nccDAO.layTatCa()));
    }

    private void hienThiForm(NhaCungCap ncc) {
        txtTen.setText(ncc.getTenNCC());
        txtDienThoai.setText(ncc.getDienThoai());
        txtDiaChi.setText(ncc.getDiaChi());
        txtEmail.setText(ncc.getEmail());
        cboLoaiSP.setValue(ncc.getLoaiSanPham());
        spnDanhGia.getValueFactory().setValue(ncc.getDanhGia());
        txtGhiChu.setText(ncc.getGhiChu());
    }

    @FXML
    private void themMoi() {
        xoaForm();
        selectedNCC = null;
        txtTen.requestFocus();
    }

    @FXML
    private void luu() {
        if (txtTen.getText().isEmpty()) {
            thongBao("Vui lòng nhập tên nhà cung cấp!");
            return;
        }

        NhaCungCap ncc = selectedNCC != null ? selectedNCC : new NhaCungCap();
        ncc.setTenNCC(txtTen.getText());
        ncc.setDienThoai(txtDienThoai.getText());
        ncc.setDiaChi(txtDiaChi.getText());
        ncc.setEmail(txtEmail.getText());
        ncc.setLoaiSanPham(cboLoaiSP.getValue());
        ncc.setDanhGia(spnDanhGia.getValue());
        ncc.setGhiChu(txtGhiChu.getText());

        boolean result;
        if (selectedNCC != null) {
            result = nccDAO.capNhat(ncc);
        } else {
            result = nccDAO.them(ncc);
        }

        if (result) {
            thongBao("✅ Đã lưu nhà cung cấp!");
            loadData();
            xoaForm();
        } else {
            thongBao("❌ Lỗi khi lưu!");
        }
    }

    @FXML
    private void xoa() {
        if (selectedNCC == null) {
            thongBao("Vui lòng chọn nhà cung cấp cần xóa!");
            return;
        }

        if (DialogUtil.confirm("Xác nhận xóa", "Bạn có chắc muốn xóa nhà cung cấp này?")) {
            if (nccDAO.xoa(selectedNCC.getMaNCC())) {
                thongBao("✅ Đã xóa!");
                loadData();
                xoaForm();
            }
        }
    }

    @FXML
    private void timKiem() {
        String keyword = txtTimKiem.getText();
        if (keyword.isEmpty()) {
            loadData();
        } else {
            tableNCC.setItems(FXCollections.observableArrayList(nccDAO.timKiem(keyword)));
        }
    }

    @FXML
    private void lamMoi() {
        txtTimKiem.clear();
        loadData();
        xoaForm();
    }

    private void xoaForm() {
        selectedNCC = null;
        txtTen.clear();
        txtDienThoai.clear();
        txtDiaChi.clear();
        txtEmail.clear();
        cboLoaiSP.setValue(null);
        spnDanhGia.getValueFactory().setValue(5);
        txtGhiChu.clear();
    }

    @FXML
    private void xemLichSu() {
        if (selectedNCC == null) {
            thongBao("Vui lòng chọn nhà cung cấp!");
            return;
        }

        // Import DAOs
        com.mekongfarm.dao.DonNhapDAO donNhapDAO = new com.mekongfarm.dao.DonNhapDAO();
        com.mekongfarm.dao.CongNoDAO congNoDAO = new com.mekongfarm.dao.CongNoDAO();

        // Lấy dữ liệu
        var donNhapList = donNhapDAO.layTheoNCC(selectedNCC.getMaNCC());
        var congNoList = congNoDAO.layTheoNCC(selectedNCC.getMaNCC());
        double tongNhap = donNhapDAO.tongTienNhapTheoNCC(selectedNCC.getMaNCC());

        // Build message
        StringBuilder sb = new StringBuilder();
        sb.append("📊 LỊCH SỬ GIAO DỊCH: ").append(selectedNCC.getTenNCC()).append("\n\n");

        sb.append("📦 ĐƠN NHẬP HÀNG: ").append(donNhapList.size()).append(" đơn\n");
        sb.append("💰 Tổng giá trị: ").append(String.format("%,.0f VNĐ", tongNhap)).append("\n\n");

        if (!donNhapList.isEmpty()) {
            sb.append("Các đơn gần nhất:\n");
            int count = Math.min(5, donNhapList.size());
            for (int i = 0; i < count; i++) {
                var dn = donNhapList.get(i);
                sb.append("  • ").append(dn.getMaDon())
                        .append(" - ").append(dn.getNgayNhapFormat())
                        .append(" - ").append(dn.getTrangThaiHienThi())
                        .append(" - ").append(dn.getThanhTienFormat()).append("\n");
            }
        }

        sb.append("\n💳 CÔNG NỢ PHẢI TRẢ: ").append(congNoList.size()).append(" khoản\n");
        if (!congNoList.isEmpty()) {
            double tongNo = congNoList.stream().mapToDouble(cn -> cn.getConNo()).sum();
            sb.append("Còn nợ: ").append(String.format("%,.0f VNĐ", tongNo)).append("\n");
        }

        DialogUtil.showSuccess("Lịch sử giao dịch", sb.toString());
    }

    private void thongBao(String msg) {
        DialogUtil.showSuccess("Thông báo", msg);
    }
}
