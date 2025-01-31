# Library

## About the project

The Simplified Library Management System is an application that enables basic operations related to borrowing and managing
books in a library. The system allows users to add, remove, and edit books, register users, and track borrowings and
returns.

### Key Features ###

In the management system, we differentiate between three roles and their corresponding actions:

* Librarian
    - find all books in database or find just specific fields from records;
    - add, update, search, delete books;
    - find all book series, add new book series or book to existing book series, update and delete;
    - find all readers, update personal data, delete record, renew due date to return book, check all borrowed books divided into readers;
    - check statistics about actual borrowed books
* Reader
  - find all books in database or find just specific fields from records, borrow and return book, renew due date of return;
  - find all book series;
  - find all borrowed books by type id of reader;
* Analyst
    - check statistics about actual borrowed books, borrowing history
* Visitors without authentication
    - browse a collection of books and book series


### Getting Started ###

_To properly use app on your computer, please follow instructions and commands in terminal._

1. Clear the target directory, build the project, and package the resulting JAR file into the target directory.

```cmd
.\mvnw.cmd clean package
```

2. Create, start, and attach a container to the service with a functioning connection to PostgreSQL. This command also build an
   image based on instructions in Dockerfile.

```cmd
docker compose up
```

## Technology Stack ##

+ Java
+ Spring Boot Framework
+ PostgreSQL
+ Maven
+ Lombok
+ JUnit
+ Testcontainers
+ Mockito
+ FlyWay
+ Docker
+ Postman