package services;

import dao.DiscountDAO;
import dao.DiscountUsageDAO;
import models.Discount;
import models.DiscountUsage;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// Service quản lý mã giảm giá
public class DiscountService {
    private DiscountDAO discountDAO;
    private DiscountUsageDAO discountUsageDAO;

    public DiscountService() {
        discountDAO = new DiscountDAO();
        discountUsageDAO = new DiscountUsageDAO();
    }
    
    // Kiểm tra tính hợp lệ của mã giảm giá và áp dụng cho đơn hàng
    public Map<String, Object> applyDiscount(String code, BigDecimal orderAmount) {
        Map<String, Object> response = new HashMap<>();
        
        // Kiểm tra mã giảm giá có tồn tại
        Discount discount = getDiscountByCode(code);
        if (discount == null) {
            response.put("success", false);
            response.put("message", "Mã giảm giá không tồn tại");
            return response;
        }
        
        // Kiểm tra mã còn hiệu lực
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (!discount.isActive() || 
            (discount.getStartDate() != null && discount.getStartDate().after(now)) || 
            (discount.getEndDate() != null && discount.getEndDate().before(now))) {
            response.put("success", false);
            response.put("message", "Mã giảm giá đã hết hạn hoặc chưa đến thời gian sử dụng");
            return response;
        }
        
        // Kiểm tra giới hạn sử dụng
        if (discount.getUsedCount() >= discount.getUsageLimit()) {
            response.put("success", false);
            response.put("message", "Mã giảm giá đã hết lượt sử dụng");
            return response;
        }
        
        // Kiểm tra giá trị đơn hàng tối thiểu
        if (discount.getMinOrderAmount() != null && orderAmount.compareTo(discount.getMinOrderAmount()) < 0) {
            response.put("success", false);
            response.put("message", "Giá trị đơn hàng chưa đủ để áp dụng mã giảm giá này");
            return response;
        }
        
        // Tính số tiền giảm
        BigDecimal discountAmount = calculateDiscountAmount(discount, orderAmount);
        
        response.put("success", true);
        response.put("message", "Áp dụng mã giảm giá thành công");
        response.put("discountAmount", discountAmount);
        response.put("discountInfo", discount);
        
        return response;
    }
    
    // Tính số tiền giảm
    private BigDecimal calculateDiscountAmount(Discount discount, BigDecimal orderAmount) {
        BigDecimal discountAmount;
        
        if ("percentage".equals(discount.getDiscountType())) {
            // Giảm theo phần trăm
            discountAmount = orderAmount.multiply(
                discount.getDiscountValue().divide(new BigDecimal(100))
            );
        } else {
            // Giảm trực tiếp
            discountAmount = discount.getDiscountValue();
        }
        
        // Kiểm tra giới hạn số tiền giảm tối đa
        if (discount.getMaxDiscountAmount() != null && 
            discountAmount.compareTo(discount.getMaxDiscountAmount()) > 0) {
            discountAmount = discount.getMaxDiscountAmount();
        }
        
        return discountAmount;
    }
    
    // Cập nhật số lần sử dụng mã giảm giá (deprecated - use recordDiscountUsage instead)
    @Deprecated
    public boolean incrementUsedCount(String code) {
        Discount discount = getDiscountByCode(code);
        if (discount != null) {
            discount.setUsedCount(discount.getUsedCount() + 1);
            return updateDiscount(discount);
        }
        return false;
    }

    // Ghi lại việc sử dụng mã giảm giá
    public boolean recordDiscountUsage(int discountId, int userId, String code, BigDecimal discountAmount) {
        DiscountUsage usage = new DiscountUsage(discountId, userId, code, discountAmount);
        return discountUsageDAO.recordUsage(usage);
    }

    // Ghi lại việc sử dụng mã giảm giá với order ID
    public boolean recordDiscountUsage(int discountId, int userId, Integer orderId, String code, BigDecimal discountAmount) {
        DiscountUsage usage = new DiscountUsage(discountId, userId, orderId, code, discountAmount);
        return discountUsageDAO.recordUsage(usage);
    }

    // Lấy số lần sử dụng mã giảm giá từ discount_usage table
    public int getUsageCount(String code) {
        return discountUsageDAO.countUsageByCode(code);
    }

    // Lấy số lần sử dụng mã giảm giá của một user
    public int getUserUsageCount(String code, int userId) {
        return discountUsageDAO.countUsageByCodeAndUser(code, userId);
    }

    // Lấy danh sách sử dụng mã giảm giá
    public List<DiscountUsage> getUsageHistory(String code) {
        return discountUsageDAO.getUsageByCode(code);
    }

    // Lấy mã giảm giá theo code
    public Discount getDiscountByCode(String code) {
        return discountDAO.getDiscountByCode(code);
    }
    
    // Kiểm tra mã giảm giá có đang hoạt động
    public boolean isDiscountActive(Discount discount) {
        if (!discount.isActive()) return false;
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        
        // Kiểm tra thời gian hiệu lực
        if (discount.getStartDate() != null && discount.getStartDate().after(now)) {
            return false;
        }
        if (discount.getEndDate() != null && discount.getEndDate().before(now)) {
            return false;
        }
        
        // Kiểm tra số lần sử dụng
        if (discount.getUsedCount() >= discount.getUsageLimit()) {
            return false;
        }
        
        return true;
    }

    // Lấy tất cả mã giảm giá
    public List<Discount> getAllDiscounts() {
        return discountDAO.getAllDiscounts();
    }

    // Thêm mã giảm giá
    public boolean addDiscount(Discount discount) {
        return discountDAO.addDiscount(discount);
    }

    // Cập nhật mã giảm giá
    public boolean updateDiscount(Discount discount) {
        return discountDAO.updateDiscount(discount);
    }

    // Xoá mã giảm giá
    public boolean deleteDiscount(int id) {
        return discountDAO.deleteDiscount(id);
    }
}
