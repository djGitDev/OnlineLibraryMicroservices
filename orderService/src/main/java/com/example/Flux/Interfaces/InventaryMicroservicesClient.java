package com.example.Flux.Interfaces;

import com.google.gson.JsonObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "services-inventary", url = "${services-inventary.url}")
public interface InventaryMicroservicesClient {

    @PostMapping(path = "/api/books/find", consumes = "application/json")
    ResponseEntity<JsonObject> callFindBookById(@RequestBody JsonObject body);

    @PostMapping(path = "/api/books/quantity", consumes = "application/json")
    ResponseEntity<JsonObject> callDecreaseBookQuantity(@RequestBody JsonObject body);

}