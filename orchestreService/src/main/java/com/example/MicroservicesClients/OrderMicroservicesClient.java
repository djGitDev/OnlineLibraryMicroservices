
package com.example.MicroservicesClients;

import com.example.config.GsonConfig;
import com.google.gson.JsonObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(
        name = "order-service",
        url = "${order-service.url}",
        configuration = GsonConfig.FeignConfig.class
)
public interface OrderMicroservicesClient {

    @PostMapping(
            value = "/api/order/{userId}/add-searched-items",
            consumes = "application/json",
            produces = "application/json"
    )
    ResponseEntity<JsonObject> addSearchedItems(
            @PathVariable("userId") int userId,
            @RequestBody JsonObject request,
            @RequestParam("prices") String pricesJson
    );

    @DeleteMapping(
            value = "/api/order/{userId}/clear-cart",
            produces = "application/json"
    )
    ResponseEntity<JsonObject>  callClearCart(@PathVariable("userId") int userId);

    @DeleteMapping(
            value = "/api/order/{userId}/clear-books",
            consumes = "application/json"
    )
    ResponseEntity<JsonObject> callClearBooks(
            @PathVariable("userId") int userId,
            @RequestBody JsonObject booksToRemove
    );

    @PostMapping(
            value = "/api/order/{userId}/place-order",
            produces = "application/json"
    )
    ResponseEntity<JsonObject> callPlaceOrder(
            @PathVariable("userId") int userId,
            @RequestParam("isAutoDelivery") boolean isAutoDelivery
    );
}