package ksiazkopol.library.integrationTest;

import ksiazkopol.library.book.Book;
import ksiazkopol.library.book.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.3-alpine");

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    void setUp() {

        bookRepository.deleteAll();
        List<Book> books = List.of(
                new Book(null, "The Lord of the Rings", "J.R.R. Tolkien", "Fantasy", "George Allen & Unwin", LocalDate.parse("1954-07-29"), 9780345538922L, null, null, null,null),
                new Book(null, "The Shining", "Stephen King", "Horror", "Doubleday", LocalDate.parse("1977-01-01"), 9780385513251L, null, null, null,null),
                new Book(null, "It", "Stephen King", "Horror", "Viking Press", LocalDate.parse("1986-01-01"), 9780385504206L, null, null, null,null),
                new Book(null, "Harry Potter and the Sorcerers Stone", "J.K. Rowling", "Fantasy", "Bloomsbury Publishing", LocalDate.parse("1997-06-26"), 9780439023540L, null, null, null,null));
        bookRepository.saveAll(books);

    }

    @Test
    void findAllBooks() {

        //given
        List<Book> list = new ArrayList<>();

        //when
        var findBooks = restTemplate.exchange("/api/books", HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>() {
        });
        list = bookRepository.findAll();

        //then
        assertThat(list.get(0).getISBN()).isEqualTo(findBooks.getBody().get(0).getISBN());
        assertThat(list.get(1).getISBN()).isEqualTo(findBooks.getBody().get(1).getISBN());
        assertThat(list.get(2).getISBN()).isEqualTo(findBooks.getBody().get(2).getISBN());
        assertThat(list.get(3).getISBN()).isEqualTo(findBooks.getBody().get(3).getISBN());
        assertThat(findBooks.getBody()).hasSize(4);

    }

    @Test
    void addBook() {
        //given
        Book newBook = new Book(null, "The Hunger Games", "Suzanne Collins", "Dystopian", "Scholastic Press", LocalDate.parse("2008-09-14"), 9780345391803L, null, null, null,null);

        //when
        ResponseEntity<Book> responseEntity = restTemplate.exchange("/api/books", HttpMethod.POST, new HttpEntity<>(newBook), Book.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo("The Hunger Games");
        assertThat(responseEntity.getBody().getAuthor()).isEqualTo("Suzanne Collins");
        assertThat(responseEntity.getBody().getGenre()).isEqualTo("Dystopian");
        assertThat(responseEntity.getBody().getISBN()).isEqualTo(9780345391803L);
        assertThat(responseEntity.getBody().getPublisher()).isEqualTo("Scholastic Press");
        assertThat(responseEntity.getBody().getPublicationDate()).isEqualTo("2008-09-14");

    }

    @Test
    void findBookByIdIfExist() {
        //given

        Long id = bookRepository.findAll().getFirst().getId();

        //when
        ResponseEntity<Book> responseEntity = restTemplate.exchange("/api/books/" + id, HttpMethod.GET, null, Book.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();

    }

    @Test
    void findBookByIdIfNotExist() {
        //given

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange("/api/books/-1", HttpMethod.GET, null, String.class);

        //then
        assertThat(responseEntity.getBody()).isEqualTo("Selected book not found");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void deleteBookIfExist() {
        //given
        Long id = bookRepository.findAll().get(0).getId();

        //when
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/api/books/" + id, HttpMethod.DELETE, null, Void.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookRepository.findById(id)).isEmpty();

    }

    @Test
    void deleteBookIfNotExist() {
        //given

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange("/api/books/-1", HttpMethod.DELETE, null, String.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Selected book not found");

    }

    @Test
    void updateBookIfExist() {
        //given
        Book updatedBook = new Book(null, "Władca pierścieni", "J.R.R. Tolkien", "Fantasy", "George Allen & Unwin", LocalDate.parse("1954-07-29"), 9780345538922L, null, null, null,null);
        Long id = bookRepository.findAll().get(0).getId();

        //when
        ResponseEntity<Book> responseEntity = restTemplate.exchange("/api/books/" + id, HttpMethod.PUT, new HttpEntity<>(updatedBook), Book.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookRepository.findById(id).get().getTitle()).isEqualTo("Władca pierścieni");
    }

    @Test
    void updateBookIfNotExist() {
        //given
        Book updatedBook = new Book(null, "Władca pierścieni", "J.R.R. Tolkien", "Fantasy", "George Allen & Unwin", LocalDate.parse("1954-07-29"), 9780345538922L, null, null, null,null);

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange("/api/books/-1", HttpMethod.PUT, new HttpEntity<>(updatedBook), String.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Selected book not found");

    }

    @Test
    void searchBookIfExist() {
        //given

//        new Book(null, "The Lord of the Rings", "J.R.R. Tolkien", "Fantasy", "George Allen & Unwin", LocalDate.parse("1954-07-29"), 9780345538922L, null, null, null),

        //when
        var responseEntity = restTemplate.exchange("/api/books/search?author=Tolkien", HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>() {});

        //then
        var result = responseEntity.getBody();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isNotNull();
        assertThat(result.get(0).getTitle()).isEqualTo("The Lord of the Rings");
        assertThat(result.get(0).getAuthor()).isEqualTo("J.R.R. Tolkien");
        assertThat(result.get(0).getGenre()).isEqualTo("Fantasy");
        assertThat(result.get(0).getISBN()).isEqualTo(9780345538922L);

    }

}
