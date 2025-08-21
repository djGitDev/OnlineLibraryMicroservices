package com.onlineLibrary.inventary.ControllerRestInventaryApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.onlineLibrary.inventary.Entities.DTO.BookDTO;
import com.onlineLibrary.inventary.Entities.DTO.BookQuantityResponseDTO;
import com.onlineLibrary.inventary.Entities.DTO.BookResponseDTO;
import com.onlineLibrary.inventary.Entities.DTO.BooksResponseDTO;
import com.onlineLibrary.inventary.Flux.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private static final Logger logger = LoggerFactory.getLogger(BookRestController.class);
    private final IBookService bookService;
    private final ObjectMapper mapper;


    @Autowired
    public BookRestController(IBookService bookService, ObjectMapper mapper) {
        this.bookService = bookService;
        this.mapper = mapper;
    }

    // ================== Ajouter un Book ==================
    @Operation(
            summary = "Add a new book",
            description = "Creates a new book in the database",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "isbn": "9780321751041",
                                          "title": "New Book",
                                          "description": "Description of the new book",
                                          "parutionDate": "Aug 21, 2025",
                                          "price": 29.99,
                                          "quantity": 10,
                                          "publisherId": 1,
                                          "authorIds": [1,2],
                                          "categoryIds": [1,3]
                                        }"""
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book added successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                        {
                                          "status": "OK",
                                          "book": {
                                            "id": 6,
                                            "isbn": "9780321751041",
                                            "title": "New Book",
                                            "description": "Description of the new book",
                                            "parutionDate": "Aug 21, 2025",
                                            "price": 29.99,
                                            "quantity": 10,
                                            "publisherId": 1,
                                            "authorIds": [1,2],
                                            "categoryIds": [1,3]
                                          }
                                        }"""
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error adding book",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/add")
    public ResponseEntity<JsonNode> addBook(@RequestBody JsonNode body) {
        try {
            BookDTO bookDTO = mapper.treeToValue(body, BookDTO.class);
            BookResponseDTO savedBook = bookService.addBook(bookDTO);
            ObjectNode result = mapper.createObjectNode();
            result.put("status", "OK");
            result.set("book", mapper.valueToTree(savedBook));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error adding book", e);
            return errorResponse(e);
        }
    }
    @Operation(
            summary = "Get all books",
            description = "Retrieves a list of all books in the database",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of books retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "AllBooksExample",
                                            value = """
                    {
                      "status": "OK",
                      "books": [
                        {
                          "id": 2,
                          "isbn": "9780135166307",
                          "title": "Effective Java",
                          "description": "Best practices for Java programming",
                          "parutionDate": "Jan 1, 2020",
                          "price": 45,
                          "quantity": 15,
                          "publisherId": 2
                        },
                        {
                          "id": 4,
                          "isbn": "9780134686097",
                          "title": "Effective Modern C++",
                          "description": "Guidelines for using C++11 and C++14 effectively",
                          "parutionDate": "Dec 27, 2016",
                          "price": 49.99,
                          "quantity": 20,
                          "publisherId": 1
                        },
                        {
                          "id": 5,
                          "isbn": "9780321751041",
                          "title": "first unavailable book",
                          "description": "an unavailable book",
                          "parutionDate": "May 3, 2006",
                          "price": 54.99,
                          "quantity": 0,
                          "publisherId": 1
                        },
                        {
                          "id": 1,
                          "isbn": "1111743273565",
                          "title": "how to add many books to the database",
                          "description": "A guide on adding books to the database",
                          "parutionDate": "Jan 1, 2025",
                          "price": 10.99,
                          "quantity": 22,
                          "publisherId": 1
                        },
                        {
                          "id": 3,
                          "isbn": "9781491950357",
                          "title": "Learning Python",
                          "description": "Comprehensive guide to Python programming",
                          "parutionDate": "Jun 12, 2013",
                          "price": 39.99,
                          "quantity": 27,
                          "publisherId": 1
                        }
                      ]
                    }"""
                                    )
                            )
                    )
            }
    )
    @GetMapping("/")
    public ResponseEntity<JsonNode> getAllBooks() throws Exception {
        try {
            logger.info("Fetching all books");
            BooksResponseDTO resultDTO = bookService.getBooks();
            JsonNode result = mapper.valueToTree(resultDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error fetching books", e);
            return errorResponse(e);
        }
    }



    @Operation(
            summary = "Decrease book quantity",
            description = "Decreases the available quantity of a book by specified amount",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "DecreaseQuantityExample",
                                    value = """
                {
                  "action": "decrease_book_quantity",
                  "book_id": 3,
                  "quantity": 2
                }"""
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Quantity updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "SuccessResponse",
                                                    value = """
                        {
                          "status": "OK",
                          "message": "Book quantity updated"
                        }"""
                                            ),
                                            @ExampleObject(
                                                    name = "FailedResponse",
                                                    value = """
                        {
                          "status": "FAILED",
                          "message": "Not enough books in stock"
                        }"""
                                            )
                                    }
                            )
                    ),

            }
    )
    @PutMapping("/{id}/quantity")
    public ResponseEntity<JsonNode> updateQuantity(
            @PathVariable int id,
            @RequestParam int quantity) throws Exception {
        try {
            logger.info("Updating quantity for book ID {} with quantity {}", id, quantity);
            BookQuantityResponseDTO resultDTO = bookService.decreaseBookQuantity(id, quantity);
            JsonNode result = mapper.valueToTree(resultDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error updating quantity for book ID {}", id, e);
            return errorResponse(e);
        }
    }




    @Operation(
            summary = "Get book by ID",
            description = "Retrieves detailed information about a specific book by its ID",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID of the book to retrieve",
                            example = "3",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                    {
                      "status": "OK",
                      "book": {
                        "id": 3,
                        "isbn": "9781491950357",
                        "title": "Learning Python",
                        "description": "Comprehensive guide to Python programming",
                        "parutionDate": "Jun 12, 2013",
                        "price": 39.99,
                        "quantity": 27,
                        "publisherId": 1
                      }
                    }"""
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<JsonNode> getBookById(@PathVariable int id) throws Exception {
        try {
            logger.info("Fetching book by ID: {}", id);
            BookResponseDTO resultDTO = bookService.getBookById(id);
            JsonNode result = mapper.valueToTree(resultDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error fetching book by ID {}", id, e);
            return errorResponse(e);
        }
    }


    @Operation(
            summary = "Find book by ISBN",
            description = "Retrieves book details by its ISBN number",
            parameters = {
                    @Parameter(
                            name = "isbn",
                            description = "ISBN number of the book to find",
                            required = true,
                            example = "1111743273565",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book found successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "BookFoundExample",
                                            value = """
                    {
                      "status": "OK",
                      "book": {
                        "id": 1,
                        "isbn": "1111743273565",
                        "title": "how to add many books to the database",
                        "description": "A guide on adding books to the database",
                        "parutionDate": "Jan 1, 2025",
                        "price": 10.99,
                        "quantity": 22,
                        "publisherId": 1
                      }
                    }"""
                                    )
                            )
                    )
            }
    )
    @GetMapping("/research/{isbn}")
    public ResponseEntity<JsonNode> getBookByIsbn( @PathVariable String isbn) throws Exception {
        try {
            logger.info("Searching book by ISBN: {}", isbn);
            BookResponseDTO resultDTO = bookService.findBookByIsbn(isbn);
            JsonNode result = mapper.valueToTree(resultDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error searching book by ISBN {}", isbn, e);
            return errorResponse(e);
        }
    }

    private ResponseEntity<JsonNode> errorResponse(Exception e) {
        ObjectNode errorNode = mapper.createObjectNode();
        errorNode.put("error", e.getMessage());
        return ResponseEntity.internalServerError().body(errorNode);
    }
}