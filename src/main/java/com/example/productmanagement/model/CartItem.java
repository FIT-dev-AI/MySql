package com.example.productmanagement.model;

import java.io.Serializable;

public class CartItem implements Serializable {

    private Long productId;
    private String productName;
    private Double price;
    private String imageUrl;
    private int quantity;

    public CartItem() {}

    public CartItem(Long productId, String productName, Double price, String imageUrl, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    // Tổng tiền của 1 item
    public Double getSubtotal() {
        return price * quantity;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}