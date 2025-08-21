package com.onlineLibrary.inventary.Entities.DTO;

import java.util.Date;
import java.util.List;

public class BookDTO {

    private String isbn;
    private String title;
    private String description;
    private Date parutionDate;
    private double price;
    private int quantity;
    private int publisherId;

    private List<String> categories;
    private List<String> authors;

    public BookDTO() {}

    // Getters et setters
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getParutionDate() { return parutionDate; }
    public void setParutionDate(Date parutionDate) { this.parutionDate = parutionDate; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getPublisherId() { return publisherId; }
    public void setPublisherId(int publisherId) { this.publisherId = publisherId; }

    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }

    public List<String> getAuthors() { return authors; }
    public void setAuthors(List<String> authors) { this.authors = authors; }
}