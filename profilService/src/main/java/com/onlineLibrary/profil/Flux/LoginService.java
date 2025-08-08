package com.onlineLibrary.profil.Flux;

import com.onlineLibrary.profil.Entities.DTO.LoginRequestDTO;
import com.onlineLibrary.profil.Entities.DAO.UserDAO;
import com.onlineLibrary.profil.Entities.DTO.LoginResponseDTO;
import com.onlineLibrary.profil.Persistance.IRepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class LoginService implements ILoginService {

    private final IRepositoryUser repositoryUser;
    private final IHashService hashService;

    @Autowired
    public LoginService(IRepositoryUser repositoryUser,IHashService hashService) {
        this.repositoryUser = repositoryUser;
        this.hashService = hashService;
    }

    @Override
    public LoginResponseDTO authentifyUser(LoginRequestDTO credentials) throws Exception {

        String passEncrypted = hashService.encyptPassword(credentials.getPassword());
        Optional<UserDAO> userEncapsuled = repositoryUser.findUserByEmail(credentials.getEmail());

        if (userEncapsuled.isPresent()) {
            UserDAO userDAO = userEncapsuled.get();

            if (userDAO.getPassword().equals(passEncrypted)) {
                return new LoginResponseDTO("success", userDAO.getId(), userDAO.getEmail());
            } else {
                throw new RuntimeException("Incorrect password");
            }

        } else {
            throw new RuntimeException("User not found");
        }
    }

}

