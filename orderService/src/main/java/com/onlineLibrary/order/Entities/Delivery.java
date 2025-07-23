package com.onlineLibrary.order.Entities;


import com.google.gson.JsonObject;
import java.time.LocalDate;

public class Delivery {

    private int id;
    private int orderId;
    private LocalDate datePrevue;
    private LocalDate EffectifDate;
    private String statut = "PENDING"; // Prévue,Livrée
    private String street;
    private String city;
    private String postalCode;
    private String province;
    private String country;



    public Delivery(int orderId) {
        this.orderId = orderId;
        this.datePrevue = LocalDate.now().plusDays(3); // Exemple de planification automatique
    }

    public int getId() {
        return id;
    }

    public void setId(int deliveryId) {
        this.id = deliveryId;
    }

    public int getOrderId() {
        return orderId;
    }


    public LocalDate getScheduledDate() {
        return datePrevue;
    }


    public LocalDate getActualDate() {
        return EffectifDate;
    }


    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setAdresse(JsonObject address){
        this.street = address.get("street").getAsString();
        this.city = address.get("city").getAsString();
        this.postalCode = address.get("postal_code").getAsString();

        // Champs optionnels avec valeurs par défaut
        this.province = address.has("province") ? address.get("province").getAsString() : "";
        this.country = address.has("country") ? address.get("country").getAsString() : "Canada";
    }

    public String getStreet() {
        return street;
    }
    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }
    public String getProvince() {
        return province;
    }
    public String getCountry() {
        return country;
    }



}
