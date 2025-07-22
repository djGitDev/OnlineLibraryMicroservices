package com.onlineLibrary.orchestre.Flux.Handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onlineLibrary.orchestre.Flux.MicroservicesClients.CartMicroservicesClient;
import com.onlineLibrary.orchestre.Util.WorkFlowStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CartHandler {


    private static final Logger logger = LoggerFactory.getLogger(InventaryHandler.class);
    private CartMicroservicesClient cartMicroserviceClient;
    private WorkFlowStateManager workFlowStateManager;


    @Autowired
    public CartHandler(CartMicroservicesClient cartMicroserviceClient, WorkFlowStateManager workFlowStateManager) {
        this.cartMicroserviceClient = cartMicroserviceClient;
        this.workFlowStateManager = workFlowStateManager;

    }


    public JsonObject handleClearCart() throws Exception {
        ResponseEntity<JsonObject> responseClearCart = cartMicroserviceClient.callClearCart(workFlowStateManager.getLastUserId());
        return responseClearCart.getBody();
    }

    public JsonObject handleClearBooks(JsonObject task) throws Exception {
        ResponseEntity<JsonObject> responseClearBooks = cartMicroserviceClient.callClearBooks(workFlowStateManager.getLastUserId(), task);
        return responseClearBooks.getBody();
    }

    public JsonObject handleAddToCartFromResearch(JsonObject task) throws Exception {
        String serializedPrices = new Gson().toJson(workFlowStateManager.getSearchedBooksIdsWithPrice());
        ResponseEntity<JsonObject> responseAddBooks = cartMicroserviceClient.addSearchedItems(workFlowStateManager.getLastUserId(), task, serializedPrices);
        JsonObject responseBody = responseAddBooks.getBody();
        return responseBody;
    }
}
