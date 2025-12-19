import java.sql.*;

public class CheckData {
    public static void main(String[] args) throws Exception {
        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:mekongfarm.db");
        System.out.println("Ket noi thanh cong");
        
        ResultSet rs = c.createStatement().executeQuery("SELECT COUNT(*) FROM truy_xuat_nguon_goc");
        if (rs.next()) {
            System.out.println("So dong truy_xuat_nguon_goc: " + rs.getInt(1));
        }
        
        rs = c.createStatement().executeQuery("SELECT ma_san_pham, so_lo, ten_nong_dan, ngay_thu_hoach, ngay_san_xuat FROM truy_xuat_nguon_goc");
        while (rs.next()) {
            System.out.println("SP ID: " + rs.getInt(1) + ", Lo: " + rs.getString(2) + ", Nong dan: " + rs.getString(3));
            System.out.println("  Ngay thu hoach: [" + rs.getString(4) + "] Type: " + rs.getObject(4).getClass().getName());
            System.out.println("  Ngay san xuat: [" + rs.getString(5) + "]");
        }
        
        rs.close();
        c.close();
    }
}
