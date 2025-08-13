package com.onlineLibrary.inventary.Persistance;

import com.onlineLibrary.inventary.Entities.DAO.AuthorBookDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthorBookRepository extends JpaRepository<AuthorBookDAO, Integer> {
}
