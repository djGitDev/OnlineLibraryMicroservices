package com.onlineLibrary.cart.Flux.Interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

public interface ICartService {
    JsonObject clearCart(int userId);
    JsonObject clearBooks(int userId, JsonArray books);
    JsonObject getCart(int userId) throws Exception;
    JsonObject getItems(int id) throws Exception;
    JsonObject addSearchedItemsToCart(int userId, JsonArray books, Map<Integer,Double> searchedBooksIds) throws Exception;
    JsonObject getTotalPrice(int cartId) throws Exception;
}

