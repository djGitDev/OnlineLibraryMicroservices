package com.onlineLibrary.inventary.Persistance;

public interface ICategoryRepository {
    int findCategoryByNameElseCreate(String category);
}
