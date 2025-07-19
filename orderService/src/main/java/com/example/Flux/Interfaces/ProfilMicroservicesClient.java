package com.example.Flux.Interfaces;

import com.google.gson.JsonObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "services-profile", url = "${services-profile.url}")
public interface ProfilMicroservicesClient {
    @PostMapping(path = "/api/profil", consumes = "application/json")
    ResponseEntity<JsonObject> callGetUserProfil(@RequestBody JsonObject body);
}
