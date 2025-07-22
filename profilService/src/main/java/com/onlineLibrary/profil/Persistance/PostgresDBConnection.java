package com.onlineLibrary.profil.Persistance;

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
                System.err.println("❌ ERREUR: Fichier application.properties introuvable dans le classpath!");
                throw new RuntimeException("Fichier application.properties introuvable dans les ressources !");
            }

            props.load(input);
            
            // Debug des propriétés lues
            System.out.println("📋 Propriétés lues:");
            props.forEach((k, v) -> System.out.println("  " + k + " = " + v));
            
            this.url = props.getProperty("spring.datasource.url");
            this.username = props.getProperty("spring.datasource.username");
            this.password = props.getProperty("spring.datasource.password");

            

            if (url == null || username == null || password == null) {
                throw new RuntimeException("Configuration de base de données incomplète dans application.properties");
            }

        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier de configuration : " + e.getMessage(), e);
        }
        
        System.out.println("✅ PostgresDBConnection initialisé avec succès");
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
            System.out.println("ℹ Utilisation de la connexion existante");
        }
        
        return connection;
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null;
            System.out.println("✅ Connexion fermée.");
        } else {
            System.out.println("ℹ Aucune connexion active à fermer");
        }
    }
}