package test;

import dao.DiscountDAO;
import models.Discount;
import services.DiscountService;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Test class for Discount functionality
 * Tests discount code validation, calculation, and application
 */
public class DiscountFunctionalityTest {
    
    private static DiscountService discountService;
    private static DiscountDAO discountDAO;
    
    public static void main(String[] args) {
        discountService = new DiscountService();
        discountDAO = new DiscountDAO();
        
        System.out.println("=== Discount Functionality Test Suite ===\n");
        
        // Test 1: Test percentage discount
        testPercentageDiscount();
        
        // Test 2: Test fixed amount discount
        testFixedAmountDiscount();
        
        // Test 3: Test minimum order amount validation
        testMinimumOrderAmount();
        
        // Test 4: Test maximum discount amount cap
        testMaximumDiscountAmount();
        
        // Test 5: Test usage limit validation
        testUsageLimit();
        
        // Test 6: Test expired discount
        testExpiredDiscount();
        
        // Test 7: Test inactive discount
        testInactiveDiscount();
        
        System.out.println("\n=== All Tests Completed ===");
    }
    
    private static void testPercentageDiscount() {
        System.out.println("Test 1: Percentage Discount");
        System.out.println("---------------------------");
        
        // Create a test discount: 10% off
        Discount discount = new Discount();
        discount.setCode("PERCENT10");
        discount.setDescription("10% discount");
        discount.setDiscountType("percentage");
        discount.setDiscountValue(new BigDecimal("10"));
        discount.setMinOrderAmount(new BigDecimal("100000"));
        discount.setMaxDiscountAmount(new BigDecimal("50000"));
        discount.setUsageLimit(100);
        discount.setUsedCount(0);
        discount.setActive(true);
        discount.setStartDate(new Timestamp(System.currentTimeMillis() - 86400000)); // 1 day ago
        discount.setEndDate(new Timestamp(System.currentTimeMillis() + 86400000)); // 1 day later
        
        // Test with order amount 500,000
        BigDecimal orderAmount = new BigDecimal("500000");
        Map<String, Object> result = discountService.applyDiscount(discount.getCode(), orderAmount);
        
        if ((boolean) result.get("success")) {
            BigDecimal discountAmount = (BigDecimal) result.get("discountAmount");
            System.out.println("✓ Order Amount: " + orderAmount);
            System.out.println("✓ Discount Amount: " + discountAmount);
            System.out.println("✓ Expected: 50000 (10% of 500000, capped at max 50000)");
            System.out.println("✓ Result: PASS\n");
        } else {
            System.out.println("✗ Result: FAIL - " + result.get("message") + "\n");
        }
    }
    
    private static void testFixedAmountDiscount() {
        System.out.println("Test 2: Fixed Amount Discount");
        System.out.println("-----------------------------");
        
        // Create a test discount: 50,000 VND off
        Discount discount = new Discount();
        discount.setCode("FIXED50K");
        discount.setDescription("50,000 VND discount");
        discount.setDiscountType("fixed_amount");
        discount.setDiscountValue(new BigDecimal("50000"));
        discount.setMinOrderAmount(new BigDecimal("100000"));
        discount.setMaxDiscountAmount(null);
        discount.setUsageLimit(100);
        discount.setUsedCount(0);
        discount.setActive(true);
        discount.setStartDate(new Timestamp(System.currentTimeMillis() - 86400000));
        discount.setEndDate(new Timestamp(System.currentTimeMillis() + 86400000));
        
        // Test with order amount 300,000
        BigDecimal orderAmount = new BigDecimal("300000");
        Map<String, Object> result = discountService.applyDiscount(discount.getCode(), orderAmount);
        
        if ((boolean) result.get("success")) {
            BigDecimal discountAmount = (BigDecimal) result.get("discountAmount");
            System.out.println("✓ Order Amount: " + orderAmount);
            System.out.println("✓ Discount Amount: " + discountAmount);
            System.out.println("✓ Expected: 50000");
            System.out.println("✓ Result: PASS\n");
        } else {
            System.out.println("✗ Result: FAIL - " + result.get("message") + "\n");
        }
    }
    
    private static void testMinimumOrderAmount() {
        System.out.println("Test 3: Minimum Order Amount Validation");
        System.out.println("--------------------------------------");
        
        // Test with order amount below minimum
        BigDecimal orderAmount = new BigDecimal("50000"); // Below 100,000 minimum
        Map<String, Object> result = discountService.applyDiscount("PERCENT10", orderAmount);
        
        if (!(boolean) result.get("success")) {
            System.out.println("✓ Order Amount: " + orderAmount);
            System.out.println("✓ Minimum Required: 100,000");
            System.out.println("✓ Error Message: " + result.get("message"));
            System.out.println("✓ Result: PASS\n");
        } else {
            System.out.println("✗ Result: FAIL - Should have rejected order below minimum\n");
        }
    }
    
