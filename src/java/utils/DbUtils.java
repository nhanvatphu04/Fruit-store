package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Lớp tiện ích thao tác cơ sở dữ liệu cho các thao tác thường gặp
 * Cung cấp các hàm hỗ trợ thao tác và quản lý tài nguyên CSDL
 */
public class DbUtils {
    
    /**
     * Đóng an toàn ResultSet, Statement và Connection
     * @param rs Đối tượng ResultSet cần đóng
     * @param stmt Đối tượng Statement cần đóng
     * @param conn Đối tượng Connection cần đóng
     */
    public static void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        closeResultSet(rs);
        closeStatement(stmt);
        closeConnection(conn);
    }
    
    /**
     * Đóng an toàn ResultSet và Statement
     * @param rs Đối tượng ResultSet cần đóng
     * @param stmt Đối tượng Statement cần đóng
     */
    public static void closeResources(ResultSet rs, Statement stmt) {
        closeResultSet(rs);
        closeStatement(stmt);
    }
    
    /**
     * Đóng an toàn Statement và Connection
     * @param stmt Đối tượng Statement cần đóng
     * @param conn Đối tượng Connection cần đóng
     */
    public static void closeResources(Statement stmt, Connection conn) {
        closeStatement(stmt);
        closeConnection(conn);
    }
    
    /**
     * Đóng an toàn ResultSet
     * @param rs Đối tượng ResultSet cần đóng
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
        }
    }
    
    /**
     * Đóng an toàn Statement
     * @param stmt Đối tượng Statement cần đóng
     */
    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing Statement: " + e.getMessage());
            }
        }
    }
    
    /**
     * Đóng an toàn Connection
     * @param conn Đối tượng Connection cần đóng
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing Connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Thực thi truy vấn và trả về true nếu có kết quả
     * @param query Câu truy vấn SQL
     * @return true nếu có kết quả, false nếu không
     */
    public static boolean hasResults(String query) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DbConnect.getInstance().getConnection();
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            return false;
        } finally {
            closeResources(rs, pstmt);
        }
    }
    
    /**
     * Lấy số lượng bản ghi từ truy vấn
     * @param countQuery Câu truy vấn đếm bản ghi
     * @return Số lượng bản ghi, -1 nếu lỗi
     */
    public static int getRecordCount(String countQuery) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DbConnect.getInstance().getConnection();
            pstmt = conn.prepareStatement(countQuery);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            System.err.println("Error getting record count: " + e.getMessage());
            return -1;
        } finally {
            closeResources(rs, pstmt);
        }
    }
    
    /**
     * Thực thi truy vấn update/insert/delete
     * @param query Câu truy vấn SQL
     * @return Số dòng bị ảnh hưởng, -1 nếu lỗi
     */
    public static int executeUpdate(String query) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DbConnect.getInstance().getConnection();
            pstmt = conn.prepareStatement(query);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error executing update: " + e.getMessage());
            return -1;
        } finally {
            closeResources(pstmt, null);
        }
    }
    
    /**
     * Kiểm tra bảng có tồn tại trong CSDL không
     * @param tableName Tên bảng cần kiểm tra
     * @return true nếu tồn tại, false nếu không
     */
    public static boolean tableExists(String tableName) {
        String query = "SHOW TABLES LIKE ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DbConnect.getInstance().getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, tableName);
            rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking table existence: " + e.getMessage());
            return false;
        } finally {
            closeResources(rs, pstmt);
        }
    }
    
    /**
     * Bắt đầu transaction
     * @param conn Kết nối cần bắt đầu transaction
     * @throws SQLException nếu không thể bắt đầu transaction
     */
    public static void beginTransaction(Connection conn) throws SQLException {
        if (conn != null) {
            conn.setAutoCommit(false);
        }
    }
    
    /**
     * Commit transaction
     * @param conn Kết nối cần commit
     * @throws SQLException nếu commit thất bại
     */
    public static void commitTransaction(Connection conn) throws SQLException {
        if (conn != null) {
            conn.commit();
            conn.setAutoCommit(true);
        }
    }
    
    /**
     * Rollback transaction
     * @param conn Kết nối cần rollback
     */
    public static void rollbackTransaction(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error rolling back transaction: " + e.getMessage());
            }
        }
    }
    
    /**
     * Escape các ký tự đặc biệt trong chuỗi SQL
     * @param input Chuỗi cần escape
     * @return Chuỗi đã escape
     */
    public static String escapeSql(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("'", "''")
                   .replace("\\", "\\\\")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
    
    // Constructor private để ngăn tạo instance
    private DbUtils() {
        throw new UnsupportedOperationException("DbUtils is a utility class");
    }
}