package com.example.Persistance.Impl;

import com.example.Entities.Book;
import com.example.Persistance.IBookRepository;
import com.example.Persistance.IDBConnection;
import com.example.UtilInventaire.BeansInjectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

public class BookRepository implements IBookRepository {
    private IDBConnection dbConnection;

    public BookRepository(BeansInjectionFactory factory) {
        this.dbConnection = factory.getDBConnection();
    }

    @Override
    public List<Book> getBooks() throws Exception {
        String sql = "SELECT * FROM books";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
             ResultSet resultSet = statement.executeQuery();
             List<Book> books = new java.util.ArrayList<>();
             while (resultSet.next()) {
                 books.add(makeBook(resultSet));
             }
             return books;
        }
    }

    @Override
    public Book getBookById(int id) throws Exception{
        String sql = "SELECT * FROM books WHERE id=?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return makeBook(resultSet);
            }
            return null;
        }
    }

    @Override
    public Book addBook(Book book) throws Exception {
        String sql = "INSERT INTO books (id, isbn, title, description, parution_date, price, quantity) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, book.getId());
            statement.setString(2, book.getIsbn());
            statement.setString(3, book.getTitle());
            statement.setString(4, book.getDescription());
            statement.setDate(5, new java.sql.Date(book.getParutionDate().getTime()));
            statement.setDouble(6, book.getPrice());
            statement.setInt(7, book.getQuantity());

            statement.executeUpdate();

            return book;
        }
    }


    @Override
    public List<Book> addManyBooks(List<Book> books) throws Exception {
        for (Book book : books) {
            addBook(book);
        }
        return books;
    }


    @Override
    public boolean updateBook(int id, Book book) throws Exception{
        String sql = "UPDATE books SET isbn=?, title=?, description=?, parution_date=?, price=?, quantity=? WHERE id=?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getDescription());
            statement.setDate(4, new java.sql.Date(book.getParutionDate().getTime()));
            statement.setDouble(5, book.getPrice());
            statement.setInt(6, book.getQuantity());
            statement.setInt(7, id);
            int rowsAffected = statement.executeUpdate();
            connection.close();
            return rowsAffected > 0;
        }
    }

    @Override
    public Book findBookByIsbn(String isbn) throws Exception {
        String sql = "SELECT * FROM books WHERE isbn = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, isbn);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                connection.close();
                return makeBook(resultSet);
            }
            connection.close();
            return null;
        }
    }

    @Override
    public List<Book> findBookByTitle(String title) throws Exception {
        String sql = "SELECT * FROM books WHERE title = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            List<Book> books = new java.util.ArrayList<>();
            while (resultSet.next()) {
                books.add(makeBook(resultSet));
            }
            connection.close();
            return books;
        }
    }


    @Override
    public List<Book> findUnavailableBooks() throws Exception{
        String sql = "SELECT * FROM books WHERE quantity = 0";
        try (
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();
            List<Book> books = new java.util.ArrayList<>();
            while (resultSet.next()) {
                books.add(makeBook(resultSet));
            }
            connection.close();
            return books;
        }
    }

    private Book makeBook(ResultSet resultSet) throws Exception {
        int id = resultSet.getInt("id");
        String isbn = resultSet.getString("isbn");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        Date parutionDate = resultSet.getDate("parution_date");
        double price = resultSet.getDouble("price");
        int quantity = resultSet.getInt("quantity");
        int publisherId = resultSet.getInt("publisher_id");//
        return new Book(id,isbn, title, description, parutionDate, price, quantity,publisherId);
    }
}
