package ksiazkopol.library.integrationTest;


import ksiazkopol.library.book.Book;
import ksiazkopol.library.book.BookRepository;
import ksiazkopol.library.booksBorrowingHistory.BooksBorrowingHistory;
import ksiazkopol.library.booksBorrowingHistory.BooksBorrowingHistoryRepository;
import ksiazkopol.library.reader.Reader;
import ksiazkopol.library.reader.ReaderRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReaderControllerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.3-alpine");

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    ReaderRepository readerRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BooksBorrowingHistoryRepository booksBorrowingHistoryRepository;


    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        readerRepository.deleteAll();
        booksBorrowingHistoryRepository.deleteAll();


        List<Reader> readers = List.of(
                new Reader(null, "Karolina", "Wilk", null),
                new Reader(null, "Wojciech", "Wilk", null),
                new Reader(null, "Anna", "Nowak", null),
                new Reader(null, "Jan", "Kowalski", null)
        );
        readerRepository.saveAll(readers);


        List<Book> books = List.of(
                new Book(null, "The Lord of the Rings", "J.R.R. Tolkien", "Fantasy", "George Allen & Unwin", LocalDate.parse("1954-07-29"), 9780345538922L, null, null, null, null),
                new Book(null, "The Shining", "Stephen King", "Horror", "Doubleday", LocalDate.parse("1977-01-01"), 9780385513251L, null, null, null, null),
                new Book(null, "It", "Stephen King", "Horror", "Viking Press", LocalDate.parse("1986-01-01"), 9780385504206L, null, null, null, null));
        bookRepository.saveAll(books);

        var bookFromRepo = books.getFirst();
        var readerFromRepo = readers.getFirst();

        List<BooksBorrowingHistory> booksBorrowingHistoryList = List.of(
                new BooksBorrowingHistory(null, bookFromRepo.getId(),
                        bookFromRepo.getTitle(), bookFromRepo.getAuthor(), bookFromRepo.getISBN(),
                        readerFromRepo.getId(), readerFromRepo.getName(), readerFromRepo.getSurname(), bookFromRepo.getBorrowDate(), bookFromRepo.getBorrowDate(), null, (short) 0));

        booksBorrowingHistoryRepository.saveAll(booksBorrowingHistoryList);
    }

    @Test
    void findAllReadersValidRole() {
        //given
        List<Reader> readers = new ArrayList<>();

        //when
        var findReaders = restTemplate.withBasicAuth("librarian", "1234").exchange("/api/readers", HttpMethod.GET, null, new ParameterizedTypeReference<List<Reader>>() {
        });
        readers = readerRepository.findAll();

        //then
        assertThat(findReaders.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(findReaders.getBody()).hasSize(4);
        assertThat(findReaders.getBody().get(0).getId()).isEqualTo(readers.get(0).getId());
        assertThat(findReaders.getBody().get(1).getId()).isEqualTo(readers.get(1).getId());
        assertThat(findReaders.getBody().get(2).getId()).isEqualTo(readers.get(2).getId());
        assertThat(findReaders.getBody().get(3).getId()).isEqualTo(readers.get(3).getId());

    }

    @Test
    void findAllReadersInvalidRole() {
        //given

        //when
        var findReaders = restTemplate.withBasicAuth("reader", "reader").exchange("/api/readers", HttpMethod.GET, null, Void.class);

        //then
        assertThat(findReaders.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    void addOneReaderValidRole() {
        //given
        var newReader = new Reader(null, "Karolina", "Wójcik", null);

        //when
        ResponseEntity<Reader> responseEntity = restTemplate.withBasicAuth("librarian", "1234").exchange("/api/readers", HttpMethod.POST, new HttpEntity<>(newReader), Reader.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getName()).isEqualTo("Karolina");
        assertThat(responseEntity.getBody().getSurname()).isEqualTo("Wójcik");

    }

    @Test
    void addOneReaderInvalidRole() {
        //given
        var newReader = new Reader(null, "Karolina", "Wójcik", null);

        //when
        ResponseEntity<Reader> responseEntity = restTemplate.withBasicAuth("analyst", "1111").exchange("/api/readers", HttpMethod.POST, new HttpEntity<>(newReader), Reader.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    void findReaderByIdIfExistValidRole() {
        //given
        Long id = readerRepository.findAll().get(0).getId();


        //when
        ResponseEntity<Reader> responseEntity = restTemplate.withBasicAuth("librarian", "1234").exchange("/api/readers/" + id, HttpMethod.GET, null, Reader.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getId()).isEqualTo(id);
        assertThat(responseEntity.getBody().getName()).isEqualTo(readerRepository.findById(id).get().getName());
        assertThat(responseEntity.getBody().getSurname()).isEqualTo(readerRepository.findById(id).get().getSurname());

    }

    @Test
    void findReaderByIdIfExistInvalidRole() {
        //given
        Long id = readerRepository.findAll().get(0).getId();

        //when
        ResponseEntity<Reader> responseEntity = restTemplate.withBasicAuth("reader", "reader").exchange("/api/readers/" + id, HttpMethod.GET, null, Reader.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    void findReaderByIdIfNotExist() {
        //given

        //when
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("admin", "admin").exchange("/api/readers/-1", HttpMethod.GET, null, String.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Selected reader not found");

    }

    @Test
    void deleteReaderByIdIfExistValidRole() {
        //given
        Long id = readerRepository.findAll().get(0).getId();

        //when
        ResponseEntity<Reader> responseEntity = restTemplate.withBasicAuth("librarian", "1234").exchange("/api/readers/" + id, HttpMethod.DELETE, null, Reader.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(readerRepository.findById(id)).isEmpty();

    }

    @Test
    void deleteReaderByIdIfExistInvalidRole() {
        //given
        Long id = readerRepository.findAll().get(0).getId();

        //when
        ResponseEntity<Reader> responseEntity = restTemplate.withBasicAuth("reader", "reader").exchange("/api/readers/" + id, HttpMethod.DELETE, null, Reader.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void deleteReaderByIdIfNotExist() {
        //given

        //when
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("admin", "admin").exchange("/api/readers/-1", HttpMethod.DELETE, null, String.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Selected reader not found");
    }

    @Test
    void searchReaderValidRole() {
        //given
        //new Reader(null, "Karolina", "Wilk", null),

        //when
        var responseEntity = restTemplate.withBasicAuth("librarian", "1234").exchange("/api/readers/search?name=Karolina", HttpMethod.GET, null, new ParameterizedTypeReference<List<Reader>>() {
        });
        var result = responseEntity.getBody();

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isNotNull();
        assertThat(result.get(0).getName()).isEqualTo("Karolina");
        assertThat(result.get(0).getSurname()).isEqualTo("Wilk");

    }

    @Test
    void searchReaderInvalidRole() {
        //given

        //when
        var responseEntity = restTemplate.withBasicAuth("reader", "reader").exchange("/api/readers/search?name=Karolina", HttpMethod.GET, null, Void.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void borrowBookIfReaderAndBookExistValidRole() {
        //given
        Long idReader = readerRepository.findAll().getFirst().getId();
        Long idBook = bookRepository.findAll().getFirst().getId();

        //when
        ResponseEntity<Void> responseEntity = restTemplate.withBasicAuth("reader", "reader").exchange("/api/readers/" + idReader + "/books/" + idBook, HttpMethod.PUT, null, Void.class);

        //then

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void borrowBookIfReaderAndBookExistInvalidRole() {
        //given
        Long idReader = readerRepository.findAll().getFirst().getId();
        Long idBook = bookRepository.findAll().getFirst().getId();

        //when
        ResponseEntity<Void> responseEntity = restTemplate.withBasicAuth("analyst", "1111").exchange("/api/readers/" + idReader + "/books/" + idBook, HttpMethod.PUT, null, Void.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void borrowBookIfReaderExistBookNotExist() {
        //given
        Long idReader = readerRepository.findAll().getFirst().getId();

        //when
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("admin", "admin").exchange("/api/readers/" + idReader + "/books/-1", HttpMethod.PUT, null, String.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Selected book not found");

    }

    @Test
    void borrowBookAlreadyBorrowed() {
        //given
        Reader reader = readerRepository.findAll().getFirst();
        Book book = bookRepository.findAll().getFirst();

        book.setReader(reader);
        book.setBorrowDate(LocalDate.now());
        bookRepository.save(book);

        //when
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("admin", "admin").exchange("/api/readers/" + reader.getId() + "/books/" + book.getId(), HttpMethod.PUT, null, String.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo("Selected book is already borrowed");
    }

    @Test
    void borrowBookIfBookExistReaderNotExist() {
        //given
        Long idBook = bookRepository.findAll().getFirst().getId();

        //when
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("admin", "admin").exchange("/api/readers/-1/books/" + idBook, HttpMethod.PUT, null, String.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Selected reader not found");

    }

    @Test
    void returnBookIfReaderAndBookExistValidRole() {
        //given
        Reader reader = readerRepository.findAll().get(0);
        Book book = bookRepository.findAll().get(0);

        reader.setBooks(List.of(book));
        readerRepository.save(reader);

        book.setReader(reader);
        book.setBorrowDate(LocalDate.now());
        book.setExpectedReturnDate(LocalDate.now().plusDays(7));
        bookRepository.save(book);

        //when
        ResponseEntity<Void> responseEntity = restTemplate.withBasicAuth("librarian", "1234").exchange("/api/readers/" + reader.getId() + "/books/" + book.getId(), HttpMethod.DELETE, null, Void.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookRepository.findById(book.getId()).get().getReader()).isNull();
        assertThat(readerRepository.findById(reader.getId()).get().getBooks()).doesNotContain(book);

    }

    @Test
    void returnBookIfReaderAndBookExistInvalidRole() {
        //given
        Reader reader = readerRepository.findAll().get(0);
        Book book = bookRepository.findAll().get(0);

        //when
        ResponseEntity<Void> responseEntity = restTemplate.withBasicAuth("analyst", "1111").exchange("/api/readers/" + reader.getId() + "/books/" + book.getId(), HttpMethod.DELETE, null, Void.class);


        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    void returnBookIfReaderNotExist() {
        //given
        Book book = bookRepository.findAll().getFirst();

        //when
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("admin", "admin").exchange("/api/readers/-1/books/" + book.getId(), HttpMethod.DELETE, null, String.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Selected reader not found");
    }

    @Test
    void returnBookIfBookNotExist() {
        //given
        Reader reader = readerRepository.findAll().getFirst();

        //when
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("admin", "admin").exchange("/api/readers/" + reader.getId() + "/books/-1", HttpMethod.DELETE, null, String.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Selected book not found");
    }

    @Test
    void returnBookNeverBorrowed() {
        //given
        Reader reader = readerRepository.findAll().getFirst();
        Book book = bookRepository.findAll().getFirst();

        //when
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("admin", "admin").exchange("/api/readers/" + reader.getId() + "/books/" + book.getId(), HttpMethod.DELETE, null, String.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Selected book wasn't borrowed");

    }

}
