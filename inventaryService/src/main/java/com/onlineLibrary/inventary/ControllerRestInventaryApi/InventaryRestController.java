package com.onlineLibrary.inventary.ControllerRestInventaryApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.onlineLibrary.inventary.Util.ConvertJsonUtils;
import com.google.gson.JsonObject;
import com.onlineLibrary.inventary.Flux.IBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.onlineLibrary.inventary.Util.ConvertJsonUtils.gsonToJackson;
import static com.onlineLibrary.inventary.Util.ConvertJsonUtils.jacksonToGson;

@RestController
@RequestMapping("/api/books")
public class InventaryRestController {

    private static final Logger logger = LoggerFactory.getLogger(InventaryRestController.class);
    private final IBookService bookService;

    @Autowired
    public InventaryRestController(IBookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<JsonNode> addBooks(@RequestBody JsonNode booksJackson) throws Exception {
        try {
            logger.info("Received request to add books: {}", booksJackson);
            JsonObject booksJson = jacksonToGson(booksJackson);
            JsonObject resultGson = bookService.addManyBooks(booksJson);
            JsonNode response = gsonToJackson(resultGson);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error adding books", e);
            return errorResponse(e);
        }
    }

    @GetMapping
    public ResponseEntity<JsonNode> getAllBooks() throws Exception {
        try {
            logger.info("Fetching all books");
            JsonObject resultGson = bookService.getBooks();
            JsonNode response = gsonToJackson(resultGson);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching books", e);
            return errorResponse(e);
        }
    }

    @PutMapping("/{id}/quantity")
    public ResponseEntity<JsonNode> updateQuantity(
            @PathVariable int id,
            @RequestParam int quantity) throws Exception {
        try {
            logger.info("Updating quantity for book ID {} with quantity {}", id, quantity);
            JsonObject resultGson = bookService.decreaseBookQuantity(id, quantity);
            JsonNode response = gsonToJackson(resultGson);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating quantity for book ID {}", id, e);
            return errorResponse(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonNode> getBookById(@PathVariable int id) throws Exception {
        try {
            logger.info("Fetching book by ID: {}", id);
            JsonObject resultGson = bookService.getBookById(id);
            JsonNode response = gsonToJackson(resultGson);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching book by ID {}", id, e);
            return errorResponse(e);
        }
    }

    @GetMapping("/research/{isbn}")
    public ResponseEntity<JsonNode> getBookByIsbn( @PathVariable String isbn) throws Exception {
        try {
            logger.info("Searching book by ISBN: {}", isbn);
            JsonObject resultGson = bookService.findBookByIsbn(isbn);
            JsonNode response = gsonToJackson(resultGson);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error searching book by ISBN {}", isbn, e);
            return errorResponse(e);
        }
    }

    @GetMapping("/search/title")
    public ResponseEntity<JsonNode> getBookByTitle(@RequestParam String title) throws Exception {
        try {
            logger.info("Searching book by title: {}", title);
            JsonObject resultGson = bookService.findBookByTitle(title);
            JsonNode response = gsonToJackson(resultGson);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error searching book by title {}", title, e);
            return errorResponse(e);
        }
    }

    @GetMapping("/unavailable")
    public ResponseEntity<JsonNode> getUnavailableBooks() throws Exception {
        try {
            logger.info("Fetching unavailable books");
            JsonObject resultGson = bookService.findUnavailableBooks();
            JsonNode response = gsonToJackson(resultGson);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching unavailable books", e);
            return errorResponse(e);
        }
    }

    private ResponseEntity<JsonNode> errorResponse(Exception e) throws Exception {
        JsonObject gsonError = new JsonObject();
        gsonError.addProperty("error", e.getMessage());
        JsonNode jacksonError = ConvertJsonUtils.gsonToJackson(gsonError);
        return ResponseEntity.internalServerError().body(jacksonError);
    }
}