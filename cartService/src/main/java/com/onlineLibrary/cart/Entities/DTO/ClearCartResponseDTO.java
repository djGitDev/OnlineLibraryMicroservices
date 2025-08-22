package com.onlineLibrary.cart.Entities.DTO;

import java.util.List;

public class ClearCartResponseDTO {
    private String status;
    private String message;
    private List<Object> items; // On peut garder Object si les items sont vides, sinon mettre un type réel

    public ClearCartResponseDTO() {}

    public ClearCartResponseDTO(String status, String message, List<Object> items) {
        this.status = status;
        this.message = message;
        this.items = items;
    }

    // Getters et setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Object> getItems() {
        return items;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }
}