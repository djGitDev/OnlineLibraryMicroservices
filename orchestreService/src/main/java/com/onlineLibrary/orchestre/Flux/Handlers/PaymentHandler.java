package com.onlineLibrary.orchestre.Flux.Handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.onlineLibrary.orchestre.Flux.MicroservicesClients.PaymentMicroservicesClient;
import com.onlineLibrary.orchestre.Util.WorkFlowStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.onlineLibrary.orchestre.Util.ConvertJsonUtils.jacksonToGson;


@Service
public class PaymentHandler {

    private static final Logger logger = LoggerFactory.getLogger(PaymentHandler.class);
    private PaymentMicroservicesClient paymentMicroserviceClient;
    private WorkFlowStateManager workFlowStateManager;

    @Autowired
    public PaymentHandler(PaymentMicroservicesClient paymentMicroserviceClient,WorkFlowStateManager workFlowStateManager) {
        this.paymentMicroserviceClient = paymentMicroserviceClient;
        this.workFlowStateManager = workFlowStateManager;
    }

    public JsonObject handleProcessPayment() throws Exception {
        ResponseEntity<JsonNode> responsePaymentJackson = paymentMicroserviceClient.callProcessPaiement
                (
                        workFlowStateManager.getCurrentCartId(),
                        workFlowStateManager.getLastUserId(),
                        true
                );
        return jacksonToGson(responsePaymentJackson.getBody());
    }

}
