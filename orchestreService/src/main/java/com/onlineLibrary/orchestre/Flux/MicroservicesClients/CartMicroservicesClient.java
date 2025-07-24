package com.onlineLibrary.orchestre.Flux.MicroservicesClients;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "cart-service",
        url = "${cart-service.url}"
)

public interface CartMicroservicesClient {


    @DeleteMapping(path = "/api/cart/{user_id}/clear-cart")
    ResponseEntity<JsonNode> callClearCart(@PathVariable("user_id") int user_id);


    @DeleteMapping(path = "/api/cart/{userId}/clear-books")
    ResponseEntity<JsonNode> callClearBooks(
            @PathVariable("userId") int userId,
            @RequestBody JsonNode requestBody);

    @PostMapping(path = "/api/cart/{userId}/add-searched-items")
    ResponseEntity<JsonNode> addSearchedItems(@PathVariable("userId") int lastUserId,
                                                @RequestBody JsonNode task,
                                                @RequestParam("prices") String serializedPrices);
}
