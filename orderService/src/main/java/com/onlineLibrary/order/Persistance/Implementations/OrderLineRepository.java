package com.onlineLibrary.order.Persistance.Implementations;

import com.google.gson.JsonObject;
import com.onlineLibrary.order.Entities.OrderLine;
import com.onlineLibrary.order.Persistance.Interfaces.IDBConnection;
import com.onlineLibrary.order.Persistance.Interfaces.IOrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class OrderLineRepository implements IOrderLineRepository {
    private final IDBConnection dbConnection;

    private static final String SQL_INSERT_LINE = """
        INSERT INTO order_lines(order_id, book_id, quantity, delivery_status, delivery_date)
        VALUES (?, ?, ?, ?, ?)
    """;
    @Autowired
    public OrderLineRepository(IDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
    @Override
    public int save(int orderId, OrderLine orderLine) throws Exception {
        String sql = "INSERT INTO order_lines(order_id, book_id, quantity, delivery_status, delivery_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, orderId);
            stmt.setInt(2, orderLine.getBookId());
            stmt.setInt(3, orderLine.getQuantity());
            stmt.setString(4, orderLine.getDeliveryStatut());

            if (orderLine.getEffectifDeliveryDate() != null) {
                stmt.setDate(5, java.sql.Date.valueOf(orderLine.getEffectifDeliveryDate()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new Exception("order line insert failed, no line affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new Exception("failure to get order line generated ID.");
                }
            }
        } catch (Exception e) {
            throw new Exception("error while registering order line: " + e.getMessage(), e);
        }
    }

    @Override
    public JsonObject markAsDelivred(int orderId) {
        JsonObject response = new JsonObject();
        String sql = """
        UPDATE order_lines
        SET delivery_status = 'Livrée', delivery_date = CURRENT_DATE
        WHERE order_id = ?
    """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            int affectedRows = stmt.executeUpdate();

            response.addProperty("status", "success");
            response.addProperty("updated_lines", affectedRows);
            response.addProperty("message", "order line marked as delivred.");
        } catch (Exception e) {
            response.addProperty("status", "error");
            response.addProperty("message", "fail to update order line : " + e.getMessage());
        }

        return response;
    }

    public JsonObject updateDeliveryStatusToDelivered(int orderId) {
        JsonObject response = new JsonObject();
        final String DELIVERED_STATUS = "DELIVRED";

        String sql = """
        UPDATE order_lines 
        SET delivery_status = ?, 
            delivery_date = CASE 
                WHEN delivery_date IS NULL THEN CURRENT_DATE 
                ELSE delivery_date 
            END
        WHERE order_id = ? AND delivery_status != ?
    """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, DELIVERED_STATUS);
            stmt.setInt(2, orderId);
            stmt.setString(3, DELIVERED_STATUS);

            int affectedRows = stmt.executeUpdate();

            response.addProperty("status", "success");
            response.addProperty("updated_lines", affectedRows);
            response.addProperty("message",
                    affectedRows + " order lines marked as delivered");

        } catch (Exception e) {
            response.addProperty("status", "error");
            response.addProperty("message", "fail to update : " + e.getMessage());
        }

        return response;
    }
}
