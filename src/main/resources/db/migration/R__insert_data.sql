DELETE FROM books;
DELETE FROM readers;

INSERT INTO books (ean, author, title, date_of_publication, genre, publisher)
VALUES
  (9780345538922, 'J.R.R. Tolkien', 'The Lord of the Rings', '1954-07-29', 'Fantasy', 'George Allen & Unwin'),
  (9780451524935, 'George Orwell', '1984', '1949-06-08', 'Dystopian', 'Secker & Warburg'),
  (9780061120084, 'Harper Lee', 'To Kill a Mockingbird', '1960-07-11', 'Southern Gothic', 'J.B. Lippincott & Co.'),
  (9780143039903, 'Jane Austen', 'Pride and Prejudice', '1813-01-01', 'Romance', 'T. Egerton'),
  (9780679786868, 'F. Scott Fitzgerald', 'The Great Gatsby', '1925-04-10', 'Jazz Age', 'Charles Scribners Sons'),
  (9780452284249, 'Douglas Adams', 'The Hitchhikers Guide to the Galaxy', '1979-01-01', 'Science Fiction', 'Pan Books'),
  (9780385504206, 'Stephen King', 'It', '1986-01-01', 'Horror', 'Viking Press'),
  (9780060889096, 'Dan Brown', 'The Da Vinci Code', '2003-03-28', 'Thriller', 'Doubleday'),
  (9780061120091, 'Harper Lee', 'Go Set a Watchman', '2015-07-14', 'Southern Gothic', 'HarperCollins Publishers'),
  (9780061181974, 'John Green', 'The Fault in Our Stars', '2012-01-10', 'Young Adult', 'Dutton Books'),
  (9780439023540, 'J.K. Rowling', 'Harry Potter and the Sorcerers Stone', '1997-06-26', 'Fantasy', 'Bloomsbury Publishing'),
  (9780345391803, 'Suzanne Collins', 'The Hunger Games', '2008-09-14', 'Dystopian', 'Scholastic Press'),
  (9780316017994, 'Khaled Hosseini', 'The Kite Runner', '2003-06-03', 'Historical Fiction', 'Riverhead Books'),
  (9780307265545, 'Gabriel García Márquez', 'One Hundred Years of Solitude', '1967-01-01', 'Magical Realism', 'Editorial Sudamericana'),
  (9780375706119, 'Paulo Coelho', 'The Alchemist', '1988-01-01', 'Philosophical Novel', 'HarperCollins Publishers'),
  (9780316086968, 'Markus Zusak', 'The Book Thief', '2005-03-15', 'Historical Fiction', 'Alfred A. Knopf'),
  (9780553382747, 'Dan Brown', 'Angels & Demons', '2000-05-14', 'Thriller', 'Doubleday'),
  (9780143107264, 'Ken Follett', 'The Pillars of the Earth', '1989-01-01', 'Historical Fiction', 'William Morrow'),
  (9780553389577, 'Dan Brown', 'Inferno', '2013-05-14', 'Thriller', 'Doubleday'),
  (9780385513251, 'Stephen King', 'The Shining', '1977-01-01', 'Horror', 'Doubleday')
  ON CONFLICT DO NOTHING;

INSERT INTO readers (name, surname)
VALUES
    ('Karolina', 'Wilk'),
    ('Wojciech', 'Wilk'),
    ('Jan', 'Nowak'),
    ('Anna', 'Kowalska')
ON CONFLICT DO NOTHING;