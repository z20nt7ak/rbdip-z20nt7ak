package com.rbdip.bookstore.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CreateOrderRequest(
        @NotBlank(message = "customerFullName is required")
        String customerFullName,
        @NotBlank(message = "customerAddress is required")
        String customerAddress,
        String customerPhone,
        String customerType,
        String couponCode,
        @NotEmpty(message = "order must contain at least one item")
        List<Item> items) {

    public record Item(
            Long productId,
            @Positive(message = "quantity must be positive")
            Integer quantity) {
    }
}
