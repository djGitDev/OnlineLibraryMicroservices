package com.onlineLibrary.orchestre.Flux.Handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.onlineLibrary.orchestre.Flux.MicroservicesClients.InventaryMicroservicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.onlineLibrary.orchestre.Util.ConvertJsonUtils.jacksonToGson;

@Service
public class InventaryHandler {

    private static final Logger logger = LoggerFactory.getLogger(InventaryHandler.class);
    private InventaryMicroservicesClient inventaryMicroserviceClient;

    @Autowired
    public InventaryHandler(InventaryMicroservicesClient inventaryMicroserviceClient) {
        this.inventaryMicroserviceClient = inventaryMicroserviceClient;
    }



    public JsonObject handleFindBookById(JsonObject task) {
        JsonObject result = new JsonObject();
        if (!task.has("book_id")) {
            logger.error("book_id manquant");
            result.addProperty("error", "Missing book_id");
            return result;
        }

        int bookId = task.get("book_id").getAsInt();
        ResponseEntity<JsonNode> responseBookIdJackson = inventaryMicroserviceClient.callFindBookById(bookId);
        result = jacksonToGson(responseBookIdJackson.getBody());

        if (result == null || !result.has("book")) {
            logger.error("Livre non trouvé ou réponse invalide");
            JsonObject errorResult = new JsonObject();
            errorResult.addProperty("error", "Book not found");
            return errorResult;
        }
        return result;
    }

    public JsonObject handleFindBookByIsbn(JsonObject task) {
        JsonObject result = new JsonObject();
        if (!task.has("isbn")) {
            logger.error("isbn manquant");
            result.addProperty("error", "Missing isbn");
            return result;
        }

        String bookIsbn = task.get("isbn").getAsString();
        ResponseEntity<JsonNode> responseIsbnJackson = inventaryMicroserviceClient.callFindBookByIsbn(bookIsbn);
        result = jacksonToGson(responseIsbnJackson.getBody());


        if (result == null || !result.has("book")) {
            logger.error("Livre non trouvé ou réponse invalide");
            JsonObject errorResult = new JsonObject();
            errorResult.addProperty("error", "Book not found");
            return errorResult;
        }
        return result;
    }

    public JsonObject handleGetAllBooks() {
        ResponseEntity<JsonNode> responseGetBooksJackson = inventaryMicroserviceClient.callGetBooks();
        JsonObject result = jacksonToGson(responseGetBooksJackson.getBody());
        return result;
    }

    public JsonObject handleDecreaseBookQuantity(JsonObject task) {
        ResponseEntity<JsonNode> responseDecreaseQuantityJackson = inventaryMicroserviceClient.callDecreaseBookQuantity
                (
                extractBookIdFromJson(task),
                task.get("quantity").getAsInt()
                );
        JsonObject result = jacksonToGson(responseDecreaseQuantityJackson.getBody());
        return result;
    }

    private static int extractBookIdFromJson(JsonObject jsonObject) {
        return jsonObject.get("book_id").getAsInt();
    }

}
