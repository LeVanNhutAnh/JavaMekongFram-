# 🔧 Hướng dẫn Migration Database

## Tình huống
App hiện tại đang chạy với database CŨ thiếu các cột mới cho tính năng Nhà cung cấp.

## Cách 1: Rebuild DB (NHANH - Mất data)
1. Đóng **TẤT CẢ** app đang chạy
2. Xóa file `d:\MekongFram\mekongfarm.db`
3. Chạy lại app → DB mới tự tạo ✅

## Cách 2: Migration SQL (GIỮ DATA)

### Bước 1: Mở DB Browser
- Download **DB Browser for SQLite**: https://sqlitebrowser.org/dl/
- Open file: `d:\MekongFram\mekongfarm.db`

### Bước 2: Execute Migration
1. Click tab "**Execute SQL**"
2. Copy toàn bộ file `migration_supplier.sql`
3. Paste vào cửa sổ SQL
4. Click "▶️ Execute"

### Bước 3: Verify
```sql
-- Check cột mới đã có chưa
PRAGMA table_info(san_pham);

-- Check bảng mới
SELECT name FROM sqlite_master WHERE type='table';
```

### Bước 4: Save & Close
- File → Write Changes
- Close DB Browser
- Restart app ✅

## Nếu gặp lỗi "duplicate column"
→ **Bỏ qua**, SQLite đã có cột rồi. Chạy tiếp các câu lệnh khác.

## Rollback (nếu cần)
```sql
-- Xóa cột (không được, SQLite không hỗ trợ DROP COLUMN trong cũ)
-- Cách duy nhất: Restore từ backup
```

## Quick Fix nếu không muốn migration
```bash
# Đóng app, xóa DB, chạy lại
cd d:\MekongFram
Remove-Item mekongfarm.db -Force
mvn javafx:run
```
