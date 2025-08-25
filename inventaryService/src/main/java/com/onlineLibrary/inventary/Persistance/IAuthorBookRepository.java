package com.onlineLibrary.inventary.Persistance;

import com.onlineLibrary.inventary.Entities.DAO.AuthorBookDAO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAuthorBookRepository extends JpaRepository<AuthorBookDAO, AuthorBookDAO.AuthorBookId> {

    @Transactional
    @Modifying
    @Query("DELETE FROM AuthorBookDAO ab WHERE ab.bookId = :bookId")
    void deleteAllByBookId(@Param("bookId") int bookId);

    List<AuthorBookDAO> findAllByBookId(int id);
}
