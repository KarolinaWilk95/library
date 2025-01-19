package ksiazkopol.library.booksBorrowingHistory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BooksBorrowingHistoryControllerTest {

    @Mock
    BooksBorrowingHistoryService booksBorrowingHistoryService;

    @InjectMocks
    BooksBorrowingHistoryController booksBorrowingHistoryController;

    @Test
    void showAllRecords() {
        //given
        List<RentalBooksInformation> rentalBooksInformationList = new ArrayList<>();

        when(booksBorrowingHistoryService.showAllRecords()).thenReturn(rentalBooksInformationList);

        //when
        var result = booksBorrowingHistoryController.showAllRecords();

        //then
        verify(booksBorrowingHistoryService).showAllRecords();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(rentalBooksInformationList);
    }
}