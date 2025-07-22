package com.onlineLibrary.orchestre.Flux.Handlers;

import com.google.gson.JsonObject;
import com.onlineLibrary.orchestre.Flux.MicroservicesClients.OrderMicroservicesClient;
import com.onlineLibrary.orchestre.Util.WorkFlowStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        ResponseEntity<JsonObject> responseOrder = orderMicroserviceClient.callPlaceOrder(workFlowStateManager.getLastUserId(), false);
        JsonObject responseBody = responseOrder.getBody();
        return responseBody;
    }

    public JsonObject handlePlaceOrderWithDelivery() throws Exception {
        ResponseEntity<JsonObject> responseOrderDelivered = orderMicroserviceClient.callPlaceOrder(workFlowStateManager.getLastUserId(), true);
        JsonObject responseBody = responseOrderDelivered.getBody();
        return responseBody;
    }


    public JsonObject handleDisplayOrdersDeliveredAndNot() {
        ResponseEntity<JsonObject> responseOrders = orderMicroserviceClient.callDisplayAllOrders();
        return responseOrders.getBody();
    }

    public JsonObject handleDeliverOrder() throws Exception {
        ResponseEntity<JsonObject> responseDeliver = orderMicroserviceClient.callDeliveryOrder(workFlowStateManager.getLastUserId());
        return responseDeliver.getBody();
    }
}
