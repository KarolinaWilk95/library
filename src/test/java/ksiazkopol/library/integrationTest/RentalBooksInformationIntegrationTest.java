package ksiazkopol.library.integrationTest;


import ksiazkopol.library.book.Book;
import ksiazkopol.library.rentalBooksInformation.RentalBooksInformation;
import ksiazkopol.library.rentalBooksInformation.RentalBooksInformationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RentalBooksInformationIntegrationTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.3-alpine");

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    RentalBooksInformationRepository rentalBooksInformationRepository;

    @BeforeEach
    void setUp() {
        rentalBooksInformationRepository.deleteAll();

        List<RentalBooksInformation> rentalBooksInformationList = List.of(
                new RentalBooksInformation(null, 20L, "The Shining", "Stephen King", 9780385513251L, 4L, "Anna", "Kowalska", LocalDate.parse("2025-01-17"), LocalDate.parse("2025-01-24"), LocalDate.parse("2025-01-17"), (short) 0),
                new RentalBooksInformation(null, 20L, "The Shining", "Stephen King", 9780385513251L, 1L, "Karolina", "Wilk", LocalDate.parse("2025-01-17"), LocalDate.parse("2025-01-31"), LocalDate.parse("2025-01-17"), (short) 1));

        rentalBooksInformationRepository.saveAll(rentalBooksInformationList);
    }

    @Test
    void findAllRecords() {
        //given
        List<RentalBooksInformation> list = new ArrayList<>();

        //when
        var result = restTemplate.exchange("/api/rental", HttpMethod.GET, null, new ParameterizedTypeReference<List<RentalBooksInformation>>() {
        });

        list = rentalBooksInformationRepository.findAll();

        //then
        assertThat(result.getBody()).hasSize(2);
        assertThat(result.getBody().get(0).getISBN()).isEqualTo(9780385513251L);
        assertThat(result.getBody().get(1).getISBN()).isEqualTo(9780385513251L);
    }
}
