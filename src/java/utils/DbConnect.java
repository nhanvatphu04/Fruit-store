
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp tiện ích kết nối cơ sở dữ liệu cho ứng dụng Fruit Store
 * Xử lý kết nối MySQL với hỗ trợ kết nối pooling
 */
public class DbConnect {
    
    // Các hằng số cấu hình cơ sở dữ liệu
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "fruitstore";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    
    // Đường dẫn JDBC URL
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME 
            + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    
    // Tên Driver JDBC
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Biến instance Singleton
    private static DbConnect instance;
    
    /**
     * Hàm khởi tạo private để ngăn tạo đối tượng trực tiếp
     */
    private DbConnect() {
        try {
            // Load MySQL JDBC Driver
            Class.forName(JDBC_DRIVER);
            System.out.println("MySQL JDBC Driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }
    
    /**
     * Lấy instance duy nhất của DbConnect
     * @return Đối tượng DbConnect
     */
    public static synchronized DbConnect getInstance() {
        if (instance == null) {
            instance = new DbConnect();
        }
        return instance;
    }
    
    /**
     * Lấy kết nối đến cơ sở dữ liệu
     * Luôn tạo kết nối mới để tránh chia sẻ Connection giữa các thread
     * @return Đối tượng Connection
     * @throws SQLException nếu kết nối thất bại
     */
    public Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Failed to connect to database!");
            System.err.println("URL: " + DB_URL);
            System.err.println("Username: " + DB_USERNAME);
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Kiểm tra kết nối cơ sở dữ liệu
     * @return true nếu kết nối thành công, false nếu thất bại
     */
    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("Database connection test: SUCCESS");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Database connection test: FAILED");
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Đóng kết nối cơ sở dữ liệu
     */
    public void closeConnection() {
        // Deprecated: mỗi lần gọi getConnection() hiện đã tạo kết nối mới
        // và nên được đóng bằng try-with-resources tại nơi sử dụng.
    }
    
    /**
     * Lấy thông tin cấu hình cơ sở dữ liệu (dùng cho debug)
     * @return Chuỗi chứa thông tin cấu hình
     */
    public String getDbInfo() {
        return "Database Configuration:\n" +
               "Host: " + DB_HOST + "\n" +
               "Port: " + DB_PORT + "\n" +
               "Database: " + DB_NAME + "\n" +
               "Username: " + DB_USERNAME + "\n" +
               "URL: " + DB_URL;
    }
    
    /**
     * Tạo một kết nối mới (dùng cho connection pooling)
     * @return Đối tượng Connection mới
     * @throws SQLException nếu kết nối thất bại
     */
    public static Connection createConnection() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
    
    /**
     * Kiểm tra cơ sở dữ liệu có tồn tại và truy cập được không
     * @return true nếu truy cập được, false nếu không
     */
    public boolean isDatabaseAccessible() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database is not accessible: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Thực thi truy vấn đơn giản để kiểm tra kết nối cơ sở dữ liệu
     * @return true nếu truy vấn thành công, false nếu thất bại
     */
    public boolean verifyDatabaseStructure() {
        String testQuery = "SELECT 1";
        try (Connection conn = getConnection();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(testQuery)) {
            
            return rs.next() && rs.getInt(1) == 1;
        } catch (SQLException e) {
            System.err.println("Database structure verification failed: " + e.getMessage());
            return false;
        }
    }
}
