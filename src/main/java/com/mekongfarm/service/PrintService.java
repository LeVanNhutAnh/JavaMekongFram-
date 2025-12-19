package com.mekongfarm.service;

import com.mekongfarm.model.DonHang;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.text.*;
import javafx.geometry.Insets;

/**
 * Service in hóa đơn
 */
public class PrintService {

    /**
     * In hóa đơn đơn hàng
     */
    public boolean inHoaDon(DonHang donHang) {
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null && job.showPrintDialog(null)) {
            // Tạo nội dung hóa đơn
            VBox hoaDon = taoNoiDungHoaDon(donHang);

            boolean success = job.printPage(hoaDon);
            if (success) {
                job.endJob();
                return true;
            }
        }
        return false;
    }

    /**
     * Tạo nội dung hóa đơn để in
     */
    private VBox taoNoiDungHoaDon(DonHang donHang) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white;");

        // Header
        Label header = new Label("🌾 MEKONG FARM");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        Label subHeader = new Label("Nông sản Đồng bằng sông Cửu Long");
        subHeader.setFont(Font.font("Arial", 11));

        Label address = new Label("Địa chỉ: ĐBSCL, Việt Nam | ĐT: 1900-xxxx");
        address.setFont(Font.font("Arial", 10));
        address.setStyle("-fx-text-fill: #666;");

        Label separator1 = new Label("════════════════════════════════════");

        Label title = new Label("HÓA ĐƠN BÁN HÀNG");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label separator2 = new Label("────────────────────────────────────");

        // Thông tin đơn hàng
        Label maDH = new Label("Mã ĐH: " + donHang.getMaDH());
        maDH.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        Label ngayDat = new Label("Ngày: " + (donHang.getNgayDat() != null
                ? donHang.getNgayDat().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : ""));
        Label khachHang = new Label(
                "Khách hàng: " + (donHang.getTenKhachHang() != null ? donHang.getTenKhachHang() : ""));

        Label separator3 = new Label("────────────────────────────────────");

        // Chi tiết sản phẩm
        Label chiTietHeader = new Label("CHI TIẾT ĐƠN HÀNG:");
        chiTietHeader.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        box.getChildren().addAll(
                header, subHeader, address, separator1,
                title, separator2,
                maDH, ngayDat, khachHang,
                separator3, chiTietHeader);

        // Thêm từng sản phẩm
        if (donHang.getChiTietList() != null && !donHang.getChiTietList().isEmpty()) {
            int stt = 1;
            for (com.mekongfarm.model.ChiTietDonHang ct : donHang.getChiTietList()) {
                Label spLine = new Label(String.format("%d. %s", stt++, ct.getTenSanPham()));
                spLine.setFont(Font.font("Arial", 11));

                Label detailLine = new Label(String.format("   %d x %,.0f = %,.0f VNĐ",
                        ct.getSoLuong(), ct.getDonGia(), ct.getThanhTien()));
                detailLine.setFont(Font.font("Arial", 11));
                detailLine.setStyle("-fx-text-fill: #333;");

                box.getChildren().addAll(spLine, detailLine);
            }
        } else {
            box.getChildren().add(new Label("(Không có chi tiết)"));
        }

        Label separator4 = new Label("────────────────────────────────────");

        // Tổng tiền
        Label tongTien = new Label("TỔNG TIỀN: " + String.format("%,.0f VNĐ", donHang.getThanhTien()));
        tongTien.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        tongTien.setStyle("-fx-text-fill: #2e7d32;");

        Label separator5 = new Label("════════════════════════════════════");

        // Footer
        Label footer = new Label("✨ Cảm ơn quý khách đã mua hàng!");
        footer.setFont(Font.font("Arial", FontPosture.ITALIC, 12));

        Label footerNote = new Label("Hẹn gặp lại! - Mekong Farm 🌾");
        footerNote.setFont(Font.font("Arial", 10));
        footerNote.setStyle("-fx-text-fill: #666;");

        box.getChildren().addAll(
                separator4, tongTien, separator5,
                footer, footerNote);

        return box;
    }
}
