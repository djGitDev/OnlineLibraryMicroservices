package com.onlineLibrary.profil.Flux;

import com.onlineLibrary.profil.Entities.DTO.LoginRequestDTO;
import com.onlineLibrary.profil.Entities.DTO.LoginResponseDTO;

public interface ILoginService {
    LoginResponseDTO authentifyUser(LoginRequestDTO credential) throws Exception;

}
