package com.onlineLibrary.inventary.Entities.DTO;


import java.util.ArrayList;
import java.util.List;

public class BooksResponseDTO {
    private String status;
    private String message;
    private List<BookDTO> books;


    public BooksResponseDTO(String status, String message, List<BookDTO> books) {
        this.status = status;
        this.message = message;
        this.books = books;
    }

    public BooksResponseDTO(String status, String message) {
        this(status, message, new ArrayList<>());
    }

    // Getters
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<BookDTO> getBooks() {
        return books;
    }
}
