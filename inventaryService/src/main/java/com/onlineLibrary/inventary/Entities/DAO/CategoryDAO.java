package com.onlineLibrary.inventary.Entities.DAO;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class CategoryDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public CategoryDAO() {}

    public CategoryDAO(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}