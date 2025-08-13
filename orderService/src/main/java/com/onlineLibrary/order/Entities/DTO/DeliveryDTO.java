package com.onlineLibrary.order.Entities.DTO;

import java.time.LocalDate;

public class DeliveryDTO {

    private Integer id;
    private int orderId;
    private LocalDate datePrevue;
    private LocalDate effectifDate;
    private String statut;
    private String street;
    private String city;
    private String postalCode;
    private String province;
    private String country;

    public DeliveryDTO() {
        // constructeur vide
    }

    public DeliveryDTO(Integer id, int orderId, LocalDate datePrevue, LocalDate effectifDate, String statut,
                       String street, String city, String postalCode, String province, String country) {
        this.id = id;
        this.orderId = orderId;
        this.datePrevue = datePrevue;
        this.effectifDate = effectifDate;
        this.statut = statut;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.province = province;
        this.country = country;
    }

    // Getters & setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LocalDate getDatePrevue() {
        return datePrevue;
    }

    public void setDatePrevue(LocalDate datePrevue) {
        this.datePrevue = datePrevue;
    }

    public LocalDate getEffectifDate() {
        return effectifDate;
    }

    public void setEffectifDate(LocalDate effectifDate) {
        this.effectifDate = effectifDate;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}