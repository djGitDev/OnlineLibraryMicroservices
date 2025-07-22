package com.onlineLibrary.payment.Persistance;

import com.google.gson.JsonObject;
import com.onlineLibrary.payment.Entities.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Repository
public class InvoiceRepository implements IInvoiceRepository {

    private final IDBConnection dbConnection;

    @Autowired
    public InvoiceRepository(IDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
    @Override
    public int save(Invoice invoice) throws SQLException {
        String sql = "INSERT INTO invoices (date, amount) VALUES (?, ?) RETURNING id";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(invoice.getDate().atStartOfDay()));
            stmt.setDouble(2, invoice.getTotalPrice());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    invoice.setNoInvoice(id); // facultatif
                    return id;
                } else {
                    throw new SQLException("Échec lors de l'insertion de la facture.");
                }
            }
        }
    }

    @Override
    public JsonObject getFactureById(int factureId) {
        return null;
    }
}