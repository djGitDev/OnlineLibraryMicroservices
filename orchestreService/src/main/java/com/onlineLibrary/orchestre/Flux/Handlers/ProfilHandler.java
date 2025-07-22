package com.onlineLibrary.orchestre.Flux.Handlers;

import com.google.gson.JsonObject;
import com.onlineLibrary.orchestre.Flux.MicroservicesClients.ProfilMicroservicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProfilHandler {


    private static final Logger logger = LoggerFactory.getLogger(ProfilHandler.class);
    private ProfilMicroservicesClient profilMicroserviceClient;

    @Autowired
    public ProfilHandler(ProfilMicroservicesClient profilMicroserviceClient) {
        this.profilMicroserviceClient = profilMicroserviceClient;
    }


    public JsonObject handleRegister(JsonObject task) {
        JsonObject result = new JsonObject();
        if (!task.has("user") || !task.has("address")) {
            logger.error("Champs manquants pour l'enregistrement");
            result.addProperty("error", "Missing user or address");
            return result;
        }
        JsonObject registerPayload = new JsonObject();
        registerPayload.add("user", task.get("user"));
        registerPayload.add("address", task.get("address"));

        logger.debug("Appel du microservice register avec payload: {}", registerPayload);
        ResponseEntity<JsonObject> responseRegister = profilMicroserviceClient.callRegister(registerPayload);
        logger.info("Réponse du microservice register reçue");
        return responseRegister.getBody();
    }

    public JsonObject handleLogin(JsonObject task) {
        JsonObject result = new JsonObject();
        if (!task.has("credentials")) {
            logger.error("Credentials manquants pour le login");
            result.addProperty("error", "Missing credentials");
            return result;
        }
        JsonObject loginPayload = new JsonObject();
        loginPayload.add("credentials", task.get("credentials"));

        logger.debug("Appel du microservice login avec payload: {}", loginPayload);
        ResponseEntity<JsonObject> responseLogin = profilMicroserviceClient.callLogin(loginPayload);
        JsonObject responseBody = responseLogin.getBody();
        logger.info("Réponse du microservice login reçue");
        return responseBody;
    }
}
