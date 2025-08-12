package com.onlineLibrary.payment.Flux;

import com.onlineLibrary.payment.Entities.DTO.InvoiceDTO;

public interface IInvoiceService {
    InvoiceDTO generateInvoice(int cartId) throws Exception;
}
