package com.onlineLibrary.order.Flux.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "services-inventary", url = "${services-inventary.url}")
public interface InventaryMicroservicesClient {

    @GetMapping(path = "/api/books/{book_id}")
    ResponseEntity<JsonNode> callFindBookById(@PathVariable("book_id") int bookId);

    @PutMapping(path = "/api/books/{book_id}/quantity")
    ResponseEntity<JsonNode> callDecreaseBookQuantity(@PathVariable("book_id") int bookId,
                                                        @RequestParam int quantity);

}