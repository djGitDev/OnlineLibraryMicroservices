package com.onlineLibrary.cart.Flux.Interfaces;

import com.onlineLibrary.cart.Entities.DAO.CartItemDAO;
import com.onlineLibrary.cart.Entities.DTO.CartTotalPriceDTO;

import java.util.List;
import java.util.Optional;

public interface ICartItemsService {
    Optional<CartItemDAO> cartItemExists(int cartId, int bookId) throws Exception;
    void updateCartItemQuantity(int cartId, int bookId, int quantity);
    List<CartItemDAO> getItems(int cartId) throws Exception;
    void insertCartItem(CartItemDAO cartItem) throws Exception;
    void clearCartItems(int id);
    void deleteCartItem(int id, int bookId) throws Exception;
    CartTotalPriceDTO getTotalPrice(int cartId) throws Exception;
}
