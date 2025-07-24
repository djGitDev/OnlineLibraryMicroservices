package com.onlineLibrary.orchestre.Flux.MicroservicesClients;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-service", url = "${payment-service.url}")
public interface PaymentMicroservicesClient {
    @PostMapping(path = "/api/payment/{userId}/process/{cartId}", consumes = "application/json")
    ResponseEntity<JsonNode> callProcessPaiement(@PathVariable int userId,
                                                 @PathVariable int cartId,
                                                 @RequestParam(defaultValue = "true") boolean autoDelivery);
}
