package com.onlineLibrary.payment.Flux;

import com.fasterxml.jackson.databind.JsonNode;
import com.onlineLibrary.payment.Entities.DTO.InvoiceDTO;
import com.onlineLibrary.payment.Entities.DTO.NotificationResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class PaymentService implements IPaymentService {


    private OrderMicroservicesClient  orderMicroservicesClient;
    private IInvoiceService invoiceService;
    private INotificationService notificationService;

    @Autowired
    public PaymentService(OrderMicroservicesClient  orderMicroservicesClient, IInvoiceService invoiceService, INotificationService notificationService) {
        this.orderMicroservicesClient = orderMicroservicesClient;
        this.invoiceService = invoiceService;
        this.notificationService = notificationService;
    }



    @Override
    public NotificationResponseDTO processPaiement(int cartId, int userId) throws Exception {
        InvoiceDTO invoiceResult = invoiceService.generateInvoice(cartId);
        double totalPrice = invoiceResult.getTotalPrice();
        ResponseEntity<JsonNode> responseJackson = orderMicroservicesClient.callPlaceOrder(userId,true);
        int orderId = responseJackson.getBody().get("orderId").asInt();
        return notificationService.notifyUser(userId,orderId,cartId,totalPrice);
    }
}
