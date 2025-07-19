package com.example.Flux;

import com.google.gson.JsonObject;

public interface IInvoiceService {
    JsonObject generateInvoice(int cartId) throws Exception;
}
