package com.onlineLibrary.inventary.Flux.Impl;


import com.onlineLibrary.inventary.Entities.DAO.*;
import com.onlineLibrary.inventary.Entities.DTO.BookDTO;
import com.onlineLibrary.inventary.Entities.DTO.BookQuantityResponseDTO;
import com.onlineLibrary.inventary.Entities.DTO.BookResponseDTO;
import com.onlineLibrary.inventary.Entities.DTO.BooksResponseDTO;
import com.onlineLibrary.inventary.Flux.IAuthorService;
import com.onlineLibrary.inventary.Flux.IBookService;
import com.onlineLibrary.inventary.Flux.ICategoryService;
import com.onlineLibrary.inventary.Flux.IPublisherService;
import com.onlineLibrary.inventary.Persistance.IAuthorBookRepository;
import com.onlineLibrary.inventary.Persistance.IBookRepository;
import com.onlineLibrary.inventary.Persistance.ICategoryBookRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService implements IBookService {


    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final IPublisherService publisherService;
    private final ICategoryService categoryService;
    private final IAuthorService authorService;
    private final IBookRepository bookRepository;

    private final ICategoryBookRepository categoryBookRepository;
    private final IAuthorBookRepository authorBookRepository;


    @Autowired
    public BookService(
            IPublisherService publisherService,
            ICategoryService categoryService,
            IAuthorService authorService,
            IBookRepository bookRepository,
            ICategoryBookRepository categoryBookRepository,
            IAuthorBookRepository authorBookRepository) {
        this.publisherService = publisherService;
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookRepository = bookRepository;
        this.categoryBookRepository = categoryBookRepository;
        this.authorBookRepository = authorBookRepository;

    }

//    @Override
//    public BooksResponseDTO getBooks() throws Exception {
//        List<BookDAO> books = bookRepository.findAll();
//       return buildBooksResponse(books);
//    }

//    @Override
//    public BooksResponseDTO getBooks() throws Exception {
//        List<BookDAO> books = bookRepository.findAll();
//
//        List<BookDTO> bookDTOs = new ArrayList<>();
//
//        for (BookDAO book : books) {
//            // Récupérer les categories liées à ce livre
//            List<BookCategoryDAO> bookCategories = categoryBookRepository.findAllByBookId(book.getId());
//            List<String> categoryNames = bookCategories.stream()
//                    .map(bc -> {
//                        CategoryDAO cat = categoryService.getCategoryById(bc.getCategoryId()).orElse(null);
//                        return (cat != null) ? cat.getName() : null;
//                    })
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.toList());
//
//            // Récupérer les auteurs liés à ce livre
//            List<AuthorBookDAO> authorBooks = authorBookRepository.findAllByBookId(book.getId());
//            List<String> authorNames = authorBooks.stream()
//                    .map(ab -> {
//                        AuthorDAO author = authorService.getAuthorById(ab.getAuthorId()).orElse(null);
//                        return (author != null) ? author.getName() : null;
//                    })
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.toList());
//
//            // Construire le DTO pour ce livre
//            BookDTO dto = new BookDTO();
//            dto.setTitle(book.getTitle());
//            dto.setIsbn(book.getIsbn());
//            dto.setDescription(book.getDescription());
//            dto.setPrice(book.getPrice());
//            dto.setQuantity(book.getQuantity());
//            dto.setPublisherId(book.getPublisherId());
//            dto.setAuthorNames(authorNames);
//            dto.setCategoryNames(categoryNames);
//            logger.info("DTO: {}", dto);
//            bookDTOs.add(dto);
//        }
//
//        return  buildBooksResponse(bookDTOs);
//    }
@Override
public BooksResponseDTO getBooks() throws Exception {

    List<BookDAO> books = bookRepository.findAll();

    List<BookDTO> bookDTOs = new ArrayList<>();

    for (BookDAO book : books) {

        // Récupérer l'éditeur lié à ce livre
        PublisherDAO publisher = publisherService.getPublisherById(book.getPublisherId()).orElse(null);
        String publisherName = (publisher != null) ? publisher.getName() : null;

        // Récupérer les catégories liées à ce livre
        List<BookCategoryDAO> bookCategories = categoryBookRepository.findAllByBookId(book.getId());

        List<String> categoryNames = bookCategories.stream()
                .map(bc -> {
                    CategoryDAO cat = categoryService.getCategoryById(bc.getCategoryId()).orElse(null);
                    if (cat == null) {
                        logger.warn("Catégorie introuvable pour categoryId={}", bc.getCategoryId());
                    }
                    return (cat != null) ? cat.getName() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        logger.info("Catégories récupérées pour le livre id={} : {}", book.getId(), categoryNames);

        // Récupérer les auteurs liés à ce livre
        List<AuthorBookDAO> authorBooks = authorBookRepository.findAllByBookId(book.getId());

        List<String> authorNames = authorBooks.stream()
                .map(ab -> {
                    AuthorDAO author = authorService.getAuthorById(ab.getAuthorId()).orElse(null);
                    if (author == null) {
                        logger.warn("Auteur introuvable pour authorId={}", ab.getAuthorId());
                    }
                    return (author != null) ? author.getName() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        logger.info("Auteurs récupérés pour le livre id={} : {}", book.getId(), authorNames);

        // Construire le DTO pour ce livre
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setDescription(book.getDescription());
        dto.setPrice(book.getPrice());
        dto.setQuantity(book.getQuantity());
        dto.setPublisherName(publisherName );
        dto.setAuthorNames(authorNames);
        dto.setCategoryNames(categoryNames);

        logger.info("DTO construit pour le livre: {}", dto);

        bookDTOs.add(dto);
    }

    logger.info(">>> Fin de getBooks() - Nombre de DTOs construits : {}", bookDTOs.size());
    return buildBooksResponse(bookDTOs);
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
    @Transactional
    public BookResponseDTO addBook(BookDTO body) {
        logger.info("Adding new book: {}", body.toString());
        BookDAO book = new BookDAO(
                body.getIsbn(),
                body.getTitle(),
                body.getDescription(),
                body.getParutionDate(),
                body.getPrice(),
                body.getQuantity(),
                body.getPublisherId()
        );
        book = bookRepository.save(book);
        int bookId = book.getId();
        if (body.getCategoryNames() != null) {
            for (String catName : body.getCategoryNames()) {
              categoryService.generateRetlationBookCategorie(catName, bookId);
            }
        }
        if (body.getAuthorNames() != null) {
            for (String authorName : body.getAuthorNames()) {
                authorService.generateRelationBookAuthor(authorName, bookId);
            }
        }
        BookResponseDTO response = new BookResponseDTO("Book added successfully",book);
        return response;
    }

    @Override
    @Transactional
    public boolean removeBook(int id) {
        Optional<BookDAO> bookOpt = bookRepository.findById(id);

        if (!bookOpt.isPresent()) {
            return false;
        }

        categoryService.removeRelationsByBookId(id);
        authorService.removeRelationsByBookId(id);

        bookRepository.deleteById(id);

        return true;
    }

    @Override
    public BookResponseDTO findBookByIsbn(String isbn) throws Exception {
        Optional<BookDAO> book = bookRepository.findByIsbn(isbn);
        return book
                .map(this::buildBookResponse)
                .orElseGet(() -> new BookResponseDTO("NOT_FOUND", "Book not found for ISBN: " + isbn));
    }



    private BooksResponseDTO buildBooksResponse(List<BookDTO> books) {
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
