package com.onlineLibrary.orchestre.Flux.MicroservicesClients;

import com.google.gson.JsonObject;
import com.onlineLibrary.orchestre.Config.GsonConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "cart-service",
        url = "${cart-service.url}",
        configuration = GsonConfig.FeignConfig.class
)

public interface CartMicroservicesClient {


    @DeleteMapping(path = "/api/cart/{user_id}/clear-cart",produces = "application/json")
    ResponseEntity<JsonObject> callClearCart(@PathVariable("user_id") int user_id);


    @DeleteMapping(path = "/api/cart/{userId}/clear-books",consumes = "application/json",produces = "application/json")
    ResponseEntity<JsonObject> callClearBooks(
            @PathVariable("userId") int userId,
            @RequestBody JsonObject requestBody);

    @PostMapping(path = "/api/cart/{userId}/add-searched-items",consumes = "application/json",
            produces = "application/json")
    ResponseEntity<JsonObject> addSearchedItems(@PathVariable("userId") int lastUserId,
                                                @RequestBody JsonObject task,
                                                @RequestParam("prices") String serializedPrices);
}
