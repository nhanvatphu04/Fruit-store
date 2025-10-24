package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Combo;
import utils.DbConnect;

// DAO cho combo khuyến mãi
public class ComboDAO {

	// Lấy tất cả combo
	public List<Combo> getAllCombos() {
		List<Combo> list = new ArrayList<>();
		String sql = "SELECT * FROM combos";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				list.add(mapResultSetToCombo(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Lấy combo đang hoạt động (flash sale)
	public List<Combo> getActiveCombo() {
		List<Combo> list = new ArrayList<>();
		String sql = "SELECT * FROM combos WHERE is_active = 1 AND start_date <= NOW() AND end_date >= NOW()";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapResultSetToCombo(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Lấy combo theo id
	public Combo getComboById(int id) {
		String sql = "SELECT * FROM combos WHERE combo_id = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToCombo(rs);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Thêm combo
	public boolean addCombo(Combo combo) {
		String sql = "INSERT INTO combos (name, description, original_price, combo_price, image_url, start_date, end_date, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, combo.getName());
			ps.setString(2, combo.getDescription());
			ps.setBigDecimal(3, combo.getOriginalPrice());
			ps.setBigDecimal(4, combo.getComboPrice());
			ps.setString(5, combo.getImageUrl());
			ps.setTimestamp(6, combo.getStartDate());
			ps.setTimestamp(7, combo.getEndDate());
			ps.setBoolean(8, combo.isActive());
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Cập nhật combo
	public boolean updateCombo(Combo combo) {
		String sql = "UPDATE combos SET name=?, description=?, original_price=?, combo_price=?, image_url=?, start_date=?, end_date=?, is_active=? WHERE combo_id=?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, combo.getName());
			ps.setString(2, combo.getDescription());
			ps.setBigDecimal(3, combo.getOriginalPrice());
			ps.setBigDecimal(4, combo.getComboPrice());
			ps.setString(5, combo.getImageUrl());
			ps.setTimestamp(6, combo.getStartDate());
			ps.setTimestamp(7, combo.getEndDate());
			ps.setBoolean(8, combo.isActive());
			ps.setInt(9, combo.getComboId());
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Xoá combo
	public boolean deleteCombo(int id) {
		String sql = "DELETE FROM combos WHERE combo_id = ?";
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

	// Hàm tiện ích chuyển ResultSet sang Combo
	private Combo mapResultSetToCombo(ResultSet rs) throws SQLException {
		Combo c = new Combo();
		c.setComboId(rs.getInt("combo_id"));
		c.setName(rs.getString("name"));
		c.setDescription(rs.getString("description"));
		c.setOriginalPrice(rs.getBigDecimal("original_price"));
		c.setComboPrice(rs.getBigDecimal("combo_price"));
		c.setImageUrl(rs.getString("image_url"));
		c.setStartDate(rs.getTimestamp("start_date"));
		c.setEndDate(rs.getTimestamp("end_date"));
		c.setActive(rs.getBoolean("is_active"));
		c.setCreatedAt(rs.getTimestamp("created_at"));
		return c;
	}
}
