package com.onlineLibrary.payment.ControllerRestPaymentApi;

import com.onlineLibrary.payment.Flux.IPaymentService;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<JsonObject> processPayment(
            @PathVariable int cartId,
            @PathVariable int userId,
            @RequestParam(defaultValue = "true") boolean autoDelivery) {
        try {
            logger.info("Processing payment for user: {}, cart: {}, autoDelivery: {}",
                    userId, cartId, autoDelivery);
            JsonObject result = paymentService.processPaiement(cartId, userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error processing payment for user: {}", userId, e);
            return errorResponse(e);
        }
    }

    private ResponseEntity<JsonObject> errorResponse(Exception e) {
        JsonObject error = new JsonObject();
        error.addProperty("error", e.getMessage());
        return ResponseEntity.internalServerError().body(error);
    }
}