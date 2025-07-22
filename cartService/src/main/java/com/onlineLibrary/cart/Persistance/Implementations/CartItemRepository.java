package com.onlineLibrary.cart.Persistance.Implementations;

import com.onlineLibrary.cart.Entities.CartItem;
import com.onlineLibrary.cart.Persistance.Interfaces.ICartItemRepository;
import com.onlineLibrary.cart.Persistance.Interfaces.IDBConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CartItemRepository implements ICartItemRepository {

    private final IDBConnection dbConnection;

    @Autowired
    public CartItemRepository(IDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public Optional<CartItem> findByCartIdAndBookId(int cartId, int bookId) throws Exception {
        String sql = "SELECT id, quantity FROM cart_items WHERE cart_id = ? AND book_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartId);
            stmt.setInt(2, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getInt("id"));
                    item.setCartId(cartId);
                    item.setBookId(bookId);
                    item.setQuantity(rs.getInt("quantity"));
                    return Optional.of(item);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public void save(CartItem item) throws Exception {
        String sql = "INSERT INTO cart_items (cart_id, book_id, quantity,book_price) VALUES (?, ?, ?,?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, item.getCartId());
            stmt.setInt(2, item.getBookId());
            stmt.setInt(3, item.getQuantity());
            stmt.setDouble(4, item.getBookPrice());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(int cartId, int bookId, int quantity) {
        String sql = "UPDATE cart_items SET quantity = quantity + ? WHERE cart_id = ? AND book_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantity);      // valeur à ajouter à l'existante
            stmt.setInt(2, cartId);
            stmt.setInt(3, bookId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Aucune ligne mise à jour, cart item non trouvé.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de la quantité au cart item", e);
        }
    }

    @Override
    public List<CartItem> findByCartId(int cartId) throws Exception {
        String sql = "SELECT id, book_id, quantity, book_price FROM cart_items WHERE cart_id = ?";
        List<CartItem> items = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getInt("id"));
                    item.setCartId((int) cartId);
                    item.setBookId(rs.getInt("book_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setBookPrice(rs.getDouble("book_price"));

                    items.add(item);
                }
            }
        }

        return items;
    }

    @Override
    public void deleteByCartId(int cartId) throws Exception {
        String sql = "DELETE FROM cart_items WHERE cart_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteItem(int cartId, int bookId) throws Exception {
        String sql = "DELETE FROM cart_items WHERE cart_id = ? AND book_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartId);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        }
    }
}