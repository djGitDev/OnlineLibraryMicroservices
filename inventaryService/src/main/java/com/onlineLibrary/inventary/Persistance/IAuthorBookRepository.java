package com.onlineLibrary.inventary.Persistance;

public interface IAuthorBookRepository {
    int createRelation(int idBook, int idAuthor);
}
