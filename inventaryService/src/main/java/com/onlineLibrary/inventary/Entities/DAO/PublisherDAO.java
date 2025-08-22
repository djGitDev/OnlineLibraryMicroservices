package com.onlineLibrary.inventary.Entities.DAO;

import jakarta.persistence.*;

@Entity
@Table(name = "publishers")
public class PublisherDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    public PublisherDAO() {}

    public PublisherDAO(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
