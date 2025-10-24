package utils;

/**
 * Các hằng số cấu hình cơ sở dữ liệu cho ứng dụng Fruit Store
 * Quản lý tập trung để dễ bảo trì
 */
public class DatabaseConfig {
    
    // Thông tin kết nối CSDL
    public static final String DB_HOST = "localhost";
    public static final String DB_PORT = "3306";
    public static final String DB_NAME = "fruitstore";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "";
    
    // Cấu hình connection pool
    public static final int MAX_POOL_SIZE = 20;
    public static final int MIN_POOL_SIZE = 5;
    public static final int INITIAL_POOL_SIZE = 10;
    public static final long CONNECTION_TIMEOUT = 30000; // 30 seconds
    public static final long IDLE_TIMEOUT = 600000; // 10 minutes
    
    // Cấu hình JDBC
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String TIMEZONE = "UTC";
    public static final String CHARSET = "UTF-8";
    
    // Tạo JDBC URL
    public static final String DB_URL = buildJdbcUrl();
    
    /**
     * Tạo JDBC URL với đầy đủ tham số cần thiết
     * @return Chuỗi JDBC URL hoàn chỉnh
     */
    private static String buildJdbcUrl() {
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mysql://")
           .append(DB_HOST)
           .append(":")
           .append(DB_PORT)
           .append("/")
           .append(DB_NAME)
           .append("?useUnicode=true")
           .append("&characterEncoding=").append(CHARSET)
           .append("&useSSL=false")
           .append("&allowPublicKeyRetrieval=true")
           .append("&serverTimezone=").append(TIMEZONE)
           .append("&autoReconnect=true")
           .append("&failOverReadOnly=false")
           .append("&maxReconnects=3");
        
        return url.toString();
    }
    
    /**
     * Lấy thông tin cấu hình CSDL dưới dạng chuỗi định dạng
     * @return Chuỗi cấu hình đã định dạng
     */
    public static String getConfigInfo() {
        return "=== Database Configuration ===" + "\n" +
               "Host: " + DB_HOST + "\n" +
               "Port: " + DB_PORT + "\n" +
               "Database: " + DB_NAME + "\n" +
               "Username: " + DB_USERNAME + "\n" +
               "JDBC Driver: " + JDBC_DRIVER + "\n" +
               "Charset: " + CHARSET + "\n" +
               "Timezone: " + TIMEZONE + "\n" +
               "Max Pool Size: " + MAX_POOL_SIZE + "\n" +
               "Min Pool Size: " + MIN_POOL_SIZE + "\n" +
               "Initial Pool Size: " + INITIAL_POOL_SIZE + "\n" +
               "Connection Timeout: " + CONNECTION_TIMEOUT + "ms\n" +
               "Idle Timeout: " + IDLE_TIMEOUT + "ms\n" +
               "Full URL: " + DB_URL;
    }
    
    // Constructor private để ngăn tạo instance
    private DatabaseConfig() {
        throw new UnsupportedOperationException("DatabaseConfig is a utility class");
    }
}