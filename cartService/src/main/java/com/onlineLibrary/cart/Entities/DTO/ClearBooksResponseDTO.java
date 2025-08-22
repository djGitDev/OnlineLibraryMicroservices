package com.onlineLibrary.cart.Entities.DTO;

import com.onlineLibrary.cart.Entities.DAO.CartItemDAO;

import java.util.List;

public class ClearBooksResponseDTO {
    private String status;
    private String message;
    private List<CartItemDAO> items;
    private String alert; // facultatif

    public ClearBooksResponseDTO() {}

    public ClearBooksResponseDTO(String status, String message, List<CartItemDAO> items, String alert) {
        this.status = status;
        this.message = message;
        this.items = items;
        this.alert = alert;
    }

    // builder for succes without alert
    public ClearBooksResponseDTO(String status, String message, List<CartItemDAO> items) {
        this(status, message, items, null);
    }

    // builder for error
    public ClearBooksResponseDTO(String status, String message) {
        this(status, message, List.of(), null);
    }

    // Getters et setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<CartItemDAO> getItems() { return items; }
    public void setItems(List<CartItemDAO> items) { this.items = items; }

    public String getAlert() { return alert; }
    public void setAlert(String alert) { this.alert = alert; }
}
