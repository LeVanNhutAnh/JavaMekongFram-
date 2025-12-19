package com.mekongfarm.controller;

import com.mekongfarm.dao.DonNhapDAO;
import com.mekongfarm.dao.NhaCungCapDAO;
import com.mekongfarm.dao.SanPhamDAO;
import com.mekongfarm.dao.CongNoDAO;
import com.mekongfarm.model.CongNo;
import com.mekongfarm.model.DonNhap;
import com.mekongfarm.model.ChiTietDonNhap;
import com.mekongfarm.model.NhaCungCap;
import com.mekongfarm.model.SanPham;
import com.mekongfarm.util.DialogUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.LocalDateTime;

/**
 * Controller quản lý Đơn nhập hàng từ Nhà cung cấp
 */
public class DonNhapController {

    @FXML
    private TableView<DonNhap> tableDonNhap;
    @FXML
    private TableColumn<DonNhap, String> colMaDon;
    @FXML
    private TableColumn<DonNhap, String> colNCC;
    @FXML
    private TableColumn<DonNhap, String> colNgayNhap;
    @FXML
    private TableColumn<DonNhap, String> colTongTien;
    @FXML
    private TableColumn<DonNhap, String> colTrangThai;
    @FXML
    private TableColumn<DonNhap, String> colNguoiTao;
    @FXML
    private TableColumn<DonNhap, Void> colAction;

    @FXML
    private TableView<ChiTietDonNhap> tableChiTiet;
    @FXML
    private TableColumn<ChiTietDonNhap, String> colSanPham;
    @FXML
    private TableColumn<ChiTietDonNhap, Integer> colSoLuong;
    @FXML
    private TableColumn<ChiTietDonNhap, String> colDonGia;
    @FXML
    private TableColumn<ChiTietDonNhap, String> colThanhTien;

    @FXML
    private ComboBox<NhaCungCap> cboNCC;
    @FXML
    private ComboBox<String> cboTrangThai;
    @FXML
    private ComboBox<NhaCungCap> cboNhaCungCap;
    @FXML
    private ComboBox<SanPham> cboSanPham;
    @FXML
    private DatePicker dpTuNgay, dpDenNgay;

    @FXML
    private TextField txtMaDon, txtGhiChu, txtSoLuong, txtDonGia;
    @FXML
    private TitledPane paneForm;

    @FXML
    private Label lblTongDon, lblTongDonNhap, lblDaDuyet, lblChoDuyet, lblTongGiaTri, lblTongTien;

    private DonNhapDAO donNhapDAO = new DonNhapDAO();
    private NhaCungCapDAO nccDAO = new NhaCungCapDAO();
    private SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private CongNoDAO congNoDAO = new CongNoDAO(); // Auto-create công nợ phải trả

    private ObservableList<DonNhap> dsDonNhap = FXCollections.observableArrayList();
    private ObservableList<ChiTietDonNhap> dsChiTiet = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTable();

        // Set prefHeight for table
        tableDonNhap.setPrefHeight(300);
        tableDonNhap.setMinHeight(250);

        // Load combos
        cboTrangThai.getItems().addAll("Tất cả", "Chờ duyệt", "Đã duyệt", "Đã nhập kho", "Đã hủy");
        cboTrangThai.setValue("Tất cả");
        cboTrangThai.setOnAction(e -> locDanhSach());

        cboNCC.getItems().addAll(nccDAO.layTatCa());
        cboNhaCungCap.getItems().addAll(nccDAO.layTatCa());
        cboSanPham.getItems().addAll(sanPhamDAO.layTatCa());

        tableChiTiet.setItems(dsChiTiet);

