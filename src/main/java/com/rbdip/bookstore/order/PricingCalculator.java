package com.rbdip.bookstore.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Модуль расчёта цены заказа. Намеренно почти не покрыт тестами и
 * содержит magic numbers / нечитаемые ветвления скидок - цель для
 * характеризационных тестов (ЛР2) и mutation-testing гейта PIT (ЛР5).
 */
public class PricingCalculator {

    private static final int MIN_QUANTITY_FOR_DISCOUNT  = 10;
    private static final BigDecimal QUANTITY_DISCOUNT = BigDecimal.valueOf(0.95);
    private static final BigDecimal VIP_DISCOUNT = BigDecimal.valueOf(0.9);
    private static final BigDecimal WHOLESALE_DISCOUNT = BigDecimal.valueOf(0.85);
    private static final BigDecimal SAVE_10_COUPON_DISCOUNT = BigDecimal.TEN;
    private static final BigDecimal SAVE_20_COUPON_DISCOUNT = BigDecimal.valueOf(0.8);
    private static final BigDecimal LARGE_ORDER_PRICE_THRESHOLD = BigDecimal.valueOf(1000);
    private static final BigDecimal LARGE_ORDER_PRICE_DISCOUNT = BigDecimal.valueOf(0.98);
    private static final String VIP_CUSTOMER = "vip";
    private static final String WHOLESALE_CUSTOMER = "wholesale";
    private static final String SAVE_10_COUPON = "SAVE10";
    private static final String SAVE_20_COUPON = "SAVE20PERCENT";

    public record LineItem(BigDecimal price, int quantity) {
    }

    public BigDecimal calculateOrderTotal(List<LineItem> items, String customerType, String couponCode) {
        BigDecimal total = calculateItemsTotal(items);
        total = applyCustomerDiscount(total, customerType);
        total = applyCouponDiscount(total, couponCode);
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }
        if (total.compareTo(LARGE_ORDER_PRICE_THRESHOLD) > 0) {
            total = total.multiply(LARGE_ORDER_PRICE_DISCOUNT);
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateItemsTotal(List<LineItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (LineItem item : items) {
            BigDecimal linePrice = item.price().multiply(BigDecimal.valueOf(item.quantity()));
            if (item.quantity() > MIN_QUANTITY_FOR_DISCOUNT) {
                linePrice = linePrice.multiply(QUANTITY_DISCOUNT);
            }
            total = total.add(linePrice);
        }
        return total;
    }

    private BigDecimal applyCustomerDiscount(BigDecimal total, String customerType) {
        if (VIP_CUSTOMER.equals(customerType)) {
            total = total.multiply(VIP_DISCOUNT);
        } else if (WHOLESALE_CUSTOMER.equals(customerType)) {
            total = total.multiply(WHOLESALE_DISCOUNT);
        }
        return total;
    }

    private BigDecimal applyCouponDiscount(BigDecimal total, String couponCode) {
        if (SAVE_10_COUPON.equals(couponCode)) {
            total = total.subtract(SAVE_10_COUPON_DISCOUNT);
        } else if (SAVE_20_COUPON.equals(couponCode)) {
            total = total.multiply(SAVE_20_COUPON_DISCOUNT);
        }
        return total;
    }
}
