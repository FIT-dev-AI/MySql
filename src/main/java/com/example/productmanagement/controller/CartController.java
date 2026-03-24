package com.example.productmanagement.controller;

import com.example.productmanagement.model.CartItem;
import com.example.productmanagement.model.Product;
import com.example.productmanagement.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    // Lấy giỏ hàng từ session, nếu chưa có thì tạo mới
    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    // Câu 5: Thêm sản phẩm vào giỏ
    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session) {

        Product product = productService.getProductById(id);
        List<CartItem> cart = getCart(session);

        // Kiểm tra sản phẩm đã có trong giỏ chưa
        boolean found = false;
        for (CartItem item : cart) {
            if (item.getProductId().equals(id)) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }

        // Nếu chưa có thì thêm mới
        if (!found) {
            cart.add(new CartItem(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                quantity
            ));
        }

        session.setAttribute("cart", cart);
        return "redirect:/products";
    }

    // Câu 6: Xem giỏ hàng
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> cart = getCart(session);

        // Tính tổng tiền
        double total = cart.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();

        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "cart";
    }

    // Xóa 1 sản phẩm khỏi giỏ
    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getProductId().equals(id));
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    // Cập nhật số lượng
    @PostMapping("/update/{id}")
    public String updateQuantity(@PathVariable Long id,
                                 @RequestParam int quantity,
                                 HttpSession session) {
        List<CartItem> cart = getCart(session);
        for (CartItem item : cart) {
            if (item.getProductId().equals(id)) {
                if (quantity <= 0) {
                    cart.remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                break;
            }
        }
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

}