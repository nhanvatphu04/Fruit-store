package services;

import dao.CartDAO;
import dao.CartComboDAO;
import java.math.BigDecimal;
import models.CartItem;
import models.CartCombo;
import models.Product;
import models.Combo;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// Service xử lý nghiệp vụ giỏ hàng
public class CartService {
    private CartDAO cartDAO;
    private CartComboDAO cartComboDAO;
    private ProductService productService;
    private DiscountService discountService;
    private ComboService comboService;

    public CartService() {
        cartDAO = new CartDAO();
        cartComboDAO = new CartComboDAO();
        productService = new ProductService();
        discountService = new DiscountService();
        comboService = new ComboService();
    }

    // Lấy giỏ hàng theo user và load thông tin sản phẩm
    public List<CartItem> getCartByUserId(int userId) {
        List<CartItem> cartItems = cartDAO.getCartByUserId(userId);
        for (CartItem item : cartItems) {
            Product product = productService.getProductById(item.getProductId());
            item.setProduct(product);
        }
        return cartItems;
    }

    // Tính tổng tiền giỏ hàng trước giảm giá
    public BigDecimal getCartSubtotal(int userId) {
        List<CartItem> cartItems = getCartByUserId(userId);
        List<CartCombo> cartCombos = getCartCombosByUserId(userId);
        BigDecimal subtotal = BigDecimal.ZERO;
        
        // Sản phẩm lẻ
        for (CartItem item : cartItems) {
            if (item.isSelected() && item.getProduct() != null) {
                subtotal = subtotal.add(item.getTotal());
            }
        }

        // Combo
        if (cartCombos != null) {
            for (CartCombo combo : cartCombos) {
                if (combo.isSelected() && combo.getCombo() != null) {
                    subtotal = subtotal.add(
                        combo.getCombo().getSalePrice().multiply(new BigDecimal(combo.getQuantity()))
                    );
                }
            }
        }
        
        return subtotal;
    }
    
    // Tính số tiền giảm giá từ mã
    public BigDecimal getDiscountAmount(int userId, String discountCode) {
        if (discountCode == null || discountCode.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal subtotal = getCartSubtotal(userId);
        Map<String, Object> discountResult = discountService.applyDiscount(discountCode, subtotal);
        
        if ((boolean) discountResult.get("success")) {
            return (BigDecimal) discountResult.get("discountAmount");
        }
        
        return BigDecimal.ZERO;
    }
    
    // Lấy thông tin tổng tiền chi tiết
    public Map<String, Object> getCartTotals(int userId, String discountCode) {
        Map<String, Object> totals = new HashMap<>();
        
        BigDecimal subtotal = getCartSubtotal(userId);
        BigDecimal discountAmount = getDiscountAmount(userId, discountCode);
        BigDecimal total = subtotal.subtract(discountAmount);
        
        totals.put("subtotal", subtotal);
        totals.put("discount", discountAmount);
        totals.put("total", total);
        
        return totals;
    }
    
    // Thêm sản phẩm vào giỏ
    public boolean addToCart(int userId, int productId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        // Kiểm tra số lượng tồn kho
        Product product = productService.getProductById(productId);
        if (product == null || product.getStockQuantity() < quantity) {
            return false;
        }
        return cartDAO.addToCart(userId, productId, quantity);
    }

    // Cập nhật số lượng sản phẩm trong giỏ
    public boolean updateCartItem(int cartItemId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        // Kiểm tra số lượng tồn kho
        CartItem cartItem = cartDAO.getCartItemById(cartItemId);
        if (cartItem == null) {
            return false;
        }
        Product product = productService.getProductById(cartItem.getProductId());
        if (product == null || product.getStockQuantity() < quantity) {
            return false;
        }
        return cartDAO.updateCartItem(cartItemId, quantity);
    }

    // Cập nhật trạng thái chọn của sản phẩm
    public boolean updateCartItemSelection(int cartItemId, boolean selected) {
        return cartDAO.updateCartItemSelection(cartItemId, selected);
    }

    // Xoá sản phẩm khỏi giỏ
    public boolean removeCartItem(int cartItemId) {
        return cartDAO.removeCartItem(cartItemId);
    }

    // Xoá toàn bộ giỏ hàng của user
    public boolean clearCart(int userId) {
        return cartDAO.clearCart(userId);
    }

    // Tính tổng tiền của các sản phẩm được chọn
    public BigDecimal calculateSelectedTotal(List<CartItem> cartItems) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            if (item.isSelected() && item.getProduct() != null) {
                total = total.add(item.getTotal());
            }
        }
        return total;
    }
    
    // Lấy số lượng sản phẩm trong giỏ hàng (đếm số lượng mục, không phải tổng số lượng)
    public int getCartCount(int userId) {
        List<CartItem> cartItems = cartDAO.getCartByUserId(userId);
        return cartItems.size(); // Chỉ đếm số lượng mặt hàng khác nhau
    }
    
    // Lấy số lượng combo trong giỏ hàng (đếm số lượng mục, không phải tổng số lượng)
    public int getCartComboCount(int userId) {
        List<CartCombo> cartCombos = cartComboDAO.getCartCombosByUserId(userId);
        return cartCombos.size(); // Chỉ đếm số lượng mục combo khác nhau
    }
    
    // Lấy danh sách combo trong giỏ hàng
    public List<CartCombo> getCartCombosByUserId(int userId) {
        List<CartCombo> cartCombos = cartComboDAO.getCartCombosByUserId(userId);
        // Load combo details for each cart combo
        for (CartCombo cartCombo : cartCombos) {
            Combo combo = comboService.getComboById(cartCombo.getComboId());
            cartCombo.setCombo(combo);
        }
        return cartCombos;
    }
    
    // Tính tổng tiền của tất cả sản phẩm và combo được chọn
    public BigDecimal calculateSelectedTotal(List<CartItem> cartItems, List<CartCombo> cartCombos) {
        BigDecimal total = calculateSelectedTotal(cartItems);
        
        // Cộng thêm giá trị của các combo được chọn
        if (cartCombos != null) {
            for (CartCombo combo : cartCombos) {
                if (combo.isSelected()) {
                    total = total.add(combo.getCombo().getSalePrice()
                            .multiply(new BigDecimal(combo.getQuantity())));
                }
            }
        }
        
        return total;
    }
    
    // Cập nhật trạng thái chọn của combo
    public boolean updateCartComboSelection(int cartComboId, boolean selected) {
        return cartComboDAO.updateCartComboSelection(cartComboId, selected);
    }
    
    // Xoá combo khỏi giỏ hàng
    public boolean removeCartCombo(int cartComboId) {
        return cartComboDAO.removeCartComboItem(cartComboId);
    }

    // Cập nhật số lượng combo trong giỏ
    public boolean updateCartComboQuantity(int cartComboId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        return cartComboDAO.updateCartComboItem(cartComboId, quantity);
    }
}
