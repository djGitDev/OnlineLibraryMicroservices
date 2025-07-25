
package com.onlineLibrary.orchestre.Flux.MicroservicesClients;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(
        name = "order-service",
        url = "${order-service.url}"
)
public interface OrderMicroservicesClient {

    @PostMapping(
            value = "/api/order/{userId}/place-order",
            produces = "application/json"
    )
    ResponseEntity<JsonNode> callPlaceOrder(
            @PathVariable("userId") int userId,
            @RequestParam("isAutoDelivery") boolean isAutoDelivery
    );
    @GetMapping(
            value = "/api/order",
            consumes = "application/json"
    )
    ResponseEntity<JsonNode> callDisplayAllOrders();


    @PostMapping(
            value = "/api/order/{orderId}/deliver",
            produces = "application/json"
    )
    ResponseEntity<JsonNode> callDeliveryOrder(@PathVariable("orderId") int orderId);


}