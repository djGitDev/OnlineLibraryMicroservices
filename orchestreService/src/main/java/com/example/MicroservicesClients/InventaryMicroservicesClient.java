package com.example.MicroservicesClients;


import com.example.config.GsonConfig;
import com.google.gson.JsonObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventary-service", url = "${inventary-service.url}",configuration = GsonConfig.FeignConfig.class
)
public interface InventaryMicroservicesClient {

    @GetMapping(
            value = "/api/books/{id}",
            produces = "application/json"
    )
    ResponseEntity<JsonObject> callFindBookById(@PathVariable("id") int id);

    @GetMapping(
            value = "/api/books/research/{isbn}",
            produces = "application/json"
    )
    ResponseEntity<JsonObject> callFindBookByIsbn(@PathVariable("isbn") String isbn);

    @PutMapping(
            value = "/api/books/{id}/quantity",
            produces = "application/json"
    )
    ResponseEntity<JsonObject> callDecreaseBookQuantity(
            @PathVariable("id") int id,
            @RequestParam("quantity") int quantity
    );}
