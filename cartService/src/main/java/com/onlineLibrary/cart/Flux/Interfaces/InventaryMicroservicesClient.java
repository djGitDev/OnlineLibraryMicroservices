package com.onlineLibrary.cart.Flux.Interfaces;

import com.google.gson.JsonObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "services-inventary", url = "${services-inventary.url}")
public interface InventaryMicroservicesClient {

    @GetMapping(
            value = "/api/books/{book_id}",
            produces = "application/json"
    )
    ResponseEntity<JsonObject> callFindBookById(@PathVariable("book_id") int bookId);

    @PutMapping(path = "/api/books/{book_id}/quantity", consumes = "application/json")
    ResponseEntity<JsonObject> callDecreaseBookQuantity(@PathVariable("book_id") int bookId,
                                                        @RequestParam int quantity);

}