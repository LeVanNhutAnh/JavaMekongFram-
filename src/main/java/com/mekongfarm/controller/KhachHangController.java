package com.mekongfarm.controller;

import com.mekongfarm.dao.KhachHangDAO;
import com.mekongfarm.dao.CongNoDAO;
import com.mekongfarm.dao.ThongKeDAO;
import com.mekongfarm.model.KhachHang;
import com.mekongfarm.service.LogService;
import com.mekongfarm.util.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class KhachHangController {
    @FXML
    private TextField txtTimKiem, txtMaKH, txtHoTen, txtDiaChi, txtSDT, txtEmail;
    @FXML
    private TableView<KhachHang> tableKhachHang;
    @FXML
    private TableColumn<KhachHang, Void> colAction;
    @FXML
    private TitledPane paneForm;
    @FXML
    private Label lblTongKH, lblTongKhachHang, lblKHMuaNhieu, lblTongDoanhThu, lblTongCongNo;

    private final KhachHangDAO dao = new KhachHangDAO();
    private final CongNoDAO congNoDAO = new CongNoDAO();
    private final ThongKeDAO thongKeDAO = new ThongKeDAO();
    private ObservableList<KhachHang> danhSach = FXCollections.observableArrayList();
    private KhachHang khachHangHienTai;

    @FXML
    public void initialize() {
        taiDanhSach();
        taoNutThaoTac();
        capNhatThongKe();
        txtTimKiem.textProperty().addListener((o, old, val) -> {
            if (val.isEmpty())
                taiDanhSach();
            else
                danhSach.setAll(dao.timKiem(val));
        });
    }

    private void taiDanhSach() {
        danhSach.setAll(dao.layTatCa());
        tableKhachHang.setItems(danhSach);
        capNhatThongKe();
    }

    private void capNhatThongKe() {
        int tongKH = danhSach.size();
        double tongCongNo = congNoDAO.tongCongNo();
        double tongDoanhThu = thongKeDAO.tinhTongDoanhThu();

        if (lblTongKH != null)
            lblTongKH.setText(tongKH + " khách hàng");
        if (lblTongKhachHang != null)
            lblTongKhachHang.setText(String.valueOf(tongKH));
        if (lblKHMuaNhieu != null)
            lblKHMuaNhieu.setText(String.valueOf(thongKeDAO.demTongDonHang())); // Số đơn hàng
        if (lblTongDoanhThu != null)
            lblTongDoanhThu.setText(String.format("%,.0f VNĐ", tongDoanhThu));
        if (lblTongCongNo != null)
            lblTongCongNo.setText(String.format("%,.0f VNĐ", tongCongNo));
    }

    private void taoNutThaoTac() {
        colAction.setCellFactory(col -> new TableCell<>() {
            private final Button btnSua = new Button("✏️");
            private final Button btnXoa = new Button("🗑️");
            private final HBox box = new HBox(5, btnSua, btnXoa);
            {
                btnSua.getStyleClass().add("btn-icon");
                btnXoa.getStyleClass().add("btn-icon-danger");
                btnSua.setOnAction(e -> sua(getTableRow().getItem()));
                btnXoa.setOnAction(e -> xoa(getTableRow().getItem()));
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
        khachHangHienTai = null;
        txtMaKH.setText(dao.layMaKHTiepTheo());
        txtHoTen.clear();
        txtDiaChi.clear();
        txtSDT.clear();
        txtEmail.clear();
        paneForm.setExpanded(true);
    }

    private void sua(KhachHang kh) {
        khachHangHienTai = kh;
        txtMaKH.setText(kh.getMaKH());
        txtHoTen.setText(kh.getHoTen());
        txtDiaChi.setText(kh.getDiaChi());
        txtSDT.setText(kh.getSoDienThoai());
        txtEmail.setText(kh.getEmail());
        paneForm.setExpanded(true);
    }

    @FXML
    private void luu() {
        // Validate họ tên
        if (!ValidationUtil.isNotEmpty(txtHoTen.getText())) {
            ValidationUtil.setErrorStyle(txtHoTen);
            DialogUtil.showError("Lỗi nhập liệu", "Vui lòng nhập họ tên khách hàng!");
            return;
        }
        ValidationUtil.clearErrorStyle(txtHoTen);

        if (!ValidationUtil.hasValidLength(txtHoTen.getText(), 2, 100)) {
            ValidationUtil.setErrorStyle(txtHoTen);
            DialogUtil.showError("Lỗi nhập liệu", "Họ tên phải từ 2-100 ký tự!");
            return;
        }

        // Validate email (nếu có)
        if (!ValidationUtil.isValidEmail(txtEmail.getText())) {
            ValidationUtil.setErrorStyle(txtEmail);
            DialogUtil.showError("Lỗi email",
                    "Email không hợp lệ!\n" +
                            "• Ví dụ: user@example.com\n" +
                            "• Hoặc để trống nếu không có");
            return;
        }
        ValidationUtil.clearErrorStyle(txtEmail);

        // Validate số điện thoại (nếu có)
        if (!ValidationUtil.isValidPhone(txtSDT.getText())) {
            ValidationUtil.setErrorStyle(txtSDT);
            DialogUtil.showError("Lỗi số điện thoại",
                    "Số điện thoại không hợp lệ!\n" +
                            "• Ví dụ: 0912345678 hoặc +84912345678\n" +
                            "• Hoặc để trống nếu không có");
            return;
        }
        ValidationUtil.clearErrorStyle(txtSDT);

        LoadingUtil.showLoading("Đang lưu khách hàng...");

        try {
            KhachHang kh = khachHangHienTai != null ? khachHangHienTai : new KhachHang();

            // Nếu thêm mới, generate mã mới để đảm bảo không trùng
            String maKH = txtMaKH.getText();
            if (khachHangHienTai == null) {
                // Kiểm tra mã hiện tại có trùng không, nếu trùng thì tạo mới
                if (dao.kiemTraMaKHTonTai(maKH)) {
                    maKH = dao.layMaKHTiepTheo();
                    txtMaKH.setText(maKH);
                }
            }

            kh.setMaKH(maKH);
            kh.setHoTen(txtHoTen.getText().trim());
            kh.setDiaChi(txtDiaChi.getText() != null ? txtDiaChi.getText().trim() : "");
            kh.setSoDienThoai(txtSDT.getText() != null ? txtSDT.getText().trim() : "");
            kh.setEmail(txtEmail.getText() != null ? txtEmail.getText().trim() : "");

            boolean ok = khachHangHienTai != null ? dao.capNhat(kh) : dao.them(kh);
            LoadingUtil.hideLoading();

            if (ok) {
                if (khachHangHienTai != null) {
                    LogService.logCapNhat("khach_hang", kh.getMaKhachHang(), "Cập nhật khách hàng: " + kh.getHoTen());
                } else {
                    LogService.logThem("khach_hang", kh.getMaKhachHang(), "Thêm khách hàng: " + kh.getHoTen());
                }
                DialogUtil.showSuccess("Thành công", "Đã lưu khách hàng: " + kh.getHoTen());
                huy();
                taiDanhSach();
            } else {
                DialogUtil.showError("Lỗi lưu dữ liệu",
                        "Không thể lưu khách hàng!\n" +
                                "Vui lòng thử lại hoặc liên hệ hỗ trợ.");
            }
        } catch (Exception e) {
            LoadingUtil.hideLoading();
            DialogUtil.showError("Lỗi", "Đã xảy ra lỗi!", e.getMessage());
            e.printStackTrace();
        }
    }

    private void xoa(KhachHang kh) {
        if (kh == null)
            return;

        if (DialogUtil.confirmDelete("khách hàng", kh.getHoTen())) {
            LoadingUtil.showLoading("Đang xóa khách hàng...");
            boolean success = dao.xoa(kh.getMaKhachHang());
            LoadingUtil.hideLoading();

            if (success) {
                LogService.logXoa("khach_hang", kh.getMaKhachHang(), "Xóa khách hàng: " + kh.getHoTen());
                DialogUtil.showSuccess("Đã xóa", "Đã xóa khách hàng: " + kh.getHoTen());
                taiDanhSach();
            } else {
                DialogUtil.showError("Lỗi xóa",
                        "Không thể xóa khách hàng!\n" +
                                "Có thể khách hàng đang có đơn hàng hoặc công nợ.");
            }
        }
    }

    @FXML
    private void huy() {
        paneForm.setExpanded(false);
    }

    @FXML
    private void lamMoi() {
        txtTimKiem.clear();
        taiDanhSach();
    }

    @FXML
    private void xuatExcel() {
        LoadingUtil.showLoading("Đang xuất danh sách khách hàng...");
        new Thread(() -> {
            try {
                javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
                fc.setTitle("Lưu file Excel");
                fc.setInitialFileName("khach_hang.xlsx");
                fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Excel", "*.xlsx"));

                javafx.application.Platform.runLater(() -> {
                    java.io.File file = fc.showSaveDialog(tableKhachHang.getScene().getWindow());
                    if (file != null) {
                        try {
                            new com.mekongfarm.service.ExcelExportService().xuatKhachHangExcel(danhSach, file);
                            LoadingUtil.hideLoading();
                            DialogUtil.showSuccess("Xuất file thành công",
                                    "Đã xuất " + danhSach.size() + " khách hàng ra file:\n" + file.getName());
                        } catch (Exception e) {
                            LoadingUtil.hideLoading();
                            DialogUtil.showError("Lỗi xuất Excel", "Không thể xuất file!", e.getMessage());
                        }
                    } else {
                        LoadingUtil.hideLoading();
                    }
                });
            } catch (Exception e) {
                LoadingUtil.hideLoading();
                javafx.application.Platform
                        .runLater(() -> DialogUtil.showError("Lỗi", "Lỗi xuất Excel!", e.getMessage()));
            }
        }).start();
    }


}
