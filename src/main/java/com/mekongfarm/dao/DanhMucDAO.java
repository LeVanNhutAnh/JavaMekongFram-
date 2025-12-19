package com.mekongfarm.dao;

import com.mekongfarm.config.CauHinhDatabase;
import com.mekongfarm.model.LoaiSanPham;
import com.mekongfarm.model.TinhThanh;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO Danh mục - Loại sản phẩm và Tỉnh thành
 */
public class DanhMucDAO {

    private final Connection conn;

    public DanhMucDAO() {
        this.conn = CauHinhDatabase.getInstance().getConnection();
    }

    public List<LoaiSanPham> layTatCaLoai() {
        List<LoaiSanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM loai_san_pham ORDER BY ten_loai";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LoaiSanPham lsp = new LoaiSanPham();
                lsp.setMaLoai(rs.getInt("ma_loai"));
                lsp.setTenLoai(rs.getString("ten_loai"));
                lsp.setMoTa(rs.getString("mo_ta"));
                list.add(lsp);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return list;
    }

    public List<TinhThanh> layTatCaTinh() {
        List<TinhThanh> list = new ArrayList<>();
        String sql = "SELECT * FROM tinh_thanh ORDER BY ten_tinh";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TinhThanh tt = new TinhThanh();
                tt.setMaTinh(rs.getInt("ma_tinh"));
                tt.setTenTinh(rs.getString("ten_tinh"));
                tt.setVungMien(rs.getString("vung_mien"));
                list.add(tt);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return list;
    }
}
