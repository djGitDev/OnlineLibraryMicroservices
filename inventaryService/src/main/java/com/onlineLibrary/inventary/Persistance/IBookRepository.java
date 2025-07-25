package com.onlineLibrary.inventary.Persistance;

import com.onlineLibrary.inventary.Entities.Book;

import java.util.List;

public interface IBookRepository {
     List<Book> getBooks() throws Exception;
     Book getBookById(int id) throws Exception;
     boolean updateBook(int id, Book book) throws Exception;
     Book findBookByIsbn(String isbn) throws Exception;
}
