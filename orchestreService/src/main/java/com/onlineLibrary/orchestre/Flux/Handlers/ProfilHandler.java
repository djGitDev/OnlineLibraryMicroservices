package com.onlineLibrary.orchestre.Flux.Handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.onlineLibrary.orchestre.Flux.MicroservicesClients.ProfilMicroservicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.function.Supplier;

import static com.onlineLibrary.orchestre.Util.ConvertJsonUtils.gsonToJackson;
import static com.onlineLibrary.orchestre.Util.ConvertJsonUtils.jacksonToGson;

@Service
public class ProfilHandler {


    private static final Logger logger = LoggerFactory.getLogger(ProfilHandler.class);
    private ProfilMicroservicesClient profilMicroserviceClient;

    @Autowired
    public ProfilHandler(ProfilMicroservicesClient profilMicroserviceClient) {
        this.profilMicroserviceClient = profilMicroserviceClient;
    }


    public JsonObject handleRegister(JsonObject task) throws Exception {
        JsonObject result = new JsonObject();
        if (!task.has("user") || !task.has("address")) {
            result.addProperty("error", "Missing user or address");
            return result;
        }
        JsonObject registerPayloadGson = new JsonObject();
        registerPayloadGson.add("user", task.get("user"));
        registerPayloadGson.add("address", task.get("address"));
        JsonNode registerPayload = gsonToJackson(registerPayloadGson);
        ResponseEntity<JsonNode> responseRegisterJackson = profilMicroserviceClient.callRegister(registerPayload);
        result = jacksonToGson(responseRegisterJackson.getBody());
        return result;
    }

    public JsonObject handleLogin(JsonObject task) throws Exception {
        JsonObject result = new JsonObject();
        if (!task.has("credentials")) {
            result.addProperty("error", "Missing credentials");
            return result;
        }
        JsonObject loginPayloadGson = new JsonObject();
        loginPayloadGson.add("credentials", task.get("credentials"));

        JsonNode loginPayload = gsonToJackson(loginPayloadGson);
        ResponseEntity<JsonNode> responseLoginJackson = profilMicroserviceClient.callLogin(loginPayload);
        result = jacksonToGson(responseLoginJackson.getBody());
        String jwt = result.get("jwt").getAsString();
        String bearerToken = "Bearer " + jwt;

        result.addProperty("responseAdminAuth",
                callEndpointSafely(() -> profilMicroserviceClient.callAdminEndpoint(bearerToken))
        );

        result.addProperty("responseUserAuth",
                callEndpointSafely(() -> profilMicroserviceClient.callUserEndpoint(bearerToken))
        );

        return result;
    }

    private String callEndpointSafely(Supplier<String> endpointCall) {
        try {
            return endpointCall.get();
        } catch (Exception e) {
            return "Error: " + e.getClass().getSimpleName();
        }
    }
}
