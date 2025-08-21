package com.onlineLibrary.cart.Flux.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;
import com.onlineLibrary.cart.Entities.DTO.*;

import java.util.Map;

public interface ICartService {
    ClearCartResponseDTO clearCart(int userId);
    ClearBooksResponseDTO clearBooks(int userId, JsonNode books);
    CartDTO getCart(int userId) throws Exception;
    CartItemsResponseDTO getItems(int id) throws Exception;
    AddBooksResponseDTO addSearchedItemsToCart(int userId, JsonNode books, Map<Integer,Double> searchedBooksIds) throws Exception;
    CartTotalPriceDTO getTotalPrice(int cartId) throws Exception;
    AddBookResponseDTO addSingleItemToCart(int userId, int bookId, int quantity, double price) throws Exception;
    ClearBooksResponseDTO clearItem(int userId, int bookId, int quantity);
}

