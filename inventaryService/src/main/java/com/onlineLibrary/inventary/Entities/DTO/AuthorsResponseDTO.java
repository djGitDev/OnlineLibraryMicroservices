package com.onlineLibrary.inventary.Entities.DTO;

import com.onlineLibrary.inventary.Entities.DAO.AuthorDAO;
import java.util.List;

public class AuthorsResponseDTO {
    private List<AuthorDAO> authors;

    public AuthorsResponseDTO(List<AuthorDAO> authors) {
        this.authors = authors;
    }

    public List<AuthorDAO> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorDAO> authors) {
        this.authors = authors;
    }
}