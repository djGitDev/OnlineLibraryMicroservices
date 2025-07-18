package com.example.UtilInventaire;


import com.example.Flux.IAuthorService;
import com.example.Flux.IBookService;
import com.example.Flux.ICategoryService;
import com.example.Flux.IPublisherService;
import com.example.Persistance.*;
import com.example.Flux.Impl.AuthorService;
import com.example.Flux.Impl.BookService;
import com.example.Flux.Impl.CategoryService;
import com.example.Flux.Impl.PublisherService;
import com.example.Persistance.Impl.*;


public class BeansInjectionFactory implements IBeansInjectionFactory{
    private static BeansInjectionFactory instance;

    private BeansInjectionFactory() {
    }

    public static synchronized BeansInjectionFactory getInstance() {
        if (instance == null) {
            instance = new BeansInjectionFactory();
        }
        return instance;
    }


    @Override
    public IBookRepository getBookRepository(IBeansInjectionFactory factory) {
        return new BookRepository(this.getInstance());
    }

    @Override
    public IDBConnection getDBConnection() {
        return new PostgresDBConnection();
    }

    @Override
    public IBookService getIBookService() {
        return new BookService(this.getInstance());
    }

    @Override
    public IAuthorService getAuthorService(IBeansInjectionFactory factory) {
        return new AuthorService(this.getInstance());
    }

    @Override
    public ICategoryService getCategoryService(IBeansInjectionFactory factory) {
        return new CategoryService(this.getInstance());
    }

    @Override
    public IPublisherService getPublisherService(IBeansInjectionFactory factory) {
        return new PublisherService(this.getInstance());
    }

    @Override
    public IAuthorRepository getAuthorRepository(IBeansInjectionFactory factory) {
        return new AuthorRepository(this.getInstance());
    }

    @Override
    public ICategoryRepository getCategoryRepository(IBeansInjectionFactory factory) {
        return new CategoryRepository(this.getInstance());
    }

    @Override
    public IPublisherRepository getPublisherRepository(IBeansInjectionFactory factory) {
        return new PublisherRepository(this.getInstance());
    }

    @Override
    public ICategoryBookRepository getCategoryBookRepository(IBeansInjectionFactory factory) {
        return new CategoryBookRepository(this.getInstance());

    }

    @Override
    public IAuthorBookRepository getAuthorBookRepository(IBeansInjectionFactory factory) {
        return new AuthorBookRepository(this.getInstance());
    }

}
