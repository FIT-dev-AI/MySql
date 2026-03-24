package com.example.productmanagement.repository;

import com.example.productmanagement.model.Category;
import com.example.productmanagement.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Câu 1: tìm theo tên
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    // Câu 4: lọc theo category
    Page<Product> findByCategory(Category category, Pageable pageable);

    // Câu 1 + 4: tìm theo tên VÀ category
    Page<Product> findByNameContainingIgnoreCaseAndCategory(String keyword, Category category, Pageable pageable);

    // Không filter gì (chỉ phân trang)
    Page<Product> findAll(Pageable pageable);
}