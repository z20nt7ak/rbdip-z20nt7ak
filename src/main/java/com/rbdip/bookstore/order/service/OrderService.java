package com.rbdip.bookstore.order.service;

import com.rbdip.bookstore.order.*;
import com.rbdip.bookstore.product.Product;
import java.math.BigDecimal;
import java.util.List;
import com.rbdip.bookstore.product.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * God-класс: валидация, расчёт цены, персистентность и "уведомление
 * клиента" смешаны в одном методе. Цель для рефакторинга по SRP в ЛР1.
 */
@Service
public class OrderService {

    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final OrderPricingService orderPricingService;
    private final NotificationService notificationService;

    public OrderService(
            ProductService productService,
            OrderRepository orderRepository,
            OrderItemService orderItemService,
            OrderPricingService orderPricingService,
            NotificationService notificationService) {
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.orderPricingService = orderPricingService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        List<Product> products = productService.getAllProducts(request.items());
        List<PricingCalculator.LineItem> lineItems = orderPricingService.createLineItem(products, request.items());
        BigDecimal total = orderPricingService.calculatePrice(lineItems, request.customerType(), request.couponCode());
        Order order = new Order(request.customerFullName(), request.customerAddress(), request.customerPhone(), "new");
        order = orderRepository.save(order);
        orderItemService.saveOrderItems(order, products, lineItems);
        notificationService.sendConfirmationEmail(request.customerFullName(), order.getId(), total);
        return order;
    }

}
