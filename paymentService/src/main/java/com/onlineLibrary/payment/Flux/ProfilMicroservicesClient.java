package com.onlineLibrary.payment.Flux;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "services-profile", url = "${services-profile.url}")
public interface ProfilMicroservicesClient {
    @GetMapping(path = "/api/profil/data/{user_id}")
    ResponseEntity<JsonNode> callGetUserData(@PathVariable("user_id") int userId);
}
