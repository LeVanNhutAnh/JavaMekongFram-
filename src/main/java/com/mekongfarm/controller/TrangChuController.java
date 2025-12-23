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

        taiDuLieuThongKe();
        taiThongTinBoSung();
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
    }

    @FXML
    private void lamMoi() {
        taiDuLieuThongKe();
        taiThongTinBoSung();
    }

    @FXML
    private void timKiemToanCuc() {
        DialogUtil.showInfo("Tìm kiếm", "Chức năng tìm kiếm toàn cục đang được phát triển!");
    }

    @FXML
    private void xuatBaoCaoThang() {
        DialogUtil.showInfo("Báo cáo", "Chức năng xuất báo cáo tháng đang được phát triển!");
    }

    @FXML
    private void xuatExcelTongHop() {
        DialogUtil.showInfo("Xuất Excel", "Chức năng xuất Excel tổng hợp đang được phát triển!");
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

    @FXML    private void moQuanLyTruyXuat() {
        taiNoiDung("/fxml/QuanLyTruyXuat.fxml");
    }

    @FXML    private void moTroLyAI() {
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
            // Ghi log chi tiết để dễ chẩn đoán
            AppLogger.error("Lỗi mở màn hình: " + fxmlPath, e);
            System.err.println("Lỗi tải FXML: " + fxmlPath);
            System.err.println("Message: " + e);
            e.printStackTrace(); // In stack trace đầy đủ
            
            // In caused by nếu có
            Throwable cause = e.getCause();
            while (cause != null) {
                System.err.println("Caused by: " + cause.getMessage());
                cause.printStackTrace();
                cause = cause.getCause();
            }
            
            // Hiển thị lỗi thân thiện kèm thông tin ngoại lệ
            DialogUtil.showError("Lỗi", "Không thể mở màn hình: " + fxmlPath, "Chi tiết: " + e);
        }
    }

    @FXML
    private void moThongBao() {
        DialogUtil.showInfo("Thông báo", "Chức năng thông báo đang được phát triển!");
    }

    @FXML
    private void moDoiMatKhau() {
        DialogUtil.showInfo("Đổi mật khẩu", "Chức năng đổi mật khẩu đang được phát triển!");
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
}
