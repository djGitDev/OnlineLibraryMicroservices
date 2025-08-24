package com.onlineLibrary.inventary.Flux;

import com.onlineLibrary.inventary.Entities.DAO.AuthorDAO;
import com.onlineLibrary.inventary.Entities.DTO.AuthorsResponseDTO;

import java.util.Optional;

public interface IAuthorService {
    void generateRelationBookAuthor(String author, int id);
    AuthorsResponseDTO getAuthors();
    AuthorDAO addAuthor(String name);
    void removeRelationsByBookId(int id);
    Optional<AuthorDAO> getAuthorById(int authorId);
}
