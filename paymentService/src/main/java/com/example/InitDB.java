package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
