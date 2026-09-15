package com.rbdip.bookstore.order.service;

import com.rbdip.bookstore.order.*;
import com.rbdip.bookstore.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public void saveOrderItems(Order order, List<Product> products, List<PricingCalculator.LineItem> lineItems) {
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            PricingCalculator.LineItem lineItem = lineItems.get(i);
            OrderItem orderItem = new OrderItem(order.getId(), product.getName(), lineItem.price(), lineItem.quantity());
            orderItemRepository.save(orderItem);
        }
    }

}
