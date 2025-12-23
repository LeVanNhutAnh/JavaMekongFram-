# 🔧 CRITICAL FIXES - IMPLEMENTATION GUIDE

**Date:** 2025-12-23  
**Status:** IN PROGRESS

---

## ✅ **COMPLETED FIXES**

### 1. ✅ **Created AppLogger Utility**
- **File:** `src/main/java/com/mekongfarm/util/AppLogger.java`
- **Features:**
  - Proper logging framework (java.util.logging)
  - Console handler with INFO level
  - File handler with ALL level (app_*.log)
  - Error-only handler (error_*.log)
  - Simple and detailed formatters
  - Replaces all System.out/err và printStackTrace()

**Usage:**
```java
import com.mekongfarm.util.AppLogger;

// Instead of System.out.println()
AppLogger.info("Message");

// Instead of System.err.println() + printStackTrace()
AppLogger.error("Error message", exception);
```

---

## 🔄 **IN PROGRESS**

### 2. 🔄 **Transaction Management cho tất cả DAO**

#### **DonNhapDAO** - ✅ PARTIALLY FIXED
- ✅ Added AppLogger import
- 🔄 Need to fix: them(), themChiTiet(), xoa()

**Required Changes:**
```java
// Pattern cho tất cả DAO write operations:
public boolean operation() {
    try {
        conn.setAutoCommit(false);
        
        // Do operation
        // ...
        
        conn.commit();
        AppLogger.info("Success message");
        return true;
    } catch (SQLException e) {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            AppLogger.error("Rollback failed", ex);
        }
        AppLogger.error("Operation failed", e);
        return false;
    } finally {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            AppLogger.error("Reset autoCommit failed", e);
        }
    }
}
```

#### **DAOs Need Transaction Fix:**
- ❌ SanPhamDAO: them(), capNhat(), capNhatTonKho(), giamSoLuong()
- ❌ DonHangDAO: them() (HAS transaction but needs logging fix)
- ❌ CongNoDAO: them(), thanhToan()
- ❌ NguoiDungDAO: them(), capNhat()
- ❌ KhachHangDAO: them(), capNhat()
- ❌ TruyXuatDAO: them(), capNhat()

---

### 3. ❌ **Fix SQL Typo trong SanPhamDAO**

**Line 97:**
```java
// WRONG - Column name typo
"WHERE sp.ma_loại = ?"  // ← Vietnamese character, will fail!

// CORRECT
"WHERE sp.ma_loai = ?"
```

---

### 4. ❌ **Password Security - Fix User Mẫu**

**File:** `CauHinhDatabase.java` line 126
```java
// CURRENT - Plain text password
stmt.execute("INSERT OR IGNORE INTO nguoi_dung (ten_dang_nhap, mat_khau, ho_ten, vai_tro) VALUES ('admin', 'admin123', 'Quản trị viên', 'quan_tri')");

// FIX - Use BCrypt
import org.mindrot.jbcrypt.BCrypt;
String hashedPassword = BCrypt.hashpw("admin123", BCrypt.gensalt());
stmt.execute("INSERT OR IGNORE INTO nguoi_dung (ten_dang_nhap, mat_khau, ho_ten, vai_tro) VALUES ('admin', '" + hashedPassword + "', 'Quản trị viên', 'quan_tri')");
```

---

### 5. ❌ **Foreign Key CASCADE Constraints**

**File:** `schema.sql`

**Current:**
```sql
FOREIGN KEY (ma_loai) REFERENCES loai_san_pham(ma_loai)
```

**Should be:**
```sql
FOREIGN KEY (ma_loai) REFERENCES loai_san_pham(ma_loai) 
    ON DELETE SET NULL 
    ON UPDATE CASCADE
```

**Apply to:**
- san_pham: ma_loai, ma_tinh, ma_ncc
- don_hang: ma_khach_hang, ma_nguoi_dung
- chi_tiet_don_hang: ma_don_hang, ma_san_pham
- truy_xuat_nguon_goc: ma_san_pham
- etc.

---

### 6. ❌ **Concurrency Control - Stock Updates**

**File:** `SanPhamDAO.java`

**Current giamSoLuong():**
```java
public boolean giamSoLuong(int maSanPham, int soLuongGiam) {
    String sql = "UPDATE san_pham SET so_luong_ton = so_luong_ton - ? WHERE ma_san_pham = ? AND so_luong_ton >= ?";
    // ...
}
```

**Issues:**
- No atomic check
- Race condition possible
- No optimistic locking

