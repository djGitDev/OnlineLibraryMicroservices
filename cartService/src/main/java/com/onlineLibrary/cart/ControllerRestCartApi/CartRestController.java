package com.onlineLibrary.cart.ControllerRestCartApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.onlineLibrary.cart.Entities.DTO.*;
import com.onlineLibrary.cart.Flux.Interfaces.ICartService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    private static final Logger logger = LoggerFactory.getLogger(CartRestController.class);
    private final ICartService cartService;
    private final ObjectMapper mapper;

    @Autowired
    public CartRestController(ICartService cartService,ObjectMapper mapper) {
        this.cartService = cartService;
        this.mapper = mapper;
    }


    @Operation(
            summary = "Get user's cart",
            description = "Retrieves the cart ID for a specific user",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID of the user whose cart to retrieve",
                            example = "123",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cart retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                    {
                      "cartId": 5
                    }"""
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{userId}")
    public ResponseEntity<JsonNode> getCart(@PathVariable int userId) throws Exception {
        try {
            logger.info("Getting cart from user_id: {}", userId);
            CartDTO resultDTO = cartService.getCart(userId);
            JsonNode result = mapper.valueToTree(resultDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error getting cart from user: {}", userId, e);
            return errorResponse(e);
        }
    }


    @Operation(
            summary = "Get cart items",
            description = "Retrieves all items in a specific shopping cart",
            parameters = {
                    @Parameter(
                            name = "cartId",
                            description = "ID of the cart to retrieve items from",
                            required = true,
                            example = "5",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cart items retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "CartItemsExample",
                                            value = """
                    {
                      "cartId": 5,
                      "items": [
                        {
                          "bookId": 1,
                          "quantity": 3
                        },
                        {
                          "bookId": 3,
                          "quantity": 2
                        }
                      ]
                    }"""
                                    )
                            )
                    )
            }
    )
    @GetMapping("/items/{cartId}")
    public ResponseEntity<JsonNode> getItems(@PathVariable int cartId) throws Exception {
        try {
            logger.info("Getting specified cart items: {}", cartId);
            CartItemsResponseDTO resultDTO = cartService.getItems(cartId);
            JsonNode result = mapper.valueToTree(resultDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error getting cart from user: {}", cartId, e);
            return errorResponse(e);
        }
    }


    @Operation(
            summary = "Clear user cart",
            description = "Removes all items from a user's shopping cart",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID of the user whose cart to clear",
                            required = true,
                            example = "123",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cart cleared successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "CartClearedSuccessfully",
                                            value = """
                    {
                      "status": "success",
                      "message": "Cart and all items deleted successfully.",
                      "items": []
                    }"""
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/{userId}/clear-cart")
    public ResponseEntity<JsonNode> clearCart(@PathVariable int userId) throws Exception {
        try {
            logger.info("Clearing cart for user: {}", userId);
            ClearCartResponseDTO resultDTO = cartService.clearCart(userId);
            JsonNode result = mapper.valueToTree(resultDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error clearing cart for user: {}", userId, e);
            return errorResponse(e);
        }
    }


    @Operation(
            summary = "Remove specific books from cart",
            description = "Removes selected books from a user's shopping cart",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID of the user whose cart to modify",
                            required = true,
                            example = "123",
                            in = ParameterIn.PATH
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "BooksToRemoveExample",
                                    value = """
                {
                  "books": [
                    {
                      "book_id": 1,
                      "quantity": 2
                    }
                  ]
                }"""
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Books removed successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "BooksRemovedResponse",
                                            value = """
                    {
                      "status": "success",
                      "message": "Books removed from cart.",
                      "items": [
                        {
                          "book_id": 1,
                          "quantity": 2
                        }
                      ]
                    }"""
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/{userId}/clear-books")
    public ResponseEntity<JsonNode> clearBooks(
            @PathVariable int userId,
            @RequestBody JsonNode requestBodyJackson) {
        logger.info("Clearing specific books for user: {}", userId);
        JsonNode booksNode = requestBodyJackson.get("books");
        ClearBooksResponseDTO resultDTO = cartService.clearBooks(userId,booksNode);
        JsonNode result = mapper.valueToTree(resultDTO);
        return ResponseEntity.ok(result);
    }


    @Operation(
            summary = "Add searched items to cart",
            description = "Adds multiple books from search results to user's cart with their prices",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID of the user adding items",
                            required = true,
                            example = "123",
                            in = ParameterIn.PATH
                    ),
                    @Parameter(
                            name = "prices",
                            description = "JSON string mapping book IDs to prices",
                            required = true,
                            example = "{\"1\":10.99,\"3\":39.99}",
                            in = ParameterIn.QUERY
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "BooksToAddExample",
                                    value = """
                {
                  "books": [
                    {
                      "book_id": 1,
                      "quantity": 2
                    },
                    {
                      "book_id": 3,
                      "quantity": 1
                    }
                  ]
                }"""
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Books added successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "BooksAddedResponse",
                                            value = """
                    {
                      "status": "success",
                      "message": "Books added to cart",
                      "cartId": 6,
                      "items": [
                        {
                          "book_id": 1,
                          "quantity": 7,
                          "bookPrice": 10.99
                        }
                      ]
                    }"""
                                    )
                            )
                    )
            }
    )
    @PostMapping("/{userId}/add-searched-items")
    public ResponseEntity<JsonNode> addSearchedItems(
            @PathVariable int userId,
            @RequestBody JsonNode requestBodyJackson,
            @RequestParam("prices") String pricesJson) throws Exception {

        try {
            logger.info("Adding searched items for user: {}", userId);
            Map<Integer, Double> bookPrices = deserializePrices(pricesJson);
            JsonNode booksNode = requestBodyJackson.get("books");
            AddBooksResponseDTO resultDTO = cartService.addSearchedItemsToCart(userId, booksNode, bookPrices);
            JsonNode result = mapper.valueToTree(resultDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error adding searched items for user: {}", userId, e);
            return errorResponse(e);
        }
    }



    @Operation(
            summary = "Get cart total price",
            description = "Calculates and returns the total price of all items in the specified cart",
            parameters = {
                    @Parameter(
                            name = "cartId",
                            description = "ID of the cart to calculate total",
                            required = true,
                            example = "5",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Total price calculated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "CartTotalExample",
                                            value = """
                    {
                      "status": "success",
                      "message": "Cart total calculated successfully",
                      "cartId": 5,
                      "totalPrice": 112.95,
                      "items": [
                        {
                          "bookId": 1,
                          "quantity": 3,
                          "unitPrice": 10.99,
                          "totalPrice": 32.97
                        },
                        {
                          "bookId": 3,
                          "quantity": 2,
                          "unitPrice": 39.99,
                          "totalPrice": 79.98
                        }
                      ]
                    }"""
                                    )
                            )
                    )
            }
    )
    @GetMapping("/carts/{cartId}/total-price")
    public ResponseEntity<JsonNode> getCartTotal(@PathVariable int cartId) throws Exception {
        try {
            logger.info("Getting total for cart: {}", cartId);
            CartTotalPriceDTO resultDTO = cartService.getTotalPrice(cartId);
            JsonNode result = mapper.valueToTree(resultDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error getting cart total for cart: {}", cartId, e);
            return errorResponse(e);
        }
    }


    private ResponseEntity<JsonNode> errorResponse(Exception e) {
        ObjectNode errorNode = mapper.createObjectNode();
        errorNode.put("error", e.getMessage());
        return ResponseEntity.internalServerError().body(errorNode);
    }

    private Map<Integer, Double> deserializePrices(String json) {
        Type type = new TypeToken<Map<String, Double>>(){}.getType();
        Map<String, Double> stringKeyMap = new Gson().fromJson(json, type);

        return stringKeyMap.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> Integer.parseInt(e.getKey()),
                        Map.Entry::getValue
                ));
    }
}