package com.onlineLibrary.cart.Entities.DTO;



public class AddBookResponseDTO {
    private String status;
    private String message;
    private int cartId;

    public AddBookResponseDTO(String status, String message, int cartId) {
        this.status = status;
        this.message = message;
        this.cartId = cartId;
    }

    // Getters
    public int getCartId() { return cartId; }
}
