package com.mekongfarm.controller;

import com.mekongfarm.dao.*;
import com.mekongfarm.service.GeminiService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.Map;

/**
 * Controller cho Trợ Lý AI
 */
public class TroLyAIController {

    @FXML
    private TabPane tabPane;
    @FXML
    private VBox chatBox;
    @FXML
    private ScrollPane scrollChat;
    @FXML
    private TextField txtMessage;
    @FXML
    private ImageView imgPreview;
    @FXML
    private Label lblFileName;
    @FXML
    private TextArea txtKetQuaAnh;
    @FXML
    private TextArea txtDuLieu;
    @FXML
    private TextArea txtKetQuaPhanTich;

    private final GeminiService geminiService = new GeminiService();
    private final ThongKeDAO thongKeDAO = new ThongKeDAO();
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private File selectedImageFile;

    @FXML
    public void initialize() {
        // Thêm tin nhắn chào mừng
        themTinNhanBot(
                "Xin chào! 👋 Tôi là trợ lý AI chuyên về nông nghiệp ĐBSCL.\n\nBạn có thể hỏi tôi về:\n• Kỹ thuật trồng trọt\n• Chăm sóc cây ăn trái\n• Nuôi thủy sản\n• Giá cả thị trường\n• Và nhiều hơn nữa...");
    }

    // =============== CHATBOT ===============

    @FXML
    private void guiTinNhan() {
        String message = txtMessage.getText().trim();
        if (message.isEmpty())
            return;

        themTinNhanUser(message);
        txtMessage.clear();

        // Hiển thị loading
        Label loading = new Label("⏳ Đang suy nghĩ...");
        loading.getStyleClass().add("chat-loading");
        chatBox.getChildren().add(loading);
        scrollToBottom();

        // Gọi AI trong background thread
        new Thread(() -> {
            String response = geminiService.chat(message);
            Platform.runLater(() -> {
                chatBox.getChildren().remove(loading);
                themTinNhanBot(response);
            });
        }).start();
    }

    @FXML
    private void hoiVeLua() {
        txtMessage.setText("Cho tôi biết về kỹ thuật trồng lúa ST25 ở Sóc Trăng?");
        guiTinNhan();
    }

    @FXML
    private void hoiVeTraiCay() {
        txtMessage.setText("Làm sao để trồng xoài cát Hòa Lộc đạt năng suất cao?");
        guiTinNhan();
    }

    @FXML
    private void hoiVeThuySan() {
        txtMessage.setText("Kỹ thuật nuôi tôm sú ở Cà Mau như thế nào?");
        guiTinNhan();
    }

    @FXML
    private void hoiVeMuaVu() {
        txtMessage.setText("Mùa vụ trồng lúa nào phù hợp nhất ở vùng ĐBSCL? Và nên trồng giống gì?");
        guiTinNhan();
    }

    @FXML
    private void duBaoXuHuong() {
        StringBuilder data = new StringBuilder("📈 DỮ LIỆU XU HƯỚNG:\n\n");
        int nam = java.time.Year.now().getValue();
        java.util.Map<String, Double> doanhThu = thongKeDAO.thongKeDoanhThuTheoThang(nam);
        for (java.util.Map.Entry<String, Double> e : doanhThu.entrySet()) {
            data.append(e.getKey()).append(": ").append(String.format("%,.0f VNĐ", e.getValue())).append("\n");
        }
        data.append("\nHãy dự báo xu hướng doanh thu cho các tháng tới.");
        txtDuLieu.setText(data.toString());
    }