**Solution - Add Row Versioning:**
```sql
-- Add version column
ALTER TABLE san_pham ADD COLUMN version INTEGER DEFAULT 0;

-- Update with version check
UPDATE san_pham 
SET so_luong_ton = so_luong_ton - ?, 
    version = version + 1 
WHERE ma_san_pham = ? 
  AND so_luong_ton >= ? 
  AND version = ?
```

```java
public boolean giamSoLuong(int maSanPham, int soLuongGiam, int expectedVersion) {
    String sql = "UPDATE san_pham SET so_luong_ton = so_luong_ton - ?, version = version + 1 WHERE ma_san_pham = ? AND so_luong_ton >= ? AND version = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, soLuongGiam);
        pstmt.setInt(2, maSanPham);
        pstmt.setInt(3, soLuongGiam);
        pstmt.setInt(4, expectedVersion);
        int updated = pstmt.executeUpdate();
        if (updated == 0) {
            AppLogger.warning("Concurrency conflict detected for product " + maSanPham);
            return false; // Retry needed
        }
        return true;
    } catch (SQLException e) {
        AppLogger.error("Failed to decrease stock", e);
        return false;
    }
}
```

---

### 7. ❌ **Case-Insensitive Search**

**File:** `SanPhamDAO.java` and others

**Current:**
```java
"WHERE sp.ten_san_pham LIKE ?"  // Case sensitive
pstmt.setString(1, "%" + tuKhoa + "%");
```

**Fix:**
```java
"WHERE LOWER(sp.ten_san_pham) LIKE LOWER(?)"
pstmt.setString(1, "%" + tuKhoa + "%");
```

**Apply to:**
- SanPhamDAO.timTheoTen()
- KhachHangDAO.timKiem()
- NhaCungCapDAO.timKiem()
- DonHangDAO search methods

---

### 8. ❌ **Dashboard Auto-Refresh**

**File:** `DashboardController.java`

**Add:**
```java
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

private Timeline refreshTimeline;

@FXML
public void initialize() {
    taiDuLieuThongKe();
    
    // Auto-refresh mỗi 30 giây
    refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(30), e -> {
        taiDuLieuThongKe();
        AppLogger.debug("Dashboard refreshed");
    }));
    refreshTimeline.setCycleCount(Timeline.INDEFINITE);
    refreshTimeline.play();
}

// Cleanup on controller close
public void cleanup() {
    if (refreshTimeline != null) {
        refreshTimeline.stop();
    }
}
```

---

### 9. ❌ **Gemini API Error Handling & Retry**

**File:** `GeminiService.java`

**Add:**
```java
private static final int MAX_RETRIES = 3;
private static final int RETRY_DELAY_MS = 1000;

public String chat(String userMessage) {
    // Check API key
    if (API_KEY == null || API_KEY.isEmpty()) {
        AppLogger.error("Gemini API key not configured");
        return "❌ API key chưa được cấu hình. Vui lòng thiết lập trong Settings.";
    }
    
    int attempt = 0;
    while (attempt < MAX_RETRIES) {
        try {
            String prompt = SYSTEM_CONTEXT + "\n\nNgười dùng hỏi: " + userMessage;
            return callGeminiText(prompt);
        } catch (Exception e) {
            attempt++;
            AppLogger.warning("Gemini API call failed (attempt " + attempt + "): " + e.getMessage());
            
            if (attempt >= MAX_RETRIES) {
                AppLogger.error("Gemini API failed after " + MAX_RETRIES + " retries", e);
                return "❌ Không thể kết nối AI sau " + MAX_RETRIES + " lần thử. Vui lòng kiểm tra kết nối mạng.";
            }
            
            try {
                Thread.sleep(RETRY_DELAY_MS * attempt);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return "❌ Bị gián đoạn khi retry";
            }
        }
    }
    return "❌ Lỗi không xác định";
}
```

---

### 10. ❌ **Export Error Handling**

**Files:** `PDFExportService.java`, `ExcelExportService.java`

**Add before export:**
```java
// Check disk space
File outputFile = new File(filePath);
File parentDir = outputFile.getParentFile();
if (parentDir != null) {
    long freeSpace = parentDir.getFreeSpace();
    long estimatedSize = 10 * 1024 * 1024; // 10MB estimate
    if (freeSpace < estimatedSize) {
        throw new IOException("Không đủ dung lượng ổ đĩa (cần ~10MB)");
    }
}

// Check write permission
if (parentDir != null && !parentDir.canWrite()) {
    throw new IOException("Không có quyền ghi vào thư mục: " + parentDir.getPath());
}

// Check if file exists and is writable
if (outputFile.exists() && !outputFile.canWrite()) {
    throw new IOException("File đang bị khóa hoặc read-only: " + outputFile.getName());
}
```

