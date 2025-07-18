package com.example.Persistance;

public interface IPublisherRepository {
    int findPublisherByNameElseCreate(String publisher);
}
