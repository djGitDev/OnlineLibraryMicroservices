package com.example.ControllerRestOrderApi;

import com.example.Flux.Interfaces.IOrderService;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderRestController {

    private static final Logger logger = LoggerFactory.getLogger(OrderRestController.class);
    private final IOrderService orderService;

    @Autowired
    public OrderRestController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{userId}/clear-cart")
    public ResponseEntity<JsonObject> clearCart(@PathVariable int userId) {
        try {
            logger.info("Clearing cart for user: {}", userId);
            JsonObject result = orderService.clearCart(userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error clearing cart for user: {}", userId, e);
            return errorResponse(e);
        }
    }

    @PostMapping("/{userId}/clear-books")
    public ResponseEntity<JsonObject> clearBooks(
            @PathVariable int userId,
            @RequestBody JsonObject requestBody) {
        logger.info("Clearing specific books for user: {}", userId);
        JsonObject result = orderService.clearBooks(userId, requestBody);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{userId}/place-order")
    public ResponseEntity<JsonObject> placeOrder(
            @PathVariable int userId,
            @RequestParam(defaultValue = "true") boolean autoDelivery) {
        try {
            logger.info("Placing order for user: {}, autoDelivery: {}", userId, autoDelivery);
            JsonObject result = orderService.placeOrder(userId, autoDelivery);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error placing order for user: {}", userId, e);
            return errorResponse(e);
        }
    }

    @PostMapping("/{orderId}/deliver")
    public ResponseEntity<JsonObject> deliverOrder(@PathVariable int orderId) {
        logger.info("Delivering order: {}", orderId);
        JsonObject result = orderService.deliveryOrder(orderId);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<JsonObject> getAllOrders() {
        logger.info("Fetching all orders");
        JsonObject result = orderService.displayAllOrders();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{userId}/add-searched-items")
    public ResponseEntity<JsonObject> addSearchedItems(
            @PathVariable int userId,
            @RequestBody JsonObject requestBody,
            @RequestParam Map<Integer, Double> bookPrices) {
        try {
            logger.info("Adding searched items for user: {}", userId);
            JsonObject result = orderService.addSearchedItemsToCart(userId, requestBody, bookPrices);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error adding searched items for user: {}", userId, e);
            return errorResponse(e);
        }
    }

    @GetMapping("/carts/{cartId}/total")
    public ResponseEntity<JsonObject> getCartTotal(@PathVariable int cartId) {
        try {
            logger.info("Getting total for cart: {}", cartId);
            JsonObject result = orderService.getTotalPriceCart(cartId);
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
}