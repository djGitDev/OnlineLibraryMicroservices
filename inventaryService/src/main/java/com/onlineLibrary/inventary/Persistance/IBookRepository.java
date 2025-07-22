package com.onlineLibrary.inventary.Persistance;

import com.onlineLibrary.inventary.Entities.Book;

import java.util.List;

public interface IBookRepository {
    public List<Book> getBooks() throws Exception;
    public Book getBookById(int id) throws Exception;
    public Book addBook(Book book) throws Exception;
    public List<Book> addManyBooks(List<Book> books) throws Exception;
    public boolean updateBook(int id, Book book) throws Exception;
    public Book findBookByIsbn(String isbn) throws Exception;
    public List<Book> findBookByTitle(String title) throws Exception;
    public List<Book> findUnavailableBooks() throws Exception;
}
