package com.onlineLibrary.profil.Flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Service
public class HashService implements IHashService {

//    @Override
//    public String encyptPassword(String password) {
//        return Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
//    }
//
//    @Override
//    public String decryptPassword(String encrypted) {
//        return new String(Base64.getDecoder().decode(encrypted), StandardCharsets.UTF_8);
//    }
//
//    @Override
//    public boolean verify(String password, String storedHash) {
//        return encyptPassword(password).equals(storedHash);
//
//    }

    private final PasswordEncoder passwordEncoder;

    public HashService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encyptPassword(String password) {
        return passwordEncoder.encode(password);
    }


    @Override
    public boolean verify(String password, String storedHash) {
        return passwordEncoder.matches(password, storedHash);
    }

}