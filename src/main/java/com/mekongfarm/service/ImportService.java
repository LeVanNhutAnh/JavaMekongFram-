package com.mekongfarm.service;

import com.mekongfarm.dao.SanPhamDAO;
import com.mekongfarm.dao.KhachHangDAO;
import com.mekongfarm.model.SanPham;
import com.mekongfarm.model.KhachHang;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Service nhập dữ liệu từ file Excel
 */
public class ImportService {

    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    /**
     * Nhập sản phẩm từ file Excel
     * Format: Tên SP | Mã Loại | Mã Tỉnh | Đơn giá | Số lượng | Đơn vị
     */
    public int nhapSanPham(File file) throws Exception {
        int count = 0;

        try (FileInputStream fis = new FileInputStream(file);
                Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean isHeader = true;

            for (Row row : sheet) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                try {
                    String tenSP = getCellString(row.getCell(0));
                    int maLoai = (int) getCellNumeric(row.getCell(1));
                    int maTinh = (int) getCellNumeric(row.getCell(2));
                    double donGia = getCellNumeric(row.getCell(3));
                    int soLuong = (int) getCellNumeric(row.getCell(4));
                    String donVi = getCellString(row.getCell(5));

                    if (!tenSP.isEmpty()) {
                        String maSP = sanPhamDAO.layMaSPTiepTheo();
                        SanPham sp = new SanPham(maSP, tenSP, maLoai, maTinh, donGia, soLuong, donVi);
                        sanPhamDAO.them(sp);
                        count++;
                    }
                } catch (Exception e) {
                    // Skip invalid rows
                    System.err.println("Skip row: " + e.getMessage());
                }
            }
        }

        return count;
    }

    /**
     * Nhập khách hàng từ file Excel
     * Format: Tên KH | Địa chỉ | SĐT | Email
     */
    public int nhapKhachHang(File file) throws Exception {
        int count = 0;

        try (FileInputStream fis = new FileInputStream(file);
                Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean isHeader = true;

            for (Row row : sheet) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                try {
                    String tenKH = getCellString(row.getCell(0));
                    String diaChi = getCellString(row.getCell(1));
                    String sdt = getCellString(row.getCell(2));
                    String email = getCellString(row.getCell(3));

                    if (!tenKH.isEmpty()) {
                        String maKH = khachHangDAO.layMaKHTiepTheo();
                        KhachHang kh = new KhachHang(maKH, tenKH, diaChi, sdt, email);
                        khachHangDAO.them(kh);
                        count++;
                    }
                } catch (Exception e) {
                    System.err.println("Skip row: " + e.getMessage());
                }
            }
        }

        return count;
    }

    private String getCellString(Cell cell) {
        if (cell == null)
            return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    private double getCellNumeric(Cell cell) {
        if (cell == null)
            return 0;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }
        try {
            return Double.parseDouble(cell.getStringCellValue().trim());
        } catch (Exception e) {
            return 0;
        }
    }
}
