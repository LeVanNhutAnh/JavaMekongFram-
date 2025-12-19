package com.mekongfarm.controller;

import com.mekongfarm.dao.*;
import com.mekongfarm.model.*;
import com.mekongfarm.service.LogService;
import com.mekongfarm.util.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.Base64;

public class SanPhamController {
    @FXML
    private TextField txtTimKiem, txtMaSP, txtTenSP, txtDonGia, txtSoLuong, txtDonVi, txtMoTa;
    @FXML
    private ComboBox<LoaiSanPham> cboLoai, cboLoaiForm;
    @FXML
    private ComboBox<TinhThanh> cboTinh, cboTinhForm;
    @FXML
    private ComboBox<com.mekongfarm.model.NhaCungCap> cboNCC;
    @FXML
    private TableView<SanPham> tableSanPham;
    @FXML
    private TableColumn<SanPham, Void> colAction;
    @FXML
    private TitledPane paneForm;
    @FXML
    private ImageView imgPreview;
    @FXML
    private Label lblTongSP, lblTongSanPham, lblTongTonKho, lblTonKhoThap, lblGiaTriKho;

    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final DanhMucDAO danhMucDAO = new DanhMucDAO();
    private final com.mekongfarm.dao.NhaCungCapDAO nccDAO = new com.mekongfarm.dao.NhaCungCapDAO();
    private ObservableList<SanPham> danhSach = FXCollections.observableArrayList();
    private SanPham sanPhamHienTai;
    private String hinhAnhBase64 = null;

    @FXML
    public void initialize() {
        taiDanhMuc();
        taiDanhSach();
        taoNutThaoTac();
        capNhatThongKe();

        txtTimKiem.textProperty().addListener((obs, old, val) -> timKiem());
        cboLoai.setOnAction(e -> locTheoLoai());
        cboTinh.setOnAction(e -> locTheoTinh());
    }

    private void taiDanhMuc() {
        cboLoai.getItems().add(null);
        cboLoai.getItems().addAll(danhMucDAO.layTatCaLoai());
        cboLoaiForm.getItems().addAll(danhMucDAO.layTatCaLoai());

        cboTinh.getItems().add(null);
        cboTinh.getItems().addAll(danhMucDAO.layTatCaTinh());
        cboTinhForm.getItems().addAll(danhMucDAO.layTatCaTinh());

        // Load NCC
        cboNCC.getItems().add(null);
        cboNCC.getItems().addAll(nccDAO.layTatCa());
    }

    private void taiDanhSach() {
        danhSach.setAll(sanPhamDAO.layTatCa());
        tableSanPham.setItems(danhSach);
        capNhatThongKe();
    }

    private void capNhatThongKe() {
        int tongSP = danhSach.size();
        int tongTonKho = danhSach.stream().mapToInt(SanPham::getSoLuongTon).sum();
        int tonKhoThap = (int) danhSach.stream().filter(sp -> sp.getSoLuongTon() < 10).count();
        double giaTriKho = danhSach.stream().mapToDouble(sp -> sp.getDonGia() * sp.getSoLuongTon()).sum();

        if (lblTongSP != null)
            lblTongSP.setText(tongSP + " sản phẩm");
        if (lblTongSanPham != null)
            lblTongSanPham.setText(String.valueOf(tongSP));
        if (lblTongTonKho != null)
            lblTongTonKho.setText(String.valueOf(tongTonKho));
        if (lblTonKhoThap != null)
            lblTonKhoThap.setText(String.valueOf(tonKhoThap));
        if (lblGiaTriKho != null)
            lblGiaTriKho.setText(String.format("%,.0f VNĐ", giaTriKho));
    }

