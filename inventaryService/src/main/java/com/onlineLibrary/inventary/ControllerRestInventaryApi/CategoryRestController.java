package com.onlineLibrary.inventary.ControllerRestInventaryApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.onlineLibrary.inventary.Entities.DAO.CategoryDAO;
import com.onlineLibrary.inventary.Entities.DTO.CategoriesResponseDTO;
import com.onlineLibrary.inventary.Flux.ICategoryService;
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
public class CategoryRestController {


    private static final Logger logger = LoggerFactory.getLogger(CategoryRestController.class);

    private final ICategoryService categoryService;
    private final ObjectMapper mapper;

    @Autowired
    public CategoryRestController(ICategoryService categoryService, ObjectMapper mapper) {

        this.categoryService = categoryService;
        this.mapper = mapper;
    }



    @Operation(
            summary = "Add a new category",
            description = "Adds a new book category by name",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category added successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                            {
                                              "status": "OK",
                                              "category": { "id": 3, "name": "New Category" }
                                            }"""
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                            {
                                              "status": "FAILED",
                                              "message": "Error message"
                                            }"""
                                    )
                            )
                    )
            }
    )
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
