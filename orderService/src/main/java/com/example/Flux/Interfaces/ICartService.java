package com.example.Flux.Interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.example.Entities.Cart;
import com.example.Entities.CartItem;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ICartService {
    JsonObject clearCart(int userId);
    JsonObject clearBooks(int userId, JsonArray books);
    Optional<Cart> getCart(int userId) throws Exception;
    List<CartItem> getItems(int id) throws Exception;
    JsonObject addSearchedItems(int userId, JsonArray books, Map<Integer,Double> searchedBooksIds) throws Exception;
    JsonObject getTotalPrice(int cartId) throws Exception;
}

