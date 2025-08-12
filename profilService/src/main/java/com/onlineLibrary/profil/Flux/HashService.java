package com.onlineLibrary.profil.Flux;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class HashService implements IHashService {


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