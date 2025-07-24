package com.onlineLibrary.order.Flux.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "services-cart", url = "${services-cart.url}")
public interface CartMicroservicesClient {

    @DeleteMapping(path = "/api/cart/{user_id}/clear-cart")
    ResponseEntity<JsonNode> callClearCart(@PathVariable("user_id") int user_id);


    @GetMapping(path = "/api/cart/{user_id}")
    ResponseEntity<JsonNode> callGetCart(@PathVariable("user_id") int user_id);

    @GetMapping(path = "/api/cart/items/{cart_id}")
    ResponseEntity<JsonNode> callGetItems(@PathVariable("cart_id") int cart_id);
}
