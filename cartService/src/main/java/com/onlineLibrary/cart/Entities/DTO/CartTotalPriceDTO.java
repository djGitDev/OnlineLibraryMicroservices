package com.onlineLibrary.cart.Entities.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartTotalPriceDTO {

    @JsonProperty("cart_id")
    private int cartId;
    @JsonProperty("total_price")
    private Double totalPrice;
    private String status;

    public CartTotalPriceDTO() {}

    public CartTotalPriceDTO(int cartId, Double totalPrice, String status) {
        this.cartId = cartId;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Getters et setters
    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
