package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Discount;
import utils.DbConnect;

// DAO cho mã giảm giá
public class DiscountDAO {

	// Lấy mã giảm giá theo code
	public Discount getDiscountByCode(String code) {
		String sql = "SELECT * FROM discounts WHERE code = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, code);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToDiscount(rs);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Lấy tất cả mã giảm giá
	public List<Discount> getAllDiscounts() {
		List<Discount> list = new ArrayList<>();
		String sql = "SELECT * FROM discounts";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				list.add(mapResultSetToDiscount(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Thêm mã giảm giá
	public boolean addDiscount(Discount discount) {
		String sql = "INSERT INTO discounts (code, description, discount_type, discount_value, min_order_amount, max_discount_amount, start_date, end_date, usage_limit, used_count, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, discount.getCode());
			ps.setString(2, discount.getDescription());
			ps.setString(3, discount.getDiscountType());
			ps.setBigDecimal(4, discount.getDiscountValue());
			ps.setBigDecimal(5, discount.getMinOrderAmount());
			ps.setBigDecimal(6, discount.getMaxDiscountAmount());
			ps.setTimestamp(7, discount.getStartDate());
			ps.setTimestamp(8, discount.getEndDate());
			ps.setInt(9, discount.getUsageLimit());
			ps.setInt(10, discount.getUsedCount());
			ps.setBoolean(11, discount.isActive());
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Cập nhật mã giảm giá
	public boolean updateDiscount(Discount discount) {
		String sql = "UPDATE discounts SET code=?, description=?, discount_type=?, discount_value=?, min_order_amount=?, max_discount_amount=?, start_date=?, end_date=?, usage_limit=?, used_count=?, is_active=? WHERE discount_id=?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, discount.getCode());
			ps.setString(2, discount.getDescription());
			ps.setString(3, discount.getDiscountType());
			ps.setBigDecimal(4, discount.getDiscountValue());
			ps.setBigDecimal(5, discount.getMinOrderAmount());
			ps.setBigDecimal(6, discount.getMaxDiscountAmount());
			ps.setTimestamp(7, discount.getStartDate());
			ps.setTimestamp(8, discount.getEndDate());
			ps.setInt(9, discount.getUsageLimit());
			ps.setInt(10, discount.getUsedCount());
			ps.setBoolean(11, discount.isActive());
			ps.setInt(12, discount.getDiscountId());
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Xoá mã giảm giá
	public boolean deleteDiscount(int id) {
		String sql = "DELETE FROM discounts WHERE discount_id = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Hàm tiện ích chuyển ResultSet sang Discount
	private Discount mapResultSetToDiscount(ResultSet rs) throws SQLException {
		Discount d = new Discount();
		d.setDiscountId(rs.getInt("discount_id"));
		d.setCode(rs.getString("code"));
		d.setDescription(rs.getString("description"));
		d.setDiscountType(rs.getString("discount_type"));
		d.setDiscountValue(rs.getBigDecimal("discount_value"));
		d.setMinOrderAmount(rs.getBigDecimal("min_order_amount"));
		d.setMaxDiscountAmount(rs.getBigDecimal("max_discount_amount"));
		d.setStartDate(rs.getTimestamp("start_date"));
		d.setEndDate(rs.getTimestamp("end_date"));
		d.setUsageLimit(rs.getInt("usage_limit"));
		d.setUsedCount(rs.getInt("used_count"));
		d.setActive(rs.getBoolean("is_active"));
		d.setCreatedAt(rs.getTimestamp("created_at"));
		return d;
	}
}
