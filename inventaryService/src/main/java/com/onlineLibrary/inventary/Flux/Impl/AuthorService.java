package com.onlineLibrary.inventary.Flux.Impl;

import com.onlineLibrary.inventary.Entities.DAO.AuthorBookDAO;
import com.onlineLibrary.inventary.Entities.DAO.AuthorDAO;
import com.onlineLibrary.inventary.Entities.DTO.AuthorsResponseDTO;
import com.onlineLibrary.inventary.Flux.IAuthorService;
import com.onlineLibrary.inventary.Persistance.IAuthorBookRepository;
import com.onlineLibrary.inventary.Persistance.IAuthorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService implements IAuthorService {

    private IAuthorRepository authorRepository;
    private IAuthorBookRepository authorBookRepository;

    @Autowired
    public AuthorService(IAuthorRepository authorRepository,IAuthorBookRepository authorBookRepository){
        this.authorRepository = authorRepository;
        this.authorBookRepository = authorBookRepository;
    }


    @Override
    public void generateRelationBookAuthor(String author, int idBook) {
        int idAuthor = this.findAuthorByNameElseCreate(author);
        AuthorBookDAO relation = new AuthorBookDAO(idBook,idAuthor);
        authorBookRepository.save(relation);
    }

    @Override
    public AuthorsResponseDTO getAuthors() {
        List<AuthorDAO> authors = authorRepository.findAll();
        return new AuthorsResponseDTO(authors);
    }

    @Override
    public AuthorDAO addAuthor(String name) {
        Optional<AuthorDAO> existing = authorRepository.findByName(name);
        if (existing.isPresent()) {
            return existing.get(); // retourne l'auteur existant
        }
        AuthorDAO newAuthor = new AuthorDAO(name);
        return authorRepository.save(newAuthor); // sauvegarde et retourne le DAO
    }

    @Override
    @Transactional
    public void removeRelationsByBookId(int id) {
        authorBookRepository.deleteAllByBookId(id);
    }

    @Override
    public Optional<AuthorDAO> getAuthorById(int authorId) {
        return authorRepository.findById(authorId);
    }

    @Transactional
    protected int findAuthorByNameElseCreate(String authorName) {
        AuthorDAO author = authorRepository.findByName(authorName).orElse(null);
        if (author != null) {
            return author.getId();
        }
        AuthorDAO newAuthor = new AuthorDAO(authorName);
        return authorRepository.save(newAuthor).getId();
    }
}
