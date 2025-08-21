package com.onlineLibrary.inventary.Flux;

import com.onlineLibrary.inventary.Entities.DAO.AuthorDAO;
import com.onlineLibrary.inventary.Entities.DTO.AuthorsResponseDTO;

public interface IAuthorService {
    int generateRelationBookAuthor(String author, int id);
    AuthorsResponseDTO getAuthors();

    AuthorDAO addAuthor(String name);
}
