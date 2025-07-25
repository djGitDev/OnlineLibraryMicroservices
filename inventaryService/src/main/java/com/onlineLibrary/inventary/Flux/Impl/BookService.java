package com.onlineLibrary.inventary.Flux.Impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onlineLibrary.inventary.Entities.Book;
import com.onlineLibrary.inventary.Flux.IAuthorService;
import com.onlineLibrary.inventary.Flux.IBookService;
import com.onlineLibrary.inventary.Flux.ICategoryService;
import com.onlineLibrary.inventary.Flux.IPublisherService;
import com.onlineLibrary.inventary.Persistance.IBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class BookService implements IBookService {


    private  IBookRepository bookRepository;
    private  IAuthorService authorService;
    private  ICategoryService categoryService;
    private  IPublisherService publisherService;

    @Autowired
    public BookService(IBookRepository bookRepository,IAuthorService authorService,ICategoryService categoryService,IPublisherService publisherService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.publisherService = publisherService;
    }

    @Override
    public JsonObject getBooks() throws Exception {
        List<Book> books = bookRepository.getBooks();
       return buildBooksResponse(books);
    }

    @Override
    public JsonObject getBookById(int id) throws Exception {
        Book book = bookRepository.getBookById(id);
        return buildBookResponse(book);
    }


    @Override
    public JsonObject decreaseBookQuantity(int id, int quantity) throws Exception {
        Book book = bookRepository.getBookById(id);
        if(book == null){
            JsonObject response = new JsonObject();
            response.addProperty("status", "NOT FOUND");
            response.addProperty("message", "Book not found");
            return response;
        }
        int newQuantity = book.getQuantity() - quantity;
        if(newQuantity < 0){
            JsonObject response = new JsonObject();
            response.addProperty("status", "FAILED");
            response.addProperty("message", "Not enough books in stock");
            return response;
        }
        if(updateBookQuantity(book, newQuantity)){
            JsonObject response = new JsonObject();
            response.addProperty("status", "OK");
            response.addProperty("message", "Book quantity updated");
            return response;
        }else{
            JsonObject response = new JsonObject();
            response.addProperty("status", "FAILED");
            response.addProperty("message", "Failed to update book quantity");
            return response;
        }
    }

    @Override
    public JsonObject findBookByIsbn(String isbn) throws Exception {
        Book book = bookRepository.findBookByIsbn(isbn);
       return buildBookResponse(book);
    }


    /**
     * Convertit une liste de livre en json
     * @param books la liste de livre
     * @return un jsonObject contenant un jsonArray de livre
     */
    private JsonObject buildBooksResponse(List<Book> books) {
        JsonObject response = new JsonObject();
        JsonArray booksArray = new JsonArray();
        if(books.isEmpty()){
            response.addProperty("status", "NOT FOUND");
            return response;
        }
        response.addProperty("status", "OK");
        Gson gson = new Gson();
        for (Book book : books) {
            booksArray.add(gson.toJsonTree(book));
        }
        response.add("books", booksArray);
        return response;
    }

    /**
     * Convertit un objet livre en json
     * @param book le livre à convertir
     * @return un jsonObject contenant les informations du livre
     */
    private JsonObject buildBookResponse(Book book){
        JsonObject response = new JsonObject();
        Gson gson = new Gson();

        if (book != null) {
            response.addProperty("status", "OK");
            response.add("book", gson.toJsonTree(book));
        } else {
            response.addProperty("status", "NOT_FOUND");
        }

        return response;
    }

    /**
     * Met à jour la quantité en stock d'un livre
     * @param book le livre à mettre à jour
     * @param newQuantity la nouvelle quantité en stock
     * @return true en cas de succès false sinon
     * @throws Exception
     */
    private boolean updateBookQuantity(Book book, int newQuantity) throws Exception {
        book.setQuantity(newQuantity);
        return bookRepository.updateBook(book.getId(), book);
    }



}
