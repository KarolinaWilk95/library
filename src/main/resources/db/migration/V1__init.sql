CREATE TABLE readers(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    surname VARCHAR(255)
);
CREATE TABLE books(
    id BIGSERIAL PRIMARY KEY,
    reader_id BIGINT REFERENCES readers(id) ON DELETE SET NULL,
    author VARCHAR(255),
    title VARCHAR(255),
    publication_date DATE,
    genre VARCHAR(255),
    publisher VARCHAR(255),
    isbn BIGINT,
    borrow_date DATE,
    return_date DATE
);

