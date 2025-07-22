package com.onlineLibrary.inventary.Flux.Impl;

import com.onlineLibrary.inventary.Flux.IAuthorService;
import com.onlineLibrary.inventary.Persistance.IAuthorBookRepository;
import com.onlineLibrary.inventary.Persistance.IAuthorRepository;
import com.onlineLibrary.inventary.UtilInventaire.IBeansInjectionFactory;

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
