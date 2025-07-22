package com.onlineLibrary.cart.Persistance.Implementations;

import com.onlineLibrary.cart.Entities.Cart;
import com.onlineLibrary.cart.Persistance.Interfaces.ICartRepository;
import com.onlineLibrary.cart.Persistance.Interfaces.IDBConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class CartRepository implements ICartRepository {

    private final IDBConnection dbConnection;
    private final String sqlCart = "SELECT id FROM carts WHERE user_id = ?";
    private final String sqlInsert = "INSERT INTO carts(user_id) VALUES (?) RETURNING id";

    @Autowired
    public CartRepository(IDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }



    @Override
    public Optional<Cart> findByUserId(int userId) throws Exception {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlCart)) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Cart cart = new Cart((int) userId);
                    cart.setId(resultSet.getInt("id"));
                    return Optional.of(cart);
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    @Override
    public void save(Cart cart) throws Exception {
        if (cart.getId() != 0) {
            // Le panier a déjà un ID, on ne fait rien
            return;
        }

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlInsert)) {

            statement.setLong(1, cart.getUserId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    cart.setId(resultSet.getInt("id"));
                } else {
                    throw new SQLException("Échec de création du panier. Aucun ID généré.");
                }
            }
        }
    }

    @Override
    public void deleteCart(int id) {
        String sql = "DELETE FROM carts WHERE id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du panier avec l’ID : " + id, e);
        }
    }
}