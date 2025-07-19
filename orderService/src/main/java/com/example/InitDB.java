package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public final class InitDB {
    private static final String PROPERTIES_FILE = "/application.properties";
    private static final Properties props = new Properties();

    static {
        loadProperties();
        loadDriver();
    }

    private InitDB() {
        throw new AssertionError("Cette classe ne doit pas être instanciée");
    }

    private static void loadProperties() {
        try (InputStream input = InitDB.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Fichier " + PROPERTIES_FILE + " non trouvé");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier de configuration", e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Driver PostgreSQL non trouvé");
        }
    }

    public static void initialize() throws SQLException {
        String url = props.getProperty("spring.datasource.url");
        String username = props.getProperty("spring.datasource.username");
        String password = props.getProperty("spring.datasource.password");

        if (url == null || username == null || password == null) {
            throw new SQLException("Configuration DB incomplète dans application.properties");
        }

        System.out.println("[InitDB] Initialisation de la base de données...");

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {

            executeDDL(stmt);
            System.out.println("[InitDB] Base initialisée avec succès");
        }
    }

    private static void executeDDL(Statement stmt) throws SQLException {
        String[] ddlScripts = {
                // Carts table - CORRIGÉ (virgule en trop après user_id)
                """
    CREATE TABLE IF NOT EXISTS carts (
       id SERIAL PRIMARY KEY,
       user_id INT NOT NULL
    );
    """,

                // Cart items table - CORRIGÉ (bonne syntaxe)
                """
    CREATE TABLE IF NOT EXISTS cart_items (
         id SERIAL PRIMARY KEY,
         cart_id INT NOT NULL,
         book_id INT NOT NULL,
         quantity INT NOT NULL CHECK (quantity > 0),
         book_price DOUBLE PRECISION NOT NULL CHECK (book_price > 0),
         FOREIGN KEY (cart_id) REFERENCES carts(id)
    );
    """,

                // Orders table - CORRIGÉ (virgule en trop après order_date)
                """
    CREATE TABLE IF NOT EXISTS orders (
       id SERIAL PRIMARY KEY,
       user_id INT NOT NULL,
       order_date DATE NOT NULL
    );
    """,

                // Order lines table - CORRIGÉ (bonne syntaxe)
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

                // Deliveries table - CORRIGÉ (bonne syntaxe)
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
