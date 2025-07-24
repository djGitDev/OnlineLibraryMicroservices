package com.onlineLibrary.cart.ControllerRestCartApi;

import com.fasterxml.jackson.databind.JsonNode;
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

import static com.onlineLibrary.cart.Util.ConvertJsonUtils.gsonToJackson;
import static com.onlineLibrary.cart.Util.ConvertJsonUtils.jacksonToGson;

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
    public ResponseEntity<JsonNode> getCart(@PathVariable int userId) throws Exception {
        try {
            logger.info("Getting cart from user_id: {}", userId);
            JsonObject resultGson = cartService.getCart(userId);
            JsonNode result = gsonToJackson(resultGson);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error getting cart from user: {}", userId, e);
            return errorResponse(e);
        }
    }

    @GetMapping("/items/{cartId}")
    public ResponseEntity<JsonNode> getItems(@PathVariable int cartId) throws Exception {
        try {
            logger.info("Getting specified cart items: {}", cartId);
            JsonObject resultGson = cartService.getItems(cartId);
            JsonNode result = gsonToJackson(resultGson);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error getting cart from user: {}", cartId, e);
            return errorResponse(e);
        }
    }



    @DeleteMapping("/{userId}/clear-cart")
    public ResponseEntity<JsonNode> clearCart(@PathVariable int userId) throws Exception {
        try {
            logger.info("Clearing cart for user: {}", userId);
            JsonObject resultGson = cartService.clearCart(userId);
            JsonNode result = gsonToJackson(resultGson);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error clearing cart for user: {}", userId, e);
            return errorResponse(e);
        }
    }

    @DeleteMapping("/{userId}/clear-books")
    public ResponseEntity<JsonNode> clearBooks(
            @PathVariable int userId,
            @RequestBody JsonNode requestBodyJackson) throws Exception {
        logger.info("Clearing specific books for user: {}", userId);
        JsonObject requestBody = jacksonToGson(requestBodyJackson);
        JsonArray books = requestBody.getAsJsonArray("books");
        JsonObject resultGson = cartService.clearBooks(userId,books);
        JsonNode result = gsonToJackson(resultGson);
        return ResponseEntity.ok(result);
    }



    @PostMapping("/{userId}/add-searched-items")
    public ResponseEntity<JsonNode> addSearchedItems(
            @PathVariable int userId,
            @RequestBody JsonNode requestBodyJackson,
            @RequestParam("prices") String pricesJson) throws Exception {

        try {
            logger.info("Adding searched items for user: {}", userId);
            Map<Integer, Double> bookPrices = deserializePrices(pricesJson);
            JsonObject requestBody = jacksonToGson(requestBodyJackson);
            JsonArray books = requestBody.getAsJsonArray("books");
            JsonObject resultGson = cartService.addSearchedItemsToCart(userId, books, bookPrices);
            JsonNode result = gsonToJackson(resultGson);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error adding searched items for user: {}", userId, e);
            return errorResponse(e);
        }
    }

    @GetMapping("/carts/{cartId}/total-price")
    public ResponseEntity<JsonNode> getCartTotal(@PathVariable int cartId) throws Exception {
        try {
            logger.info("Getting total for cart: {}", cartId);
            JsonObject resultGson = cartService.getTotalPrice(cartId);
            JsonNode result = gsonToJackson(resultGson);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error getting cart total for cart: {}", cartId, e);
            return errorResponse(e);
        }
    }


    private ResponseEntity<JsonNode> errorResponse(Exception e) throws Exception {
        JsonObject gsonError = new JsonObject();
        gsonError.addProperty("error", e.getMessage());
        JsonNode jacksonError = gsonToJackson(gsonError);
        return ResponseEntity.internalServerError().body(jacksonError);
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