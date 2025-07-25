package com.onlineLibrary.profil.ControllerRestProfilApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.onlineLibrary.profil.Flux.IProfilServiceDispatcher;
import com.onlineLibrary.profil.Util.ConvertJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.onlineLibrary.profil.Util.ConvertJsonUtils.gsonToJackson;
import static com.onlineLibrary.profil.Util.ConvertJsonUtils.jacksonToGson;

@RestController
@RequestMapping("/api/profil")
public class ProfilRestController {

    private static final Logger logger = LoggerFactory.getLogger(ProfilRestController.class);

    private IProfilServiceDispatcher dispatcher;

    @Autowired
    public ProfilRestController(IProfilServiceDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<JsonNode> registerUser(@RequestBody JsonNode dataJacksson) throws Exception {
        logger.info("Début de l'enregistrement utilisateur - Données reçues: {}", dataJacksson);
        try {
            JsonObject data = jacksonToGson(dataJacksson);
            JsonObject resultGson = dispatcher.handleRegistration(data);
            logger.info("Enregistrement réussi - Résultat: {}", resultGson);
            JsonNode result = gsonToJackson(resultGson);
            ResponseEntity<JsonNode> response = ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(result);
            debugResponse(response);

            return response;
        } catch (Exception e) {
            logger.error("Échec de l'enregistrement - Erreur: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(errorResponse(e).getBody());
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<JsonNode> authentifyUser(@RequestBody JsonNode dataJacksson) throws Exception {
        logger.info("Tentative de connexion - Données reçues: {}", dataJacksson);
        try {
            JsonObject data = jacksonToGson(dataJacksson);
            JsonObject resultGson = dispatcher.handleLogin(data);
            logger.info("Connexion réussie - Résultat: {}", resultGson);
            JsonNode result = gsonToJackson(resultGson);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.warn("Échec de la connexion - Erreur: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse(e).getBody());
        }
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<JsonNode> getUserProfil(@PathVariable int userId) throws Exception {
        logger.info("Récupération du profil pour l'utilisateur ID: {}", userId);
        try {
            JsonObject resultGson = dispatcher.getProfile(userId);
            JsonNode result = gsonToJackson(resultGson);

            if (result == null) {
                logger.warn("Profil non trouvé pour l'utilisateur ID: {}", userId);
                return ResponseEntity.notFound().build();
            }
            logger.debug("Profil trouvé: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du profil - ID: {} - Erreur: {}", userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(errorResponse(e).getBody());
        }
    }

    @GetMapping(path = "/data/{userId}")
    public ResponseEntity<JsonNode> getUserData(@PathVariable int userId) throws Exception {
        logger.info("Récupération des données pour l'utilisateur ID: {}", userId);
        try {
            JsonObject resultGson = dispatcher.getUserData(userId);
            JsonNode result = gsonToJackson(resultGson);

            if (result == null) {
                logger.warn("Données non trouvées pour l'utilisateur ID: {}", userId);
                return ResponseEntity.notFound().build();
            }
            logger.debug("Données utilisateur trouvées: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des données - ID: {} - Erreur: {}", userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(errorResponse(e).getBody());
        }
    }

    private ResponseEntity<JsonNode> errorResponse(Exception e) throws Exception {
        JsonObject gsonError = new JsonObject();
        gsonError.addProperty("error", e.getMessage());
        JsonNode jacksonError = ConvertJsonUtils.gsonToJackson(gsonError);
        return ResponseEntity.internalServerError().body(jacksonError);
    }

    private void debugResponse(ResponseEntity<?> response) {
        System.out.println("----- DEBUG RESPONSE ENTITY -----");


        // Affiche le body
        System.out.println("Body:");
        if (response.getBody() instanceof JsonObject) {
            System.out.println(((JsonObject) response.getBody()).toString());
        } else {
            System.out.println(response.getBody());
        }

        System.out.println("--------------------------------");
    }
}
