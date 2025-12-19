package com.mekongfarm.test;

import com.mekongfarm.dao.*;
import com.mekongfarm.model.*;
import java.util.List;

public class TestTruyXuat {
    public static void main(String[] args) {
        System.out.println("=== TEST TRUY XUẤT NGUỒN GỐC ===\n");
        
        TruyXuatDAO dao = new TruyXuatDAO();
        SanPhamDAO spDAO = new SanPhamDAO();
        
        // Test 1: Lấy tất cả truy xuất
        List<TruyXuatNguonGoc> all = dao.layTatCa();
        System.out.println("1. Tổng số bản ghi truy xuất: " + all.size());
        
        // Test 2: Kiểm tra sản phẩm SP001
        SanPham sp001 = spDAO.timTheoMaSP("SP001");
        if (sp001 != null) {
            System.out.println("\n2. Sản phẩm SP001 tồn tại:");
            System.out.println("   - Mã SP: " + sp001.getMaSanPham());
            System.out.println("   - Tên: " + sp001.getTenSanPham());
            
            // Tìm truy xuất của SP001
            TruyXuatNguonGoc tx = dao.timTheoSanPham(sp001.getMaSanPham());
            if (tx != null) {
                System.out.println("\n3. ✅ Tìm thấy truy xuất cho SP001:");
                System.out.println("   - Số lô: " + tx.getSoLo());
                System.out.println("   - Địa chỉ: " + tx.getDiaChiSanXuat());
                System.out.println("   - Nông dân: " + tx.getTenNongDan());
                System.out.println("   - Chứng nhận: " + tx.getChungNhan());
            } else {
                System.out.println("\n3. ❌ KHÔNG tìm thấy truy xuất cho SP001!");
            }
        } else {
            System.out.println("\n2. ❌ Không tìm thấy sản phẩm SP001");
        }
        
        // Test 3: Tìm theo số lô
        System.out.println("\n4. Thử tìm theo số lô: LO-ST25-001");
        TruyXuatNguonGoc txByLo = dao.timTheoSoLo("LO-ST25-001");
        if (txByLo != null) {
            System.out.println("   ✅ Tìm thấy!");
        } else {
            System.out.println("   ❌ Không tìm thấy!");
        }
        
        // Test 4: Liệt kê tất cả
        System.out.println("\n5. Danh sách tất cả truy xuất:");
        for (TruyXuatNguonGoc tx : all) {
            System.out.println("   - Sản phẩm: " + tx.getTenSanPham() + 
                             " | Số lô: " + tx.getSoLo());
        }
    }
}
