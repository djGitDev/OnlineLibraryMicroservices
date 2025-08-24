package com.onlineLibrary.inventary.Entities.DTO;

import java.util.Date;
import java.util.List;

public class BookDTO {
    private int id;
    private String isbn;
    private String title;
    private String description;
    private Date parutionDate;
    private double price;
    private int quantity;
    private int publisherId;
    private String publisherName;

    private List<String> categoryNames;
    private List<String> authorNames;

    public BookDTO() {}

    // Getters et setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

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

    public int getPublisherId() {
        return publisherId;
    }
    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }
    public String getPublisherName() { return publisherName; }
    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }

    public List<String> getAuthorNames() { return authorNames; }
    public void setAuthorNames(List<String> authorNames) { this.authorNames = authorNames; }

    public List<String> getCategoryNames() { return categoryNames; }
    public void setCategoryNames(List<String> categoryNames) { this.categoryNames = categoryNames; }

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", parutionDate=" + parutionDate +
                ", price=" + price +
                ", quantity=" + quantity +
                ", publisherId=" + publisherId +
                ", publisherName=" + publisherName +
                ", categories=" + (categoryNames != null ? String.join(", ", categoryNames) : "[]") +
                ", authors=" + (authorNames != null ? String.join(", ", authorNames) : "[]") +
                '}';
    }

}