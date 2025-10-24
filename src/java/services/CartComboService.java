package services;

import dao.CartComboDAO;
import dao.ComboDAO;
import java.math.BigDecimal;
import models.CartCombo;
import models.Combo;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// Service xử lý nghiệp vụ giỏ hàng combo
public class CartComboService {
    private CartComboDAO cartComboDAO;
    private ComboDAO comboDAO;
    private DiscountService discountService;

    public CartComboService() {
        cartComboDAO = new CartComboDAO();
        comboDAO = new ComboDAO();
        discountService = new DiscountService();
    }

    // Lấy giỏ hàng combo theo user và load thông tin combo
    public List<CartCombo> getCartCombosByUserId(int userId) {
        List<CartCombo> cartCombos = cartComboDAO.getCartCombosByUserId(userId);
        for (CartCombo item : cartCombos) {
            Combo combo = comboDAO.getComboById(item.getComboId());
            item.setCombo(combo);
        }
        return cartCombos;
    }

    // Tính tổng tiền giỏ hàng combo trước giảm giá
    public BigDecimal getCartComboSubtotal(int userId) {
        List<CartCombo> cartCombos = getCartCombosByUserId(userId);
        BigDecimal subtotal = BigDecimal.ZERO;
        
        for (CartCombo item : cartCombos) {
            if (item.isSelected() && item.getCombo() != null) {
                BigDecimal price = item.getCombo().getComboPrice();
                BigDecimal quantity = new BigDecimal(item.getQuantity());
                subtotal = subtotal.add(price.multiply(quantity));
            }
        }
        
        return subtotal;
    }
    
    // Tính số tiền giảm giá từ mã cho combo
    public BigDecimal getComboDiscountAmount(int userId, String discountCode) {
        if (discountCode == null || discountCode.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal subtotal = getCartComboSubtotal(userId);
        Map<String, Object> discountResult = discountService.applyDiscount(discountCode, subtotal);
        
        if ((boolean) discountResult.get("success")) {
            return (BigDecimal) discountResult.get("discountAmount");
        }
        
        return BigDecimal.ZERO;
    }
    
    // Lấy thông tin tổng tiền combo chi tiết
    public Map<String, Object> getCartComboTotals(int userId, String discountCode) {
        Map<String, Object> totals = new HashMap<>();
        
        BigDecimal subtotal = getCartComboSubtotal(userId);
        BigDecimal discountAmount = getComboDiscountAmount(userId, discountCode);
        BigDecimal total = subtotal.subtract(discountAmount);
        
        totals.put("subtotal", subtotal);
        totals.put("discount", discountAmount);
        totals.put("total", total);
        
        return totals;
    }
    
    // Thêm combo vào giỏ
    public boolean addComboToCart(int userId, int comboId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        // Kiểm tra combo có tồn tại và đang hoạt động
        Combo combo = comboDAO.getComboById(comboId);
        if (combo == null || !combo.isActive()) {
            return false;
        }
        return cartComboDAO.addComboToCart(userId, comboId, quantity);
    }

    // Cập nhật số lượng combo trong giỏ
    public boolean updateCartComboItem(int cartComboId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        return cartComboDAO.updateCartComboItem(cartComboId, quantity);
    }

    // Cập nhật trạng thái chọn của combo
    public boolean updateCartComboSelection(int cartComboId, boolean selected) {
        return cartComboDAO.updateCartComboSelection(cartComboId, selected);
    }

    // Xóa combo khỏi giỏ
    public boolean removeCartComboItem(int cartComboId) {
        return cartComboDAO.removeCartComboItem(cartComboId);
    }

    // Xóa toàn bộ combo trong giỏ của user
    public boolean clearCartCombos(int userId) {
        return cartComboDAO.clearCartCombos(userId);
    }

    // Tính tổng tiền của các combo được chọn
    public BigDecimal calculateSelectedComboTotal(List<CartCombo> cartCombos) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartCombo item : cartCombos) {
            if (item.isSelected() && item.getCombo() != null) {
                total = total.add(item.getTotal());
            }
        }
        return total;
    }
}
