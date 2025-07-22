package com.onlineLibrary.payment.Flux;

import com.google.gson.JsonObject;



public interface IPaymentService {

    JsonObject processPaiement(int cartId,int userId) throws Exception;
}
