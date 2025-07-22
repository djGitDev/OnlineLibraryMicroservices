package com.onlineLibrary.profil.Flux;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HashService implements IHashService {

    @Override
    public String encyptPassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String decryptPassword(String encrypted) {
        return new String(Base64.getDecoder().decode(encrypted), StandardCharsets.UTF_8);
    }

    @Override
    public boolean verify(String password, String storedHash) {
        return encyptPassword(password).equals(storedHash);

    }

}