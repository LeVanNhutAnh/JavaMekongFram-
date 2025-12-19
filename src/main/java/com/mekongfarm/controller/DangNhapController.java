package com.mekongfarm.controller;

import com.mekongfarm.dao.NguoiDungDAO;
import com.mekongfarm.model.NguoiDung;
import com.mekongfarm.service.LogService;
import com.mekongfarm.util.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class DangNhapController {

    @FXML
    private TextField txtTenDangNhap;
    @FXML
    private PasswordField txtMatKhau;
    @FXML
    private CheckBox chkNhoMatKhau;
    @FXML
    private Button btnDangNhap;
    @FXML
    private Label lblThongBao;

    private final NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();

    public static NguoiDung nguoiDungHienTai;

    @FXML
    public void initialize() {
        txtTenDangNhap.setText("admin");
        txtMatKhau.setText("admin123");
    }

    @FXML
    private void xuLyDangNhap() {
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = txtMatKhau.getText().trim();

        // Validate input
        if (!ValidationUtil.isNotEmpty(tenDangNhap)) {
            ValidationUtil.setErrorStyle(txtTenDangNhap);
            hienThiLoi("⚠️ Vui lòng nhập tên đăng nhập!");
            return;
        }
        ValidationUtil.clearErrorStyle(txtTenDangNhap);
        
        if (!ValidationUtil.isNotEmpty(matKhau)) {
            ValidationUtil.setErrorStyle(txtMatKhau);
            hienThiLoi("⚠️ Vui lòng nhập mật khẩu!");
            return;
        }
        ValidationUtil.clearErrorStyle(txtMatKhau);
        
        // Show loading
        LoadingUtil.showLoading("Đang đăng nhập...");
        btnDangNhap.setDisable(true);
        
        // Authenticate in background thread
        new Thread(() -> {
            try {
                NguoiDung nd = nguoiDungDAO.dangNhap(tenDangNhap, matKhau);
                
                javafx.application.Platform.runLater(() -> {
                    LoadingUtil.hideLoading();
                    btnDangNhap.setDisable(false);
                    
                    if (nd != null) {
                        nguoiDungHienTai = nd;
                        LogService.setCurrentUser(nd.getMaNguoiDung());
                        LogService.logDangNhap(nd.getMaNguoiDung(), nd.getHoTen());
                        moManHinhChinh();
                    } else {
                        ValidationUtil.setErrorStyle(txtTenDangNhap);
                        ValidationUtil.setErrorStyle(txtMatKhau);
                        hienThiLoi("❌ Tên đăng nhập hoặc mật khẩu không đúng!");
                        txtMatKhau.clear();
                        txtMatKhau.requestFocus();
                    }
                });
            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    LoadingUtil.hideLoading();
                    btnDangNhap.setDisable(false);
                    hienThiLoi("❌ Lỗi đăng nhập: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    private void moManHinhChinh() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/TrangChu.fxml"));
            Stage stage = (Stage) btnDangNhap.getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("🌾 Quản Lý Nông Sản ĐBSC L - " + nguoiDungHienTai.getHoTen());
            stage.setMaximized(true);
            stage.centerOnScreen();
            
            System.out.println("✅ Đăng nhập thành công: " + nguoiDungHienTai.getHoTen() + " (" + nguoiDungHienTai.getVaiTro() + ")");
        } catch (Exception e) {
            DialogUtil.showError("Lỗi mở ứng dụng",
                "Không thể mở màn hình chính!",
                "Chi tiết: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void hienThiLoi(String thongBao) {
        lblThongBao.setText(thongBao);
        lblThongBao.setVisible(true);
    }
}
