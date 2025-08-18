package com.onlineLibrary.profil.Entities.DTO;

public class RefreshTokenResponseDTO {
    private String status;
    private String accessToken;

    public RefreshTokenResponseDTO() {}

    public RefreshTokenResponseDTO(String status, String accessToken) {
        this.status = status;
        this.accessToken = accessToken;
    }

    // getters et setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
}
