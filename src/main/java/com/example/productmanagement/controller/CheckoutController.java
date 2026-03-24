package com.example.productmanagement.controller;

import com.example.productmanagement.model.CartItem;
import com.example.productmanagement.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CheckoutController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Authentication authentication) {
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        String username = authentication.getName();
        orderService.placeOrder(cart, username);

        // Xoa gio hang sau khi dat thanh cong
        session.removeAttribute("cart");

        return "redirect:/checkout/success";
    }

    @GetMapping("/checkout/success")
    public String checkoutSuccess() {
        return "checkout-success";
    }
}
