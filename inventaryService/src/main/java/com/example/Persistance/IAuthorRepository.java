package com.example.Persistance;

public interface IAuthorRepository {
    int findAuthorByNameElseCreate(String author);
}
