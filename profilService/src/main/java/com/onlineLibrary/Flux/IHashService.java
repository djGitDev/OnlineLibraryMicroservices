package com.onlineLibrary.Flux;

public interface IHashService {
    String encyptPassword(String password);
    String decryptPassword(String password);
    boolean verify(String password, String storedHash);
}
