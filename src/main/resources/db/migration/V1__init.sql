CREATE TABLE books(
    id BIGSERIAL PRIMARY KEY,
    author VARCHAR(255),
    title VARCHAR(255),
    publication_date DATE,
    genre VARCHAR(255),
    publisher VARCHAR(255),
    isbn BIGINT,
    borrow_date DATE,
    reader_id BIGINT
);

CREATE TABLE readers(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    surname VARCHAR(255)
);