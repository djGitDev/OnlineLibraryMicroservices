package com.onlineLibrary.cart.Entities.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartDTO {

    @JsonProperty("cartId")
    private int cartId;

    @JsonProperty("userId")
    private int userId;

    public CartDTO() {
    }

    public CartDTO(int cartId, int userId) {
        this.cartId = cartId;
        this.userId = userId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}