    private void taoNutThaoTac() {
        colAction.setCellFactory(col -> new TableCell<>() {
            private final Button btnSua = new Button("✏️");
            private final Button btnXoa = new Button("🗑️");
            private final HBox box = new HBox(5, btnSua, btnXoa);
            {
                btnSua.getStyleClass().add("btn-icon");
                btnXoa.getStyleClass().add("btn-icon-danger");
                btnSua.setOnAction(e -> suaSanPham(getTableRow().getItem()));
                btnXoa.setOnAction(e -> xoaSanPham(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    @FXML
    private void moFormThem() {
        sanPhamHienTai = null;
        hinhAnhBase64 = null;
        txtMaSP.setText(sanPhamDAO.layMaSPTiepTheo());
        txtTenSP.clear();
        txtDonGia.clear();
        txtSoLuong.clear();
        txtDonVi.setText("kg");
        txtMoTa.clear();
        cboLoaiForm.setValue(null);
        cboTinhForm.setValue(null);
        if (imgPreview != null)
            imgPreview.setImage(null);
        paneForm.setExpanded(true);
    }

    private void suaSanPham(SanPham sp) {
        sanPhamHienTai = sp;
        hinhAnhBase64 = sp.getHinhAnh();
        txtMaSP.setText(sp.getMaSP());
        txtTenSP.setText(sp.getTenSanPham());
        txtDonGia.setText(String.valueOf(sp.getDonGia()));
        txtSoLuong.setText(String.valueOf(sp.getSoLuongTon()));
        txtDonVi.setText(sp.getDonViTinh());
        txtMoTa.setText(sp.getMoTa());
        cboLoaiForm.getItems().stream().filter(l -> l.getMaLoai() == sp.getMaLoai())
                .findFirst().ifPresent(cboLoaiForm::setValue);
        cboTinhForm.getItems().stream().filter(t -> t.getMaTinh() == sp.getMaTinh())
                .findFirst().ifPresent(cboTinhForm::setValue);

        // Set NCC
        if (sp.getMaNCC() > 0) {
            cboNCC.getItems().stream().filter(n -> n != null && n.getMaNCC() == sp.getMaNCC())
                    .findFirst().ifPresent(cboNCC::setValue);
        }

        hienThiAnhTuBase64(sp.getHinhAnh());
        paneForm.setExpanded(true);
    }

    @FXML
    private void luuSanPham() {
        // Validate tên sản phẩm
        if (!ValidationUtil.isNotEmpty(txtTenSP.getText())) {
            ValidationUtil.setErrorStyle(txtTenSP);
            DialogUtil.showError("Lỗi nhập liệu", "Vui lòng nhập tên sản phẩm!");
            return;
        }
        ValidationUtil.clearErrorStyle(txtTenSP);

        if (!ValidationUtil.hasValidLength(txtTenSP.getText(), 2, 200)) {
            ValidationUtil.setErrorStyle(txtTenSP);
            DialogUtil.showError("Lỗi nhập liệu", "Tên sản phẩm phải từ 2-200 ký tự!");
            return;
        }

        // Validate loại và tỉnh
        if (cboLoaiForm.getValue() == null) {
            DialogUtil.showError("Lỗi nhập liệu", "Vui lòng chọn loại sản phẩm!");
            return;
        }
        if (cboTinhForm.getValue() == null) {
            DialogUtil.showError("Lỗi nhập liệu", "Vui lòng chọn tỉnh/thành!");
            return;
        }

        // Validate đơn giá
        if (!ValidationUtil.isPositiveNumber(txtDonGia.getText())) {
            ValidationUtil.setErrorStyle(txtDonGia);
            DialogUtil.showError("Lỗi đơn giá",
                    "Đơn giá không hợp lệ!\n" +
                            "• Phải là số dương\n" +
                            "• Không được âm hoặc 0\n" +
                            "• Ví dụ: 50000 hoặc 50,000");
            return;
        }
        ValidationUtil.clearErrorStyle(txtDonGia);

        // Validate số lượng
        if (!ValidationUtil.isNonNegativeInteger(txtSoLuong.getText())) {
            ValidationUtil.setErrorStyle(txtSoLuong);
            DialogUtil.showError("Lỗi số lượng",
                    "Số lượng không hợp lệ!\n" +
                            "• Phải là số nguyên không âm\n" +
                            "• Ví dụ: 100 hoặc 0");
            return;
        }
        ValidationUtil.clearErrorStyle(txtSoLuong);

        // Validate combo boxes
        if (cboLoaiForm.getValue() == null) {
            ValidationUtil.setErrorStyle(cboLoaiForm);
            DialogUtil.showError("Thiếu thông tin", "Vui lòng chọn loại sản phẩm!");
            return;
        }
        ValidationUtil.clearErrorStyle(cboLoaiForm);

        if (cboTinhForm.getValue() == null) {
            ValidationUtil.setErrorStyle(cboTinhForm);
            DialogUtil.showError("Thiếu thông tin", "Vui lòng chọn tỉnh/thành!");
            return;
        }
        ValidationUtil.clearErrorStyle(cboTinhForm);

        // Validate đơn vị
        if (!ValidationUtil.isNotEmpty(txtDonVi.getText())) {
            txtDonVi.setText("kg");
        }

        try {
            SanPham sp = sanPhamHienTai != null ? sanPhamHienTai : new SanPham();

            // Nếu thêm mới, generate mã mới để đảm bảo không trùng
            String maSP = txtMaSP.getText();
            if (sanPhamHienTai == null) {
                // Kiểm tra mã hiện tại có trùng không, nếu trùng thì tạo mới
                if (sanPhamDAO.kiemTraMaSPTonTai(maSP)) {
                    maSP = sanPhamDAO.layMaSPTiepTheo();
                    txtMaSP.setText(maSP);
                }
            }

            sp.setMaSP(maSP);
            sp.setTenSanPham(txtTenSP.getText().trim());
            sp.setMaLoai(cboLoaiForm.getValue().getMaLoai());
            sp.setMaTinh(cboTinhForm.getValue().getMaTinh());

            // Nhà cung cấp
            if (cboNCC.getValue() != null) {
                sp.setMaNCC(cboNCC.getValue().getMaNCC());
            }

            sp.setDonGia(ValidationUtil.parseDouble(txtDonGia.getText()));
            sp.setSoLuongTon(ValidationUtil.parseInt(txtSoLuong.getText()));
            sp.setDonViTinh(txtDonVi.getText().trim());
            sp.setMoTa(txtMoTa.getText() != null ? txtMoTa.getText().trim() : "");
            sp.setHinhAnh(hinhAnhBase64);

            LoadingUtil.showLoading("Đang lưu sản phẩm...");
            boolean ok = sanPhamHienTai != null ? sanPhamDAO.capNhat(sp) : sanPhamDAO.them(sp);
            LoadingUtil.hideLoading();

            if (ok) {
                if (sanPhamHienTai != null) {
                    LogService.logCapNhat("san_pham", sp.getMaSanPham(), "Cập nhật sản phẩm: " + sp.getTenSanPham());
                } else {
                    LogService.logThem("san_pham", sp.getMaSanPham(), "Thêm sản phẩm: " + sp.getTenSanPham());
                }
                DialogUtil.showSuccess("Thành công", "Đã lưu sản phẩm: " + sp.getTenSanPham());
                huyForm();
                taiDanhSach();
            } else {
                System.err.println("FAILED TO SAVE: maSP=" + sp.getMaSP() + ", ten=" + sp.getTenSanPham() +
                        ", maLoai=" + sp.getMaLoai() + ", maTinh=" + sp.getMaTinh());
                DialogUtil.showError("Lỗi lưu dữ liệu",
                        "Không thể lưu sản phẩm!\n\n" +
                                "Kiểm tra:\n• Mã sản phẩm: " + sp.getMaSP() + "\n" +
                                "• Loại: " + sp.getMaLoai() + "\n" +
                                "• Tỉnh: " + sp.getMaTinh() + "\n\n" +
                                "Vui lòng kiểm tra console để xem lỗi chi tiết.");
            }
        } catch (NumberFormatException e) {
            LoadingUtil.hideLoading();
            DialogUtil.showError("Lỗi định dạng",
                    "Lỗi định dạng số!\n" +
                            "Vui lòng kiểm tra lại đơn giá và số lượng.",
                    "Chi tiết: " + e.getMessage());
        } catch (Exception e) {
            LoadingUtil.hideLoading();
            DialogUtil.showError("Lỗi không xác định",
                    "Đã xảy ra lỗi khi lưu sản phẩm!",
                    "Chi tiết: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void xoaSanPham(SanPham sp) {
        if (sp == null)
            return;

        // Confirmation với chi tiết
        if (DialogUtil.confirmDelete("sản phẩm", sp.getTenSanPham())) {
            LoadingUtil.showLoading("Đang xóa sản phẩm...");
            boolean success = sanPhamDAO.xoa(sp.getMaSanPham());
            LoadingUtil.hideLoading();

            if (success) {
                LogService.logXoa("san_pham", sp.getMaSanPham(), "Xóa sản phẩm: " + sp.getTenSanPham());
                DialogUtil.showSuccess("Đã xóa", "Đã xóa sản phẩm: " + sp.getTenSanPham());
                taiDanhSach();
            } else {
                DialogUtil.showError("Lỗi xóa",
                        "Không thể xóa sản phẩm!\n" +
                                "Có thể sản phẩm đang được sử dụng trong đơn hàng.");
            }
        }
    }

    @FXML
    private void huyForm() {
        paneForm.setExpanded(false);
    }

    @FXML
    private void lamMoi() {
        txtTimKiem.clear();
        cboLoai.setValue(null);
        cboTinh.setValue(null);
        taiDanhSach();
    }

    private void timKiem() {
        String key = txtTimKiem.getText().trim();
        if (key.isEmpty())
            taiDanhSach();
        else
            danhSach.setAll(sanPhamDAO.timTheoTen(key));
    }

    private void locTheoLoai() {
        LoaiSanPham loai = cboLoai.getValue();
        if (loai == null)
            taiDanhSach();
        else
            danhSach.setAll(sanPhamDAO.locTheoLoai(loai.getMaLoai()));
    }

    private void locTheoTinh() {
        TinhThanh tinh = cboTinh.getValue();
        if (tinh == null)
            taiDanhSach();
        else
            danhSach.setAll(sanPhamDAO.locTheoTinh(tinh.getMaTinh()));
    }

    @FXML
    private void moThungRac() {
        java.util.List<SanPham> daXoa = sanPhamDAO.layDaXoa();

        if (daXoa.isEmpty()) {
            DialogUtil.showSuccess("Thùng rác trống", "🗑️ Không có sản phẩm nào trong thùng rác.");
            return;
        }

        // Tạo dialog chọn SP để khôi phục
        javafx.scene.control.ChoiceDialog<SanPham> dialog = new javafx.scene.control.ChoiceDialog<>(daXoa.get(0),
                daXoa);
        dialog.setTitle("🗑️ Thùng rác");
        dialog.setHeaderText("Có " + daXoa.size() + " sản phẩm đã xóa");
        dialog.setContentText("Chọn sản phẩm để khôi phục:");

        java.util.Optional<SanPham> result = dialog.showAndWait();
        result.ifPresent(sp -> {
            if (DialogUtil.confirm("Khôi phục sản phẩm",
                    "Bạn muốn khôi phục sản phẩm: " + sp.getTenSanPham() + "?")) {
                if (sanPhamDAO.khoiPhuc(sp.getMaSanPham())) {
                    DialogUtil.showSuccess("Khôi phục thành công", "✅ Đã khôi phục: " + sp.getTenSanPham());
                    taiDanhSach();
                } else {
                    DialogUtil.showError("Lỗi", "Không thể khôi phục sản phẩm!");
                }
            }
        });
    }

    @FXML
    private void xuatPDF() {
        try {
            javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
            fc.setTitle("Lưu file PDF");
            fc.setInitialFileName("san_pham.pdf");
            fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("PDF", "*.pdf"));
            java.io.File file = fc.showSaveDialog(tableSanPham.getScene().getWindow());
            if (file != null) {
                LoadingUtil.showLoading("Đang xuất file PDF...");
                new Thread(() -> {
                    try {
                        new com.mekongfarm.service.PDFExportService().xuatSanPhamPDF(danhSach, file);
                        LoadingUtil.hideLoading();
                        javafx.application.Platform.runLater(() -> DialogUtil.showSuccess("Xuất file thành công",
                                "Đã xuất " + danhSach.size() + " sản phẩm ra file:\n" + file.getName()));
                    } catch (Exception e) {
                        LoadingUtil.hideLoading();
                        javafx.application.Platform.runLater(
                                () -> DialogUtil.showError("Lỗi xuất PDF", "Không thể xuất file PDF!", e.getMessage()));
                    }
                }).start();
            }
        } catch (Exception e) {
            LoadingUtil.hideLoading();
            DialogUtil.showError("Lỗi", "Lỗi xuất PDF: " + e.getMessage());
        }
    }

    @FXML
    private void xuatExcel() {
        try {
            javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
            fc.setTitle("Lưu file Excel");
            fc.setInitialFileName("san_pham.xlsx");
            fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Excel", "*.xlsx"));
            java.io.File file = fc.showSaveDialog(tableSanPham.getScene().getWindow());
            if (file != null) {
                LoadingUtil.showLoading("Đang xuất file Excel...");
                new Thread(() -> {
                    try {
                        new com.mekongfarm.service.ExcelExportService().xuatSanPhamExcel(danhSach, file);
                        LoadingUtil.hideLoading();
                        javafx.application.Platform.runLater(() -> DialogUtil.showSuccess("Xuất file thành công",
                                "Đã xuất " + danhSach.size() + " sản phẩm ra file:\n" + file.getName()));
                    } catch (Exception e) {
                        LoadingUtil.hideLoading();
                        javafx.application.Platform.runLater(() -> DialogUtil.showError("Lỗi xuất Excel",
                                "Không thể xuất file Excel!", e.getMessage()));
                    }
                }).start();
            }
        } catch (Exception e) {
            LoadingUtil.hideLoading();
            DialogUtil.showError("Lỗi", "Lỗi xuất Excel: " + e.getMessage());
        }
    }

    @FXML
    private void chonAnh() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh sản phẩm");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File file = fileChooser.showOpenDialog(imgPreview.getScene().getWindow());
        if (file != null && imgPreview != null) {
            try {
                Image image = new Image(file.toURI().toString());
                imgPreview.setImage(image);

                // Chuyển ảnh sang Base64 để lưu vào database
                byte[] fileContent = java.nio.file.Files.readAllBytes(file.toPath());
                hinhAnhBase64 = Base64.getEncoder().encodeToString(fileContent);
            } catch (Exception e) {
                DialogUtil.showError("Lỗi tải ảnh", "Không thể tải ảnh!", e.getMessage());
            }
        }
    }

    private void hienThiAnhTuBase64(String base64) {
        if (base64 != null && !base64.isEmpty() && imgPreview != null) {
            try {
                byte[] imageBytes = Base64.getDecoder().decode(base64);
                Image image = new Image(new java.io.ByteArrayInputStream(imageBytes));
                imgPreview.setImage(image);
            } catch (Exception e) {
                System.err.println("Lỗi hiển thị ảnh: " + e.getMessage());
            }
        }
    }
}
