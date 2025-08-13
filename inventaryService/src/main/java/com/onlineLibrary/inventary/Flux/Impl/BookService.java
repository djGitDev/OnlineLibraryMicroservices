package com.onlineLibrary.inventary.Flux.Impl;


import com.onlineLibrary.inventary.Entities.DAO.BookDAO;
import com.onlineLibrary.inventary.Entities.DTO.BookQuantityResponseDTO;
import com.onlineLibrary.inventary.Entities.DTO.BookResponseDTO;
import com.onlineLibrary.inventary.Entities.DTO.BooksResponseDTO;
import com.onlineLibrary.inventary.Flux.IBookService;
import com.onlineLibrary.inventary.Persistance.IBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookService implements IBookService {


    private  IBookRepository bookRepository;


    @Autowired
    public BookService(IBookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public BooksResponseDTO getBooks() throws Exception {
        List<BookDAO> books = bookRepository.findAll();
       return buildBooksResponse(books);
    }

    @Override
    public BookResponseDTO getBookById(int id) throws Exception {
        Optional<BookDAO> book = bookRepository.findById(id);
        return book
                .map(this::buildBookResponse)
                .orElseGet(() -> new BookResponseDTO("NOT_FOUND", "Book not found"));
    }


    @Override
    public BookQuantityResponseDTO decreaseBookQuantity(int id, int quantity) throws Exception {
        Optional<BookDAO> bookOpt = bookRepository.findById(id);

        if (!bookOpt.isPresent()) {
            return new BookQuantityResponseDTO("NOT_FOUND", "Book not found");
        }

        BookDAO bookDAO = bookOpt.get();
        int newQuantity = bookDAO.getQuantity() - quantity;

        if (newQuantity < 0) {
            return new BookQuantityResponseDTO("FAILED", "Not enough books in stock");
        }

        updateBookQuantity(bookDAO, newQuantity);

        return new BookQuantityResponseDTO("OK", "Book quantity updated");
    }

    @Override
    public BookResponseDTO findBookByIsbn(String isbn) throws Exception {
        Optional<BookDAO> book = bookRepository.findByIsbn(isbn);
        return book
                .map(this::buildBookResponse)
                .orElseGet(() -> new BookResponseDTO("NOT_FOUND", "Book not found for ISBN: " + isbn));
    }



    private BooksResponseDTO buildBooksResponse(List<BookDAO> books) {
        if (books.isEmpty()) {
            return new BooksResponseDTO("NOT_FOUND", "No books found");
        }
        return new BooksResponseDTO("OK", "Books retrieved successfully", books);
    }


    private BookResponseDTO buildBookResponse(BookDAO book){
        if (book != null) {
            return new BookResponseDTO("OK", book);
        } else {
            return new BookResponseDTO("NOT_FOUND", "Book not found");
        }
    }

    private BookDAO updateBookQuantity(BookDAO book, int newQuantity) throws Exception {
        book.setQuantity(newQuantity);
        return bookRepository.save(book);
    }



}
