package services;

import dao.ComboDAO;
import dao.ComboItemDAO;
import models.Combo;
import models.ComboItem;
import java.util.List;

// Service quản lý combo sản phẩm, flash sale
public class ComboService {
    private ComboDAO comboDAO;
    private ComboItemDAO comboItemDAO;

    public ComboService() {
        comboDAO = new ComboDAO();
        comboItemDAO = new ComboItemDAO();
    }

    // Lấy tất cả combo
    public List<Combo> getAllCombos() {
        return comboDAO.getAllCombos();
    }

    // Lấy combo theo id
    public Combo getComboById(int id) {
        return comboDAO.getComboById(id);
    }

    // Thêm combo
    public boolean addCombo(Combo combo) {
        return comboDAO.addCombo(combo);
    }

    // Cập nhật combo
    public boolean updateCombo(Combo combo) {
        return comboDAO.updateCombo(combo);
    }

    // Xoá combo
    public boolean deleteCombo(int id) {
        return comboDAO.deleteCombo(id);
    }

    // Lấy các item của combo
    public List<ComboItem> getComboItemsByComboId(int comboId) {
        return comboItemDAO.getComboItemsByComboId(comboId);
    }

    // Thêm item vào combo
    public boolean addComboItem(ComboItem item) {
        return comboItemDAO.addComboItem(item);
    }

    // Xoá item khỏi combo
    public boolean removeComboItem(int comboItemId) {
        return comboItemDAO.removeComboItem(comboItemId);
    }

    // Lấy danh sách combo đang hoạt động (flash sale)
    public List<Combo> getActiveCombo() {
        return comboDAO.getActiveCombo();
    }
}
