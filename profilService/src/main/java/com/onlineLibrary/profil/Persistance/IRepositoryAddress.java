package com.onlineLibrary.profil.Persistance;

import com.google.gson.JsonObject;
import com.onlineLibrary.profil.Entities.Address;

public interface IRepositoryAddress {
    int createAddressUser(Address address) throws Exception;
    JsonObject findUserProfilById(int userId) throws Exception;
}
