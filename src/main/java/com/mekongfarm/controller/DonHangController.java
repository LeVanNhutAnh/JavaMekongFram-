package com.mekongfarm.controller;

import com.mekongfarm.dao.*;
import com.mekongfarm.model.*;
import com.mekongfarm.service.LogService;
import com.mekongfarm.util.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class DonHangController {
    @FXML
    private ComboBox<String> cboTrangThai;
    @FXML
    private TableView<DonHang> tableDonHang;
    @FXML
    private TableColumn<DonHang, Void> colAction;
    @FXML
    private TitledPane paneForm;
    @FXML
    private TextField txtMaDH, txtGhiChu, txtSoLuong, txtDonGiaBan;
    @FXML
    private ComboBox<KhachHang> cboKhachHang;
    @FXML
    private ComboBox<SanPham> cboSanPham;
    @FXML
    private TableView<ChiTietDonHang> tableChiTiet;
    @FXML
    private Label lblTongTien, lblTongDon, lblTongDonHang, lblDonHoanThanh, lblDonDangXuLy, lblTongDoanhThu,
            lblDonHomNay;

    private final DonHangDAO donHangDAO = new DonHangDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private ObservableList<DonHang> dsDonHang = FXCollections.observableArrayList();
    private ObservableList<ChiTietDonHang> dsChiTiet = FXCollections.observableArrayList();
    private double tongTien = 0;

    @FXML
    public void initialize() {
        System.out.println("🚀 DonHangController.initialize() started");
        
        // Force table height
        tableDonHang.setPrefHeight(350);
        tableDonHang.setMinHeight(300);

        cboTrangThai.getItems().addAll("Tất cả", "Chờ xử lý", "Đang giao", "Đã giao", "Đã hủy");
        cboTrangThai.setValue("Tất cả");
        cboTrangThai.setOnAction(e -> locTheoTrangThai());

        cboKhachHang.getItems().addAll(khachHangDAO.layTatCa());
        cboSanPham.getItems().addAll(sanPhamDAO.layTatCa());
        tableChiTiet.setItems(dsChiTiet);

        taiDanhSach();
        taoNutThaoTac();
        taoMauTrangThai();
        capNhatThongKe();
        
        System.out.println("✅ DonHangController.initialize() completed - " + dsDonHang.size() + " orders loaded");
    }

    private void taiDanhSach() {
        System.out.println("📥 Loading orders from database...");
        dsDonHang.setAll(donHangDAO.layTatCa());
        System.out.println("   Loaded " + dsDonHang.size() + " orders from database");
        tableDonHang.setItems(dsDonHang);
        System.out.println("   Table items set: " + tableDonHang.getItems().size() + " items");
        capNhatThongKe();
        
        // Debug: Print first few orders
        if (!dsDonHang.isEmpty()) {
            System.out.println("   Sample orders:");
            for (int i = 0; i < Math.min(3, dsDonHang.size()); i++) {
                DonHang dh = dsDonHang.get(i);
                System.out.println("   - " + dh.getMaDH() + " | " + dh.getTenKhachHang() + " | " + dh.getTrangThaiHienThi());
            }
        } else {
            System.out.println("   ⚠️ WARNING: No orders found in database!");
        }
    }

    private void capNhatThongKe() {
        int tongDH = dsDonHang.size();
        int hoanThanh = (int) dsDonHang.stream().filter(dh -> "da_giao".equals(dh.getTrangThai())).count();
        int dangXuLy = (int) dsDonHang.stream()
                .filter(dh -> "cho_xu_ly".equals(dh.getTrangThai()) || "dang_giao".equals(dh.getTrangThai())).count();
        // Tính doanh thu tất cả đơn hàng (trừ đã hủy)
        double doanhThu = dsDonHang.stream()
                .filter(dh -> !"da_huy".equals(dh.getTrangThai()))
                .mapToDouble(DonHang::getThanhTien).sum();
        java.time.LocalDate today = java.time.LocalDate.now();
        int homNay = (int) dsDonHang.stream()
                .filter(dh -> dh.getNgayDat() != null && dh.getNgayDat().toLocalDate().equals(today)).count();

        if (lblTongDon != null)
            lblTongDon.setText(tongDH + " đơn hàng");
        if (lblTongDonHang != null)
            lblTongDonHang.setText(String.valueOf(tongDH));
        if (lblDonHoanThanh != null)
            lblDonHoanThanh.setText(String.valueOf(hoanThanh));
        if (lblDonDangXuLy != null)
            lblDonDangXuLy.setText(String.valueOf(dangXuLy));
        if (lblTongDoanhThu != null)
            lblTongDoanhThu.setText(String.format("%,.0f VNĐ", doanhThu));
        if (lblDonHomNay != null)
            lblDonHomNay.setText(String.valueOf(homNay));
    }

    private void taoNutThaoTac() {
        colAction.setCellFactory(col -> new TableCell<>() {
            private final Button btnXem = new Button("Xem");
            private final Button btnDangGiao = new Button("Giao");
            private final Button btnHoanThanh = new Button("Xong");
            private final Button btnHuy = new Button("Hủy");
            private final HBox box = new HBox(5, btnXem, btnDangGiao, btnHoanThanh, btnHuy);
            {
                btnXem.getStyleClass().add("btn-icon");
                btnXem.setStyle(
                        "-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 3 8;");

                btnDangGiao.getStyleClass().add("btn-icon");
                btnDangGiao.setStyle(
                        "-fx-background-color: #f59e0b; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 3 8;");

                btnHoanThanh.getStyleClass().add("btn-icon");
                btnHoanThanh.setStyle(
                        "-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 3 8;");

                btnHuy.getStyleClass().add("btn-icon-danger");
                btnHuy.setStyle("-fx-font-size: 10px; -fx-padding: 3 8;");

                btnXem.setOnAction(e -> xemChiTiet(getTableRow().getItem()));
                btnDangGiao.setOnAction(e -> chuyenDangGiao(getTableRow().getItem()));
                btnHoanThanh.setOnAction(e -> hoanThanhDon(getTableRow().getItem()));
                btnHuy.setOnAction(e -> huyDon(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                DonHang dh = getTableRow() != null ? getTableRow().getItem() : null;
                if (empty || dh == null) {
                    setGraphic(null);
                } else {
                    String status = dh.getTrangThai();
                    // Show "Giao" button only for "Chờ xử lý" orders
                    btnDangGiao.setVisible("cho_xu_ly".equals(status));
                    btnDangGiao.setManaged(btnDangGiao.isVisible());

                    // Show "Xong" button for "Chờ xử lý" and "Đang giao" orders
                    btnHoanThanh.setVisible("cho_xu_ly".equals(status) || "dang_giao".equals(status));
                    btnHoanThanh.setManaged(btnHoanThanh.isVisible());

                    // Hide cancel button if already cancelled
                    btnHuy.setVisible(!"da_huy".equals(status) && !"da_giao".equals(status));
                    btnHuy.setManaged(btnHuy.isVisible());

                    setGraphic(box);
                }
            }
        });
    }

    private void taoMauTrangThai() {
        tableDonHang.setRowFactory(tv -> new javafx.scene.control.TableRow<DonHang>() {
            @Override
            protected void updateItem(DonHang dh, boolean empty) {
                super.updateItem(dh, empty);
                if (empty || dh == null) {
                    setStyle("");
                } else {
                    String status = dh.getTrangThai();
                    // Xử lý null status
                    if (status == null) {
                        status = "cho_xu_ly"; // Default to "Chờ xử lý"
                    }
                    String bgColor = switch (status) {
                        case "cho_xu_ly" -> "#fef3c7"; // Vàng nhạt
                        case "dang_giao" -> "#fed7aa"; // Cam nhạt
                        case "da_giao" -> "#d1fae5"; // Xanh lá nhạt
                        case "da_huy" -> "#fee2e2"; // Đỏ nhạt
                        default -> "#ffffff"; // Trắng
                    };
                    setStyle("-fx-background-color: " + bgColor + ";");
                }
            }
        });
    }

    @FXML
    private void taoDonMoi() {
        dsChiTiet.clear();
        tongTien = 0;
        lblTongTien.setText("Tổng: 0 VNĐ");
        txtMaDH.setText(donHangDAO.layMaDHTiepTheo());
        txtGhiChu.clear();
        cboKhachHang.setValue(null);
        paneForm.setExpanded(true);
    }

    @FXML
    private void themSanPham() {
        SanPham sp = cboSanPham.getValue();
        if (sp == null) {
            DialogUtil.showError("Lỗi nhập liệu", "Vui lòng chọn sản phẩm!");
            return;
        }

        // Validate số lượng
        if (!ValidationUtil.isPositiveInteger(txtSoLuong.getText())) {
            ValidationUtil.setErrorStyle(txtSoLuong);
            DialogUtil.showError("Lỗi số lượng",
                    "Số lượng không hợp lệ!\n" +
                            "• Phải là số nguyên dương\n" +
                            "• Ví dụ: 1, 10, 100");
            return;
        }
        ValidationUtil.clearErrorStyle(txtSoLuong);

        int sl = ValidationUtil.parseInt(txtSoLuong.getText());
        if (sl > sp.getSoLuongTon()) {
            DialogUtil.showWarning("Không đủ hàng",
                    "Số lượng yêu cầu: " + sl + "\n" +
                            "Tồn kho hiện tại: " + sp.getSoLuongTon() + "\n\n" +
                            "Vui lòng giảm số lượng hoặc chọn sản phẩm khác!");
            return;
        }

        // Check if custom price is provided
        double donGia = sp.getDonGia(); // Default price
        if (txtDonGiaBan.getText() != null && !txtDonGiaBan.getText().trim().isEmpty()) {
            // Validate and use custom price
            if (!ValidationUtil.isPositiveNumber(txtDonGiaBan.getText())) {
                ValidationUtil.setErrorStyle(txtDonGiaBan);
                DialogUtil.showError("Lỗi đơn giá",
                        "Đơn giá không hợp lệ!\n" +
                                "• Phải là số dương\n" +
                                "• Ví dụ: 50000 hoặc 50,000");
                return;
            }
            ValidationUtil.clearErrorStyle(txtDonGiaBan);
            donGia = ValidationUtil.parseDouble(txtDonGiaBan.getText());
        }

        ChiTietDonHang ct = new ChiTietDonHang(sp, sl);
        ct.setDonGia(donGia); // Set custom price
        ct.setThanhTien(donGia * sl); // Recalculate total
        dsChiTiet.add(ct);
        tongTien += ct.getThanhTien();
        lblTongTien.setText(String.format("Tổng: %,.0f VNĐ", tongTien));
        txtSoLuong.clear();
        txtDonGiaBan.clear();
        cboSanPham.setValue(null);
    }

    @FXML
    private void luuDon() {
        // Validate khách hàng
        if (cboKhachHang.getValue() == null) {
            DialogUtil.showError("Lỗi nhập liệu", "Vui lòng chọn khách hàng!");
            return;
        }

        // Validate có sản phẩm
        if (dsChiTiet.isEmpty()) {
            DialogUtil.showError("Lỗi nhập liệu",
                    "Đơn hàng chưa có sản phẩm!\n" +
                            "Vui lòng thêm ít nhất 1 sản phẩm vào đơn hàng.");
            return;
        }

        // Confirm tạo đơn
        if (!DialogUtil.confirm("Xác nhận tạo đơn hàng",
                "Khách hàng: " + cboKhachHang.getValue().getHoTen() + "\n" +
                        "Số sản phẩm: " + dsChiTiet.size() + "\n" +
                        "Tổng tiền: " + ValidationUtil.formatCurrency(tongTien) + "\n\n" +
                        "Xác nhận tạo đơn hàng?")) {
            return;
        }

        LoadingUtil.showLoading("Đang tạo đơn hàng...");

        try {
            DonHang dh = new DonHang();
            dh.setMaDH(txtMaDH.getText());
            dh.setMaKhachHang(cboKhachHang.getValue().getMaKhachHang());
            dh.setMaNguoiDung(DangNhapController.nguoiDungHienTai.getMaNguoiDung());
            dh.setGhiChu(txtGhiChu.getText() != null ? txtGhiChu.getText().trim() : "");
            dh.setChiTietList(dsChiTiet);
            dh.setTongTien(tongTien);
            dh.setThanhTien(tongTien);

            if (donHangDAO.them(dh)) {
                // Giảm tồn kho
                for (ChiTietDonHang ct : dsChiTiet) {
                    sanPhamDAO.giamSoLuong(ct.getMaSanPham(), ct.getSoLuong());
                }
                LogService.logThem("don_hang", dh.getMaDonHang(), 
                    "Tạo đơn hàng: " + dh.getMaDH() + " - " + cboKhachHang.getValue().getHoTen() + " - " + ValidationUtil.formatCurrency(tongTien));
                LoadingUtil.hideLoading();
                DialogUtil.showSuccess("Thành công",
                        "Đã tạo đơn hàng: " + dh.getMaDH() + "\n" +
                                "Tổng tiền: " + ValidationUtil.formatCurrency(tongTien));
                huy();
                taiDanhSach();
            } else {
                LoadingUtil.hideLoading();
                DialogUtil.showError("Lỗi tạo đơn",
                        "Không thể tạo đơn hàng!\n" +
                                "Vui lòng thử lại.");
            }
        } catch (Exception e) {
            LoadingUtil.hideLoading();
            DialogUtil.showError("Lỗi không xác định",
                    "Đã xảy ra lỗi khi tạo đơn hàng!",
                    "Chi tiết: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void xemChiTiet(DonHang dh) {
        DonHang full = donHangDAO.timTheoId(dh.getMaDonHang());
        StringBuilder sb = new StringBuilder("Chi tiết đơn hàng " + dh.getMaDH() + ":\n\n");
        for (ChiTietDonHang ct : full.getChiTietList()) {
            sb.append("- ").append(ct.getTenSanPham()).append(" x ").append(ct.getSoLuong())
                    .append(" = ").append(ct.getThanhTienFormat()).append(" VNĐ\n");
        }
        sb.append("\nTổng: ").append(full.getThanhTienFormat());
        new Alert(Alert.AlertType.INFORMATION, sb.toString()).showAndWait();
    }

    private void chuyenDangGiao(DonHang dh) {
        if (dh == null)
            return;

        // Confirm chuyển sang đang giao
        if (DialogUtil.confirm("Xác nhận giao hàng",
                "Chuyển đơn hàng sang trạng thái 'Đang giao'?\\n\\n" +
                        "• Mã đơn: " + dh.getMaDH() + "\\n" +
                        "• Khách hàng: " + dh.getTenKhachHang() + "\\n" +
                        "• Giá trị: " + ValidationUtil.formatCurrency(dh.getThanhTien()) + "\\n\\n" +
                        "Đơn hàng đang được vận chuyển đến khách")) {

            LoadingUtil.showLoading("Đang cập nhật trạng thái...");
            boolean success = donHangDAO.capNhatTrangThai(dh.getMaDonHang(), "dang_giao");
            LoadingUtil.hideLoading();

            if (success) {
                DialogUtil.showSuccess("Thành công", "Đơn hàng " + dh.getMaDH() + " đang được giao!");
                taiDanhSach();
            } else {
                DialogUtil.showError("Lỗi cập nhật",
                        "Không thể cập nhật trạng thái đơn hàng!");
            }
        }
    }

    private void hoanThanhDon(DonHang dh) {
        if (dh == null)
            return;

        // Confirm hoàn thành đơn
        if (DialogUtil.confirm("Xác nhận hoàn thành",
                "Đánh dấu đơn hàng đã giao?\\n\\n" +
                        "• Mã đơn: " + dh.getMaDH() + "\\n" +
                        "• Khách hàng: " + dh.getTenKhachHang() + "\\n" +
                        "• Giá trị: " + ValidationUtil.formatCurrency(dh.getThanhTien()) + "\\n\\n" +
                        "Đơn hàng sẽ chuyển sang trạng thái 'Đã giao'")) {

            LoadingUtil.showLoading("Đang cập nhật trạng thái...");
            boolean success = donHangDAO.capNhatTrangThai(dh.getMaDonHang(), "da_giao");
            LoadingUtil.hideLoading();

            if (success) {
                DialogUtil.showSuccess("Thành công", "Đơn hàng " + dh.getMaDH() + " đã được giao!");
                taiDanhSach();
            } else {
                DialogUtil.showError("Lỗi cập nhật",
                        "Không thể cập nhật trạng thái đơn hàng!");
            }
        }
    }

    private void huyDon(DonHang dh) {
        if (dh == null)
            return;

        // Confirm hủy đơn với cảnh báo
        if (DialogUtil.confirm("Xác nhận hủy đơn",
                "Bạn có chắc muốn hủy đơn hàng?\n\n" +
                        "• Mã đơn: " + dh.getMaDH() + "\n" +
                        "• Khách hàng: " + dh.getTenKhachHang() + "\n" +
                        "• Giá trị: " + ValidationUtil.formatCurrency(dh.getThanhTien()) + "\n\n" +
                        "Đơn hàng sẽ chuyển sang trạng thái 'Đã hủy'")) {

            LoadingUtil.showLoading("Đang hủy đơn hàng...");
            boolean success = donHangDAO.huyDonHang(dh.getMaDonHang());
            LoadingUtil.hideLoading();

            if (success) {
                DialogUtil.showSuccess("Đã hủy", "Đã hủy đơn hàng: " + dh.getMaDH());
                taiDanhSach();
            } else {
                DialogUtil.showError("Lỗi hủy đơn",
                        "Không thể hủy đơn hàng!\n" +
                                "Có thể đơn hàng đã được giao.");
            }
        }
    }

    private void locTheoTrangThai() {
        String val = cboTrangThai.getValue();
        if ("Tất cả".equals(val))
            taiDanhSach();
        else {
            String code = switch (val) {
                case "Chờ xử lý" -> "cho_xu_ly";
                case "Đang giao" -> "dang_giao";
                case "Đã giao" -> "da_giao";
                case "Đã hủy" -> "da_huy";
                default -> "";
            };
            dsDonHang.setAll(donHangDAO.locTheoTrangThai(code));
        }
    }

    @FXML
    private void lamMoi() {
        cboTrangThai.setValue("Tất cả");
        taiDanhSach();
    }

    @FXML
    private void huy() {
        paneForm.setExpanded(false);
    }

    private void thongBao(String msg) {
        DialogUtil.showSuccess("Thông báo", msg);
    }



    @FXML
    private void nhapExcel() {
        javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
        fc.setTitle("Chọn file Excel đơn hàng");
        fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls"));
        java.io.File file = fc.showOpenDialog(tableDonHang.getScene().getWindow());

        if (file != null) {
            try {
                // Validate file
                if (!file.exists()) {
                    thongBao("❌ File không tồn tại!");
                    return;
                }
                if (file.length() == 0) {
                    thongBao("❌ File rỗng!");
                    return;
                }
                if (file.length() > 10 * 1024 * 1024) {
                    thongBao("❌ File quá lớn (max 10MB)!");
                    return;
                }

                DialogUtil.showWarning("Tính năng đang phát triển",
                        "File hợp lệ: " + file.getName() + "\n\n" +
                                "Tính năng import đơn hàng từ Excel đang được phát triển.\n" +
                                "Vui lòng nhập đơn hàng thủ công.");
            } catch (Exception e) {
                DialogUtil.showError("Lỗi", "Lỗi đọc file!", e.getMessage());
            }
        }
    }

    @FXML
    private void xuatPDF() {
        DonHang selected = tableDonHang.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogUtil.showWarning("Chưa chọn đơn hàng", "Vui lòng chọn đơn hàng cần xuất PDF!");
            return;
        }

        javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
        fc.setTitle("Lưu hóa đơn PDF");
        fc.setInitialFileName("HoaDon_" + selected.getMaDH() + ".pdf");
        fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        java.io.File file = fc.showSaveDialog(tableDonHang.getScene().getWindow());

        if (file != null) {
            LoadingUtil.showLoading("Đang xuất hóa đơn PDF...");
            new Thread(() -> {
                try {
                    com.mekongfarm.service.PDFExportService pdfService = new com.mekongfarm.service.PDFExportService();
                    pdfService.xuatDonHangPDF(donHangDAO.timTheoId(selected.getMaDonHang()), file);
                    LoadingUtil.hideLoading();
                    javafx.application.Platform.runLater(() -> DialogUtil.showSuccess("Xuất file thành công",
                            "Đã xuất hóa đơn: " + selected.getMaDH() + "\n" +
                                    "File: " + file.getName()));
                } catch (Exception e) {
                    LoadingUtil.hideLoading();
                    javafx.application.Platform.runLater(
                            () -> DialogUtil.showError("Lỗi xuất PDF", "Không thể xuất hóa đơn!", e.getMessage()));
                }
            }).start();
        }
    }

    @FXML
    private void xuatExcel() {
        javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
        fc.setTitle("Xuất danh sách đơn hàng");
        fc.setInitialFileName("DonHang_" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx");
        fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        java.io.File file = fc.showSaveDialog(tableDonHang.getScene().getWindow());

        if (file != null) {
            LoadingUtil.showLoading("Đang xuất Excel...");
            new Thread(() -> {
                try {
                    new com.mekongfarm.service.ExcelExportService().xuatDonHangExcel(dsDonHang, file);
                    LoadingUtil.hideLoading();
                    LogService.logXuatFile("Excel", file.getName());
                    javafx.application.Platform.runLater(() -> DialogUtil.showSuccess("Xuất Excel thành công",
                            "Đã xuất " + dsDonHang.size() + " đơn hàng\nFile: " + file.getName()));
                } catch (Exception e) {
                    LoadingUtil.hideLoading();
                    javafx.application.Platform.runLater(
                            () -> DialogUtil.showError("Lỗi xuất Excel", "Không thể xuất file!", e.getMessage()));
                }
            }).start();
        }
    }

    @FXML
    private void inDonHang() {
        DonHang selected = tableDonHang.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogUtil.showWarning("Chưa chọn đơn hàng", "Vui lòng chọn đơn hàng cần in!");
            return;
        }

        LoadingUtil.showLoading("Đang chuẩn bị in...");
        try {
            DonHang full = donHangDAO.timTheoId(selected.getMaDonHang());
            com.mekongfarm.service.PrintService printService = new com.mekongfarm.service.PrintService();
            printService.inHoaDon(full);
            LoadingUtil.hideLoading();
            DialogUtil.showSuccess("Đã gửi lệnh in",
                    "Đã gửi hóa đơn " + selected.getMaDH() + " tới máy in.");
        } catch (Exception e) {
            LoadingUtil.hideLoading();
            DialogUtil.showError("Lỗi in", "Không thể in hóa đơn!", e.getMessage());
        }
    }
}
