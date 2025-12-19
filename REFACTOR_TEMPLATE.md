# TEMPLATE REFACTOR CHO CÁC CONTROLLERS

## Bước 1: Thêm imports
```java
import com.mekongfarm.util.*;
```

## Bước 2: Replace Alert patterns

### Pattern 1: Simple Alert
```java
// ❌ CŨ
new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();

// ✅ MỚI
DialogUtil.showSuccess("Thông báo", msg);
```

### Pattern 2: Confirmation
```java
// ❌ CŨ
new Alert(Alert.AlertType.CONFIRMATION, msg).showAndWait()
    .filter(b -> b == ButtonType.OK).isPresent();

// ✅ MỚI
DialogUtil.confirm("Xác nhận", msg);
```

### Pattern 3: Delete Confirmation
```java
// ❌ CŨ
if (xacNhan("Xóa " + item + "?")) {
    dao.xoa(id);
}

// ✅ MỚI
if (DialogUtil.confirmDelete("item_type", item.getName())) {
    LoadingUtil.showLoading("Đang xóa...");
    boolean success = dao.xoa(id);
    LoadingUtil.hideLoading();
    if (success) {
        DialogUtil.showSuccess("Đã xóa", "Đã xóa: " + item.getName());
    }
}
```

### Pattern 4: Save with Validation
```java
// ❌ CŨ
if (txtField.getText().isEmpty()) {
    thongBao("Nhập field!");
    return;
}

// ✅ MỚI
if (!ValidationUtil.isNotEmpty(txtField.getText())) {
    ValidationUtil.setErrorStyle(txtField);
    DialogUtil.showError("Lỗi nhập liệu", "Vui lòng nhập field!");
    return;
}
ValidationUtil.clearErrorStyle(txtField);
```

### Pattern 5: Export with Loading
```java
// ❌ CŨ
try {
    exportService.export(data, file);
    thongBao("Xuất thành công!");
} catch (Exception e) {
    thongBao("Lỗi: " + e.getMessage());
}

// ✅ MỚI
LoadingUtil.showLoading("Đang xuất file...");
new Thread(() -> {
    try {
        exportService.export(data, file);
        LoadingUtil.hideLoading();
        javafx.application.Platform.runLater(() ->
            DialogUtil.showSuccess("Thành công", "Đã xuất file!"));
    } catch (Exception e) {
        LoadingUtil.hideLoading();
        javafx.application.Platform.runLater(() ->
            DialogUtil.showError("Lỗi xuất file", e.getMessage()));
    }
}).start();
```

## Controllers CÒN LẠI CẦN REFACTOR:
1. ✅ SanPhamController
2. ✅ DonHangController
3. ✅ KhachHangController
4. ✅ DangNhapController
5. ✅ QuanLyUserController
6. ⏳ ThongKeController (4 alerts)
7. ⏳ CongNoController (1 alert)
8. ⏳ CaiDatController (2 alerts)
9. ⏳ GiaVungController (2 alerts)
10. ⏳ KhoController (2 alerts)
11. ⏳ MuaVuController (2 alerts)
12. ⏳ NhaCungCapController (2 alerts)
13. ⏳ LichSuController (1 alert)
14. ⏳ TrangChuController (1 alert)
15. ⏳ TruyXuatController (1 alert)
16. ⏳ TroLyAIController (check)
17. ⏳ DashboardController (check)
18. ⏳ LaiLoController (check)
19. ⏳ BaseController (utility - done)
