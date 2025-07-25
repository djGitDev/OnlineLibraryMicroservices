

package com.onlineLibrary.order.ControllerRestOrderApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.onlineLibrary.order.Flux.Interfaces.IOrderService;
import com.google.gson.JsonObject;
import com.onlineLibrary.order.Util.ConvertJsonUtils;
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

@RestController
@RequestMapping("/api/order")
public class OrderRestController {

    private static final Logger logger = LoggerFactory.getLogger(OrderRestController.class);
    private final IOrderService orderService;

    @Autowired
    public OrderRestController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            summary = "Place order",
            description = "Creates an order from the user's cart and optionally schedules delivery",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID of the user placing the order",
                            required = true,
                            example = "123",
                            in = ParameterIn.PATH
                    ),
                    @Parameter(
                            name = "isAutoDelivery",
                            description = "Whether to automatically schedule delivery (default: true)",
                            example = "true",
                            in = ParameterIn.QUERY
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order placed successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "OrderPlacedSuccessfully",
                                            value = """
                    {
                      "orderId": 6,
                      "deliveryId": 6,
                      "cartClearedInfo": {
                        "status": "success",
                        "message": "Cart and all items deleted successfully.",
                        "items": []
                      }
                    }"""
                                    )
                            )
                    )
            }
    )
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


    @Operation(
            summary = "Mark order as delivered",
            description = "Updates order status to mark it as delivered",
            parameters = {
                    @Parameter(
                            name = "orderId",
                            description = "ID of the order to mark as delivered",
                            required = true,
                            example = "6",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order successfully marked as delivered",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "OrderDeliveredResponse",
                                            value = """
                    {
                      "status": "success",
                      "updated_lines": 2,
                      "message": "order lines marked as delivered"
                    }"""
                                    )
                            )
                    )
            }
    )
    @PostMapping(value = "/{orderId}/deliver")
    public ResponseEntity<JsonNode> deliverOrder(@PathVariable int orderId) throws Exception {
        logger.info("Delivering order: {}", orderId);
        JsonObject resultGson = orderService.deliveryOrder(orderId);
        JsonNode result = ConvertJsonUtils.gsonToJackson(resultGson);
        return ResponseEntity.ok(result);
    }



    @Operation(
            summary = "Get all orders",
            description = "Retrieves a complete list of all orders with their current statuses",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved all orders",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "AllOrdersResponse",
                                            value = """
                    {
                      "orders_with_status": [
                        {
                          "order_id": 1,
                          "status": "PENDING"
                        },
                        {
                          "order_id": 2,
                          "status": "DELIVERED"
                        },
                        {
                          "order_id": 3,
                          "status": "DELIVERED"
                        },
                        {
                          "order_id": 4,
                          "status": "DELIVERED"
                        },
                        {
                          "order_id": 5,
                          "status": "DELIVERED"
                        },
                        {
                          "order_id": 6,
                          "status": "DELIVERED"
                        },
                        {
                          "order_id": 7,
                          "status": "PENDING"
                        },
                        {
                          "order_id": 8,
                          "status": "DELIVERED"
                        },
                        {
                          "order_id": 9,
                          "status": "PENDING"
                        },
                        {
                          "order_id": 10,
                          "status": "PENDING"
                        }
                      ]
                    }"""
                                    )
                            )
                    )
            }
    )
    @GetMapping()
    public ResponseEntity<JsonNode> getAllOrders() throws Exception {
        logger.info("Fetching all orders");
        JsonObject resultGson = orderService.displayAllOrders();
        JsonNode result = ConvertJsonUtils.gsonToJackson(resultGson);
        return ResponseEntity.ok(result);
    }


    private ResponseEntity<JsonNode> errorResponse(Exception e) throws Exception {
        JsonObject gsonError = new JsonObject();
        gsonError.addProperty("error", e.getMessage());
        JsonNode jacksonError = ConvertJsonUtils.gsonToJackson(gsonError);
        return ResponseEntity.internalServerError().body(jacksonError);
    }
}