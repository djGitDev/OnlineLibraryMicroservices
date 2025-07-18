package com.example;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/workflow")
@CrossOrigin(origins = "http://localhost:5173") // Autorise votre frontend React
public class WorkFlowProcessController {

    private static final Logger logger = LoggerFactory.getLogger(WorkFlowProcessController.class);

    @Autowired
    private WorkflowMicroservicesClients microserviceClient;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<JsonArray> orchestrate(@RequestBody JsonArray inputArray) {
        logger.info("Début du traitement de la requête - Réception d'un tableau de {} éléments", inputArray.size());
        System.out.println("[DEBUG] Requête reçue : " + inputArray);

        JsonArray resultsArray = new JsonArray();
        logger.debug("Initialisation du tableau de résultats");

        for (int i = 0; i < inputArray.size(); i++) {
            logger.info("Traitement de l'élément {} sur {}", i+1, inputArray.size());
            System.out.println("[PROCESS] Traitement de l'élément " + (i+1));

            JsonObject task = inputArray.get(i).getAsJsonObject();
            JsonObject result = new JsonObject();

            if (!task.has("action")) {
                logger.warn("Élément {}: Action manquante", i);
                System.out.println("[WARN] Action manquante pour l'élément " + i);

                result.addProperty("error", "Missing action");
                resultsArray.add(result);
                continue;
            }

            String action = task.get("action").getAsString();
            logger.debug("Élément {}: Action détectée - {}", i, action);
            System.out.println("[INFO] Action: " + action);

            try {
                switch (action) {
                    case "register":
                        logger.info("Traitement d'une action 'register'");
                        if (!task.has("user") || !task.has("address")) {
                            logger.error("Champs manquants pour l'enregistrement");
                            result.addProperty("error", "Missing user or address");
                            break;
                        }
                        JsonObject registerPayload = new JsonObject();
                        registerPayload.add("user", task.get("user"));
                        registerPayload.add("address", task.get("address"));

                        logger.debug("Appel du microservice register avec payload: {}", registerPayload);
                        ResponseEntity<JsonObject> responseRegister = microserviceClient.callRegister(registerPayload);
                        System.out.println(responseRegister);
                        result = responseRegister.getBody();
                        System.out.println(result);
                        logger.info("Réponse du microservice register reçue");
                        break;

                    case "login":
                        logger.info("Traitement d'une action 'login'");
                        if (!task.has("credentials")) {
                            logger.error("Credentials manquants pour le login");
                            result.addProperty("error", "Missing credentials");
                            break;
                        }
                        JsonObject loginPayload = new JsonObject();
                        loginPayload.add("credentials", task.get("credentials"));

                        logger.debug("Appel du microservice login avec payload: {}", loginPayload);
                        ResponseEntity<JsonObject> responseLogin = microserviceClient.callLogin(loginPayload);
                        result = responseLogin.getBody();
                        System.out.println(responseLogin);

                        logger.info("Réponse du microservice login reçue");
                        break;

                    default:
                        logger.warn("Action inconnue: {}", action);
                        result.addProperty("error", "Unknown action: " + action);
                }
            } catch (Exception e) {
                logger.error("Erreur lors du traitement de l'action '{}': {}", action, e.getMessage(), e);
                System.out.println("[ERROR] Erreur: " + e.getMessage());

                result.addProperty("action", action);
                result.addProperty("error", "Failed to process: " + e.getMessage());
            }

            resultsArray.add(result);
            logger.debug("Résultat ajouté pour l'élément {}: {}", i, result);
        }

        logger.info("Traitement terminé - Retour de {} résultats", resultsArray.size());
        System.out.println("[DEBUG] Résultats finaux : " + resultsArray);

        return ResponseEntity.ok(resultsArray);
    }
}