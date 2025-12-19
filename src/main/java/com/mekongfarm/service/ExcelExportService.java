package com.mekongfarm.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import com.mekongfarm.model.*;

import java.io.*;
import java.util.List;

/**
 * Service xuất Excel
 */
public class ExcelExportService {

    /**
     * Xuất danh sách sản phẩm ra Excel
     */
    public void xuatSanPhamExcel(List<SanPham> danhSach, File file) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Sản phẩm");

            // Style header
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Header row
            Row headerRow = sheet.createRow(0);
            String[] headers = { "Mã SP", "Tên sản phẩm", "Loại", "Tỉnh", "Đơn giá", "Tồn kho", "Đơn vị" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            int rowNum = 1;
            for (SanPham sp : danhSach) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(sp.getMaSP());
                row.createCell(1).setCellValue(sp.getTenSanPham());
                row.createCell(2).setCellValue(sp.getTenLoai() != null ? sp.getTenLoai() : "");
                row.createCell(3).setCellValue(sp.getTenTinh() != null ? sp.getTenTinh() : "");
                row.createCell(4).setCellValue(sp.getDonGia());
                row.createCell(5).setCellValue(sp.getSoLuongTon());
                row.createCell(6).setCellValue(sp.getDonViTinh());
            }

            // Auto size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write
            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }
    }

    /**
     * Xuất danh sách khách hàng ra Excel
     */
    public void xuatKhachHangExcel(List<KhachHang> danhSach, File file) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Khách hàng");

            CellStyle headerStyle = createHeaderStyle(workbook);

            Row headerRow = sheet.createRow(0);
            String[] headers = { "Mã KH", "Họ tên", "Địa chỉ", "SĐT", "Email", "Ngày tạo" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (KhachHang kh : danhSach) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(kh.getMaKH());
                row.createCell(1).setCellValue(kh.getHoTen());
                row.createCell(2).setCellValue(kh.getDiaChi() != null ? kh.getDiaChi() : "");
                row.createCell(3).setCellValue(kh.getSoDienThoai() != null ? kh.getSoDienThoai() : "");
                row.createCell(4).setCellValue(kh.getEmail() != null ? kh.getEmail() : "");
                row.createCell(5).setCellValue(kh.getNgayTao() != null ? kh.getNgayTao().toString() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }
    }

    /**
     * Xuất danh sách đơn hàng ra Excel
     */
    public void xuatDonHangExcel(List<DonHang> danhSach, File file) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Đơn hàng");

            CellStyle headerStyle = createHeaderStyle(workbook);

            Row headerRow = sheet.createRow(0);
            String[] headers = { "Mã ĐH", "Khách hàng", "Ngày đặt", "Tổng tiền", "Trạng thái" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (DonHang dh : danhSach) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(dh.getMaDH());
                row.createCell(1).setCellValue(dh.getTenKhachHang() != null ? dh.getTenKhachHang() : "");
                row.createCell(2).setCellValue(dh.getNgayDat() != null ? dh.getNgayDat().toString() : "");
                row.createCell(3).setCellValue(dh.getThanhTien());
                row.createCell(4).setCellValue(dh.getTrangThai() != null ? dh.getTrangThai() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }
    }

    /**
     * Xuất lịch sử hoạt động ra Excel
     */
    public void xuatLichSuExcel(List<com.mekongfarm.model.LogHoatDong> danhSach, File file) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Lịch sử");

            CellStyle headerStyle = createHeaderStyle(workbook);

            Row headerRow = sheet.createRow(0);
            String[] headers = { "Thời gian", "Người dùng", "Loại", "Mô tả" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (com.mekongfarm.model.LogHoatDong log : danhSach) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(log.getThoiGian() != null ? 
                    log.getThoiGian().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : "");
                row.createCell(1).setCellValue(log.getTenNguoiDung() != null ? log.getTenNguoiDung() : "");
                row.createCell(2).setCellValue(log.getLoaiHoatDong() != null ? log.getLoaiHoatDong() : "");
                row.createCell(3).setCellValue(log.getMoTa() != null ? log.getMoTa() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }
    }
    
    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        return style;
    }
}
