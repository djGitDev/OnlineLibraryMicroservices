
package com.onlineLibrary.orchestre.ControllersWorkFlows;

import com.onlineLibrary.orchestre.Flux.Handlers.InventaryHandler;
import com.onlineLibrary.orchestre.Flux.Handlers.OrderHandler;
import com.onlineLibrary.orchestre.Flux.Handlers.PaymentHandler;
import com.onlineLibrary.orchestre.Flux.Handlers.ProfilHandler;
import com.onlineLibrary.orchestre.Util.WorkFlowStateManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/workflow")
public class WorkFlowProcessController {

    private static final Logger logger = LoggerFactory.getLogger(WorkFlowProcessController.class);


    private ProfilHandler profilHandler;
    private InventaryHandler inventaryHandler;
    private OrderHandler orderHandler;
    private PaymentHandler paymentHandler;
    private WorkFlowStateManager workFlowStateManager;

    @Autowired
    public void setProfilHandler(ProfilHandler profilHandler) {}

    @Autowired
    public WorkFlowProcessController(ProfilHandler profilHandler, InventaryHandler inventaryHandler, OrderHandler orderHandler, PaymentHandler paymentHandler,WorkFlowStateManager workFlowStateManager) {
        this.profilHandler = profilHandler;
        this.inventaryHandler = inventaryHandler;
        this.orderHandler = orderHandler;
        this.paymentHandler = paymentHandler;
        this.workFlowStateManager = workFlowStateManager;
    }



    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<JsonArray> orchestrate(@RequestBody JsonArray inputArray) {
        logger.info("Starting request processing - Received array of {} elements", inputArray.size());
        System.out.println("[DEBUG] Request received: " + inputArray);

        JsonArray resultsArray = new JsonArray();
        logger.debug("Initializing results array");

        for (int i = 0; i < inputArray.size(); i++) {
            logger.info("Processing element {} of {}", i + 1, inputArray.size());
            System.out.println("[PROCESS] Handling element " + (i + 1));

            JsonObject task = inputArray.get(i).getAsJsonObject();
            JsonObject result = new JsonObject();

            if (!task.has("action")) {
                logger.warn("Element {}: Missing action", i);
                System.out.println("[WARN] Missing action for element " + i);

                result.addProperty("error", "Missing action");
                resultsArray.add(result);
                continue;
            }

            String action = task.get("action").getAsString();
            logger.debug("Element {}: Detected action - {}", i, action);
            System.out.println("[INFO] Action: " + action);
            result = processAction(action, task);
            resultsArray.add(result);
            logger.debug("Result added for element {}: {}", i, result);
        }

        logger.info("Processing completed - Returning {} results", resultsArray.size());
        System.out.println("[DEBUG] Final results: " + resultsArray);
        return ResponseEntity.ok(resultsArray);
    }


    private JsonObject processAction(String action, JsonObject task) {

        JsonObject result = new JsonObject();
        try {
            switch (action) {
                case "register":
                    result = profilHandler.handleRegister(task);
                    break;
                case "login":
                    result = profilHandler.handleLogin(task);
                    workFlowStateManager.addUserId(result.get("user_id").getAsInt());
                    break;
                case "find_book_by_id":
                    result = inventaryHandler.handleFindBookById(task);
                    JsonObject book = result.get("book").getAsJsonObject();
                    int id = book.get("id").getAsInt();
                    double price = book.get("price").getAsDouble();
                    workFlowStateManager.addSearchedBooksId(id, price);
                    break;
                case "find_book_by_isbn":
                    result = inventaryHandler.handleFindBookByIsbn(task);
                    JsonObject bookISBN = result.get("book").getAsJsonObject();
                    int idBook = bookISBN.get("id").getAsInt();
                    double priceBook = bookISBN.get("price").getAsDouble();
                    workFlowStateManager.addSearchedBooksId(idBook, priceBook);
                    break;
                case "get_all_books":
                    result = inventaryHandler.handleGetAllBooks();
                    break;
                case "decrease_book_quantity":
                    result = inventaryHandler.handleDecreaseBookQuantity(task);
                    break;
                case "add_to_cart_from_research":
                    result = orderHandler.handleAddToCartFromResearch(task);
                    workFlowStateManager.setCurrentCartId(result.get("cartId").getAsInt());
                    break;
                case "clear_cart":
                    result = orderHandler.handleClearCart();
                    break;
                case "clear_books":
                    result = orderHandler.handleClearBooks(task);
                    break;
                case "place_order":
                    result = orderHandler.handlePlaceOrder();
                    workFlowStateManager.addOrderId(result.get("orderId").getAsInt());
                    break;
                case "place_order_with_delivery":
                    result = orderHandler.handlePlaceOrderWithDelivery();
                    workFlowStateManager.addOrderId(result.get("orderId").getAsInt());
                    break;
                case "process_payment":
                    result = paymentHandler.handleProcessPayment();
                    break;
                case "display_orders_delivred_and_not":
                    result = orderHandler.handleDisplayOrdersDeliveredAndNot();
                    break;
                case "deliver_order":
                    result = orderHandler.handleDeliverOrder();
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
        return result;
    }


}