package com.onlineLibrary.profil.Entities.DTO;

public class LoginResponseDTO {

    private String status;
    private int userId;
    private String email;


    public LoginResponseDTO(String status, int userId, String email) {
        this.status = status;
        this.userId = userId;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}