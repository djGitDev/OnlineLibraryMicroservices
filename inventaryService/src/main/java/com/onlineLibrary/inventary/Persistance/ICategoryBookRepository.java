package com.onlineLibrary.inventary.Persistance;

public interface ICategoryBookRepository {
    int createRelation(int idBook, int idCategory);
}
