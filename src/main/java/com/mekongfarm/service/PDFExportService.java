package com.mekongfarm.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
import com.mekongfarm.model.*;
import com.mekongfarm.dao.*;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service xuất báo cáo PDF
 */
public class PDFExportService {

    private static final DeviceRgb GREEN = new DeviceRgb(46, 125, 50);

    /**
     * Xuất danh sách sản phẩm ra PDF
     */
    public void xuatSanPhamPDF(List<SanPham> danhSach, File file) throws Exception {
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        // Tiêu đề
        doc.add(new Paragraph("DANH SÁCH SẢN PHẨM")
                .setFontSize(20)
                .setBold()
                .setFontColor(GREEN)
                .setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph(
                "Ngày xuất: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph("\n"));

        // Bảng
        Table table = new Table(UnitValue.createPercentArray(new float[] { 1, 3, 2, 2, 2 }))
                .setWidth(UnitValue.createPercentValue(100));

        // Header
        table.addHeaderCell(createHeaderCell("Mã SP"));
        table.addHeaderCell(createHeaderCell("Tên sản phẩm"));
        table.addHeaderCell(createHeaderCell("Loại"));
        table.addHeaderCell(createHeaderCell("Đơn giá"));
        table.addHeaderCell(createHeaderCell("Tồn kho"));

        // Data
        for (SanPham sp : danhSach) {
            table.addCell(createCell(sp.getMaSP()));
            table.addCell(createCell(sp.getTenSanPham()));
            table.addCell(createCell(sp.getTenLoai() != null ? sp.getTenLoai() : ""));
            table.addCell(createCell(String.format("%,.0f VNĐ", sp.getDonGia())));
            table.addCell(createCell(sp.getSoLuongTon() + " " + sp.getDonViTinh()));
        }

        doc.add(table);

        // Footer
        doc.add(new Paragraph("\nTổng: " + danhSach.size() + " sản phẩm")
                .setFontSize(12)
                .setBold());

        doc.close();
    }

    /**
     * Xuất đơn hàng ra PDF
     */
    public void xuatDonHangPDF(DonHang donHang, File file) throws Exception {
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        // Header
        doc.add(new Paragraph("HÓA ĐƠN BÁN HÀNG")
                .setFontSize(24)
                .setBold()
                .setFontColor(GREEN)
                .setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph("MEKONG FARM - Nông sản ĐBSCL")
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph("\n"));

        // Thông tin đơn hàng
        doc.add(new Paragraph("Mã đơn hàng: " + donHang.getMaDH()).setBold());
        doc.add(new Paragraph("Ngày đặt: " + donHang.getNgayDat()));
        doc.add(new Paragraph("Khách hàng: " + donHang.getTenKhachHang()));
        doc.add(new Paragraph("Trạng thái: " + donHang.getTrangThai()));

        doc.add(new Paragraph("\n"));

        // Tổng tiền
        doc.add(new Paragraph("TỔNG TIỀN: " + String.format("%,.0f VNĐ", donHang.getThanhTien()))
                .setFontSize(18)
                .setBold()
                .setFontColor(GREEN));

        doc.close();
    }

    /**
     * Xuất báo cáo thống kê
     */
    public void xuatBaoCaoThongKe(File file) throws Exception {
        ThongKeDAO thongKeDAO = new ThongKeDAO();

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        // Tiêu đề
        doc.add(new Paragraph("BÁO CÁO THỐNG KÊ")
                .setFontSize(24)
                .setBold()
                .setFontColor(GREEN)
                .setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph("MEKONG FARM - Nông sản ĐBSCL")
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph("Ngày: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph("\n"));

        // Thống kê tổng quan
        doc.add(new Paragraph("TỔNG QUAN").setFontSize(16).setBold());
        doc.add(new Paragraph("• Tổng sản phẩm: " + thongKeDAO.demTongSanPham()));
        doc.add(new Paragraph("• Tổng khách hàng: " + thongKeDAO.demTongKhachHang()));
        doc.add(new Paragraph("• Tổng đơn hàng: " + thongKeDAO.demTongDonHang()));
        doc.add(new Paragraph("• Tổng doanh thu: " + String.format("%,.0f VNĐ", thongKeDAO.tinhTongDoanhThu())));

        doc.close();
    }

    /**
     * Xuất lịch sử hoạt động ra PDF
     */
    public void xuatLichSuPDF(List<com.mekongfarm.model.LogHoatDong> danhSach, File file) throws Exception {
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        // Tiêu đề
        doc.add(new Paragraph("LỊCH SỬ HOẠT ĐỘNG HỆ THỐNG")
                .setFontSize(20)
                .setBold()
                .setFontColor(GREEN)
                .setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph(
                "Ngày xuất: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph("\n"));

        // Bảng
        Table table = new Table(UnitValue.createPercentArray(new float[] { 2, 1.5f, 1.5f, 4 }))
                .setWidth(UnitValue.createPercentValue(100));

        // Header
        table.addHeaderCell(createHeaderCell("Thời gian"));
        table.addHeaderCell(createHeaderCell("Người dùng"));
        table.addHeaderCell(createHeaderCell("Loại"));
        table.addHeaderCell(createHeaderCell("Mô tả"));

        // Data
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (com.mekongfarm.model.LogHoatDong log : danhSach) {
            table.addCell(createCell(log.getThoiGian() != null ? log.getThoiGian().format(dtf) : ""));
            table.addCell(createCell(log.getTenNguoiDung() != null ? log.getTenNguoiDung() : ""));
            table.addCell(createCell(log.getLoaiHoatDong() != null ? log.getLoaiHoatDong() : ""));
            table.addCell(createCell(log.getMoTa() != null ? log.getMoTa() : ""));
        }

        doc.add(table);

        // Footer
        doc.add(new Paragraph("\nTổng: " + danhSach.size() + " hoạt động")
                .setFontSize(12)
                .setBold());

        doc.close();
    }

    private Cell createHeaderCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setBold().setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(GREEN)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(5);
    }

    private Cell createCell(String text) {
        return new Cell()
                .add(new Paragraph(text != null ? text : ""))
                .setPadding(5);
    }
}
