package com.example.productmanagement.service;

import com.example.productmanagement.model.Category;
import com.example.productmanagement.model.Product;
import com.example.productmanagement.repository.CategoryRepository;
import com.example.productmanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Câu 2 + 3 + 4: phân trang + sắp xếp + lọc
    public Page<Product> getProducts(String keyword, Long categoryId, String sortDir, int page) {
        // Câu 3: sắp xếp theo giá
        Sort sort = Sort.by("price");
        if ("desc".equalsIgnoreCase(sortDir)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        // Câu 2: mỗi trang 5 sản phẩm
        Pageable pageable = PageRequest.of(page, 5, sort);

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasCategory = categoryId != null && categoryId > 0;

        if (hasKeyword && hasCategory) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            return productRepository.findByNameContainingIgnoreCaseAndCategory(keyword, category, pageable);
        } else if (hasKeyword) {
            return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else if (hasCategory) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            return productRepository.findByCategory(category, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
}