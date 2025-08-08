package com.onlineLibrary.profil.Flux;

import com.onlineLibrary.profil.Entities.DTO.RegisterRequestDTO;
import com.onlineLibrary.profil.Entities.DTO.RegisterResponseDTO;

public interface IRegisterService {
    RegisterResponseDTO  registerUser(RegisterRequestDTO dataRequest) throws Exception;
}
