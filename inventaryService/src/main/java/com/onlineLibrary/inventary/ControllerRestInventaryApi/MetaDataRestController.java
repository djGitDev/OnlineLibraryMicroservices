package com.onlineLibrary.inventary.ControllerRestInventaryApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.onlineLibrary.inventary.Entities.DAO.AuthorDAO;
import com.onlineLibrary.inventary.Entities.DAO.CategoryDAO;
import com.onlineLibrary.inventary.Entities.DAO.PublisherDAO;
import com.onlineLibrary.inventary.Entities.DTO.PublishersResponseDTO;
import com.onlineLibrary.inventary.Entities.DTO.AuthorsResponseDTO;
import com.onlineLibrary.inventary.Entities.DTO.CategoriesResponseDTO;
import com.onlineLibrary.inventary.Flux.IAuthorService;
import com.onlineLibrary.inventary.Flux.ICategoryService;
import com.onlineLibrary.inventary.Flux.IPublisherService;
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
public class MetaDataRestController {

    private static final Logger logger = LoggerFactory.getLogger(MetaDataRestController.class);
    private final IAuthorService authorService;
    private final IPublisherService publisherService;
    private final ICategoryService categoryService;
    private final ObjectMapper mapper;

    @Autowired
    public MetaDataRestController( IAuthorService authorService, IPublisherService publisherService, ICategoryService categoryService, ObjectMapper mapper) {
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.categoryService = categoryService;
        this.mapper = mapper;
    }



    // ================== Ajouter un Publisher ==================
    @PostMapping("/publishers/add")
    public ResponseEntity<JsonNode> addPublisher(@RequestBody JsonNode body) {
        try {
            String name = body.get("name").asText();
            PublisherDAO publisher = publisherService.addPublisher(name); // retourne un DAO
            ObjectNode result = mapper.createObjectNode();
            result.put("status", "OK");
            result.set("publisher", mapper.valueToTree(publisher));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error adding publisher", e);
            return errorResponse(e);
        }
    }

    // ================== Ajouter un Author ==================
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

    // ================== Ajouter une Category ==================
    @PostMapping("/categories/add")
    public ResponseEntity<JsonNode> addCategory(@RequestBody JsonNode body) {
        try {
            String name = body.get("name").asText();
            CategoryDAO category = categoryService.addCategory(name); // retourne un DAO
            ObjectNode result = mapper.createObjectNode();
            result.put("status", "OK");
            result.set("category", mapper.valueToTree(category));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error adding category", e);
            return errorResponse(e);
        }
    }

    // ================== Publishers ==================
    @Operation(
            summary = "Get all publishers",
            description = "Retrieves a list of all publishers",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of publishers retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                            {
                                              "status": "OK",
                                              "publishers": [
                                                { "id": 1, "name": "O'Reilly" },
                                                { "id": 2, "name": "Pearson" }
                                              ]
                                            }"""
                                    )
                            )
                    )
            }
    )
    @GetMapping("/publishers")
    public ResponseEntity<JsonNode> getAllPublishers() {
        try {
            logger.info("Fetching all publishers");
            PublishersResponseDTO resultDTO = publisherService.getPublishers();
            JsonNode result = mapper.valueToTree(resultDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error fetching publishers", e);
            return errorResponse(e);
        }
    }

    // ================== Authors ==================
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

    // ================== Categories ==================
    @Operation(
            summary = "Get all categories",
            description = "Retrieves a list of all book categories",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of categories retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                            {
                                              "status": "OK",
                                              "categories": [
                                                { "id": 1, "name": "Programming" },
                                                { "id": 2, "name": "Databases" }
                                              ]
                                            }"""
                                    )
                            )
                    )
            }
    )
    @GetMapping("/categories")
    public ResponseEntity<JsonNode> getAllCategories() {
        try {
            logger.info("Fetching all categories");
            CategoriesResponseDTO resultDTO = categoryService.getCategories();
            JsonNode result = mapper.valueToTree(resultDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error fetching categories", e);
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

