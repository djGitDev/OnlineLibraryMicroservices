package com.example.Persistance.Interfaces;

import com.example.Entities.CartItem;

import java.util.List;
import java.util.Optional;

public interface ICartItemRepository {

    Optional<CartItem> findByCartIdAndBookId(int cartId, int bookId) throws Exception;
    void save(CartItem item) throws Exception;
    void update(int cartId, int bookId, int quantity);
    List<CartItem> findByCartId(int cartId) throws Exception;
    void deleteByCartId(int cartId) throws Exception;
    void deleteItem(int cartId, int bookId) throws Exception;
}
