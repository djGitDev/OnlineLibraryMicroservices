package com.example.Flux;

import com.google.gson.JsonObject;
import com.example.Entities.Invoice;
import com.example.Persistance.IInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static java.time.LocalDate.now;

@Service
public class InvoiceService implements IInvoiceService {

    private OrderMicroservicesClient  orderMicroservicesClient;
    private IInvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(OrderMicroservicesClient  orderMicroservicesClient, IInvoiceRepository invoiceRepository) {
        this.orderMicroservicesClient = orderMicroservicesClient;
        this.invoiceRepository = invoiceRepository;
    }


    @Override
    public JsonObject generateInvoice(int cartId) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("cartId", cartId);
        ResponseEntity<JsonObject> response = orderMicroservicesClient.callGetTotalPriceCart(body);
        JsonObject jsonTotalPrice =  response.getBody().getAsJsonObject();
        double totalPrice = jsonTotalPrice.get("total_price").getAsDouble();
        Invoice invoice = new Invoice(now(), totalPrice);
        int invoiceId =  invoiceRepository.save(invoice);
        JsonObject invoiceJson = new JsonObject();
        invoiceJson.addProperty("invoice_id", invoiceId);
        invoiceJson.addProperty("date", String.valueOf(invoice.getDate()) );
        invoiceJson.addProperty("total_price", totalPrice);
        return invoiceJson;
    }
}