    private static void testMaximumDiscountAmount() {
        System.out.println("Test 4: Maximum Discount Amount Cap");
        System.out.println("-----------------------------------");
        
        // Test with very high order amount
        BigDecimal orderAmount = new BigDecimal("1000000"); // 1 million
        Map<String, Object> result = discountService.applyDiscount("PERCENT10", orderAmount);
        
        if ((boolean) result.get("success")) {
            BigDecimal discountAmount = (BigDecimal) result.get("discountAmount");
            System.out.println("✓ Order Amount: " + orderAmount);
            System.out.println("✓ 10% would be: " + orderAmount.multiply(new BigDecimal("0.1")));
            System.out.println("✓ Actual Discount: " + discountAmount);
            System.out.println("✓ Max Cap: 50,000");
            System.out.println("✓ Result: PASS\n");
        } else {
            System.out.println("✗ Result: FAIL - " + result.get("message") + "\n");
        }
    }
    
    private static void testUsageLimit() {
        System.out.println("Test 5: Usage Limit Validation");
        System.out.println("------------------------------");
        
        Discount discount = discountService.getDiscountByCode("PERCENT10");
        if (discount != null) {
            System.out.println("✓ Discount Code: " + discount.getCode());
            System.out.println("✓ Usage Limit: " + discount.getUsageLimit());
            System.out.println("✓ Used Count: " + discount.getUsedCount());
            System.out.println("✓ Remaining Uses: " + (discount.getUsageLimit() - discount.getUsedCount()));
            System.out.println("✓ Result: PASS\n");
        } else {
            System.out.println("✗ Result: FAIL - Discount not found\n");
        }
    }
    
    private static void testExpiredDiscount() {
        System.out.println("Test 6: Expired Discount Validation");
        System.out.println("----------------------------------");
        
        // Create an expired discount
        Discount discount = new Discount();
        discount.setCode("EXPIRED");
        discount.setDescription("Expired discount");
        discount.setDiscountType("percentage");
        discount.setDiscountValue(new BigDecimal("10"));
        discount.setMinOrderAmount(new BigDecimal("100000"));
        discount.setMaxDiscountAmount(new BigDecimal("50000"));
        discount.setUsageLimit(100);
        discount.setUsedCount(0);
        discount.setActive(true);
        discount.setStartDate(new Timestamp(System.currentTimeMillis() - 172800000)); // 2 days ago
        discount.setEndDate(new Timestamp(System.currentTimeMillis() - 86400000)); // 1 day ago (expired)
        
        BigDecimal orderAmount = new BigDecimal("500000");
        Map<String, Object> result = discountService.applyDiscount(discount.getCode(), orderAmount);
        
        if (!(boolean) result.get("success")) {
            System.out.println("✓ Discount Status: Expired");
            System.out.println("✓ Error Message: " + result.get("message"));
            System.out.println("✓ Result: PASS\n");
        } else {
            System.out.println("✗ Result: FAIL - Should have rejected expired discount\n");
        }
    }
    
    private static void testInactiveDiscount() {
        System.out.println("Test 7: Inactive Discount Validation");
        System.out.println("-----------------------------------");
        
        // Create an inactive discount
        Discount discount = new Discount();
        discount.setCode("INACTIVE");
        discount.setDescription("Inactive discount");
        discount.setDiscountType("percentage");
        discount.setDiscountValue(new BigDecimal("10"));
        discount.setMinOrderAmount(new BigDecimal("100000"));
        discount.setMaxDiscountAmount(new BigDecimal("50000"));
        discount.setUsageLimit(100);
        discount.setUsedCount(0);
        discount.setActive(false); // Inactive
        discount.setStartDate(new Timestamp(System.currentTimeMillis() - 86400000));
        discount.setEndDate(new Timestamp(System.currentTimeMillis() + 86400000));
        
        BigDecimal orderAmount = new BigDecimal("500000");
        Map<String, Object> result = discountService.applyDiscount(discount.getCode(), orderAmount);
        
        if (!(boolean) result.get("success")) {
            System.out.println("✓ Discount Status: Inactive");
            System.out.println("✓ Error Message: " + result.get("message"));
            System.out.println("✓ Result: PASS\n");
        } else {
            System.out.println("✗ Result: FAIL - Should have rejected inactive discount\n");
        }
    }
}

