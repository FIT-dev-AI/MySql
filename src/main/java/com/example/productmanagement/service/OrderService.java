package com.example.productmanagement.service;

import com.example.productmanagement.model.*;
import com.example.productmanagement.repository.OrderRepository;
import com.example.productmanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order placeOrder(List<CartItem> cartItems, String username) {
        // Tạo Order
        Order order = new Order();
        order.setAccountUsername(username);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        // Tính tổng tiền
        double total = cartItems.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
        order.setTotalAmount(total);

        // Tạo danh sách OrderDetail
        List<OrderDetail> details = new ArrayList<>();
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found")));
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getPrice());
            details.add(detail);
        }

        order.setOrderDetails(details);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUsername(String username) {
        return orderRepository.findByAccountUsernameOrderByOrderDateDesc(username);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}