package com.onlineLibrary.payment.ControllerRestPaymentApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.onlineLibrary.payment.Flux.IPaymentService;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.onlineLibrary.payment.Util.ConvertJsonUtils.gsonToJackson;


@RestController
@RequestMapping("/api/payment")
public class PaymentRestController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentRestController.class);
    private final IPaymentService paymentService;

    @Autowired
    public PaymentRestController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{cartId}/process/{userId}")
    public ResponseEntity<JsonNode> processPayment(
            @PathVariable int cartId,
            @PathVariable int userId,
            @RequestParam(defaultValue = "true") boolean autoDelivery) throws Exception {
        try {
            logger.info("Processing payment for user: {}, cart: {}, autoDelivery: {}",
                    userId, cartId, autoDelivery);
            JsonObject resultGson = paymentService.processPaiement(cartId, userId);
            return ResponseEntity.ok(gsonToJackson(resultGson));
        } catch (Exception e) {
            logger.error("Error processing payment for user: {}", userId, e);
            return errorResponse(e);
        }
    }

    private ResponseEntity<JsonNode> errorResponse(Exception e) throws Exception {
        JsonObject gsonError = new JsonObject();
        gsonError.addProperty("error", e.getMessage());
        JsonNode jacksonError = gsonToJackson(gsonError);
        return ResponseEntity.internalServerError().body(jacksonError);
    }
}