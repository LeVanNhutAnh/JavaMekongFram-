package com.mekongfarm.dao;

import com.mekongfarm.config.CauHinhDatabase;
import com.mekongfarm.model.LogHoatDong;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO Log hoạt động
 */
public class LogDAO {

    private final Connection conn;

    public LogDAO() {
        this.conn = CauHinhDatabase.getInstance().getConnection();
        taoTableNeuChuaCo();
    }

    /**
     * Tạo table log nếu chưa có
     */
    private void taoTableNeuChuaCo() {
        String sql = """
                CREATE TABLE IF NOT EXISTS log_hoat_dong (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    ma_nguoi_dung INTEGER,
                    loai_hoat_dong TEXT NOT NULL,
                    mo_ta TEXT,
                    bang_lien_quan TEXT,
                    ma_doi_tuong INTEGER,
                    thoi_gian TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    dia_chi_ip TEXT,
                    FOREIGN KEY (ma_nguoi_dung) REFERENCES nguoi_dung(ma_nguoi_dung)
                )
                """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Lỗi tạo table log: " + e.getMessage());
        }
    }

    /**
     * Thêm log mới
     */
    public boolean them(LogHoatDong log) {
        String sql = """
                INSERT INTO log_hoat_dong (ma_nguoi_dung, loai_hoat_dong, mo_ta, bang_lien_quan, ma_doi_tuong, dia_chi_ip)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, log.getMaNguoiDung());
            pstmt.setString(2, log.getLoaiHoatDong());
            pstmt.setString(3, log.getMoTa());
            pstmt.setString(4, log.getBangLienQuan());
            pstmt.setInt(5, log.getMaDoiTuong());
            pstmt.setString(6, log.getDiaChi_IP());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm log: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy tất cả log
     */
    public List<LogHoatDong> layTatCa(int limit) {
        List<LogHoatDong> list = new ArrayList<>();
        String sql = """
                SELECT l.*, nd.ho_ten as ten_nguoi_dung
                FROM log_hoat_dong l
                LEFT JOIN nguoi_dung nd ON l.ma_nguoi_dung = nd.ma_nguoi_dung
                ORDER BY l.thoi_gian DESC
                LIMIT ?
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapLog(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy log: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lấy log theo user
     */
    public List<LogHoatDong> layTheoUser(int maNguoiDung, int limit) {
        List<LogHoatDong> list = new ArrayList<>();
        String sql = """
                SELECT l.*, nd.ho_ten as ten_nguoi_dung
                FROM log_hoat_dong l
                LEFT JOIN nguoi_dung nd ON l.ma_nguoi_dung = nd.ma_nguoi_dung
                WHERE l.ma_nguoi_dung = ?
                ORDER BY l.thoi_gian DESC
                LIMIT ?
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maNguoiDung);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapLog(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return list;
    }

    private LogHoatDong mapLog(ResultSet rs) throws SQLException {
        LogHoatDong log = new LogHoatDong();
        log.setId(rs.getInt("id"));
        log.setMaNguoiDung(rs.getInt("ma_nguoi_dung"));
        log.setTenNguoiDung(rs.getString("ten_nguoi_dung"));
        log.setLoaiHoatDong(rs.getString("loai_hoat_dong"));
        log.setMoTa(rs.getString("mo_ta"));
        log.setBangLienQuan(rs.getString("bang_lien_quan"));
        log.setMaDoiTuong(rs.getInt("ma_doi_tuong"));
        log.setDiaChi_IP(rs.getString("dia_chi_ip"));
        String thoiGianStr = rs.getString("thoi_gian");
        if (thoiGianStr != null) {
            log.setThoiGian(java.time.LocalDateTime.parse(thoiGianStr.replace(" ", "T")));
        }
        return log;
    }
}
