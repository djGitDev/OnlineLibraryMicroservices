package com.onlineLibrary.orchestre.Flux.Handlers;

import com.google.gson.JsonObject;
import com.onlineLibrary.orchestre.Flux.MicroservicesClients.PaymentMicroservicesClient;
import com.onlineLibrary.orchestre.Util.WorkFlowStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


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
        ResponseEntity<JsonObject> responsePayment = paymentMicroserviceClient.callProcessPaiement
                (
                        workFlowStateManager.getCurrentCartId(),
                        workFlowStateManager.getLastUserId(),
                        true
                );
        return responsePayment.getBody();
    }

}
