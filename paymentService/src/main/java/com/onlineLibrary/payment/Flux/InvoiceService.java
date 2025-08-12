package com.onlineLibrary.payment.Flux;

import com.fasterxml.jackson.databind.JsonNode;
import com.onlineLibrary.payment.Entities.DAO.InvoiceDAO;
import com.onlineLibrary.payment.Entities.DTO.InvoiceDTO;
import com.onlineLibrary.payment.Persistance.IRepositoryInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static java.time.LocalDate.now;

@Service
public class InvoiceService implements IInvoiceService {

    private CartMicroservicesClient  cartMicroservicesClient;
    private IRepositoryInvoice invoiceRepository;

    @Autowired
    public InvoiceService(CartMicroservicesClient  cartMicroservicesClient, IRepositoryInvoice invoiceRepository) {
        this.cartMicroservicesClient = cartMicroservicesClient;
        this.invoiceRepository = invoiceRepository;
    }


    @Override
    public InvoiceDTO generateInvoice(int cartId) throws Exception {
        ResponseEntity<JsonNode> responseJackson = cartMicroservicesClient.callGetTotalPriceCart(cartId);
        double totalPrice = responseJackson.getBody()
                .get("total_price")
                .asDouble();
        InvoiceDAO invoice = new InvoiceDAO(now(), totalPrice);
        invoice =  invoiceRepository.save(invoice);

        InvoiceDTO invoiceDTO = new InvoiceDTO(
                invoice.getNoInvoice(),
                invoice.getDate(),
                totalPrice
        );

        return invoiceDTO;
    }
}
