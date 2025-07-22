package com.onlineLibrary.order.Entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Order {

    private int id;
    private int userId;
    private LocalDate dateCommande;
    private List<OrderLine> lignes = new ArrayList<>();


    public Order() {
    }

    public Order(int userId) {
        this.userId = userId;
        this.dateCommande = LocalDate.now(); // Date du jour automatiquement
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<OrderLine> getLines() {
        return lignes;
    }

    public void setLignes(List<OrderLine> lignes) {
        this.lignes = lignes;
    }

    public int getUserId() {
        return userId;
    }
}


