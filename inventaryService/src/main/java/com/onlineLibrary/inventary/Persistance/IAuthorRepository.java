package com.onlineLibrary.inventary.Persistance;

public interface IAuthorRepository {
    int findAuthorByNameElseCreate(String author);
}
