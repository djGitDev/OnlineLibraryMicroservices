package com.onlineLibrary.cart.Entities.DTO;

import com.onlineLibrary.cart.Entities.DAO.CartItemDAO;

import java.util.List;

public class AddBooksResponseDTO {
    private String status;
    private String message;
    private int cartId;
    private List<CartItemDAO> items;

    public AddBooksResponseDTO(String status, String message, int cartId, List<CartItemDAO> items) {
        this.status = status;
        this.message = message;
        this.cartId = cartId;
        this.items = items;
    }

    // Getters
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public int getCartId() { return cartId; }
    public List<CartItemDAO> getItems() { return items; }
}
