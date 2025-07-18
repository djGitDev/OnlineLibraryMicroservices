package com.example.Flux.Impl;

import com.example.Flux.IAuthorService;
import com.example.Persistance.IAuthorBookRepository;
import com.example.Persistance.IAuthorRepository;
import com.example.UtilInventaire.IBeansInjectionFactory;

public class AuthorService implements IAuthorService {

    private IAuthorRepository authorRepository;
    private IAuthorBookRepository authorBookRepository;

    public AuthorService(IBeansInjectionFactory factory){
        this.authorRepository = factory.getAuthorRepository(factory);
        this.authorBookRepository = factory.getAuthorBookRepository(factory);
    }


    @Override
    public int generateRelationBookAuthor(String author, int idBook) {
        int idAuthor = authorRepository.findAuthorByNameElseCreate(author);
        return  authorBookRepository.createRelation(idBook,idAuthor);
    }
}
