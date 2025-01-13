package ksiazkopol.library.integrationTest;

import ksiazkopol.library.book.Book;
import ksiazkopol.library.book.BookRepository;
import ksiazkopol.library.bookseries.BookSeries;
import ksiazkopol.library.bookseries.BookSeriesRepository;
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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookSeriesIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.3-alpine");

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    BookSeriesRepository bookSeriesRepository;
    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    void setUp() {

        bookRepository.deleteAll();
        bookSeriesRepository.deleteAll();

        List<BookSeries> bookSeriesList = List.of(
                new BookSeries(null, "The Chronicles of Narnia", "C.S. Lewis", null),
                new BookSeries(null, "Seria z Sewerynem Zaorskim", "Remigiusz Mróz", null),
                new BookSeries(null, "Jeżycjada", "Małgorzata Musierowicz", null));

        bookSeriesRepository.saveAll(bookSeriesList);


        List<Book> books = List.of(
                new Book(null, "Szósta klepka", "Małgorzata Musierowicz", "Youth literature", "Akapit Press", LocalDate.parse("1977-01-01"), 9788360773031L, null, null, null, null),
                new Book(null, "Ida sierpniowa", "Małgorzata Musierowicz", "Youth literature", "Akapit Press", LocalDate.parse("1988-09-01"), 9788360773901L, null, null, null, null));
        bookRepository.saveAll(books);

    }

    @Test
    void findAllBookSeries() {
        //given
        List<BookSeries> bookSeries = new ArrayList<>();

        //when
        var bookSeriesList = restTemplate.exchange("/api/book-series", HttpMethod.GET, null, new ParameterizedTypeReference<List<BookSeries>>() {
        });
        bookSeries = bookSeriesRepository.findAll();

        //then
        assertThat(bookSeriesList.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookSeriesList.getBody()).hasSize(3);
        assertThat(bookSeriesList.getBody().get(0).getId()).isEqualTo(bookSeries.get(0).getId());
        assertThat(bookSeriesList.getBody().get(1).getId()).isEqualTo(bookSeries.get(1).getId());
        assertThat(bookSeriesList.getBody().get(2).getId()).isEqualTo(bookSeries.get(2).getId());

    }

    @Test
    void addBookSeries() {
        //given
        BookSeries bookSeries = new BookSeries(null, "The Dark Tower", "Stephen King", null);

        //when
        var result = restTemplate.exchange("/api/book-series", HttpMethod.POST, new HttpEntity<>(bookSeries), BookSeries.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getNameOfSeries()).isEqualTo("The Dark Tower");
        assertThat(result.getBody().getAuthor()).isEqualTo("Stephen King");

    }

    @Test
    void findBookSeriesIfExist() {
        //given
        Long id = bookSeriesRepository.findAll().get(0).getId();

        //when
        var result = restTemplate.exchange("/api/book-series/" + id, HttpMethod.GET, null, BookSeries.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getId()).isEqualTo(id);

    }

    @Test
    void findBookSeriesIfNotExist() {
        //given

        //when
        var result = restTemplate.exchange("/api/book-series/-1", HttpMethod.GET, null, String.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo("Selected book series not found");

    }

    @Test
    void deleteBookSeriesIfExist() {
        //given
        Long id = bookSeriesRepository.findAll().get(0).getId();

        //when
        var result = restTemplate.exchange("/api/book-series/" + id, HttpMethod.DELETE, null, BookSeries.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookSeriesRepository.findById(id)).isEmpty();

    }

    @Test
    void deleteBookSeriesIfNotExist() {
        //given

        //when
        var result = restTemplate.exchange("/api/book-series/-1", HttpMethod.DELETE, null, String.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo("Selected book series not found");

    }

    @Test
    void updateById() {
        //given
        Long id = bookSeriesRepository.findAll().get(0).getId();
        BookSeries updateBookSeries = new BookSeries();
        updateBookSeries.setNameOfSeries("Updated name of series");
        updateBookSeries.setAuthor("Updated author of book series");

        //when
        var result = restTemplate.exchange("/api/book-series/" + id, HttpMethod.PUT, new HttpEntity<>(updateBookSeries), BookSeries.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookSeriesRepository.findById(id).get().getNameOfSeries()).isEqualTo("Updated name of series");
        assertThat(bookSeriesRepository.findById(id).get().getAuthor()).isEqualTo("Updated author of book series");

    }

    @Test
    void updateByIdIfNotExist() {
        //given
        BookSeries updateBookSeries = new BookSeries();
        updateBookSeries.setNameOfSeries("Updated name of series");
        updateBookSeries.setAuthor("Updated author of book series");

        //when
        var result = restTemplate.exchange("/api/book-series/-1", HttpMethod.PUT, new HttpEntity<>(updateBookSeries), String.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo("Selected book series not found");

    }

    @Test
    void addBookToBookSeriesIfExist() {
        //given
        Long idBook = bookRepository.findAll().get(0).getId();
        Long idBookSeries = bookSeriesRepository.findAll().get(0).getId();

        //when
        var result = restTemplate.exchange("/api/book-series/" + idBookSeries + "/books/" + idBook, HttpMethod.PUT, null, BookSeries.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookRepository.findById(idBook).get().getBookSeries().getId()).isEqualTo(idBookSeries);
        assertThat(bookSeriesRepository.findById(idBookSeries).get().getBooks()).hasSize(1);
    }

    @Test
    void addBookToBookSeriesIfNotExist() {
        //given
        Long idBook = bookRepository.findAll().get(0).getId();

        //when
        var result = restTemplate.exchange("/api/book-series/-1/books/" + idBook, HttpMethod.PUT, null, String.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo("Selected book series not found");
    }

    @Test
    void deleteBookFromBookSeriesIfExist() {
        //given
        BookSeries bookSeries = bookSeriesRepository.findAll().getFirst();
        Book book = bookRepository.findAll().getFirst();
        Long idBook = book.getId();
        Long idBookSeries = bookSeries.getId();
        book.setBookSeries(bookSeries);
        bookSeries.setBooks(List.of(book));

        bookSeriesRepository.save(bookSeries);
        bookRepository.save(book);

        //when
        var result = restTemplate.exchange("/api/book-series/" + idBookSeries + "/books/" + idBook, HttpMethod.DELETE, null, BookSeries.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookSeriesRepository.findById(idBookSeries).get().getBooks()).isEmpty();
        assertThat(bookRepository.findById(idBook).get().getBookSeries()).isNull();

    }

    @Test
    void deleteBookFromBookSeriesIfNotExist() {
        //given
        Book book = bookRepository.findAll().getFirst();
        Long idBook = book.getId();

        //when
        var result = restTemplate.exchange("/api/book-series/-1/books/" + idBook, HttpMethod.DELETE, null, String.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo("Selected book series not found");
    }
}
