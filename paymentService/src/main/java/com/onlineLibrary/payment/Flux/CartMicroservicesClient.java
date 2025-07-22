package com.onlineLibrary.payment.Flux;


import com.google.gson.JsonObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "services-cart", url = "${services-cart.url}")
public interface CartMicroservicesClient {

    @GetMapping(path = "/api/cart/carts/{cartId}/total-price", consumes = "application/json")
    ResponseEntity<JsonObject> callGetTotalPriceCart(@PathVariable("cartId") int cartId);

}
