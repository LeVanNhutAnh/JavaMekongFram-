package com.mekongfarm.service;

import com.mekongfarm.model.BaoGia;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;

import java.io.File;
import java.time.format.DateTimeFormatter;

/**
 * Service xuất báo giá PDF chuyên nghiệp
 */
public class BaoGiaService {

    private static final DeviceRgb GREEN = new DeviceRgb(46, 125, 50);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Xuất báo giá ra file PDF
     */
    public void xuatPDF(BaoGia baoGia, File file) throws Exception {
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        // Header
        doc.add(new Paragraph("BÁO GIÁ")
                .setFontSize(28)
                .setBold()
                .setFontColor(GREEN)
                .setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph("MEKONG FARM - Nông sản ĐBSCL")
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph("\n"));

        // Thông tin báo giá
        doc.add(new Paragraph("Mã báo giá: BG-" + String.format("%06d", baoGia.getMaBaoGia()))
                .setBold());
        doc.add(new Paragraph("Sản phẩm: " + baoGia.getTenSanPham()));
        doc.add(new Paragraph("Nhà cung cấp: " + baoGia.getTenNCC()));
        doc.add(new Paragraph("Đơn giá: " + String.format("%,.0f VNĐ", baoGia.getDonGia())));
        doc.add(new Paragraph("Ngày báo giá: " + baoGia.getNgayBaoGia().format(DATE_FORMAT)));
        doc.add(new Paragraph("Hiệu lực đến: " + (baoGia.getHanHieuLuc() != null ? baoGia.getHanHieuLuc().format(DATE_FORMAT) : "N/A"))
                .setFontColor(new DeviceRgb(255, 152, 0)));

        doc.add(new Paragraph("\n"));
        
        // Thông tin thêm
        if (baoGia.getDieuKien() != null && !baoGia.getDieuKien().isEmpty()) {
            doc.add(new Paragraph("Điều kiện: " + baoGia.getDieuKien()));
        }
        doc.add(new Paragraph("Số lượng tối thiểu: " + baoGia.getSoLuongToiThieu()));
        doc.add(new Paragraph("Thời gian giao: " + baoGia.getThoiGianGiao() + " ngày"));
        
        // Footer
        doc.add(new Paragraph("\nCam on quy khach!")
                .setFontSize(18)
                .setBold()
                .setFontColor(GREEN)
                .setTextAlignment(TextAlignment.RIGHT));

        doc.add(new Paragraph("\n\n"));

        // Điều khoản
        doc.add(new Paragraph("ĐIỀU KHOẢN:")
                .setBold());
        doc.add(new Paragraph("• Giá trên chưa bao gồm VAT"));
        doc.add(new Paragraph("• Thanh toán: 50% đặt cọc, 50% khi giao hàng"));
        doc.add(new Paragraph("• Giao hàng trong vòng 3-5 ngày làm việc"));
        doc.add(new Paragraph("• Báo giá có hiệu lực đến ngày: " + baoGia.getHanHieuLuc().format(DATE_FORMAT)));

        doc.add(new Paragraph("\n\n"));

        // Chữ ký
        Table signTable = new Table(2).setWidth(UnitValue.createPercentValue(100));
        signTable.addCell(
                new Cell().add(new Paragraph("ĐẠI DIỆN BÊN MUA").setBold().setTextAlignment(TextAlignment.CENTER))
                        .setBorder(null));
        signTable.addCell(
                new Cell().add(new Paragraph("ĐẠI DIỆN BÊN BÁN").setBold().setTextAlignment(TextAlignment.CENTER))
                        .setBorder(null));
        signTable.addCell(
                new Cell().add(new Paragraph("\n\n\n(Ký, ghi rõ họ tên)").setTextAlignment(TextAlignment.CENTER))
                        .setBorder(null));
        signTable.addCell(new Cell().add(new Paragraph("\n\n\n(Ký, đóng dấu)").setTextAlignment(TextAlignment.CENTER))
                .setBorder(null));
        doc.add(signTable);

        doc.close();
        System.out.println("📄 Đã xuất báo giá: " + file.getAbsolutePath());
    }

    private Cell createHeaderCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setBold().setFontColor(new DeviceRgb(255, 255, 255)))
                .setBackgroundColor(GREEN)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(8);
    }

    private Cell createCell(String text) {
        return new Cell()
                .add(new Paragraph(text != null ? text : ""))
                .setPadding(5);
    }
}
