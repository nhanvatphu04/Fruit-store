package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.ComboItem;
import utils.DbConnect;

// DAO cho sản phẩm trong combo
public class ComboItemDAO {

	// Lấy các item của combo
	public List<ComboItem> getComboItemsByComboId(int comboId) {
		List<ComboItem> list = new ArrayList<>();
		String sql = "SELECT * FROM combo_items WHERE combo_id = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, comboId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ComboItem item = new ComboItem();
				item.setComboItemId(rs.getInt("combo_item_id"));
				item.setComboId(rs.getInt("combo_id"));
				item.setProductId(rs.getInt("product_id"));
				item.setQuantity(rs.getInt("quantity"));
				list.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Thêm item vào combo
	public boolean addComboItem(ComboItem item) {
		String sql = "INSERT INTO combo_items (combo_id, product_id, quantity) VALUES (?, ?, ?)";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, item.getComboId());
			ps.setInt(2, item.getProductId());
			ps.setInt(3, item.getQuantity());
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Xoá item khỏi combo
	public boolean removeComboItem(int comboItemId) {
		String sql = "DELETE FROM combo_items WHERE combo_item_id = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, comboItemId);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
