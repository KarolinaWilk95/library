CREATE TABLE books(
    id SERIAL PRIMARY KEY,
    author VARCHAR(255),
    title VARCHAR(255),
    date_of_publication DATE,
    genre VARCHAR(255),
    publisher VARCHAR(255),
    isbn BIGINT
);

CREATE TABLE readers(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    surname VARCHAR(255)
);