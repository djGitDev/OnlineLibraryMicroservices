package com.example.Flux;

import com.google.gson.JsonObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "services-order", url = "${services-order.url}")
public interface OrderMicroservicesClient {

    @GetMapping(path = "/api/books/total-price", consumes = "application/json")
    ResponseEntity<JsonObject> callGetTotalPriceCart(@RequestBody JsonObject body);

    @PostMapping(path = "/api/books/quantity", consumes = "application/json")
    ResponseEntity<JsonObject> callPlaceOrder(@RequestBody JsonObject body);

}