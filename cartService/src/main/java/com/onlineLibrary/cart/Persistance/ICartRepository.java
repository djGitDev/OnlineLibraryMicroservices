package com.onlineLibrary.cart.Persistance;

import com.onlineLibrary.cart.Entities.DAO.CartDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICartRepository extends JpaRepository<CartDAO, Integer> {
    Optional<CartDAO> findByUserId(int userId);
}