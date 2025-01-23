package ksiazkopol.library.booksBorrowingHistory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        List<BooksBorrowingHistory> booksBorrowingHistories = new ArrayList<>();

        when(booksBorrowingHistoryService.showAllRecords()).thenReturn(booksBorrowingHistories);

        //when
        var result = booksBorrowingHistoryController.showAllRecords();

        //then
        verify(booksBorrowingHistoryService).showAllRecords();
        assertThat(result).hasSize(0);
        assertThat(result).isEqualTo(booksBorrowingHistories);
    }

    @Test
    void showStatusTable() {
        //given
        BooksBorrowingHistorySummary borrowingHistory = new BooksBorrowingHistorySummary();

        when(booksBorrowingHistoryService.bookStatus()).thenReturn(borrowingHistory);

        //when
        BooksBorrowingHistorySummaryAPI borrowingHistorySummaryAPI = new BooksBorrowingHistorySummaryAPI(borrowingHistory);
        var result = booksBorrowingHistoryController.booksStatus();

        //then
        verify(booksBorrowingHistoryService).bookStatus();
        assertThat(result).isEqualTo(borrowingHistorySummaryAPI);
    }
}