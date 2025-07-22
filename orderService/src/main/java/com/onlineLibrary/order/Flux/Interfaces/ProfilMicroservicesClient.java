package com.onlineLibrary.order.Flux.Interfaces;

import com.google.gson.JsonObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "services-profile", url = "${services-profile.url}")
public interface ProfilMicroservicesClient {
    @GetMapping(path = "/api/profil/{user_id}", consumes = "application/json")
    ResponseEntity<JsonObject> callGetUserProfil(@PathVariable("user_id") int user_id);
}
