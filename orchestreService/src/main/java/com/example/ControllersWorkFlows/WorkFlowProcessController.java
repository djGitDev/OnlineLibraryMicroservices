package com.example.ControllersWorkFlows;

import com.example.MicroservicesClients.InventaryMicroservicesClient;
import com.example.MicroservicesClients.OrderMicroservicesClient;
import com.example.MicroservicesClients.PaymentMicroservicesClient;
import com.example.MicroservicesClients.ProfilMicroservicesClient;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workflow")
@CrossOrigin(origins = "http://localhost:5173") // Autorise votre frontend React
public class WorkFlowProcessController {

    private static final Logger logger = LoggerFactory.getLogger(WorkFlowProcessController.class);
    private static final List<Integer> userIds = new ArrayList<>();
    private static final List<Integer> orderIds = new ArrayList<>();
    private static final Map<Integer, Double> searchedBooksIdsWithPrice = new HashMap<>();
    private static Integer currentCartId = 0;


    private ProfilMicroservicesClient profilMicroserviceClient;
    private InventaryMicroservicesClient inventaryMicroserviceClient;
    private OrderMicroservicesClient orderMicroserviceClient;
    private PaymentMicroservicesClient paymentMicroserviceClient;


    @Autowired
    public WorkFlowProcessController(ProfilMicroservicesClient profilMicroserviceClient, InventaryMicroservicesClient inventaryMicroserviceClient, OrderMicroservicesClient orderMicroserviceClient, PaymentMicroservicesClient paymentMicroserviceClient) {
        this.profilMicroserviceClient = profilMicroserviceClient;
        this.inventaryMicroserviceClient = inventaryMicroserviceClient;
        this.orderMicroserviceClient = orderMicroserviceClient;
        this.paymentMicroserviceClient = paymentMicroserviceClient;
    }

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
                        ResponseEntity<JsonObject> responseRegister = profilMicroserviceClient.callRegister(registerPayload);
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
                        ResponseEntity<JsonObject> responseLogin = profilMicroserviceClient.callLogin(loginPayload);
                        result = responseLogin.getBody();
                        System.out.println(responseLogin);
                        userIds.add(result.get("user_id").getAsInt());
                        logger.info("Réponse du microservice login reçue");
                        break;

                    case "find_book_by_id":
                        logger.info("Traitement d'une action 'find_book_by_id'");

                        if (!task.has("book_id")) {
                            logger.error("book_id manquant");
                            result.addProperty("error", "Missing book_id");
                            break;
                        }

                        int bookId = task.get("book_id").getAsInt();
                        ResponseEntity<JsonObject> responseBookId = inventaryMicroserviceClient.callFindBookById(bookId);
                        result = responseBookId.getBody();

                        if (result == null || !result.has("book")) {
                            logger.error("Livre non trouvé ou réponse invalide");
                            result.addProperty("error", "Book not found");
                            break;
                        }

                        JsonObject book = result.get("book").getAsJsonObject();
                        int id = book.get("id").getAsInt();
                        double price = book.get("price").getAsDouble();
                        searchedBooksIdsWithPrice.put(id, price);
                        logger.info("Livre trouvé - ID: {}, Prix: {}", id, price);
                        break;

                    case "find_book_by_isbn":
                        logger.info("Traitement d'une action 'find_book_by_isbn'");

                        if (!task.has("isbn")) {
                            logger.error("isbn manquant");
                            result.addProperty("error", "Missing isbn");
                            break;
                        }

                        String bookIsbn = task.get("isbn").getAsString();
                        ResponseEntity<JsonObject> responseIsbn = inventaryMicroserviceClient.callFindBookByIsbn(bookIsbn);
                        result = responseIsbn.getBody();

                        if (result == null || !result.has("book")) {
                            logger.error("Livre non trouvé ou réponse invalide");
                            result.addProperty("error", "Book not found");
                            break;
                        }

                        JsonObject bookISBN = result.get("book").getAsJsonObject();
                        int idBook = bookISBN.get("id").getAsInt();
                        double priceBook = bookISBN.get("price").getAsDouble();
                        searchedBooksIdsWithPrice.put(idBook, priceBook);

                        logger.info("Livre trouvé - ID: {}, Prix: {}", idBook, priceBook);
                        break;
                    case "decrease_book_quantity" :
                        ResponseEntity<JsonObject> responseDecreaseQuantity = inventaryMicroserviceClient.callDecreaseBookQuantity(extractBookIdFromJson(task), task.get("quantity").getAsInt());
                        result = responseDecreaseQuantity.getBody();
                        break;

                    case "add_to_cart_from_research":
                        logger.info("Traitement d'une action 'add_to_cart_from_research'");

                        if (!task.has("books")) {
                            logger.error("books_manquant");
                            result.addProperty("error", "Missing books");
                            break;
                        }
                        String serializedPrices = new Gson().toJson(searchedBooksIdsWithPrice);
                        ResponseEntity<JsonObject> responseAddBooks = orderMicroserviceClient.addSearchedItems(getLastUserId(),task,serializedPrices);
                        result = responseAddBooks.getBody();
                        currentCartId = result.get("cartId").getAsInt();

                        break;

                    case "clear_cart":
                        ResponseEntity<JsonObject> responseClearCart = orderMicroserviceClient.callClearCart(getLastUserId());
                        result = responseClearCart.getBody();
                        break;

                    case "clear_books":
                        ResponseEntity<JsonObject> responseClearBooks = orderMicroserviceClient.callClearBooks(getLastUserId(), task);
                        result = responseClearBooks.getBody();
                        break;

                    case "place_order" :
                        ResponseEntity<JsonObject> responseOrder =  orderMicroserviceClient.callPlaceOrder(getLastUserId(), false);
                        result = responseOrder.getBody();
                        orderIds.add(result.get("orderId").getAsInt());
                        break;

                    case "process_payment" :
                        System.out.println(getLastUserId());
                        System.out.println(currentCartId);

                        ResponseEntity<JsonObject> responsePayment = paymentMicroserviceClient.callProcessPaiement(currentCartId,getLastUserId(),true);
                        result = responsePayment.getBody();
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

    private static int getLastUserId() throws Exception {
        if (userIds.isEmpty())
            throw new Exception("Aucun utilisateur connecté.");
        return userIds.get(userIds.size() - 1);
    }

    private static int extractBookIdFromJson(JsonObject jsonObject) {
        return jsonObject.get("book_id").getAsInt();
    }

}