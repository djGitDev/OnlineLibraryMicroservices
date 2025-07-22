package com.onlineLibrary.inventary.Persistance;

public interface IPublisherRepository {
    int findPublisherByNameElseCreate(String publisher);
}
