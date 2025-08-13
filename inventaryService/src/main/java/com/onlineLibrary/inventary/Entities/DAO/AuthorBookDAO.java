package com.onlineLibrary.inventary.Entities.DAO;

import jakarta.persistence.*;

@Entity
@Table(name = "author_book")
public class AuthorBookDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "book_id", nullable = false)
    private int bookId;

    @Column(name = "author_id", nullable = false)
    private int authorId;

    public AuthorBookDAO() {}

    public AuthorBookDAO(int bookId, int authorId) {
        this.bookId = bookId;
        this.authorId = authorId;
    }

    public int getId() {
        return id;
    }

    public int getBookId() {
        return bookId;
    }

    public int getAuthorId() {
        return authorId;
    }
}
