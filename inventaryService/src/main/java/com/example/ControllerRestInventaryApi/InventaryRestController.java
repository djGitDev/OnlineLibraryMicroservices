package com.example.ControllerRestInventaryApi;

import com.example.UtilInventaire.BeansInjectionFactory;
import com.example.UtilInventaire.IBeansInjectionFactory;
import com.google.gson.JsonObject;
import com.example.Flux.IBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class InventaryRestController {

    private static final Logger logger = LoggerFactory.getLogger(InventaryRestController.class);
    private final IBookService bookService;

    public InventaryRestController() {
        IBeansInjectionFactory factory = BeansInjectionFactory.getInstance();
        this.bookService = factory.getIBookService();
    }

    @PostMapping
    public ResponseEntity<JsonObject> addBooks(@RequestBody JsonObject booksJson) {
        try {
            logger.info("Received request to add books: {}", booksJson);
            JsonObject response = bookService.addManyBooks(booksJson);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error adding books", e);
            return errorResponse(e);
        }
    }

    @GetMapping
    public ResponseEntity<JsonObject> getAllBooks() {
        try {
            logger.info("Fetching all books");
            JsonObject response = bookService.getBooks();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching books", e);
            return errorResponse(e);
        }
    }

    @PutMapping("/{id}/quantity")
    public ResponseEntity<JsonObject> updateQuantity(
            @PathVariable int id,
            @RequestParam int quantity) {
        try {
            logger.info("Updating quantity for book ID {} with quantity {}", id, quantity);
            JsonObject response = bookService.decreaseBookQuantity(id, quantity);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating quantity for book ID {}", id, e);
            return errorResponse(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonObject> getBookById(@PathVariable int id) {
        try {
            logger.info("Fetching book by ID: {}", id);
            JsonObject response = bookService.getBookById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching book by ID {}", id, e);
            return errorResponse(e);
        }
    }

    @GetMapping("/research/{isbn}")
    public ResponseEntity<JsonObject> getBookByIsbn( @PathVariable String isbn) {
        try {
            logger.info("Searching book by ISBN: {}", isbn);
            JsonObject response = bookService.findBookByIsbn(isbn);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error searching book by ISBN {}", isbn, e);
            return errorResponse(e);
        }
    }

    @GetMapping("/search/title")
    public ResponseEntity<JsonObject> getBookByTitle(@RequestParam String title) {
        try {
            logger.info("Searching book by title: {}", title);
            JsonObject response = bookService.findBookByTitle(title);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error searching book by title {}", title, e);
            return errorResponse(e);
        }
    }

    @GetMapping("/unavailable")
    public ResponseEntity<JsonObject> getUnavailableBooks() {
        try {
            logger.info("Fetching unavailable books");
            JsonObject response = bookService.findUnavailableBooks();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching unavailable books", e);
            return errorResponse(e);
        }
    }

    private ResponseEntity<JsonObject> errorResponse(Exception e) {
        JsonObject error = new JsonObject();
        error.addProperty("error", e.getMessage());
        return ResponseEntity.internalServerError().body(error);
    }
}