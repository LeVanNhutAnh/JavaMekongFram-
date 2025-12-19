package com.mekongfarm.controller;

import com.mekongfarm.dao.*;
import com.mekongfarm.model.*;
import com.mekongfarm.util.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import java.time.format.DateTimeFormatter;

public class TruyXuatController {
    @FXML
    private TextField txtMaTraCuu;
    @FXML
    private VBox paneKetQua;
    @FXML
    private Label lblTenSP, lblSoLo, lblNgayThuHoach, lblDiaChi, lblNongDan;
    @FXML
    private Label lblChungNhan, lblNgaySX, lblHanSD, lblKhongTimThay;
    @FXML
    private ImageView imgQR;

    private final TruyXuatDAO dao = new TruyXuatDAO();
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void traCuu() {
        String ma = txtMaTraCuu.getText().trim();
        if (ma.isEmpty()) {
            DialogUtil.showWarning("Thông báo", "Vui lòng nhập mã sản phẩm hoặc số lô để tra cứu!");
            return;
        }

        System.out.println("⚡ Bắt đầu tra cứu: " + ma);
        
        // Thử tìm theo số lô trước
        TruyXuatNguonGoc tx = dao.timTheoSoLo(ma);
        System.out.println("  - Tìm theo số lô: " + (tx != null ? "✓ Có" : "✗ Không"));
        
        // Nếu không thấy, tìm theo mã sản phẩm
        if (tx == null) {
            SanPham sp = sanPhamDAO.timTheoMaSP(ma);
            System.out.println("  - Tìm sản phẩm: " + (sp != null ? "✓ " + sp.getTenSanPham() : "✗ Không"));
            
            if (sp != null) {
                tx = dao.timTheoSanPham(sp.getMaSanPham());
                System.out.println("  - Tìm truy xuất cho SP ID " + sp.getMaSanPham() + ": " + 
                    (tx != null ? "✓ Có" : "✗ Không"));
            }
        }

        if (tx != null) {
            System.out.println("✓ Tìm thấy! Hiển thị kết quả...");
            hienThiKetQua(tx);
        } else {
            System.out.println("✗ KHÔNG tìm thấy thông tin truy xuất!");
            paneKetQua.setVisible(false);
            lblKhongTimThay.setVisible(true);
            lblKhongTimThay.setText("❌ Không tìm thấy thông tin truy xuất cho mã: " + ma);
        }
    }

    private void hienThiKetQua(TruyXuatNguonGoc tx) {
        lblKhongTimThay.setVisible(false);
        paneKetQua.setVisible(true);

        lblTenSP.setText("🌾 " + tx.getTenSanPham());
        lblSoLo.setText(tx.getSoLo());
        lblNgayThuHoach.setText(tx.getNgayThuHoach() != null ? tx.getNgayThuHoach().format(fmt) : "N/A");
        lblDiaChi.setText(tx.getDiaChiSanXuat());
        lblNongDan.setText(tx.getTenNongDan());
        lblChungNhan.setText(tx.getChungNhan());
        lblNgaySX.setText(tx.getNgaySanXuat() != null ? tx.getNgaySanXuat().format(fmt) : "N/A");
        lblHanSD.setText(tx.getHanSuDung() != null ? tx.getHanSuDung().format(fmt) : "N/A");

        taoQRCode(tx);
    }

    private void taoQRCode(TruyXuatNguonGoc tx) {
        try {
            String data = "Sản phẩm: " + tx.getTenSanPham() + "\n" +
                    "Số lô: " + tx.getSoLo() + "\n" +
                    "Xuất xứ: " + tx.getDiaChiSanXuat() + "\n" +
                    "Chứng nhận: " + tx.getChungNhan();

            BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 150, 150);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
            imgQR.setImage(SwingFXUtils.toFXImage(image, null));
        } catch (Exception e) {
            System.err.println("Lỗi tạo QR: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        // Ẩn kết quả khi khởi động
        paneKetQua.setVisible(false);
        lblKhongTimThay.setVisible(false);
        
        // Statistics
        capNhatThongKe();
    }
    
    private void capNhatThongKe() {
        // TODO: Thêm labels statistics nếu có trong FXML
    }
}
