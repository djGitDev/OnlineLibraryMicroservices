package com.onlineLibrary.orchestre.Flux.MicroservicesClients;


import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventary-service", url = "${inventary-service.url}")
public interface InventaryMicroservicesClient {

    @GetMapping(value = "/api/books/{id}")
    ResponseEntity<JsonNode> callFindBookById(@PathVariable("id") int id);

    @GetMapping(
            value = "/api/books/research/{isbn}",
            produces = "application/json"
    )
    ResponseEntity<JsonNode> callFindBookByIsbn(@PathVariable("isbn") String isbn);

    @PutMapping(
            value = "/api/books/{id}/quantity",
            produces = "application/json"
    )
    ResponseEntity<JsonNode> callDecreaseBookQuantity(
            @PathVariable("id") int id,
            @RequestParam("quantity") int quantity
    );

    @GetMapping(
            value = "/api/books",
            produces = "application/json"
    )
    ResponseEntity<JsonNode> callGetBooks();

}
