package com.onlineLibrary.payment.Entities.DTO;

import java.time.LocalDate;

public class InvoiceDTO {
    private int noInvoice;
    private LocalDate invoiceDate;
    private double totalPrice;

    public InvoiceDTO() {
    }

    public InvoiceDTO(int noInvoice, LocalDate invoiceDate, Double amount) {
        this.noInvoice = noInvoice;
        this.invoiceDate = invoiceDate;
        this.totalPrice = amount;
    }

    public int getNoInvoice() {
        return noInvoice;
    }

    public void setNoInvoice(int noInvoice) {
        this.noInvoice = noInvoice;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double amount) {
        this.totalPrice = amount;
    }
}
