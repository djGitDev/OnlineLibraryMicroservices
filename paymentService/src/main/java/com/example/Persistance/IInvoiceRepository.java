package com.example.Persistance;

import com.google.gson.JsonObject;
import com.example.Entities.Invoice;

public interface IInvoiceRepository {

    int save(Invoice invoice) throws Exception;
    JsonObject getFactureById(int factureId);
}