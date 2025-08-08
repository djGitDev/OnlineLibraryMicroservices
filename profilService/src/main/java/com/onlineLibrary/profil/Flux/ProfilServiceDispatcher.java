package com.onlineLibrary.profil.Flux;

import com.google.gson.JsonObject;
import com.onlineLibrary.profil.Entities.DAO.AddressDAO;
import com.onlineLibrary.profil.Entities.DAO.UserDAO;
import com.onlineLibrary.profil.Entities.DTO.*;
import com.onlineLibrary.profil.Persistance.IRepositoryAddress;
import com.onlineLibrary.profil.Persistance.IRepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ProfilServiceDispatcher implements IProfilServiceDispatcher{

    private IRegisterService registerService;
    private ILoginService loginService;
    private IRepositoryAddress repositoryAddress;
    private IRepositoryUser repositoryUser;

    @Autowired
    public ProfilServiceDispatcher(IRegisterService registerService,ILoginService loginService,IRepositoryAddress repositoryAddress,IRepositoryUser repositoryUser) {
        this.registerService = registerService ;
        this.loginService = loginService ;
        this.repositoryAddress = repositoryAddress;
        this.repositoryUser = repositoryUser;
    }


    @Override
    public RegisterResponseDTO  handleRegistration(RegisterRequestDTO dataRequest) throws Exception {
        return registerService.registerUser(dataRequest);
    }

    @Override
    public LoginResponseDTO handleLogin(LoginRequestDTO credential) throws Exception {
        return loginService.authentifyUser(credential);
    }


    @Override
    public AddressDTO getProfile(int userId) throws Exception {
        Optional<AddressDAO> addressOpt = repositoryAddress.findUserProfilById(userId);

        if (addressOpt.isEmpty()) {
            throw new Exception("Profile not found for user with ID " + userId);
        }

        AddressDAO dao = addressOpt.get();

        // Création manuelle du DTO depuis le DAO
        AddressDTO dto = new AddressDTO
        (
                dao.getStreet(),
                dao.getCity(),
                dao.getPostalCode(),
                dao.getProvince(),
                dao.getCountry()
        );
        dto.setId(dao.getId());

        return dto;
    }

    @Override
    public UserDTO getUserData(int userId) throws Exception {
        Optional<UserDAO> userOpt = repositoryUser.findById(userId);

        if (userOpt.isEmpty()) {
            throw new Exception("User with ID " + userId + " not found");
        }

        UserDAO user = userOpt.get();

       UserDTO dto = new UserDTO
       (
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getPassword()
       );
        dto.setId(user.getId());
        return dto;
    }
}

