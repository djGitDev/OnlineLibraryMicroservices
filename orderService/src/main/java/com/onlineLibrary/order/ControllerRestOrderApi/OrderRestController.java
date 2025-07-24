

package com.onlineLibrary.order.ControllerRestOrderApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.onlineLibrary.order.Flux.Interfaces.IOrderService;
import com.google.gson.JsonObject;
import com.onlineLibrary.order.Util.ConvertJsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderRestController {

    private static final Logger logger = LoggerFactory.getLogger(OrderRestController.class);
    private final IOrderService orderService;

    @Autowired
    public OrderRestController(IOrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping(value = "/{userId}/place-order")
    public ResponseEntity<JsonNode> placeOrder(
            @PathVariable int userId,
            @RequestParam(defaultValue = "true") boolean isAutoDelivery) throws Exception {
        try {
            logger.info("Placing order for user: {}, autoDelivery: {}", userId, isAutoDelivery);
            JsonObject resultGson = orderService.placeOrder(userId, isAutoDelivery);
            JsonNode result = ConvertJsonUtils.gsonToJackson(resultGson);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error placing order for user: {}", userId, e);
            return errorResponse(e);
        }
    }

    @PostMapping(value = "/{orderId}/deliver")
    public ResponseEntity<JsonNode> deliverOrder(@PathVariable int orderId) throws Exception {
        logger.info("Delivering order: {}", orderId);
        JsonObject resultGson = orderService.deliveryOrder(orderId);
        JsonNode result = ConvertJsonUtils.gsonToJackson(resultGson);
        return ResponseEntity.ok(result);
    }

    @GetMapping()
    public ResponseEntity<JsonNode> getAllOrders() throws Exception {
        logger.info("Fetching all orders");
        JsonObject resultGson = orderService.displayAllOrders();
        JsonNode result = ConvertJsonUtils.gsonToJackson(resultGson);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/display-all")
    public ResponseEntity<JsonNode> displayAllOrders() throws Exception {
        try {
            JsonObject resultGson = orderService.displayOrders();
            JsonNode result = ConvertJsonUtils.gsonToJackson(resultGson);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error getting orders");
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