CREATE TABLE books_authors (
    book_id   BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    PRIMARY KEY (book_id, author_id),

    CONSTRAINT fk_book_id
        FOREIGN KEY (book_id) REFERENCES books (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_author_id
        FOREIGN KEY (author_id) REFERENCES authors (id)
            ON DELETE CASCADE
);