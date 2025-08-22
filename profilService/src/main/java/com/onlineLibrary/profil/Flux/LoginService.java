package com.onlineLibrary.profil.Flux;

import com.onlineLibrary.profil.Entities.DTO.LoginRequestDTO;
import com.onlineLibrary.profil.Entities.DAO.UserDAO;
import com.onlineLibrary.profil.Entities.DTO.LoginResponseDTO;
import com.onlineLibrary.profil.Entities.DTO.RefreshTokenResponseDTO;
import com.onlineLibrary.profil.Persistance.IRepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class LoginService implements ILoginService {

    private final IRepositoryUser repositoryUser;
    private final IHashService hashService;
    private final JwtService jwtService;

    @Autowired
    public LoginService(IRepositoryUser repositoryUser,IHashService hashService,JwtService jwtService) {
        this.repositoryUser = repositoryUser;
        this.hashService = hashService;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponseDTO authentifyUser(LoginRequestDTO credentials) throws Exception {

        String  enteredPassword = credentials.getPassword();
        Optional<UserDAO> userEncapsuled = repositoryUser.findUserByEmail(credentials.getEmail());

        if (userEncapsuled.isPresent()) {
            UserDAO userDAO = userEncapsuled.get();
            String storedHash = userDAO.getPassword();
            String role = userDAO.getRole();
            String email = userDAO.getEmail();
            String jwt = jwtService.generateToken(email,role);
            String refreshToken = jwtService.generateRefreshToken(email,role);

            if (hashService.verify(enteredPassword,storedHash)) {
                return new LoginResponseDTO("success", userDAO.getId(), email, jwt, refreshToken,role);
            } else {
                throw new RuntimeException("Incorrect password");
            }

        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public RefreshTokenResponseDTO generateAccesTokenFromRefresh(String refreshToken) {
        String email = jwtService.extractEmail(refreshToken);
        String role = jwtService.extractRole(refreshToken);
        String newAccessToken = jwtService.generateToken(email, role);
        return new RefreshTokenResponseDTO("success", newAccessToken);
    }

}

