package com.onlineLibrary.orchestre.Flux.MicroservicesClients;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "profil-service",
        url = "${profil-service.url}"
)
public interface ProfilMicroservicesClient {

    @PostMapping(path = "/api/profil/register")
    ResponseEntity<JsonNode> callRegister(@RequestBody JsonNode body);

    @PostMapping(path = "/api/profil/login")
    ResponseEntity<JsonNode> callLogin(@RequestBody JsonNode body);

    @GetMapping("/api/verif/profil/actuator/adminEndpoint")
    String callAdminEndpoint(@RequestHeader("Authorization") String authorizationHeader);

    @GetMapping("/api/verif/profil/actuator/userEndpoint")
    String callUserEndpoint(@RequestHeader("Authorization") String authorizationHeader);
}