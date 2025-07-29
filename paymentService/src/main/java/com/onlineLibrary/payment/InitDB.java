package com.onlineLibrary.payment;

import com.onlineLibrary.payment.config.LoaderConfig;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
                //Invoices table
                """
                       CREATE TABLE invoices (
                                    id SERIAL PRIMARY KEY,
                                    date TIMESTAMP NOT NULL,
                                    amount DOUBLE PRECISION NOT NULL
                       );
                """
        };

        for (String ddl : ddlScripts) {
            stmt.execute(ddl);
        }
    }

}
