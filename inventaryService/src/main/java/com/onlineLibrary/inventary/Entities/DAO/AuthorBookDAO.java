
package com.onlineLibrary.inventary.Entities.DAO;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "books_authors")
@IdClass(AuthorBookDAO.AuthorBookId.class)
public class AuthorBookDAO {

    @Id
    @Column(name = "book_id", nullable = false)
    private int bookId;

    @Id
    @Column(name = "author_id", nullable = false)
    private int authorId;

    public AuthorBookDAO() {}

    public AuthorBookDAO(int bookId, int authorId) {
        this.bookId = bookId;
        this.authorId = authorId;
    }

    public int getBookId() { return bookId; }
    public int getAuthorId() { return authorId; }

    // Static class for the composite key
    public static class AuthorBookId implements Serializable {
        private int bookId;
        private int authorId;

        public AuthorBookId() {}

        public AuthorBookId(int bookId, int authorId) {
            this.bookId = bookId;
            this.authorId = authorId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AuthorBookId)) return false;
            AuthorBookId that = (AuthorBookId) o;
            return bookId == that.bookId && authorId == that.authorId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(bookId, authorId);
        }
    }
}
