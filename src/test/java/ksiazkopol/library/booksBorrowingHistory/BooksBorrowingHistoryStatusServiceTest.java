package ksiazkopol.library.booksBorrowingHistory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BooksBorrowingHistoryStatusServiceTest {

    @Mock
    BooksBorrowingHistoryRepository booksBorrowingHistoryRepository;

    @InjectMocks
    BooksBorrowingHistoryStatusService borrowingHistoryStatusService;

    @Test
    void countBooksAfterTheDueDate() {
        //given
        long books = 10;

        when(booksBorrowingHistoryRepository.countBooksAfterTheDueDate()).thenReturn(books);

        //when
        var result = borrowingHistoryStatusService.countBooksAfterTheDueDate();

        //then
        verify(booksBorrowingHistoryRepository).countBooksAfterTheDueDate();
        assertThat(result).isEqualTo(books);
    }

    @Test
    void countBooksAfterDueDateNotReturned() {
        //given
        long books = 10;

        when(booksBorrowingHistoryRepository.countBooksAfterDueDateNotReturned()).thenReturn(books);

        //when
        var result = borrowingHistoryStatusService.countBooksAfterDueDateNotReturned();

        //then
        verify(booksBorrowingHistoryRepository).countBooksAfterDueDateNotReturned();
        assertThat(result).isEqualTo(books);
    }

    @Test
    void countAllReadersAfterDueDate() {
        //given
        long books = 10;

        when(booksBorrowingHistoryRepository.countAllReadersAfterDueDate()).thenReturn(books);

        //when
        var result = borrowingHistoryStatusService.countAllReadersAfterDueDate();

        //then
        verify(booksBorrowingHistoryRepository).countAllReadersAfterDueDate();
        assertThat(result).isEqualTo(books);
    }
}