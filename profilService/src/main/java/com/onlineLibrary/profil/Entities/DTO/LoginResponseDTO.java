package com.onlineLibrary.profil.Entities.DTO;

public class LoginResponseDTO {

    private String status;
    private int userId;
    private String email;
    private String jwt;



    public LoginResponseDTO(String status, int userId, String email,String jwt) {
        this.status = status;
        this.userId = userId;
        this.email = email;
        this.jwt = jwt;
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
    public String getJwt() {
        return jwt;
    }

}