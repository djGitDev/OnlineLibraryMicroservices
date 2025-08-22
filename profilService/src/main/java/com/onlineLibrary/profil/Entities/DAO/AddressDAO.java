

package com.onlineLibrary.profil.Entities.DAO;

import jakarta.persistence.*;

@Entity
@Table(name = "user_profiles")
public class AddressDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "street")
    private String street;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "province", length = 50)
    private String province;

    @Column(name = "country", length = 50)
    private String country = "Canada";

    @Column(name = "user_id", nullable = false)
    private int userId;

    public AddressDAO() {}

    public AddressDAO(String street, String city, String postalCode, String province, String country, int userId) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.province = province;
        this.country = country;
        this.userId = userId;
    }

    // Getters & Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}