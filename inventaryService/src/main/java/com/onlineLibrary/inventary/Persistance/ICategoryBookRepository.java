package com.onlineLibrary.inventary.Persistance;

import com.onlineLibrary.inventary.Entities.DAO.BookCategoryDAO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryBookRepository extends JpaRepository<BookCategoryDAO, BookCategoryDAO.BookCategoryId> {

    @Transactional
    @Modifying
    @Query("DELETE FROM BookCategoryDAO bc WHERE bc.bookId = :bookId")
    void deleteAllByBookId(@Param("bookId") int bookId);

    List<BookCategoryDAO> findAllByBookId(int id);
}
