package com.onlineLibrary.payment.Flux;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.onlineLibrary.payment.Entities.Invoice;
import com.onlineLibrary.payment.Persistance.IInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.onlineLibrary.payment.Util.ConvertJsonUtils.jacksonToGson;
import static java.time.LocalDate.now;

@Service
public class InvoiceService implements IInvoiceService {

    private CartMicroservicesClient  cartMicroservicesClient;

    private IInvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(CartMicroservicesClient  cartMicroservicesClient, IInvoiceRepository invoiceRepository) {
        this.cartMicroservicesClient = cartMicroservicesClient;
        this.invoiceRepository = invoiceRepository;
    }


    @Override
    public JsonObject generateInvoice(int cartId) throws Exception {
        ResponseEntity<JsonNode> responseJackson = cartMicroservicesClient.callGetTotalPriceCart(cartId);
        JsonObject response = jacksonToGson(responseJackson.getBody());
        JsonObject jsonTotalPrice =  response.getAsJsonObject();
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
