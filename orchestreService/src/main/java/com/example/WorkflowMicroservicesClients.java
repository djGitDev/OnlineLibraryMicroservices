package com.example;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.google.gson.JsonObject;

@FeignClient(name = "workflow-service", url = "${workflow-service.url}")
public interface WorkflowMicroservicesClients {

    @PostMapping(path = "/api/profil/register", consumes = "application/json")
    ResponseEntity<JsonObject> callRegister(@RequestBody JsonObject body);

    @PostMapping(path = "/api/profil/login", consumes = "application/json")
    ResponseEntity<JsonObject> callLogin(@RequestBody JsonObject body);
}