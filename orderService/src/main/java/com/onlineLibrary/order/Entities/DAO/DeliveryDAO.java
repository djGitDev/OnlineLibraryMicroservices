package com.onlineLibrary.order.Entities.DAO;

import com.onlineLibrary.order.Entities.DTO.AddressDTO;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "deliveries")
public class DeliveryDAO
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Column(name = "date_prevue", nullable = false)
    private LocalDate datePrevue;

    @Column(name = "effectif_date")
    private LocalDate effectifDate;

    @Column(nullable = false)
    private String statut = "PENDING";

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    private String province = "";

    private String country = "Canada";

    // Constructeur par défaut requis par JPA
    public DeliveryDAO() {
    }

    // Constructeur personnalisé (id est généré automatiquement)
    public DeliveryDAO(int orderId) {
        this.orderId = orderId;
        this.datePrevue = LocalDate.now().plusDays(3);
        this.statut = "PENDING";
        this.province = "";
        this.country = "Canada";
    }

    // Getters & Setters

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



    public void setAdresse(AddressDTO addressDTO) {
        if (addressDTO != null) {
            this.street = addressDTO.getStreet();
            this.city = addressDTO.getCity();
            this.postalCode = addressDTO.getPostalCode();
            this.province = addressDTO.getProvince() != null ? addressDTO.getProvince() : "";
            this.country = addressDTO.getCountry() != null ? addressDTO.getCountry() : "Canada";
        }
    }


}