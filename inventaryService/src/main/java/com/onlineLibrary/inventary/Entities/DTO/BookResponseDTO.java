package com.onlineLibrary.inventary.Entities.DTO;

import com.onlineLibrary.inventary.Entities.DAO.BookDAO;

public class BookResponseDTO {

    private String status;
    private String message;
    private BookDAO book;

    public BookResponseDTO(String status, BookDAO book) {
        this.status = status;
        this.book = book;
        this.message = null;
    }

    public BookResponseDTO(String status, String message) {
        this.status = status;
        this.message = message;
        this.book = null;
    }

    // Getters
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public BookDAO getBook() {
        return book;
    }
}