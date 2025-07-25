package com.onlineLibrary.profil.Flux;

import com.google.gson.JsonObject;
import com.onlineLibrary.profil.Entities.Credential;
import com.onlineLibrary.profil.Entities.User;
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
    public JsonObject authentifyUser(JsonObject data) throws Exception {
        Credential credentials = convertJsonToCredential(data);
        String passEncrypted = hashService.encyptPassword(credentials.getPassword());
        Optional<User> userEncapsuled = repositoryUser.findUserByEmail(credentials.getEmail());

        if (userEncapsuled.isPresent()) {
            User user = userEncapsuled.get();

            if (user.getPassword().equals(passEncrypted)) {
                JsonObject response = new JsonObject();
                response.addProperty("status", "success");
                response.addProperty("user_id", user.getId());
                response.addProperty("email", user.getEmail());
                return response;
            } else {
                JsonObject response = new JsonObject();
                response.addProperty("status", "failure");
                response.addProperty("message", "Mot de passe incorrect");
                return response;
            }

        } else {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failure");
            response.addProperty("message", "Utilisateur non trouvé");
            return response;
        }
    }

    private Credential convertJsonToCredential(JsonObject data) {
        JsonObject credentialsJson = data.getAsJsonObject("credentials");
        return new Credential(
                credentialsJson.get("email").getAsString(),
                credentialsJson.get("password").getAsString()
        );
    }
}

