package com.onlineLibrary.cart.Entities.DTO;

import com.onlineLibrary.cart.Entities.DAO.CartItemDAO;

import java.util.List;




public class CartItemsResponseDTO {
    private String status;
    private int cartId;
    private List<CartItemDAO> items;

    public CartItemsResponseDTO() {}

    public CartItemsResponseDTO(String status, int cartId, List<CartItemDAO> items) {
        this.status = status;
        this.cartId = cartId;
        this.items = items;
    }

    // getters et setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public List<CartItemDAO> getItems() { return items; }
    public void setItems(List<CartItemDAO> items) { this.items = items; }
}
