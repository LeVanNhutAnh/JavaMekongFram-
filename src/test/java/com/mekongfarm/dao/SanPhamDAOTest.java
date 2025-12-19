package com.mekongfarm.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.mekongfarm.model.SanPham;
import java.util.List;

/**
 * Unit tests cho SanPhamDAO
 */
class SanPhamDAOTest {

    private SanPhamDAO dao;

    @BeforeEach
    void setUp() {
        dao = new SanPhamDAO();
    }

    @Test
    @DisplayName("Kiểm tra lấy tất cả sản phẩm")
    void testLayTatCa() {
        List<SanPham> danhSach = dao.layTatCa();
        assertNotNull(danhSach, "Danh sách không được null");
    }

    @Test
    @DisplayName("Kiểm tra tìm kiếm theo tên")
    void testTimTheoTen() {
        List<SanPham> ketQua = dao.timTheoTen("Gạo");
        assertNotNull(ketQua, "Kết quả tìm kiếm không được null");
    }

    @Test
    @DisplayName("Kiểm tra lấy mã SP tiếp theo")
    void testLayMaSPTiepTheo() {
        String maSP = dao.layMaSPTiepTheo();
        assertNotNull(maSP, "Mã SP không được null");
        assertTrue(maSP.startsWith("SP"), "Mã SP phải bắt đầu bằng SP");
    }
}
