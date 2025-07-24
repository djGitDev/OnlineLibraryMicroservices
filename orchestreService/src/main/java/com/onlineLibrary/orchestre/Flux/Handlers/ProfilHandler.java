package com.onlineLibrary.orchestre.Flux.Handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.onlineLibrary.orchestre.Flux.MicroservicesClients.ProfilMicroservicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
            logger.error("Champs manquants pour l'enregistrement");
            result.addProperty("error", "Missing user or address");
            return result;
        }
        JsonObject registerPayloadGson = new JsonObject();
        registerPayloadGson.add("user", task.get("user"));
        registerPayloadGson.add("address", task.get("address"));

        logger.debug("Appel du microservice register avec payload: {}", registerPayloadGson);
        JsonNode registerPayload = gsonToJackson(registerPayloadGson);
        logger.info("Payload JACKSON envoyé: {}", registerPayload.toString());

        ResponseEntity<JsonNode> responseRegisterJackson = profilMicroserviceClient.callRegister(registerPayload);
        logger.info("Réponse du microservice register reçue");
        result = jacksonToGson(responseRegisterJackson.getBody());
        return result;
    }

    public JsonObject handleLogin(JsonObject task) throws Exception {
        JsonObject result = new JsonObject();
        if (!task.has("credentials")) {
            logger.error("Credentials manquants pour le login");
            result.addProperty("error", "Missing credentials");
            return result;
        }
        JsonObject loginPayloadGson = new JsonObject();
        loginPayloadGson.add("credentials", task.get("credentials"));

        logger.debug("Appel du microservice login avec payload: {}", loginPayloadGson);
        JsonNode loginPayload = gsonToJackson(loginPayloadGson);
        ResponseEntity<JsonNode> responseLoginJackson = profilMicroserviceClient.callLogin(loginPayload);
        logger.info("Réponse du microservice login reçue");
        result = jacksonToGson(responseLoginJackson.getBody());
        return result;
    }
}
