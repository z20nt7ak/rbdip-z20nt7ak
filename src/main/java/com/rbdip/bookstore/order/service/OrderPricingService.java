package com.rbdip.bookstore.order.service;

import com.rbdip.bookstore.order.CreateOrderRequest;
import com.rbdip.bookstore.order.PricingCalculator;
import com.rbdip.bookstore.product.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderPricingService {

    private final PricingCalculator pricingCalculator = new PricingCalculator();

    public List<PricingCalculator.LineItem> createLineItem(List<Product> products, List<CreateOrderRequest.Item> items) {
        List<PricingCalculator.LineItem> lineItems = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            CreateOrderRequest.Item raw = items.get(i);
            int quantity = raw.quantity() == null ? 1 : raw.quantity();
            lineItems.add(new PricingCalculator.LineItem(product.getPrice(), quantity));
        }
        return lineItems;
    }

    public BigDecimal calculatePrice(List<PricingCalculator.LineItem> lineItems, String customerType, String couponCode) {
        BigDecimal price = pricingCalculator.calculateOrderTotal(
                lineItems, customerType == null ? "regular" : customerType, couponCode);
        return price;
    }
}
