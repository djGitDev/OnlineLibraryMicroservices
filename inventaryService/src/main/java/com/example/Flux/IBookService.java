package com.example.Flux;

import com.google.gson.JsonObject;

public interface IBookService {

     JsonObject getBooks() throws Exception;
     JsonObject getBookById(int id) throws Exception;
     JsonObject addManyBooks(JsonObject jsonBooks) throws Exception;
     JsonObject decreaseBookQuantity(int id, int quantity) throws Exception;
     JsonObject findBookByIsbn(String isbn) throws Exception;
     JsonObject findBookByTitle(String title) throws Exception;
     JsonObject findUnavailableBooks() throws Exception;
}
