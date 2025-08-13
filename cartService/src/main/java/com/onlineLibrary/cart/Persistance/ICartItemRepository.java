package com.onlineLibrary.cart.Persistance;

import com.onlineLibrary.cart.Entities.DAO.CartItemDAO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ICartItemRepository extends JpaRepository<CartItemDAO, Integer> {


    @Modifying
    @Transactional
    @Query("UPDATE CartItemDAO c SET c.quantity = c.quantity + :quantity WHERE c.cartId = :cartId AND c.bookId = :bookId")
    void update(@Param("cartId") int cartId, @Param("bookId") int bookId, @Param("quantity") int quantity);

    Optional<CartItemDAO> findByCartIdAndBookId(int cartId, int bookId);
    List<CartItemDAO> findByCartId(int cartId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItemDAO c WHERE c.cartId = :cartId")
    void deleteByCartId(@Param("cartId") int cartId);

    void deleteByCartIdAndBookId(int cartId, int bookId);
}