package com.onlineLibrary.inventary.Entities.DTO;

public class BookQuantityResponseDTO {
    private String status;
    private String message;

    public BookQuantityResponseDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getters
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