    private void themTinNhanUser(String message) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        Label label = new Label(message);
        label.getStyleClass().add("chat-user");
        label.setWrapText(true);
        label.setMaxWidth(400);
        hbox.getChildren().add(label);
        chatBox.getChildren().add(hbox);
        scrollToBottom();
    }

    private void themTinNhanBot(String message) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label("🤖 " + message);
        label.getStyleClass().add("chat-bot");
        label.setWrapText(true);
        label.setMaxWidth(450);
        hbox.getChildren().add(label);
        chatBox.getChildren().add(hbox);
        scrollToBottom();
    }

    private void scrollToBottom() {
        Platform.runLater(() -> scrollChat.setVvalue(1.0));
    }

    // =============== NHẬN DIỆN ẢNH ===============

    @FXML
    private void chonAnh() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh nông sản");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.webp"));

        File file = fileChooser.showOpenDialog(imgPreview.getScene().getWindow());
        if (file != null) {
            selectedImageFile = file;
            lblFileName.setText(file.getName());
            imgPreview.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    private void phanTichAnh() {
        if (selectedImageFile == null) {
            txtKetQuaAnh.setText("⚠️ Vui lòng chọn ảnh trước!");
            return;
        }

        txtKetQuaAnh.setText("⏳ Đang phân tích ảnh với AI...");

        new Thread(() -> {
            String result = geminiService.analyzeImage(selectedImageFile);
            Platform.runLater(() -> txtKetQuaAnh.setText(result));
        }).start();
    }

    // =============== PHÂN TÍCH DỮ LIỆU ===============

    @FXML
    private void phanTichDoanhThu() {
        StringBuilder data = new StringBuilder("📊 DỮ LIỆU DOANH THU:\n\n");
        int nam = java.time.Year.now().getValue();
        Map<String, Double> doanhThu = thongKeDAO.thongKeDoanhThuTheoThang(nam);
        for (Map.Entry<String, Double> e : doanhThu.entrySet()) {
            data.append(e.getKey()).append(": ").append(String.format("%,.0f VNĐ", e.getValue())).append("\n");
        }
        data.append("\nTổng doanh thu: ").append(String.format("%,.0f VNĐ", thongKeDAO.tinhTongDoanhThu()));
        txtDuLieu.setText(data.toString());
    }

    @FXML
    private void phanTichSanPham() {
        StringBuilder data = new StringBuilder("📦 DỮ LIỆU SẢN PHẨM:\n\n");
        data.append("Tổng sản phẩm: ").append(thongKeDAO.demTongSanPham()).append("\n\n");

        data.append("Theo loại:\n");
        Map<String, Integer> theoLoai = thongKeDAO.thongKeSanPhamTheoLoai();
        for (Map.Entry<String, Integer> e : theoLoai.entrySet()) {
            data.append("- ").append(e.getKey()).append(": ").append(e.getValue()).append(" sản phẩm\n");
        }

        data.append("\nTheo tỉnh:\n");
        Map<String, Integer> theoTinh = thongKeDAO.thongKeSanPhamTheoTinh();
        for (Map.Entry<String, Integer> e : theoTinh.entrySet()) {
            data.append("- ").append(e.getKey()).append(": ").append(e.getValue()).append(" sản phẩm\n");
        }
        txtDuLieu.setText(data.toString());
    }

    @FXML
    private void phanTichKhachHang() {
        StringBuilder data = new StringBuilder("👥 DỮ LIỆU KHÁCH HÀNG:\n\n");
        data.append("Tổng khách hàng: ").append(thongKeDAO.demTongKhachHang()).append("\n");
        data.append("Tổng đơn hàng: ").append(thongKeDAO.demTongDonHang()).append("\n");
        data.append("Tổng doanh thu: ").append(String.format("%,.0f VNĐ", thongKeDAO.tinhTongDoanhThu())).append("\n");
        txtDuLieu.setText(data.toString());
    }

    @FXML
    private void phanTichDuLieu() {
        String data = txtDuLieu.getText().trim();
        if (data.isEmpty()) {
            txtKetQuaPhanTich.setText("⚠️ Vui lòng nhập dữ liệu hoặc chọn loại phân tích!");
            return;
        }

        txtKetQuaPhanTich.setText("⏳ Đang phân tích dữ liệu với AI...");

        new Thread(() -> {
            String result = geminiService.analyzeData(data);
            Platform.runLater(() -> txtKetQuaPhanTich.setText(result));
        }).start();
    }
}
