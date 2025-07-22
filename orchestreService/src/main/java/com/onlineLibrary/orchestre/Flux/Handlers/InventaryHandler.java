package com.onlineLibrary.orchestre.Flux.Handlers;

import com.google.gson.JsonObject;
import com.onlineLibrary.orchestre.Flux.MicroservicesClients.InventaryMicroservicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        ResponseEntity<JsonObject> responseBookId = inventaryMicroserviceClient.callFindBookById(bookId);
        result = responseBookId.getBody();

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
        ResponseEntity<JsonObject> responseIsbn = inventaryMicroserviceClient.callFindBookByIsbn(bookIsbn);
        result = responseIsbn.getBody();

        if (result == null || !result.has("book")) {
            logger.error("Livre non trouvé ou réponse invalide");
            JsonObject errorResult = new JsonObject();
            errorResult.addProperty("error", "Book not found");
            return errorResult;
        }
        return result;
    }

    public JsonObject handleGetAllBooks() {
        ResponseEntity<JsonObject> responseGetBooks = inventaryMicroserviceClient.callGetBooks();
        return responseGetBooks.getBody();
    }

    public JsonObject handleDecreaseBookQuantity(JsonObject task) {
        ResponseEntity<JsonObject> responseDecreaseQuantity = inventaryMicroserviceClient.callDecreaseBookQuantity
                (
                extractBookIdFromJson(task),
                task.get("quantity").getAsInt()
                );
        return responseDecreaseQuantity.getBody();
    }

    private static int extractBookIdFromJson(JsonObject jsonObject) {
        return jsonObject.get("book_id").getAsInt();
    }

}
