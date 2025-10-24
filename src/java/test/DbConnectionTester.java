
// Gói kiểm thử kết nối CSDL
package test;


import java.sql.Connection;
import java.sql.SQLException;
import utils.DatabaseConfig;
import utils.DbConnect;
import utils.DbUtils;

/**
 * Lớp kiểm thử kết nối CSDL cho ứng dụng Fruit Store
 * Dùng để xác minh cấu hình và khả năng kết nối đến cơ sở dữ liệu
 */
public class DbConnectionTester {
    
    /**
     * Hàm main để kiểm thử kết nối CSDL
     * @param args Tham số dòng lệnh (không sử dụng)
     */
    public static void main(String[] args) {
        System.out.println("=== Fruit Store Database Connection Test ===\n");
        
    // Hiển thị thông tin cấu hình
    System.out.println(DatabaseConfig.getConfigInfo());
    System.out.println("\n" + "=".repeat(50) + "\n");

    // Kiểm thử kết nối cơ bản
    testBasicConnection();

    // Kiểm thử singleton
    testSingletonInstance();

    // Kiểm thử khả năng truy cập CSDL
    testDatabaseAccessibility();

    // Kiểm thử cấu trúc CSDL
    testDatabaseStructure();

    // Kiểm thử các hàm tiện ích
    testUtilityMethods();

    System.out.println("\n=== Đã hoàn thành kiểm thử kết nối CSDL ===");
    }
    
    /**
     * Kiểm thử kết nối cơ bản đến CSDL
     */
    private static void testBasicConnection() {
    System.out.println("1. Kiểm thử kết nối cơ bản...");
        
        try {
            DbConnect dbConnect = DbConnect.getInstance();
            Connection conn = dbConnect.getConnection();
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("   ✓ Kết nối cơ bản: THÀNH CÔNG");
                System.out.println("   Tên CSDL: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("   Phiên bản CSDL: " + conn.getMetaData().getDatabaseProductVersion());
                System.out.println("   JDBC Driver: " + conn.getMetaData().getDriverName());
                System.out.println("   JDBC Version: " + conn.getMetaData().getDriverVersion());
            } else {
                System.out.println("   ✗ Kết nối cơ bản: THẤT BẠI - Connection null hoặc đã đóng");
            }
        } catch (SQLException e) {
            System.out.println("   ✗ Kết nối cơ bản: THẤT BẠI");
            System.out.println("   Lỗi: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * Kiểm thử hành vi singleton
     */
    private static void testSingletonInstance() {
    System.out.println("2. Kiểm thử singleton...");
        
        DbConnect instance1 = DbConnect.getInstance();
        DbConnect instance2 = DbConnect.getInstance();
        
        if (instance1 == instance2) {
            System.out.println("   ✓ Singleton: THÀNH CÔNG - Trả về cùng một instance");
        } else {
            System.out.println("   ✗ Singleton: THẤT BẠI - Trả về các instance khác nhau");
        }
        System.out.println();
    }
    
    /**
     * Kiểm thử khả năng truy cập CSDL
     */
    private static void testDatabaseAccessibility() {
    System.out.println("3. Kiểm thử khả năng truy cập CSDL...");
        
        DbConnect dbConnect = DbConnect.getInstance();
        
        if (dbConnect.testConnection()) {
            System.out.println("   ✓ Truy cập CSDL: THÀNH CÔNG");
        } else {
            System.out.println("   ✗ Truy cập CSDL: THẤT BẠI");
        }
        
        if (dbConnect.isDatabaseAccessible()) {
            System.out.println("   ✓ Kiểm tra truy cập CSDL: THÀNH CÔNG");
        } else {
            System.out.println("   ✗ Kiểm tra truy cập CSDL: THẤT BẠI");
        }
        System.out.println();
    }
    
    /**
     * Kiểm thử xác minh cấu trúc CSDL
     */
    private static void testDatabaseStructure() {
    System.out.println("4. Kiểm thử xác minh cấu trúc CSDL...");
        
        DbConnect dbConnect = DbConnect.getInstance();
        
        if (dbConnect.verifyDatabaseStructure()) {
            System.out.println("   ✓ Xác minh cấu trúc CSDL: THÀNH CÔNG");
        } else {
            System.out.println("   ✗ Xác minh cấu trúc CSDL: THẤT BẠI");
        }
        System.out.println();
    }
    
    /**
     * Kiểm thử các hàm tiện ích
     */
    private static void testUtilityMethods() {
    System.out.println("5. Kiểm thử các hàm tiện ích...");
        
    // Kiểm tra sự tồn tại của bảng (có thể sẽ báo không tồn tại nếu CSDL mới)
        String[] expectedTables = {"users", "products", "categories", "cart", "orders", 
                                  "order_items", "discounts", "notifications", "combos", "combo_items"};
        
        int existingTables = 0;
        for (String tableName : expectedTables) {
            if (DbUtils.tableExists(tableName)) {
                System.out.println("   ✓ Bảng '" + tableName + "' tồn tại");
                existingTables++;
            } else {
                System.out.println("   ○ Bảng '" + tableName + "' chưa tồn tại (bình thường với CSDL mới)");
            }
        }
        
    System.out.println("   Đã tìm thấy " + existingTables + "/" + expectedTables.length + " bảng mong đợi");
        
    // Kiểm thử đếm bản ghi (có thể chạy kể cả bảng rỗng)
        int testCount = DbUtils.getRecordCount("SELECT 1");
        if (testCount >= 0) {
            System.out.println("   ✓ Hàm đếm bản ghi: THÀNH CÔNG");
        } else {
            System.out.println("   ✗ Hàm đếm bản ghi: THẤT BẠI");
        }
        
        System.out.println();
    }
    
    /**
     * Hiển thị thông tin kết nối
     */
    public static void displayConnectionInfo() {
    System.out.println("Thông tin kết nối CSDL:");
    System.out.println(DbConnect.getInstance().getDbInfo());
    }
    
    /**
     * Hàm kiểm tra nhanh kết nối cho class khác
     * @return true nếu kết nối thành công, false nếu thất bại
     */
    public static boolean quickConnectionTest() {
        try {
            DbConnect dbConnect = DbConnect.getInstance();
            return dbConnect.testConnection();
        } catch (Exception e) {
            System.err.println("Kiểm thử nhanh kết nối thất bại: " + e.getMessage());
            return false;
        }
    }
}