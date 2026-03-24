package com.example.productmanagement.repository;

import com.example.productmanagement.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByAccountUsernameOrderByOrderDateDesc(String username);
}