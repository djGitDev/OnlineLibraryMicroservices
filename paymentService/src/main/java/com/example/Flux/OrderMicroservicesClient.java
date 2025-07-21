package com.example.Flux;

import com.google.gson.JsonObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "services-order", url = "${services-order.url}")
public interface OrderMicroservicesClient {

    @GetMapping(path = "/api/order/carts/{cartId}/total-price", consumes = "application/json")
    ResponseEntity<JsonObject> callGetTotalPriceCart(@PathVariable("cartId") int cartId);


    @PostMapping(
            value = "/api/order/{userId}/place-order",
            produces = "application/json"
    )
    ResponseEntity<JsonObject> callPlaceOrder(@PathVariable("userId") int userId,
                                              @RequestParam("isAutoDelivery") boolean isAutoDelivery);
}