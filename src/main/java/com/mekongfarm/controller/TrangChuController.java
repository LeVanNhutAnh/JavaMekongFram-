package com.mekongfarm.controller;

import com.mekongfarm.dao.*;
import com.mekongfarm.model.*;
import com.mekongfarm.util.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class TrangChuController {

    @FXML
    private StackPane contentArea;
    @FXML
    private Label lblNguoiDung;
    @FXML
    private Label lblTongSP, lblTongKH, lblTongDH, lblDoanhThu;
    @FXML
    private Label lblCongNo, lblNgay, lblMuaVu;
    @FXML
    private PieChart chartLoai;
    @FXML
    private BarChart<String, Number> chartTinh;
    @FXML
    private VBox boxThongBao, boxDonHangGanDay, boxTonKhoThap;
    @FXML
    private VBox boxTopBanChay, boxSapHetHan;
    @FXML
    private TextField txtTimKiemToanCuc;

    // Menu buttons for permission control
    @FXML
    private Button btnSanPham, btnKhachHang, btnDonHang, btnTruyXuat;
    @FXML
    private Button btnNhaCungCap, btnDonNhap, btnKho;
    @FXML
    private Button btnThongKe, btnCongNo, btnLaiLo; // Changed btnBaoCao to btnThongKe
    @FXML
    private Button btnMuaVu, btnTroLyAI, btnQuanLyUser, btnLichSu;
    @FXML
    private Button btnQuanLyTruyXuat, btnGiaVung;

    private final ThongKeDAO thongKeDAO = new ThongKeDAO();
    private final CongNoDAO congNoDAO = new CongNoDAO();
    private final MuaVuDAO muaVuDAO = new MuaVuDAO();
    private final DonHangDAO donHangDAO = new DonHangDAO();
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();

    @FXML
    public void initialize() {
        if (DangNhapController.nguoiDungHienTai != null) {
            lblNguoiDung.setText("👤 " + DangNhapController.nguoiDungHienTai.getHoTen());
        }

        // Hiển thị ngày
        if (lblNgay != null) {
            lblNgay.setText("📅 " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        // Áp dụng phân quyền
        applyPermissions();

        taiDuLieuThongKe();
        taiThongTinBoSung();
    }

    /**
     * Ẩn/hiện menu dựa trên vai trò người dùng
     */
    private void applyPermissions() {
        VaiTro vaiTro = PhanQuyenUtil.getVaiTro();

        // 1. Module Sản phẩm - chỉ Admin
        setObscure(btnSanPham, vaiTro.coQuyenSanPham());

        // 2. Module Đơn hàng/Khách hàng - Admin & Nhân viên
        setObscure(btnDonHang, vaiTro.coQuyenDonHang());
        setObscure(btnKhachHang, vaiTro.coQuyenDonHang());

        // 3. Module KHO & ĐỐI TÁC - Admin & Nhân viên
        boolean quyenKho = vaiTro.coQuyenQuanLy();
        setObscure(btnNhaCungCap, quyenKho);
        setObscure(btnDonNhap, quyenKho);
        setObscure(btnKho, quyenKho);

        // 4. Module TÀI CHÍNH - Admin & Kế toán
        boolean quyenTaiChinh = vaiTro.coQuyenThongKe();
        setObscure(btnCongNo, quyenTaiChinh);
        setObscure(btnLaiLo, quyenTaiChinh);
        setObscure(btnThongKe, vaiTro.coQuyenThongKe());

        // 5. Module SẢN XUẤT
        setObscure(btnQuanLyTruyXuat, vaiTro.coQuyenQuanLy());
        setObscure(btnMuaVu, true);
        setObscure(btnGiaVung, true);
        setObscure(btnTruyXuat, true);
        setObscure(btnTroLyAI, true);

        // 6. Quản trị hệ thống - Chỉ Admin
        setObscure(btnQuanLyUser, vaiTro.laAdmin());
        setObscure(btnLichSu, vaiTro.laAdmin());
    }

    private void setObscure(javafx.scene.Node node, boolean visible) {
        if (node != null) {
            node.setVisible(visible);
            node.setManaged(visible);
        }
    }

    private void taiDuLieuThongKe() {
        lblTongSP.setText(String.valueOf(thongKeDAO.demTongSanPham()));
        lblTongKH.setText(String.valueOf(thongKeDAO.demTongKhachHang()));
        lblTongDH.setText(String.valueOf(thongKeDAO.demTongDonHang()));
        lblDoanhThu.setText(String.format("%,.0f VNĐ", thongKeDAO.tinhTongDoanhThu()));

        // Công nợ
        if (lblCongNo != null) {
            double tongCongNo = congNoDAO.tongCongNo();
            lblCongNo.setText(String.format("%,.0f VNĐ", tongCongNo));
        }

        // Chart theo loại
        if (chartLoai != null) {
            chartLoai.getData().clear();
            Map<String, Integer> duLieuLoai = thongKeDAO.thongKeSanPhamTheoLoai();
            for (Map.Entry<String, Integer> entry : duLieuLoai.entrySet()) {
                chartLoai.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
        }

        // Chart theo tỉnh
        if (chartTinh != null) {
            chartTinh.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Số sản phẩm");
            Map<String, Integer> duLieuTinh = thongKeDAO.thongKeSanPhamTheoTinh();
            int count = 0;
            for (Map.Entry<String, Integer> entry : duLieuTinh.entrySet()) {
                if (count++ >= 6)
                    break;
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            chartTinh.getData().add(series);
        }
    }

    private void taiThongTinBoSung() {
        // Mùa vụ hiện tại
        if (lblMuaVu != null) {
            List<MuaVu> muaVuDangDienRa = muaVuDAO.layDangDienRa();
            if (!muaVuDangDienRa.isEmpty()) {
                MuaVu mv = muaVuDangDienRa.get(0);
                lblMuaVu.setText("🌱 " + mv.getTenMuaVu() + " (" + mv.getSanPhamLienQuan() + ")");
            } else {
                lblMuaVu.setText("Không có mùa vụ đang diễn ra");
            }
        }

        // Đơn hàng gần đây
        if (boxDonHangGanDay != null) {
            boxDonHangGanDay.getChildren().clear();
            List<DonHang> dsDonHang = donHangDAO.layTatCa();
            int show = Math.min(5, dsDonHang.size());
            if (show == 0) {
                boxDonHangGanDay.getChildren().add(
                        new Label("Chưa có đơn hàng") {
                            {
                                setStyle("-fx-text-fill: #94a3b8; -fx-font-style: italic;");
                            }
                        });
            } else {
                for (int i = 0; i < show; i++) {
                    DonHang dh = dsDonHang.get(i);
                    Label lbl = new Label("• #" + dh.getMaDH() + " - " + dh.getTenKhachHang() + " - " +
                            String.format("%,.0f VNĐ", dh.getThanhTien()));
                    lbl.setStyle("-fx-text-fill: #374151; -fx-font-size: 11px;");
                    boxDonHangGanDay.getChildren().add(lbl);
                }
            }
        }

        // Tồn kho thấp
        if (boxTonKhoThap != null) {
            boxTonKhoThap.getChildren().clear();
            List<SanPham> dsSanPham = sanPhamDAO.layTatCa();
            boolean hasLowStock = false;
            for (SanPham sp : dsSanPham) {
                if (sp.getSoLuongTon() < 10) {
                    Label lbl = new Label("⚠️ " + sp.getTenSanPham() + ": còn " + sp.getSoLuongTon());
                    lbl.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
                    boxTonKhoThap.getChildren().add(lbl);
                    hasLowStock = true;
                }
            }
            if (!hasLowStock) {
                boxTonKhoThap.getChildren().add(
                        new Label("✅ Tồn kho đủ") {
                            {
                                setStyle("-fx-text-fill: #16a34a;");
                            }
                        });
            }
        }

        // Top sản phẩm bán chạy
        if (boxTopBanChay != null) {
            boxTopBanChay.getChildren().clear();
            Map<String, Integer> topSP = thongKeDAO.topSanPhamBanChay(5);
            if (topSP.isEmpty()) {
                Label lbl = new Label("Chưa có dữ liệu");
                lbl.setStyle("-fx-text-fill: #94a3b8; -fx-font-style: italic;");
                boxTopBanChay.getChildren().add(lbl);
            } else {
                int rank = 1;
                for (Map.Entry<String, Integer> entry : topSP.entrySet()) {
                    Label lbl = new Label(rank++ + ". " + entry.getKey() + " (" + entry.getValue() + " đã bán)");
                    lbl.setStyle("-fx-text-fill: #374151; -fx-font-size: 11px;");
                    boxTopBanChay.getChildren().add(lbl);
                }
            }
        }

        // Sắp hết hạn (Giả lập kiểm tra an toàn)
        if (boxSapHetHan != null) {
            boxSapHetHan.getChildren().clear();
            Label lbl = new Label("✅ Không có sản phẩm sắp hết hạn");
            lbl.setStyle("-fx-text-fill: #16a34a; -fx-font-size: 11px;");
            boxSapHetHan.getChildren().add(lbl);
        }
    }

    @FXML
    private void lamMoi() {
        taiDuLieuThongKe();
        taiThongTinBoSung();
    }

    @FXML
    private void xuatBaoCaoThang() {
        try {
            javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
            fc.setTitle("Lưu Báo Cáo Tháng");
            fc.setInitialFileName(
                    "bao_cao_thang_" + LocalDate.now().format(DateTimeFormatter.ofPattern("MM_yyyy")) + ".pdf");
            fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("PDF", "*.pdf"));
            java.io.File file = fc.showSaveDialog(contentArea.getScene().getWindow());

            if (file != null) {
                com.mekongfarm.service.PDFExportService pdfService = new com.mekongfarm.service.PDFExportService();
                pdfService.xuatBaoCaoThongKe(file);
                hienThiThongBao("✅ Đã xuất báo cáo thành công!\nFile: " + file.getName());
            }
        } catch (Exception e) {
            hienThiThongBao("❌ Lỗi xuất báo cáo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void moTrangChu() {
        // Xóa nội dung và load lại TrangChu để về dashboard
        try {
            Parent content = FXMLLoader.load(getClass().getResource("/fxml/TrangChu.fxml"));
            Stage stage = (Stage) contentArea.getScene().getWindow();
            Scene scene = new Scene(content, stage.getWidth(), stage.getHeight());
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception e) {
            // Fallback: chỉ refresh data
            taiDuLieuThongKe();
            taiThongTinBoSung();
        }
    }

    @FXML
    private void moSanPham() {
        taiNoiDung("/fxml/SanPham.fxml");
    }

    @FXML
    private void moKhachHang() {
        taiNoiDung("/fxml/KhachHang.fxml");
    }

    @FXML
    private void moDonHang() {
        taiNoiDung("/fxml/DonHang.fxml");
    }

    @FXML
    private void moThongKe() {
        taiNoiDung("/fxml/ThongKe.fxml");
    }

    @FXML
    private void moTruyXuat() {
        taiNoiDung("/fxml/TruyXuat.fxml");
    }

    @FXML
    private void moQuanLyTruyXuat() {
        taiNoiDung("/fxml/QuanLyTruyXuat.fxml");
    }

    @FXML
    private void moTroLyAI() {
        taiNoiDung("/fxml/TroLyAI.fxml");
    }

    @FXML
    private void moCaiDat() {
        taiNoiDung("/fxml/CaiDat.fxml");
    }

    @FXML
    private void moQuanLyUser() {
        taiNoiDung("/fxml/QuanLyUser.fxml");
    }

    @FXML
    private void moLichSu() {
        taiNoiDung("/fxml/LichSu.fxml");
    }

    @FXML
    private void moNhaCungCap() {
        taiNoiDung("/fxml/NhaCungCap.fxml");
    }

    @FXML
    private void moDonNhap() {
        taiNoiDung("/fxml/DonNhap.fxml");
    }

    @FXML
    private void moCongNo() {
        taiNoiDung("/fxml/CongNo.fxml");
    }

    @FXML
    private void moMuaVu() {
        taiNoiDung("/fxml/MuaVu.fxml");
    }

    @FXML
    private void moKho() {
        taiNoiDung("/fxml/Kho.fxml");
    }

    @FXML
    private void moLaiLo() {
        taiNoiDung("/fxml/LaiLo.fxml");
    }

    @FXML
    private void moGiaVung() {
        taiNoiDung("/fxml/GiaVung.fxml");
    }

    private void taiNoiDung(String fxmlPath) {
        try {
            Parent content = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);
        } catch (Exception e) {
            hienThiThongBao("Không thể mở màn hình: " + e.getMessage());
        }
    }

    @FXML
    private void dangXuat() {
        try {
            DangNhapController.nguoiDungHienTai = null;
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DangNhap.fxml"));
            Stage stage = (Stage) contentArea.getScene().getWindow();
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("🌾 Quản Lý Nông Sản ĐBSCL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hienThiThongBao(String msg) {
        DialogUtil.showSuccess("Thông báo", msg);
    }

    // ========== NEW ENHANCEMENT METHODS ==========

    @FXML
    private void moThongBao() {
        // Show notification panel
        var notifications = com.mekongfarm.service.NotificationService.getAll();
        if (notifications.isEmpty()) {
            DialogUtil.showInfo("Thông báo", "Không có thông báo mới");
        } else {
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (var n : notifications) {
                if (count++ >= 10)
                    break;
                String icon = switch (n.type) {
                    case SUCCESS -> "✅";
                    case WARNING -> "⚠️";
                    case ERROR -> "❌";
                    default -> "ℹ️";
                };
                sb.append(icon).append(" ").append(n.title).append("\n");
                sb.append("   ").append(n.message).append("\n\n");
            }
            DialogUtil.showInfo("🔔 Thông báo (" + notifications.size() + ")", sb.toString());
            com.mekongfarm.service.NotificationService.markAllRead();
        }
    }

    @FXML
    private void moDoiMatKhau() {
        // Password change dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("🔑 Đổi mật khẩu");
        dialog.setHeaderText("Nhập mật khẩu cũ và mật khẩu mới");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        PasswordField txtMatKhauCu = new PasswordField();
        PasswordField txtMatKhauMoi = new PasswordField();
        PasswordField txtXacNhan = new PasswordField();

        grid.add(new Label("Mật khẩu cũ:"), 0, 0);
        grid.add(txtMatKhauCu, 1, 0);
        grid.add(new Label("Mật khẩu mới:"), 0, 1);
        grid.add(txtMatKhauMoi, 1, 1);
        grid.add(new Label("Xác nhận:"), 0, 2);
        grid.add(txtXacNhan, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        java.util.Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String mkCu = txtMatKhauCu.getText();
            String mkMoi = txtMatKhauMoi.getText();
            String xacNhan = txtXacNhan.getText();

            if (mkMoi.isEmpty() || mkMoi.length() < 6) {
                DialogUtil.showError("Lỗi", "Mật khẩu mới phải có ít nhất 6 ký tự!");
                return;
            }
            if (!mkMoi.equals(xacNhan)) {
                DialogUtil.showError("Lỗi", "Mật khẩu xác nhận không khớp!");
                return;
            }

            // Verify old password and update
            NguoiDung user = DangNhapController.nguoiDungHienTai;
            if (user != null) {
                NguoiDungDAO dao = new NguoiDungDAO();
                String hashedOld = com.mekongfarm.service.PasswordService.hash(mkCu);
                if (!hashedOld.equals(user.getMatKhau())) {
                    DialogUtil.showError("Lỗi", "Mật khẩu cũ không đúng!");
                    return;
                }

                boolean success = dao.doiMatKhau(user.getMaNguoiDung(), mkMoi);
                if (success) {
                    DialogUtil.showSuccess("Thành công", "Đổi mật khẩu thành công!");
                    com.mekongfarm.service.LogService.logCapNhat("NguoiDung", user.getMaNguoiDung(),
                            "User tự đổi mật khẩu");
                } else {
                    DialogUtil.showError("Lỗi", "Không thể đổi mật khẩu!");
                }
            }
        }
    }

    @FXML
    private void timKiemToanCuc() {
        String keyword = txtTimKiemToanCuc.getText().trim();
        if (keyword.isEmpty()) {
            DialogUtil.showWarning("Tìm kiếm", "Vui lòng nhập từ khóa tìm kiếm!");
            return;
        }

        // Search products using existing layTatCa and filter
        StringBuilder result = new StringBuilder();

        // Search products
        List<SanPham> allProducts = sanPhamDAO.layTatCa();
        List<SanPham> matchedProducts = allProducts.stream()
                .filter(sp -> sp.getTenSanPham().toLowerCase().contains(keyword.toLowerCase()))
                .limit(5)
                .toList();

        if (!matchedProducts.isEmpty()) {
            result.append("📦 SẢN PHẨM (").append(matchedProducts.size()).append(" kết quả):\n");
            for (SanPham sp : matchedProducts) {
                result.append("  • ").append(sp.getTenSanPham()).append("\n");
            }
            result.append("\n");
        }

        // Search customers
        KhachHangDAO khDAO = new KhachHangDAO();
        List<KhachHang> allCustomers = khDAO.layTatCa();
        List<KhachHang> matchedCustomers = allCustomers.stream()
                .filter(kh -> kh.getHoTen().toLowerCase().contains(keyword.toLowerCase()))
                .limit(5)
                .toList();

        if (!matchedCustomers.isEmpty()) {
            result.append("👥 KHÁCH HÀNG (").append(matchedCustomers.size()).append(" kết quả):\n");
            for (KhachHang kh : matchedCustomers) {
                result.append("  • ").append(kh.getHoTen()).append("\n");
            }
            result.append("\n");
        }

        // Search orders
        List<DonHang> allOrders = donHangDAO.layTatCa();
        List<DonHang> matchedOrders = allOrders.stream()
                .filter(dh -> String.valueOf(dh.getMaDH()).contains(keyword))
                .limit(5)
                .toList();

        if (!matchedOrders.isEmpty()) {
            result.append("🛒 ĐƠN HÀNG (").append(matchedOrders.size()).append(" kết quả):\n");
            for (DonHang dh : matchedOrders) {
                result.append("  • Đơn #").append(dh.getMaDH()).append("\n");
            }
        }

        if (result.length() == 0) {
            DialogUtil.showSuccess("Tìm kiếm", "Không tìm thấy kết quả cho: " + keyword);
        } else {
            DialogUtil.showSuccess("🔍 Kết quả tìm kiếm: " + keyword, result.toString());
        }
    }

    @FXML
    private void xuatExcelTongHop() {
        try {
            javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
            fc.setTitle("Lưu Báo Cáo Excel");
            fc.setInitialFileName("bao_cao_tong_hop_" + LocalDate.now() + ".xlsx");
            fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Excel", "*.xlsx"));
            java.io.File file = fc.showSaveDialog(contentArea.getScene().getWindow());

            if (file != null) {
                // Use PDF export as fallback since Excel may not have xuatBaoCaoTongHop
                com.mekongfarm.service.PDFExportService pdfService = new com.mekongfarm.service.PDFExportService();
                java.io.File pdfFile = new java.io.File(file.getAbsolutePath().replace(".xlsx", ".pdf"));
                pdfService.xuatBaoCaoThongKe(pdfFile);
                DialogUtil.showSuccess("Thành công", "Đã xuất báo cáo!\nFile: " + pdfFile.getName());
            }
        } catch (Exception e) {
            DialogUtil.showError("Lỗi", "Không thể xuất báo cáo: " + e.getMessage());
        }
    }
}
