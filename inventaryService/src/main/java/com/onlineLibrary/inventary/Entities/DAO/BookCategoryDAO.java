package com.onlineLibrary.inventary.Entities.DAO;

import jakarta.persistence.*;

@Entity
@Table(name = "book_category")
public class BookCategoryDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "book_id", nullable = false)
    private int bookId;

    @Column(name = "category_id", nullable = false)
    private int categoryId;

    public BookCategoryDAO() {}

    public BookCategoryDAO(int bookId, int categoryId) {
        this.bookId = bookId;
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public int getBookId() {
        return bookId;
    }

    public int getCategoryId() {
        return categoryId;
    }
}
