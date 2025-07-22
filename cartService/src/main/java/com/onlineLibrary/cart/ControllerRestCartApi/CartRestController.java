package com.onlineLibrary.cart.ControllerRestCartApi;

import com.google.gson.JsonArray;
import com.onlineLibrary.cart.Flux.Interfaces.ICartService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
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

    @Autowired
    public CartRestController(ICartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<JsonObject> getCart(@PathVariable int userId) {
        try {
            logger.info("Getting cart from user_id: {}", userId);
            JsonObject result = cartService.getCart(userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error getting cart from user: {}", userId, e);
            return errorResponse(e);
        }
    }

    @GetMapping("/items/{cartId}")
    public ResponseEntity<JsonObject> getItems(@PathVariable int cartId) {
        try {
            logger.info("Getting specified cart items: {}", cartId);
            JsonObject result = cartService.getItems(cartId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error getting cart from user: {}", cartId, e);
            return errorResponse(e);
        }
    }



    @DeleteMapping("/{userId}/clear-cart")
    public ResponseEntity<JsonObject> clearCart(@PathVariable int userId) {
        try {
            logger.info("Clearing cart for user: {}", userId);
            JsonObject result = cartService.clearCart(userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error clearing cart for user: {}", userId, e);
            return errorResponse(e);
        }
    }

    @DeleteMapping("/{userId}/clear-books")
    public ResponseEntity<JsonObject> clearBooks(
            @PathVariable int userId,
            @RequestBody JsonObject requestBody) {
        logger.info("Clearing specific books for user: {}", userId);
        JsonArray books = requestBody.getAsJsonArray("books");
        JsonObject result = cartService.clearBooks(userId,books);
        return ResponseEntity.ok(result);
    }



    @PostMapping("/{userId}/add-searched-items")
    public ResponseEntity<JsonObject> addSearchedItems(
            @PathVariable int userId,
            @RequestBody JsonObject requestBody,
            @RequestParam("prices") String pricesJson) {

        try {
            logger.info("Adding searched items for user: {}", userId);
            Map<Integer, Double> bookPrices = deserializePrices(pricesJson);
            JsonArray books = requestBody.getAsJsonArray("books");
            JsonObject result = cartService.addSearchedItemsToCart(userId, books, bookPrices);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error adding searched items for user: {}", userId, e);
            return errorResponse(e);
        }
    }

    @GetMapping("/carts/{cartId}/total-price")
    public ResponseEntity<JsonObject> getCartTotal(@PathVariable int cartId) {
        try {
            logger.info("Getting total for cart: {}", cartId);
            JsonObject result = cartService.getTotalPrice(cartId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error getting cart total for cart: {}", cartId, e);
            return errorResponse(e);
        }
    }


    private ResponseEntity<JsonObject> errorResponse(Exception e) {
        JsonObject error = new JsonObject();
        error.addProperty("error", e.getMessage());
        return ResponseEntity.internalServerError().body(error);
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