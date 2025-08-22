package com.onlineLibrary.profil.Flux;

import com.onlineLibrary.profil.Entities.DTO.LoginRequestDTO;
import com.onlineLibrary.profil.Entities.DTO.LoginResponseDTO;
import com.onlineLibrary.profil.Entities.DTO.RefreshTokenResponseDTO;

public interface ILoginService {
    LoginResponseDTO authentifyUser(LoginRequestDTO credential) throws Exception;
    RefreshTokenResponseDTO generateAccesTokenFromRefresh(String refreshToken);
}
