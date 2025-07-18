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
            initInventory(url,username,password);
            System.out.println("[InitDB] Base initialisée avec succès");
        }
    }

    private static void executeDDL(Statement stmt) throws SQLException {
        String[] ddlScripts = {
//                "DROP TABLE IF EXISTS publishers, authors, CASCADE",

                //Publishers table
                """
                          CREATE TABLE IF NOT EXISTS publishers (
                              id SERIAL PRIMARY KEY,
                              name VARCHAR(100) NOT NULL UNIQUE
                          );
                  """,
                //Autors table
                """
                          CREATE TABLE IF NOT EXISTS authors (
                              id SERIAL PRIMARY KEY,
                              name VARCHAR(100) NOT NULL UNIQUE
                          );
                  """,

                //Categories table
                """
                          CREATE TABLE IF NOT EXISTS categories (
                              id SERIAL PRIMARY KEY,
                              name VARCHAR(50) NOT NULL UNIQUE
                          );
                  """,
                //Books table
                """
                          CREATE TABLE IF NOT EXISTS books (
                              id SERIAL PRIMARY KEY,
                              isbn VARCHAR(20) UNIQUE NOT NULL,
                              title VARCHAR(50) NOT NULL,
                              description VARCHAR(255),
                              parution_date DATE,
                              price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
                              quantity INT NOT NULL CHECK (quantity >= 0),
                              publisher_id INT REFERENCES publishers(id)
                          );
                """,
                //Books_authors table (many-to-many)
                """
                          CREATE TABLE IF NOT EXISTS books_authors (
                              book_id INT REFERENCES books(id) ON DELETE CASCADE,
                              author_id INT REFERENCES authors(id) ON DELETE CASCADE,
                              PRIMARY KEY (book_id, author_id)
                          );
                """,

                //Books_categories table (many-to-many)
                """
                         CREATE TABLE IF NOT EXISTS books_categories (
                              book_id INT REFERENCES books(id) ON DELETE CASCADE,
                              category_id INT REFERENCES categories(id) ON DELETE CASCADE,
                              PRIMARY KEY (book_id, category_id)
                         );
                """


        };

        for (String ddl : ddlScripts) {
            stmt.execute(ddl);
        }
    }
    private static void initInventory(String url, String username, String password) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Insert  publishers
            insertPublisher(connection, "Spong Bob");
            insertPublisher(connection, "Omer Simpson");

            // Insert categories
            insertCategory(connection, "Classic Literature");
            insertCategory(connection, "Programming");
            insertCategory(connection, "unavailable books");

            // Insert authors
            insertAuthor(connection, "Alex Ouedraogo");
            insertAuthor(connection, "Joshua Bloch");
            insertAuthor(connection, "Mark Lutz");
            insertAuthor(connection, "Scott Meyers");
            insertAuthor(connection, "unknown");

            // Book 1
            insertBook(connection, "1111743273565", "how to add many books to the database",
                    "A guide on adding books to the database", "2025-01-01", 10.99, 25, 1);
            insertBookAuthor(connection, 1, 1); // Alex Ouedraogo
            insertBookCategory(connection, 1, 1); // Classic Literature

            // Book 2
            insertBook(connection, "9780135166307", "Effective Java",
                    "Best practices for Java programming", "2020-01-01", 45.00, 15, 2);
            insertBookAuthor(connection, 2, 2); // Joshua Bloch
            insertBookCategory(connection, 2, 2); // Programming

            // Book 3
            insertBook(connection, "9781491950357", "Learning Python",
                    "Comprehensive guide to Python programming", "2013-06-12", 39.99, 30, 1);
            insertBookAuthor(connection, 3, 3); // Mark Lutz
            insertBookCategory(connection, 3, 2); // Programming

            // Book 4
            insertBook(connection, "9780134686097", "Effective Modern C++",
                    "Guidelines for using C++11 and C++14 effectively", "2016-12-27", 49.99, 20, 1);
            insertBookAuthor(connection, 4, 4); // Scott Meyers
            insertBookCategory(connection, 4, 2); // Programming

            // Book 5
            insertBook(connection, "9780321751041", "first unavailable book",
                    "an unavailable book", "2006-05-03", 54.99, 0, 1);
            insertBookAuthor(connection, 5, 5); // unknown
            insertBookCategory(connection, 5, 3); // unavailable books

            // Book 6
            insertBook(connection, "9780132350884", "second unavailable book",
                    "an unavailable book", "2008-08-01", 42.99, 0, 2);
            insertBookAuthor(connection, 6, 5); // unknown
            insertBookCategory(connection, 6, 3); // unavailable books

            // Book 7
            insertBook(connection, "9780201616224", "third unavailable book",
                    "an unavailable book", "1994-10-31", 39.99, 0, 2);
            insertBookAuthor(connection, 7, 5); // unknown
            insertBookCategory(connection, 7, 3); // unavailable books
        }
    }

    // Méthodes utilitaires
    private static void insertPublisher(Connection conn, String name) throws SQLException {
        String sql = "INSERT INTO publishers (name) VALUES (?) ON CONFLICT (name) DO NOTHING";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        }
    }

    private static void insertAuthor(Connection conn, String name) throws SQLException {
        String sql = "INSERT INTO authors (name) VALUES (?) ON CONFLICT (name) DO NOTHING";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        }
    }

    private static void insertCategory(Connection conn, String name) throws SQLException {
        String sql = "INSERT INTO categories (name) VALUES (?) ON CONFLICT (name) DO NOTHING";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        }
    }

    private static void insertBook(Connection conn, String isbn, String title, String description,
                                   String parutionDate, double price, int quantity, int publisherId) throws SQLException {
        String sql = "INSERT INTO books (isbn, title, description, parution_date, price, quantity, publisher_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) ON CONFLICT (isbn) DO NOTHING";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setDate(4, Date.valueOf(parutionDate));
            stmt.setDouble(5, price);
            stmt.setInt(6, quantity);
            stmt.setInt(7, publisherId);
            stmt.executeUpdate();
        }
    }

    private static void insertBookAuthor(Connection conn, int bookId, int authorId) throws SQLException {
        String sql = "INSERT INTO books_authors (book_id, author_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, authorId);
            stmt.executeUpdate();
        }
    }

    private static void insertBookCategory(Connection conn, int bookId, int categoryId) throws SQLException {
        String sql = "INSERT INTO books_categories (book_id, category_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, categoryId);
            stmt.executeUpdate();
        }
    }

}
