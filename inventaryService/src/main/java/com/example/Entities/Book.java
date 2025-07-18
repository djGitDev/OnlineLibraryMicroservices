package com.example.Entities;

import java.util.Date;

public class Book {

    private int id;
    private String isbn;
    private String title;
    private String description;
    private Date parutionDate;
    private double price;
    private int quantity;
    private int publisherId;

    public Book(int id, String isbn, String title, String description,Date parutionDate, double price, int quantity, int PublisherId) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.parutionDate = parutionDate;
        this.price = price;
        this.quantity = quantity;
        this.publisherId = PublisherId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getParutionDate() {
        return parutionDate;
    }

    public void setParutionDate(Date parutionDate) {
        this.parutionDate = parutionDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPublisherId() {
        return publisherId;
    }
    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }
}
