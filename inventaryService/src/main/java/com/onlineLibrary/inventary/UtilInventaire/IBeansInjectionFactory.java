package com.onlineLibrary.inventary.UtilInventaire;

import com.onlineLibrary.inventary.Flux.IAuthorService;
import com.onlineLibrary.inventary.Flux.IBookService;
import com.onlineLibrary.inventary.Flux.ICategoryService;
import com.onlineLibrary.inventary.Flux.IPublisherService;
import com.onlineLibrary.inventary.Persistance.*;

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
