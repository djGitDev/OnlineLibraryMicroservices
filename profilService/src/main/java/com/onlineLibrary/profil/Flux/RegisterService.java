package com.onlineLibrary.profil.Flux;

import com.google.gson.JsonObject;
import com.onlineLibrary.profil.Entities.User;
import com.onlineLibrary.profil.Entities.Address;
import com.onlineLibrary.profil.Persistance.IRepositoryAddress;
import com.onlineLibrary.profil.Persistance.IRepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService implements IRegisterService {

    private final IRepositoryUser repositoryUser;
    private final IRepositoryAddress repositoryAddress;
    private final IHashService passwordHasher;

    @Autowired
    public RegisterService(IRepositoryUser repositoryUser,IRepositoryAddress repositoryAddress,IHashService passwordHasher) {
        this.repositoryUser = repositoryUser;
        this.repositoryAddress = repositoryAddress;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public JsonObject registerUser(JsonObject data) throws Exception {
        User user = convertJsonToUser(data);
        Address address = convertJsonToAddress(data);
        user.setPassword(passwordHasher.encyptPassword(user.getPassword()));
        JsonObject response = new JsonObject();
        int userId = repositoryUser.createUser(user);
        if (userId <= 0) {
            throw new Exception("Failed to save user");
        }
        address.setUserId(userId);
        int addressId = repositoryAddress.createAddressUser(address);
        if (addressId > 0) {
            response.addProperty("status", "success");
            response.addProperty("user_id", userId);
            response.addProperty("address_id", addressId);
        } else {
            response.addProperty("status", "partial_success");
            response.addProperty("user_id", userId);
            response.addProperty("message", "User saved but address failed");
        }

        return response;
    }

    private User convertJsonToUser(JsonObject data) {
        JsonObject userJson = data.getAsJsonObject("user");
        return new User(
                userJson.get("first_name").getAsString(),
                userJson.get("last_name").getAsString(),
                userJson.get("email").getAsString(),
                userJson.get("phone").getAsString(),
                userJson.get("password").getAsString()
        );
    }

    private Address convertJsonToAddress(JsonObject data) {
        JsonObject addressJson = data.getAsJsonObject("address");
        Address address = new Address();
        address.setStreet(addressJson.get("street").getAsString());
        address.setCity(addressJson.get("city").getAsString());
        address.setPostalCode(addressJson.get("postal_code").getAsString());
        address.setProvince(addressJson.get("province").getAsString());
        address.setCountry(addressJson.get("country").getAsString());
        return address;
    }
}