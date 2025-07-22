package com.onlineLibrary.profil.ControllerRestProfilApi;

import com.onlineLibrary.profil.Flux.IProfilServiceDispatcher;
import com.onlineLibrary.profil.UtilProfil.BeansInjectionFactory;
import com.onlineLibrary.profil.UtilProfil.IBeansInjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/profil")
public class ProfilRestController {

    private static final Logger logger = LoggerFactory.getLogger(ProfilRestController.class);

    private IProfilServiceDispatcher dispatcher;

    public ProfilRestController() {
        IBeansInjectionFactory factory = BeansInjectionFactory.getInstance();
        this.dispatcher = factory.getProfilServiceDispatcher();
    }

    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JsonObject> registerUser(@RequestBody JsonObject data) {
        logger.info("Début de l'enregistrement utilisateur - Données reçues: {}", data);
        try {
            JsonObject result = dispatcher.handleRegistration(data);
            logger.info("Enregistrement réussi - Résultat: {}", result);
            ResponseEntity<JsonObject> response = ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(result);
            debugResponse(response);

            return response;
        } catch (Exception e) {
            logger.error("Échec de l'enregistrement - Erreur: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(errorJson(e));
        }
    }

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JsonObject> authentifyUser(@RequestBody JsonObject data) {
        logger.info("Tentative de connexion - Données reçues: {}", data);
        try {
            JsonObject result = dispatcher.handleLogin(data);
            logger.info("Connexion réussie - Résultat: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.warn("Échec de la connexion - Erreur: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorJson(e));
        }
    }

    @GetMapping(path = "/{userId}", produces = "application/json")
    public ResponseEntity<JsonObject> getUserProfil(@PathVariable int userId) {
        logger.info("Récupération du profil pour l'utilisateur ID: {}", userId);
        try {
            JsonObject result = dispatcher.getProfile(userId);
            if (result == null) {
                logger.warn("Profil non trouvé pour l'utilisateur ID: {}", userId);
                return ResponseEntity.notFound().build();
            }
            logger.debug("Profil trouvé: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du profil - ID: {} - Erreur: {}", userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(errorJson(e));
        }
    }

    @GetMapping(path = "/data/{userId}", produces = "application/json")
    public ResponseEntity<JsonObject> getUserData(@PathVariable int userId) {
        logger.info("Récupération des données pour l'utilisateur ID: {}", userId);
        try {
            JsonObject result = dispatcher.getUserData(userId);
            if (result == null) {
                logger.warn("Données non trouvées pour l'utilisateur ID: {}", userId);
                return ResponseEntity.notFound().build();
            }
            logger.debug("Données utilisateur trouvées: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des données - ID: {} - Erreur: {}", userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(errorJson(e));
        }
    }

    private JsonObject errorJson(Exception e) {
        logger.debug("Création de la réponse d'erreur: {}", e.getMessage());
        JsonObject json = new JsonObject();
        json.addProperty("error", e.getMessage());
        return json;
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
