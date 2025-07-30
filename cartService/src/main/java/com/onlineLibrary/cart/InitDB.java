package com.onlineLibrary.cart;

import com.onlineLibrary.cart.config.LoaderConfig;
import java.sql.*;

public final class InitDB {

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
    """

        };

        for (String ddl : ddlScripts) {
            stmt.execute(ddl);
        }
    }

}