---

### 11. ❌ **Constants for Magic Numbers**

**Create:** `src/main/java/com/mekongfarm/config/AppConstants.java`

```java
package com.mekongfarm.config;

public class AppConstants {
    // Stock thresholds
    public static final int STOCK_WARNING_THRESHOLD = 10;
    public static final int STOCK_CRITICAL_THRESHOLD = 5;
    
    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 50;
    public static final int MAX_PAGE_SIZE = 500;
    
    // Auto-refresh intervals (seconds)
    public static final int DASHBOARD_REFRESH_INTERVAL = 30;
    public static final int NOTIFICATION_CHECK_INTERVAL = 60;
    
    // File limits
    public static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    public static final long MIN_DISK_SPACE_REQUIRED = 100 * 1024 * 1024; // 100MB
    
    // Validation
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 100;
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 50;
    
    // API
    public static final int GEMINI_MAX_RETRIES = 3;
    public static final int GEMINI_RETRY_DELAY_MS = 1000;
    public static final int API_TIMEOUT_SECONDS = 30;
}
```

**Usage:**
```java
if (sp.getSoLuongTon() < AppConstants.STOCK_WARNING_THRESHOLD) {
    // Warn low stock
}
```

---

## 📋 **VALIDATION FOR ALL CONTROLLERS**

### Controllers Need Validation (15/23):
- ❌ CaiDatController
- ❌ DashboardController
- ❌ GiaVungController
- ❌ KhoController
- ❌ LaiLoController
- ❌ LichSuController
- ❌ MuaVuController
- ❌ NhaCungCapController (partial)
- ❌ TrangChuController
- ❌ TroLyAIController
- ❌ NguonGocController
- ❌ QuanLyTruyXuatController
- ❌ BaoCaoController
- ❌ DonNhapController
- ❌ ThongKeController (partial)

**Validation Pattern:**
```java
// Before save
if (!ValidationUtil.isNonNegativeNumber(txtGia.getText())) {
    ValidationUtil.setErrorStyle(txtGia);
    DialogUtil.showError("Lỗi", "Giá phải là số không âm!");
    return;
}
ValidationUtil.clearErrorStyle(txtGia);

if (!ValidationUtil.isValidEmail(txtEmail.getText())) {
    ValidationUtil.setErrorStyle(txtEmail);
    DialogUtil.showError("Lỗi", "Email không đúng định dạng!");
    return;
}
```

---

## 🧪 **TESTING CHECKLIST**

### Unit Tests Needed:
- [ ] ValidationUtil - all methods
- [ ] AppLogger - file creation, logging levels
- [ ] SanPhamDAO - CRUD + concurrency
- [ ] DonHangDAO - transaction rollback
- [ ] BCrypt password hashing

### Integration Tests Needed:
- [ ] Login flow with BCrypt
- [ ] Stock update concurrency
- [ ] Transaction rollback on error
- [ ] Dashboard auto-refresh
- [ ] Export with error conditions

### Manual Tests:
- [ ] Create order → check transaction
- [ ] Delete order → check rollback if error
- [ ] Update stock simultaneously from 2 users
- [ ] Export PDF with no disk space
- [ ] Login with wrong password 5 times
- [ ] Search products case-insensitive
- [ ] Dashboard updates after new order

---

## 📝 **BUILD & RUN COMMANDS**

```bash
# Clean build
mvn clean compile

# Run tests
mvn test

# Package
mvn clean package

# Run application
mvn javafx:run

# Check for issues
mvn dependency:analyze
mvn versions:display-dependency-updates
```

---

## 🚀 **DEPLOYMENT CHECKLIST**

- [ ] All System.out/err replaced with AppLogger
- [ ] All DAOs have transaction management
- [ ] All passwords use BCrypt
- [ ] All searches case-insensitive
- [ ] All exports have error handling
- [ ] All controllers have validation
- [ ] Foreign keys have CASCADE
- [ ] Magic numbers replaced with constants
- [ ] Concurrency control added
- [ ] All tests pass
- [ ] No printStackTrace() in code
- [ ] Logs directory created
- [ ] Database backed up

---

**Last Updated:** 2025-12-23  
**Next Steps:** Continue fixing remaining DAOs and controllers systematically
