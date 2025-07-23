package com.onlineLibrary.order.Persistance.Implementations;

import com.onlineLibrary.order.Persistance.Interfaces.IDBConnection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PostgresDBConnection implements IDBConnection {

    private Connection connection;
    private final String url;
    private final String username;
    private final String password;

    public PostgresDBConnection() {
        Properties props = new Properties();

        try (InputStream input = PostgresDBConnection.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {


            if (input == null) {
                System.err.println("❌ ERROR: application.properties file not found in the classpath!");
                throw new RuntimeException("application.properties file not found in resources!");
            }

            props.load(input);

            // Debug loaded properties
            System.out.println("📋 Loaded properties:");
            props.forEach((k, v) -> System.out.println("  " + k + " = " + v));

            this.url = props.getProperty("spring.datasource.url");
            this.username = props.getProperty("spring.datasource.username");
            this.password = props.getProperty("spring.datasource.password");

            if (url == null || username == null || password == null) {
                throw new RuntimeException("Incomplete database configuration in application.properties");
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading configuration file: " + e.getMessage(), e);
        }

        System.out.println("✅ PostgresDBConnection successfully initialized");
    }

    @Override
    public Connection getConnection() throws SQLException {

        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(url, username, password);

            } catch (SQLException e) {

                throw e;
            }
        } else {
            System.out.println("ℹ Using existing connection");
        }

        return connection;
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null;
            System.out.println("✅ Connection closed.");
        } else {
            System.out.println("ℹ No active connection to close");
        }
    }
}