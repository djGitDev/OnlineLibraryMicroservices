package com.onlineLibrary.payment.Flux;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "services-order", url = "${services-order.url}")
public interface OrderMicroservicesClient {


    @PostMapping(path = "/api/order/{userId}/place-order")
    ResponseEntity<JsonNode> callPlaceOrder(@PathVariable("userId") int userId,
                                            @RequestParam("isAutoDelivery") boolean isAutoDelivery);
}