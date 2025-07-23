package com.onlineLibrary.order.ControllerRestOrderApi;

import com.onlineLibrary.order.Flux.Interfaces.IOrderService;
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
@RequestMapping("/api/order")
public class OrderRestController {

    private static final Logger logger = LoggerFactory.getLogger(OrderRestController.class);
    private final IOrderService orderService;

    @Autowired
    public OrderRestController(IOrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/{userId}/place-order")
    public ResponseEntity<JsonObject> placeOrder(
            @PathVariable int userId,
            @RequestParam(defaultValue = "true") boolean isAutoDelivery) {
        try {
            logger.info("Placing order for user: {}, autoDelivery: {}", userId, isAutoDelivery);
            JsonObject result = orderService.placeOrder(userId, isAutoDelivery);
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
    

    @GetMapping("/display-all")
    public ResponseEntity<JsonObject> displayAllOrders() {

        try {
            JsonObject result = orderService.displayOrders();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error getting orders");
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