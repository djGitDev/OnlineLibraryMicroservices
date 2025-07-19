package com.example.Flux;

import com.google.gson.JsonObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "services-profile", url = "${services-profile.url}")
public interface ProfilMicroservicesClient {
    @GetMapping(path = "/api/data", consumes = "application/json")
    ResponseEntity<JsonObject> callGetUserData(@RequestBody JsonObject body);
}
