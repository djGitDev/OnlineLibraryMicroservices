package com.onlineLibrary.profil.Flux;

public interface IHashService {
    String encyptPassword(String password);
    String decryptPassword(String password);
    boolean verify(String password, String storedHash);
}
