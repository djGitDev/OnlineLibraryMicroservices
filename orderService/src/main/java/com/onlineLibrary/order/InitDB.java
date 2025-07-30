package com.onlineLibrary.order;

import com.onlineLibrary.order.config.LoaderConfig;


import java.sql.*;

public final class InitDB {
//
private static final LoaderConfig configLoader = new LoaderConfig();

    static {
        loadDriver();
    }

    private InitDB() {
        throw new AssertionError("Cette classe ne doit pas être instanciée");
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Driver PostgreSQL non trouvé");
        }
    }

    public static void initialize() throws SQLException {
        String url = configLoader.getProperty("spring.datasource.url");
        String username = configLoader.getProperty("spring.datasource.username");
        String password = configLoader.getProperty("spring.datasource.password");

        if (url == null || username == null || password == null) {
            throw new SQLException("Configuration DB incomplète dans " +
                    "application-" + configLoader.getProfile() + ".properties");
        }

        System.out.println("[InitDB] Initialisation de la base de données avec le profil " + configLoader.getProfile() + "...");

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {

            executeDDL(stmt);
            System.out.println("[InitDB] Base initialisée avec succès");
        }
    }


    private static void executeDDL(Statement stmt) throws SQLException {
        String[] ddlScripts = {

                // Orders table -
                """
    CREATE TABLE IF NOT EXISTS orders (
       id SERIAL PRIMARY KEY,
       user_id INT NOT NULL,
       order_date DATE NOT NULL
    );
    """,

                // Order lines table -
                """
    CREATE TABLE IF NOT EXISTS order_lines (
       id SERIAL PRIMARY KEY,
       order_id INT NOT NULL,
       book_id INT NOT NULL,
       quantity INT NOT NULL CHECK (quantity > 0),
       delivery_status VARCHAR(50) DEFAULT 'Pending',
       delivery_date DATE,
       FOREIGN KEY (order_id) REFERENCES orders(id)
    );
    """,

                // Deliveries table -
                """
    CREATE TABLE IF NOT EXISTS deliveries (
       id SERIAL PRIMARY KEY,
       order_id INTEGER NOT NULL,
       street VARCHAR(255) NOT NULL,
       city VARCHAR(255) NOT NULL,
       postal_code VARCHAR(50) NOT NULL,
       province VARCHAR(100) NOT NULL,
       country VARCHAR(100) NOT NULL,
       scheduled_date DATE NOT NULL,
       actual_date DATE,
       status VARCHAR(50) NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (order_id) REFERENCES orders(id)
    );
    """
        };

        for (String ddl : ddlScripts) {
            stmt.execute(ddl);
        }
    }

}
