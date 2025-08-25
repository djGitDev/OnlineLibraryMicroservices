package com.onlineLibrary.inventary.ControllerRestInventaryApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.onlineLibrary.inventary.Entities.DAO.AuthorDAO;
import com.onlineLibrary.inventary.Entities.DTO.AuthorsResponseDTO;
import com.onlineLibrary.inventary.Flux.IAuthorService;
import io.swagger.v3.oas.annotations.Operation;
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
public class AuthorRestController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorRestController.class);
    private final IAuthorService authorService;
    private final ObjectMapper mapper;

    @Autowired
    public AuthorRestController( IAuthorService authorService, ObjectMapper mapper) {
        this.authorService = authorService;
        this.mapper = mapper;
    }

    @Operation(
            summary = "Add a new author",
            description = "Adds a new author by name",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Author added successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                    {
                      "status": "OK",
                      "author": { "id": 3, "name": "New Author" }
                    }"""
                                    )
                            )
                    )
            }
    )
    @PostMapping("/authors/add")
    public ResponseEntity<JsonNode> addAuthor(@RequestBody JsonNode body) {
        try {
            String name = body.get("name").asText();
            AuthorDAO author = authorService.addAuthor(name); // retourne un DAO
            ObjectNode result = mapper.createObjectNode();
            result.put("status", "OK");
            result.set("author", mapper.valueToTree(author));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error adding author", e);
            return errorResponse(e);
        }
    }

    @Operation(
            summary = "Get all authors",
            description = "Retrieves a list of all authors",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of authors retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                            {
                                              "status": "OK",
                                              "authors": [
                                                { "id": 1, "name": "Joshua Bloch" },
                                                { "id": 2, "name": "Bjarne Stroustrup" }
                                              ]
                                            }"""
                                    )
                            )
                    )
            }
    )
    @GetMapping("/authors")
    public ResponseEntity<JsonNode> getAllAuthors() {
        try {
            logger.info("Fetching all authors");
            AuthorsResponseDTO resultDTO = authorService.getAuthors();
            JsonNode result = mapper.valueToTree(resultDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error fetching authors", e);
            return errorResponse(e);
        }
    }

    private ResponseEntity<JsonNode> errorResponse(Exception e) {
        ObjectNode errorNode = mapper.createObjectNode();
        errorNode.put("status", "FAILED");
        errorNode.put("message", e.getMessage());
        return ResponseEntity.internalServerError().body(errorNode);
    }
}
