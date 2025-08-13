package com.onlineLibrary.inventary.Persistance;

import com.onlineLibrary.inventary.Entities.DAO.BookDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IBookRepository extends JpaRepository<BookDAO, Integer> {
     Optional<BookDAO> findByIsbn(String isbn);
}
