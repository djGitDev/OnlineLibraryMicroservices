package com.onlineLibrary.profil.Flux;

import com.onlineLibrary.profil.Entities.DTO.*;

public interface IProfilServiceDispatcher {

    RegisterResponseDTO  handleRegistration(RegisterRequestDTO dataRequest) throws Exception;
    LoginResponseDTO handleLogin(LoginRequestDTO credential) throws Exception;
    AddressDTO getProfile(int userId) throws Exception;
    UserDTO getUserData(int userId) throws Exception;
    RefreshTokenResponseDTO handleRefreshToken(String refreshToken);
}
