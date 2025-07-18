package com.onlineLibrary.Entities;

public class Address {
    private int id;
    private String street;
    private String city;
    private String postalCode;
    private String province;
    private String country;
    private int userId;

    public Address() {}

    public Address(String street, String city, String postalCode, String province, String country) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.province = province;
        this.country = country;
    }

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