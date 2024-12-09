CREATE TABLE books(
    ean BIGINT PRIMARY KEY,
    author VARCHAR(255),
    title VARCHAR(255),
    date_of_publication DATE,
    genre VARCHAR(255),
    publisher VARCHAR(255)
);

CREATE TABLE readers(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    surname VARCHAR(255)
);