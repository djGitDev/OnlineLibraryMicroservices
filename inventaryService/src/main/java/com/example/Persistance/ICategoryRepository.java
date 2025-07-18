package com.example.Persistance;

public interface ICategoryRepository {
    int findCategoryByNameElseCreate(String category);
}
