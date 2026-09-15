package com.rbdip.bookstore.product;

import com.rbdip.bookstore.order.CreateOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("product " + id + " not found"));
    }

    public List<Product> getAllProducts(List<CreateOrderRequest.Item> items) {
        List<Product> products = new ArrayList<>();
        for (CreateOrderRequest.Item raw : items) {
            Product product = getProductById(raw.productId());
            products.add(product);
        }
        return products;
    }
}