        taiDanhSach();
        taoNutThaoTac();
        capNhatThongKe();
    }

    private void setupTable() {
        // Setup tableDonNhap columns
        colMaDon.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("maDon"));
        colNCC.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("tenNCC"));
        colNgayNhap.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("ngayNhapFormat"));
        colTongTien.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("thanhTienFormat"));
        colTrangThai.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("trangThaiHienThi"));
        colNguoiTao.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("tenNguoiDung"));

        // Setup tableChiTiet columns
        colSanPham.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("tenSanPham"));
        colSoLuong.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("soLuong"));
        colDonGia.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("donGiaFormat"));
        colThanhTien.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("thanhTienFormat"));
    }

    private void taiDanhSach() {
        dsDonNhap.setAll(donNhapDAO.layTatCa());
        tableDonNhap.setItems(dsDonNhap);
        capNhatThongKe();
    }

    private void locDanhSach() {
        // TODO: Implement filtering
        taiDanhSach();
    }

    private void capNhatThongKe() {
        int tong = dsDonNhap.size();
        int choDuyet = 0;
        int daDuyet = 0;
        double tongGiaTri = 0;

        for (DonNhap dn : dsDonNhap) {
            if ("cho_duyet".equals(dn.getTrangThai()))
                choDuyet++;
            if ("da_duyet".equals(dn.getTrangThai()) || "da_nhap".equals(dn.getTrangThai()))
                daDuyet++;
            if (!"da_huy".equals(dn.getTrangThai()))
                tongGiaTri += dn.getThanhTien();
        }

        lblTongDon.setText(tong + " đơn nhập");
        lblTongDonNhap.setText(String.valueOf(tong));
        lblChoDuyet.setText(String.valueOf(choDuyet));
        lblDaDuyet.setText(String.valueOf(daDuyet));
        lblTongGiaTri.setText(String.format("%,.0f VNĐ", tongGiaTri));
    }

    private void taoNutThaoTac() {
        colAction.setCellFactory(col -> new TableCell<>() {
            private final Button btnXem = new Button("Xem");
            private final Button btnDuyet = new Button("Duyệt");
            private final Button btnNhapKho = new Button("Nhập kho");
            private final Button btnHuy = new Button("Hủy");

            {
                btnXem.setStyle(
                        "-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 3 8;");
                btnDuyet.setStyle(
                        "-fx-background-color: #22c55e; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 3 8;");
                btnNhapKho.setStyle(
                        "-fx-background-color: #f59e0b; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 3 8;");
                btnHuy.setStyle(
                        "-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 3 8;");

                btnXem.setOnAction(e -> xemChiTiet(getTableRow().getItem()));
                btnDuyet.setOnAction(e -> duyetDon(getTableRow().getItem()));
                btnNhapKho.setOnAction(e -> nhapKho(getTableRow().getItem()));
                btnHuy.setOnAction(e -> huyDon(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    DonNhap dn = getTableRow().getItem();
                    HBox box = new HBox(5);
                    box.getChildren().add(btnXem);

                    if (dn != null) {
                        String status = dn.getTrangThai();
                        if ("cho_duyet".equals(status)) {
                            box.getChildren().addAll(btnDuyet, btnHuy);
                        } else if ("da_duyet".equals(status) && !dn.isDaNhapKho()) {
                            box.getChildren().add(btnNhapKho);
                        }
                    }
                    setGraphic(box);
                }
            }
        });
    }

    @FXML
    private void taoDonMoi() {
        dsChiTiet.clear();
        txtMaDon.setText(donNhapDAO.layMaDonTiepTheo());
        cboNhaCungCap.setValue(null);
        txtGhiChu.clear();
        capNhatTongTien();
        paneForm.setExpanded(true);
    }

    @FXML
    private void themSanPham() {
        SanPham sp = cboSanPham.getValue();
        if (sp == null) {
            DialogUtil.showWarning("Thiếu thông tin", "Vui lòng chọn sản phẩm!");
            return;
        }

        int soLuong;
        double donGia;
        try {
            soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            donGia = Double.parseDouble(txtDonGia.getText().replace(",", "").trim());
        } catch (NumberFormatException e) {
            DialogUtil.showWarning("Lỗi nhập liệu", "Số lượng và đơn giá phải là số!");
            return;
        }

        ChiTietDonNhap ct = new ChiTietDonNhap(sp.getMaSanPham(), sp.getTenSanPham(), soLuong, donGia);
        dsChiTiet.add(ct);

        txtSoLuong.clear();
        txtDonGia.clear();
        cboSanPham.setValue(null);
        capNhatTongTien();
    }

    private void capNhatTongTien() {
        double tong = dsChiTiet.stream().mapToDouble(ChiTietDonNhap::getThanhTien).sum();
        lblTongTien.setText(String.format("💰 Tổng: %,.0f VNĐ", tong));
    }

    @FXML
    private void luuDon() {
        NhaCungCap ncc = cboNhaCungCap.getValue();
        if (ncc == null) {
            DialogUtil.showWarning("Thiếu thông tin", "Vui lòng chọn nhà cung cấp!");
            return;
        }

        if (dsChiTiet.isEmpty()) {
            DialogUtil.showWarning("Thiếu sản phẩm", "Đơn nhập phải có ít nhất 1 sản phẩm!");
            return;
        }

        double tongTien = dsChiTiet.stream().mapToDouble(ChiTietDonNhap::getThanhTien).sum();

        DonNhap dn = new DonNhap();
        dn.setMaDon(txtMaDon.getText());
        dn.setMaNCC(ncc.getMaNCC());
        dn.setMaNguoiDung(1); // Default user
        dn.setNgayNhap(LocalDateTime.now());
        dn.setTongTien(tongTien);
        dn.setThanhTien(tongTien);
        dn.setTrangThai("cho_duyet");
        dn.setGhiChu(txtGhiChu.getText());

        int maDonNhap = donNhapDAO.them(dn);
        if (maDonNhap > 0) {
            // Lưu chi tiết
            for (ChiTietDonNhap ct : dsChiTiet) {
                ct.setMaDonNhap(maDonNhap);
                donNhapDAO.themChiTiet(ct);
            }

            DialogUtil.showSuccess("Thành công", "Đã tạo đơn nhập " + dn.getMaDon());
            paneForm.setExpanded(false);
            taiDanhSach();
        } else {
            DialogUtil.showError("Lỗi", "Không thể tạo đơn nhập!");
        }
    }

    @FXML
    private void huy() {
        dsChiTiet.clear();
        paneForm.setExpanded(false);
    }

    @FXML
    private void lamMoi() {
        taiDanhSach();
    }

    private void xemChiTiet(DonNhap dn) {
        if (dn == null)
            return;

        var chiTiet = donNhapDAO.layChiTiet(dn.getMaDonNhap());
        StringBuilder sb = new StringBuilder();
        sb.append("Đơn nhập: ").append(dn.getMaDon()).append("\n");
        sb.append("NCC: ").append(dn.getTenNCC()).append("\n");
        sb.append("Ngày: ").append(dn.getNgayNhapFormat()).append("\n");
        sb.append("Trạng thái: ").append(dn.getTrangThaiHienThi()).append("\n\n");
        sb.append("Chi tiết:\n");

        for (var ct : chiTiet) {
            sb.append("- ").append(ct.getTenSanPham())
                    .append(" x ").append(ct.getSoLuong())
                    .append(" = ").append(ct.getThanhTienFormat()).append("\n");
        }
        sb.append("\nTổng: ").append(dn.getThanhTienFormat());

        DialogUtil.showSuccess("Chi tiết đơn nhập", sb.toString());
    }

    private void duyetDon(DonNhap dn) {
        if (dn == null)
            return;

        if (DialogUtil.confirm("Xác nhận duyệt", "Duyệt đơn nhập " + dn.getMaDon() + "?")) {
            if (donNhapDAO.capNhatTrangThai(dn.getMaDonNhap(), "da_duyet")) {

                // Tự động tạo công nợ phải trả (nợ NCC)
                try {
                    CongNo cn = new CongNo();
                    cn.setLoaiCongNo("phai_tra");
                    cn.setMaNCC(dn.getMaNCC());
                    cn.setSoTien(dn.getThanhTien());
                    cn.setNgayPhatSinh(java.time.LocalDate.now());
                    cn.setHanThanhToan(java.time.LocalDate.now().plusDays(30)); // 30 ngày
                    cn.setTrangThai("Chưa thanh toán");
                    cn.setGhiChu("Đơn nhập: " + dn.getMaDon());
                    congNoDAO.them(cn);
                } catch (Exception e) {
                    System.err.println("Lỗi tạo công nợ NCC: " + e.getMessage());
                }

                DialogUtil.showSuccess("Thành công",
                        "Đã duyệt đơn nhập: " + dn.getMaDon() + "\n" +
                                "✅ Công nợ phải trả đã tự động tạo!");
                taiDanhSach();
            }
        }
    }

    private void nhapKho(DonNhap dn) {
        if (dn == null)
            return;

        if (DialogUtil.confirm("Xác nhận nhập kho",
                "Nhập kho đơn " + dn.getMaDon() + "?\n" +
                        "Tồn kho sẽ được cập nhật tự động.")) {

            // Cập nhật tồn kho
            var chiTiet = donNhapDAO.layChiTiet(dn.getMaDonNhap());
            for (var ct : chiTiet) {
                sanPhamDAO.capNhatTonKho(ct.getMaSanPham(), ct.getSoLuong());
            }

            // Đánh dấu đã nhập kho
            if (donNhapDAO.danhDauDaNhapKho(dn.getMaDonNhap())) {
                DialogUtil.showSuccess("Thành công", "Đã nhập kho thành công!\nTồn kho đã được cập nhật.");
                taiDanhSach();
            }
        }
    }

    private void huyDon(DonNhap dn) {
        if (dn == null)
            return;

        if (DialogUtil.confirm("Xác nhận hủy", "Hủy đơn nhập " + dn.getMaDon() + "?")) {
            if (donNhapDAO.capNhatTrangThai(dn.getMaDonNhap(), "da_huy")) {
                DialogUtil.showSuccess("Thành công", "Đã hủy đơn nhập!");
                taiDanhSach();
            }
        }
    }
}
