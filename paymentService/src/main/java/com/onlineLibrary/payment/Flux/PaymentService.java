package com.onlineLibrary.payment.Flux;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.onlineLibrary.payment.Util.ConvertJsonUtils.jacksonToGson;

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
    public JsonObject processPaiement(int cartId,int userId) throws Exception {
        JsonObject invoiceResult = invoiceService.generateInvoice(cartId);
        double totalPrice = invoiceResult.get("total_price").getAsDouble();
        ResponseEntity<JsonNode> responseJackson = orderMicroservicesClient.callPlaceOrder(userId,true);
        JsonObject orderResult = jacksonToGson(responseJackson.getBody());
        int orderId = orderResult.get("orderId").getAsInt();
        return notificationService.notifyUser(userId,orderId,cartId,totalPrice);
    }
}
