package com.onlineLibrary.payment.ControllerRestPaymentApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.onlineLibrary.payment.Entities.DTO.NotificationResponseDTO;
import com.onlineLibrary.payment.Flux.IPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    private final ObjectMapper objectMapper;

    @Autowired
    public PaymentRestController(IPaymentService paymentService,ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.paymentService = paymentService;
    }


    @Operation(
            summary = "Process payment",
            description = "Process payment for a shopping cart and return confirmation details",
            parameters = {
                    @Parameter(
                            name = "cartId",
                            description = "ID of the cart to process",
                            example = "7",
                            in = ParameterIn.PATH
                    ),
                    @Parameter(
                            name = "userId",
                            description = "ID of the user making payment",
                            example = "2",
                            in = ParameterIn.PATH
                    ),
                    @Parameter(
                            name = "autoDelivery",
                            description = "Auto-delivery flag (default: true)",
                            example = "true",
                            in = ParameterIn.QUERY
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Payment processed successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                    {
                      "to": "lucas.dupuis8@example.com",
                      "subject": "Confirmation de paiement",
                      "message": "Bonjour Lucas,\\n\\nNous vous confirmons que le paiement de 112.95$ a été effectué avec succès pour votre panier (ID : 7).\\nVotre commande (ID : 2) a été déclenchée et est en cours de traitement.\\n\\nMerci pour votre confiance.\\n\\n"
                    }"""
                                    )
                            )
                    )
            }
    )
    @PostMapping("/{cartId}/process/{userId}")
    public ResponseEntity<JsonNode> processPayment(
            @PathVariable int cartId,
            @PathVariable int userId,
            @RequestParam(defaultValue = "true") boolean autoDelivery) throws Exception {
        try {
            logger.info("Processing payment for user: {}, cart: {}, autoDelivery: {}",
                    userId, cartId, autoDelivery);
            NotificationResponseDTO response= paymentService.processPaiement(cartId, userId);
            JsonNode result = objectMapper.valueToTree(response);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error processing payment for user: {}", userId, e);
            return errorResponse(e);
        }
    }

    private ResponseEntity<JsonNode> errorResponse(Exception e) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("error", e.getMessage());
        return ResponseEntity.internalServerError().body(errorNode);
    }
}