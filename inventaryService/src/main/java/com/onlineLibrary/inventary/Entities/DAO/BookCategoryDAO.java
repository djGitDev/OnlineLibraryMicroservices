package com.onlineLibrary.inventary.Entities.DAO;//package com.onlineLibrary.inventary.Entities.DAO;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "books_categories")
@IdClass(BookCategoryDAO.BookCategoryId.class)
public class BookCategoryDAO {

    @Id
    @Column(name = "book_id")
    private int bookId;

    @Id
    @Column(name = "category_id")
    private int categoryId;

    public BookCategoryDAO() {}

    public BookCategoryDAO(int bookId, int categoryId) {
        this.bookId = bookId;
        this.categoryId = categoryId;
    }

    public int getBookId() { return bookId; }
    public int getCategoryId() { return categoryId; }

    // Static class for the composite key
    public static class BookCategoryId implements Serializable {
        private int bookId;
        private int categoryId;

        public BookCategoryId() {}

        public BookCategoryId(int bookId, int categoryId) {
            this.bookId = bookId;
            this.categoryId = categoryId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BookCategoryId)) return false;
            BookCategoryId that = (BookCategoryId) o;
            return bookId == that.bookId && categoryId == that.categoryId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(bookId, categoryId);
        }
    }
}
