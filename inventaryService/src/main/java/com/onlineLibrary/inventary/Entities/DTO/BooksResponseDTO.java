package com.onlineLibrary.inventary.Entities.DTO;

import com.onlineLibrary.inventary.Entities.DAO.BookDAO;

import java.util.ArrayList;
import java.util.List;

public class BooksResponseDTO {
    private String status;
    private String message;
    private List<BookDAO> books;


    public BooksResponseDTO(String status, String message, List<BookDAO> books) {
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

    public List<BookDAO> getBooks() {
        return books;
    }
}
