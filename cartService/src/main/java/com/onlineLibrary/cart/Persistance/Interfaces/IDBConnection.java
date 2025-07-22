package com.onlineLibrary.cart.Persistance.Interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDBConnection {

    Connection getConnection() throws SQLException;
    void close() throws SQLException;
}
