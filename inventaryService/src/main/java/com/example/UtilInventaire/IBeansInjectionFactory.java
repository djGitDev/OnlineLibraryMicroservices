package com.example.UtilInventaire;

import com.example.Persistance.*;
import com.example.Flux.IAuthorService;
import com.example.Flux.IBookService;
import com.example.Flux.ICategoryService;
import com.example.Flux.IPublisherService;

public interface IBeansInjectionFactory {

    IBookRepository getBookRepository(IBeansInjectionFactory factory);
    IDBConnection getDBConnection();
    IBookService getIBookService();
    IAuthorService getAuthorService(IBeansInjectionFactory factory);
    ICategoryService getCategoryService(IBeansInjectionFactory factory);
    IPublisherService getPublisherService(IBeansInjectionFactory factory);
    IAuthorRepository getAuthorRepository(IBeansInjectionFactory factory);
    ICategoryRepository getCategoryRepository(IBeansInjectionFactory factory);
    IPublisherRepository getPublisherRepository(IBeansInjectionFactory factory);
    ICategoryBookRepository getCategoryBookRepository(IBeansInjectionFactory factory);
    IAuthorBookRepository getAuthorBookRepository(IBeansInjectionFactory factory);
}
