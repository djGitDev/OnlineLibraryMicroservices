package com.onlineLibrary.profil.Entities.DTO;

public class RegisterResponseDTO {

    private String status;
    private int userId;
    private int addressId;



    public RegisterResponseDTO(String status, int userId, int addressId) {
        this.status = status;
        this.userId = userId;
        this.addressId = addressId;
    }

    // Getters & Setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}