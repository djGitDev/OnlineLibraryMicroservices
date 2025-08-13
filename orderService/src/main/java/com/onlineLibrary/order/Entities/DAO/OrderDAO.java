package com.onlineLibrary.order.Entities.DAO;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate = LocalDate.now();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderLineDAO> lignes = new ArrayList<>();



    public OrderDAO(int userId) {
        this.userId = userId;
        this.orderDate = LocalDate.now();
    }

    public OrderDAO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<OrderLineDAO> getLignes() {
        return lignes;
    }

    public void setLignes(List<OrderLineDAO> lignes) {
        this.lignes = lignes;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
}
