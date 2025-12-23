import java.sql.*;

public class TestBCryptPassword {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:mekongfarm.db");
            System.out.println("✓ Kết nối database thành công!");
            
            // Lấy password từ database
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ten_dang_nhap, mat_khau FROM nguoi_dung WHERE ten_dang_nhap='admin'");
            
            if (rs.next()) {
                String tenDangNhap = rs.getString(1);
                String matKhau = rs.getString(2);
                
                System.out.println("\nThông tin admin:");
                System.out.println("Tên đăng nhập: " + tenDangNhap);
                System.out.println("Mật khẩu: " + matKhau);
                System.out.println("Độ dài: " + matKhau.length());
                System.out.println("Bắt đầu với $2: " + matKhau.startsWith("$2"));
                
                // Kiểm tra BCrypt
                if (matKhau.startsWith("$2a$") || matKhau.startsWith("$2b$")) {
                    System.out.println("\n✅ Mật khẩu đã được hash bằng BCrypt!");
                } else {
                    System.out.println("\n❌ Mật khẩu vẫn là plain text!");
                }
            } else {
                System.out.println("❌ Không tìm thấy user admin!");
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            System.err.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
