package com.onlineLibrary.inventary.ControllerRestInventaryApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.onlineLibrary.inventary.Entities.DAO.PublisherDAO;
import com.onlineLibrary.inventary.Entities.DTO.PublishersResponseDTO;
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
public class PublisherRestController {



    private static final Logger logger = LoggerFactory.getLogger(PublisherRestController.class);
    private final IPublisherService publisherService;
    private final ObjectMapper mapper;

    @Autowired
    public PublisherRestController(  IPublisherService publisherService, ObjectMapper mapper) {
        this.publisherService = publisherService;
        this.mapper = mapper;
    }




    @Operation(
            summary = "Add a new publisher",
            description = "Adds a new publisher by name",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Publisher added successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                            {
                                              "status": "OK",
                                              "publisher": { "id": 3, "name": "New Publisher" }
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


    private ResponseEntity<JsonNode> errorResponse(Exception e) {
        ObjectNode errorNode = mapper.createObjectNode();
        errorNode.put("status", "FAILED");
        errorNode.put("message", e.getMessage());
        return ResponseEntity.internalServerError().body(errorNode);
    }
}
