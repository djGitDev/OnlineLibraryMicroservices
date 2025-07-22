package com.onlineLibrary.inventary.Flux.Impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.onlineLibrary.inventary.Entities.Book;
import com.onlineLibrary.inventary.Flux.IAuthorService;
import com.onlineLibrary.inventary.Flux.IBookService;
import com.onlineLibrary.inventary.Flux.ICategoryService;
import com.onlineLibrary.inventary.Flux.IPublisherService;
import com.onlineLibrary.inventary.Persistance.IBookRepository;
import com.onlineLibrary.inventary.UtilInventaire.IBeansInjectionFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookService implements IBookService {


    private  IBookRepository bookRepository;
    private  IAuthorService authorService;
    private  ICategoryService categoryService;
    private  IPublisherService publisherService;

    public BookService(IBeansInjectionFactory factory) {
        this.bookRepository = factory.getBookRepository(factory);
        this.authorService = factory.getAuthorService(factory);
        this.categoryService = factory.getCategoryService(factory);
        this.publisherService = factory.getPublisherService(factory);
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
    public JsonObject addManyBooks(JsonObject jsonBooks) throws Exception {
        List<Book> books = extractBooksFromJson(jsonBooks);
        if(books == null || books.isEmpty()){
            JsonObject response = new JsonObject();
            response.addProperty("status", "FAILED");
            return response;
        }
        return buildBooksResponse(books);
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

    @Override
    public JsonObject findBookByTitle(String title) throws Exception {
        List<Book> books = bookRepository.findBookByTitle(title);
       return buildBooksResponse(books);
    }


    @Override
    public JsonObject findUnavailableBooks() throws Exception {
        List<Book> books = bookRepository.findUnavailableBooks();
       return buildBooksResponse(books);
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


    // Utilitaire pour extraire un livre d'un jsonObject
    private Book extractBookDataFromJson(JsonObject bookElement) {
        int id = bookElement.get("id").getAsInt();
        String isbn = bookElement.get("isbn").getAsString();
        String title = bookElement.get("title").getAsString();
        String description = bookElement.get("description").getAsString();
        String date = bookElement.get("parutionDate").getAsString();
        Date parutionDate;
        double price = bookElement.get("price").getAsDouble();
        int quantity = bookElement.get("quantity").getAsInt();
        String publisher = bookElement.get("publisher").getAsString();
        int publisherId = publisherService.getPublisherByName(publisher);
        try {
            parutionDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (java.text.ParseException e) {
            throw new RuntimeException("Date de parution invalide: " + date, e);
        }

        return new Book(id, isbn, title, description, parutionDate, price, quantity,publisherId);
    }


    // Utilitaire pour extraire une liste de livres d'un jsonObject
    private  List<Book> extractBooksFromJson(JsonObject jsonObject) throws Exception {
        JsonArray booksArray = jsonObject.getAsJsonArray("books");
        List<Book> books = new ArrayList<>();
        for (JsonElement bookElement : booksArray) {
            JsonObject bookJson = bookElement.getAsJsonObject();
            Book book = extractBookDataFromJson(bookJson);
            books.add(book);
            bookRepository.addBook(book);
            int id = bookJson.get("id").getAsInt();
            String category = bookJson.get("category").getAsString();
            String author = bookJson.get("author").getAsString();
            int relationCBId = categoryService.generateRetlationBookCategorie(category,id);
            int relationABId = authorService.generateRelationBookAuthor(author,id);
        }
        return books;
    }
}
