package com.onlineLibrary.orchestre.Flux.Handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onlineLibrary.orchestre.Flux.MicroservicesClients.CartMicroservicesClient;
import com.onlineLibrary.orchestre.Util.WorkFlowStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.onlineLibrary.orchestre.Util.ConvertJsonUtils.gsonToJackson;
import static com.onlineLibrary.orchestre.Util.ConvertJsonUtils.jacksonToGson;

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
        ResponseEntity<JsonNode> responseClearCart = cartMicroserviceClient.callClearCart(workFlowStateManager.getLastUserId());
        return jacksonToGson(responseClearCart.getBody());
    }

    public JsonObject handleClearBooks(JsonObject task) throws Exception {
        ResponseEntity<JsonNode> responseClearBooks = cartMicroserviceClient.callClearBooks(workFlowStateManager.getLastUserId(), gsonToJackson(task));
        return jacksonToGson(responseClearBooks.getBody());
    }

    public JsonObject handleAddToCartFromResearch(JsonObject task) throws Exception {
        String serializedPrices = new Gson().toJson(workFlowStateManager.getSearchedBooksIdsWithPrice());
        ResponseEntity<JsonNode> responseAddBooks = cartMicroserviceClient.addSearchedItems(workFlowStateManager.getLastUserId(), gsonToJackson(task), serializedPrices);
        return jacksonToGson(responseAddBooks.getBody());
    }
}
