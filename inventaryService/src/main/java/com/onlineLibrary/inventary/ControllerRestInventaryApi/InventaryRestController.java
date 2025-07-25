package com.onlineLibrary.inventary.ControllerRestInventaryApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.onlineLibrary.inventary.Util.ConvertJsonUtils;
import com.google.gson.JsonObject;
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
import static com.onlineLibrary.inventary.Util.ConvertJsonUtils.gsonToJackson;

@RestController
@RequestMapping("/api/books")
public class InventaryRestController {

    private static final Logger logger = LoggerFactory.getLogger(InventaryRestController.class);
    private final IBookService bookService;

    @Autowired
    public InventaryRestController(IBookService bookService) {
        this.bookService = bookService;
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
            JsonObject resultGson = bookService.decreaseBookQuantity(id, quantity);
            JsonNode response = gsonToJackson(resultGson);
            return ResponseEntity.ok(response);
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
            JsonObject resultGson = bookService.getBookById(id);
            JsonNode response = gsonToJackson(resultGson);
            return ResponseEntity.ok(response);
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
            JsonObject resultGson = bookService.findBookByIsbn(isbn);
            JsonNode response = gsonToJackson(resultGson);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error searching book by ISBN {}", isbn, e);
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