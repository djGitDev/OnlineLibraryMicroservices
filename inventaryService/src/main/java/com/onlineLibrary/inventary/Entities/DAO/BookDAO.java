package com.onlineLibrary.inventary.Entities.DAO;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "books")
public class BookDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "parution_date")
    @Temporal(TemporalType.DATE)
    private Date parutionDate;

    private double price;

    private int quantity;

    @Column(name = "publisher_id")
    private int publisherId;

    public BookDAO() {}

    public BookDAO(String isbn, String title, String description, Date parutionDate, double price, int quantity, int publisherId) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.parutionDate = parutionDate;
        this.price = price;
        this.quantity = quantity;
        this.publisherId = publisherId;
    }

    public int getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getParutionDate() {
        return parutionDate;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }
}
