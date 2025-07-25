package com.onlineLibrary.inventary.Flux.Impl;

import com.onlineLibrary.inventary.Flux.IAuthorService;
import com.onlineLibrary.inventary.Persistance.IAuthorBookRepository;
import com.onlineLibrary.inventary.Persistance.IAuthorRepository;
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
        int idAuthor = authorRepository.findAuthorByNameElseCreate(author);
        return  authorBookRepository.createRelation(idBook,idAuthor);
    }
}
