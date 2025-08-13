

package com.onlineLibrary.order.ControllerRestOrderApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.onlineLibrary.order.Entities.DAO.OrderLineDAO;
import com.onlineLibrary.order.Entities.DTO.DeliveryDTO;
import com.onlineLibrary.order.Entities.DTO.OrderResponseDTO;
import com.onlineLibrary.order.Flux.Interfaces.IOrderService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
            OrderResponseDTO result = orderService.placeOrder(userId, isAutoDelivery);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode resultJsonNode = mapper.valueToTree(result);
            return ResponseEntity.ok(resultJsonNode);
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
            name = "userId",
            description = "ID of the user who owns the order",
            required = true,
            example = "3",
            in = ParameterIn.PATH
        ),
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
    @PostMapping(value = "/users/{userId}/orders/{orderId}/deliver")
    public ResponseEntity<JsonNode> deliverOrder(
            @PathVariable int userId,
            @PathVariable int orderId) throws Exception {

        logger.info("Delivering order {} for user {}", orderId, userId);

        Optional<List<OrderLineDAO>> orderLinesDelivered = orderService.deliveryOrder(userId,orderId);
        JsonNode result = convertOrderLinesToJson(orderLinesDelivered);

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
        List<DeliveryDTO> deliveries = orderService.displayAllOrders();
        List<Map<String, Object>> ordersWithStatus = deliveries.stream()
                .map(d -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("order_id", d.getOrderId());
                    map.put("status", d.getStatut());
                    return map;
                })
                .collect(Collectors.toList());

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode wrapper = mapper.createObjectNode();
        wrapper.set("orders_with_status", mapper.valueToTree(ordersWithStatus));

        return ResponseEntity.ok(wrapper);
    }

    private JsonNode convertOrderLinesToJson(Optional<List<OrderLineDAO>> orderLinesOpt) {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode responseNode = mapper.createObjectNode();

        responseNode.put("status", "success delivery");

        if (orderLinesOpt.isPresent()) {
            ArrayNode orderLinesArray = mapper.valueToTree(orderLinesOpt.get());
            responseNode.set("order_lines", orderLinesArray);
        } else {
            responseNode.putArray("order_lines");
        }

        return responseNode;
    }


    private ResponseEntity<JsonNode> errorResponse(Exception e) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("error", e.getMessage());
        return ResponseEntity.internalServerError().body(errorNode);
    }
}