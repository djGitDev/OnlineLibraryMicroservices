package com.onlineLibrary.inventary.Flux.Impl;

import com.onlineLibrary.inventary.Entities.DAO.AuthorBookDAO;
import com.onlineLibrary.inventary.Entities.DAO.AuthorDAO;
import com.onlineLibrary.inventary.Flux.IAuthorService;
import com.onlineLibrary.inventary.Persistance.IAuthorBookRepository;
import com.onlineLibrary.inventary.Persistance.IAuthorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public int generateRelationBookAuthor(String author, int idBook) {
        int idAuthor = this.findAuthorByNameElseCreate(author);
        AuthorBookDAO relation = new AuthorBookDAO(idBook,idAuthor);
        relation = authorBookRepository.save(relation);
        return relation.getId();
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
