package com.onlineLibrary.orchestre.Flux.Handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.onlineLibrary.orchestre.Flux.MicroservicesClients.OrderMicroservicesClient;
import com.onlineLibrary.orchestre.Util.WorkFlowStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.onlineLibrary.orchestre.Util.ConvertJsonUtils.jacksonToGson;

@Service
public class OrderHandler {

    private static final Logger logger = LoggerFactory.getLogger(InventaryHandler.class);
    private OrderMicroservicesClient orderMicroserviceClient;
    private WorkFlowStateManager workFlowStateManager;


    @Autowired
    public OrderHandler(OrderMicroservicesClient orderMicroserviceClient,WorkFlowStateManager workFlowStateManager) {
        this.orderMicroserviceClient = orderMicroserviceClient;
        this.workFlowStateManager = workFlowStateManager;

    }


    public JsonObject handlePlaceOrder() throws Exception {
        ResponseEntity<JsonNode> responseOrder = orderMicroserviceClient.callPlaceOrder(workFlowStateManager.getLastUserId(), false);
        JsonNode responseBody = responseOrder.getBody();
        return jacksonToGson(responseBody);

    }

    public JsonObject handlePlaceOrderWithDelivery() throws Exception {
        ResponseEntity<JsonNode> responseOrderDelivered = orderMicroserviceClient.callPlaceOrder(workFlowStateManager.getLastUserId(), true);
        JsonNode responseBody = responseOrderDelivered.getBody();
        return jacksonToGson(responseBody);
    }


    public JsonObject handleDisplayOrdersDeliveredAndNot() {
        ResponseEntity<JsonNode> responseOrders = orderMicroserviceClient.callDisplayAllOrders();
        JsonNode responseBody = responseOrders.getBody();
        return jacksonToGson(responseBody);
    }

    public JsonObject handleDeliverOrder() throws Exception {
        ResponseEntity<JsonNode> responseDeliver = orderMicroserviceClient.callDeliveryOrder(workFlowStateManager.getLastUserId());
        JsonNode responseBody = responseDeliver.getBody();
        return jacksonToGson(responseBody);
    }
}
