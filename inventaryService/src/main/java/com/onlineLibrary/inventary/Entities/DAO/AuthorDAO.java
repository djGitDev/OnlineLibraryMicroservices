package com.onlineLibrary.inventary.Entities.DAO;

import jakarta.persistence.*;

@Entity
@Table(name = "authors")
public class AuthorDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    // Constructeur vide obligatoire pour JPA
    public AuthorDAO() {
    }

    // Constructeur pratique
    public AuthorDAO(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

