package ksiazkopol.library.integrationTest;


import ksiazkopol.library.book.BookRepository;
import ksiazkopol.library.booksBorrowingHistory.BooksBorrowingHistory;
import ksiazkopol.library.booksBorrowingHistory.BooksBorrowingHistoryRepository;
import ksiazkopol.library.booksBorrowingHistory.BooksBorrowingHistorySummary;
import ksiazkopol.library.reader.ReaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BooksBorrowingHistoryIntegrationTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.3-alpine");

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    BooksBorrowingHistoryRepository booksBorrowingHistoryRepository;
    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    void setUp() {

        booksBorrowingHistoryRepository.deleteAll();

        List<BooksBorrowingHistory> booksBorrowingHistories = List.of(
                new BooksBorrowingHistory(null, 20L, "The Shining", "Stephen King", 9780385513251L, 4L, "Anna", "Kowalska", LocalDate.parse("2025-01-17"), LocalDate.parse("2025-01-24"), LocalDate.parse("2025-01-17"), (short) 0),
                new BooksBorrowingHistory(null, 20L, "The Shining", "Stephen King", 9780385513251L, 1L, "Karolina", "Wilk", LocalDate.parse("2025-01-17"), LocalDate.parse("2025-01-31"), LocalDate.parse("2025-01-17"), (short) 1));

        booksBorrowingHistoryRepository.saveAll(booksBorrowingHistories);

    }

    @Test
    void findAllRecords() {
        //given
        List<BooksBorrowingHistory> list = new ArrayList<>();

        //when
        var result = restTemplate.exchange("/api/status", HttpMethod.GET, null, new ParameterizedTypeReference<List<BooksBorrowingHistory>>() {
        });

        list = booksBorrowingHistoryRepository.findAll();

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).hasSize(2);
        assertThat(result.getBody().get(0).getISBN()).isEqualTo(9780385513251L);
        assertThat(result.getBody().get(1).getISBN()).isEqualTo(9780385513251L);
    }

    @Test
    void bookStatus() {
        //given

        var amountOfBooks = bookRepository.countAllBooks();
        var amountOfAvailableBooks = bookRepository.countBooksAvailableToBorrow();
        var amountOfBorrowedBooks = bookRepository.countBorrowedBooks();
        var amountOfBooksAfterDueDate = booksBorrowingHistoryRepository.countBooksAfterTheDueDate();
        var amountOfBooksAfterDueDateNotReturned = booksBorrowingHistoryRepository.countBooksAfterDueDateNotReturned();
        var amountOfReadersAfterDueDate = booksBorrowingHistoryRepository.countAllReadersAfterDueDate();

        //when
        var result = restTemplate.exchange("/api/books/info", HttpMethod.GET, null, BooksBorrowingHistorySummary.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getAmountOfBooks()).isEqualTo(amountOfBooks);
        assertThat(result.getBody().getAmountOfBorrowedBooks()).isEqualTo(amountOfBorrowedBooks);
        assertThat(result.getBody().getAmountOfAvailableBooks()).isEqualTo(amountOfAvailableBooks);
        assertThat(result.getBody().getBooksAfterDueDateReturned()).isEqualTo(amountOfBooksAfterDueDate);
        assertThat(result.getBody().getBooksAfterDueDateNotReturned()).isEqualTo(amountOfBooksAfterDueDateNotReturned);
        assertThat(result.getBody().getReadersAfterDueDate()).isEqualTo(amountOfReadersAfterDueDate);
    }
}
