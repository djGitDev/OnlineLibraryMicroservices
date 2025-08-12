package com.onlineLibrary.payment.Entities.DAO;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "invoices")
public class InvoiceDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int noInvoice;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private double totalPrice;

    public InvoiceDAO() {
    }

    public InvoiceDAO(LocalDate date, double totalPrice) {
        this.date = date;
        this.totalPrice = totalPrice;
    }

    public int getNoInvoice() {
        return noInvoice;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // Setters
    public void setNoInvoice(int noInvoice) {
        this.noInvoice = noInvoice;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}