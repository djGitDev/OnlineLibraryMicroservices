package com.example.Entities;


import java.time.LocalDate;

public class Invoice {
    private int noInvoice;
    private LocalDate date;
    private double totalPrice;

    public Invoice(LocalDate date, double totalPrice) {
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
