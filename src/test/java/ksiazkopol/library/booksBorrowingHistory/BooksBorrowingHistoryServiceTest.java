package ksiazkopol.library.booksBorrowingHistory;

import ksiazkopol.library.book.Book;
import ksiazkopol.library.book.BooksStatusService;
import ksiazkopol.library.reader.Reader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BooksBorrowingHistoryServiceTest {

    @Mock
    BooksBorrowingHistoryRepository booksBorrowingHistoryRepository;

    @Mock
    BooksStatusService booksStatusService;

    @Mock
    BooksBorrowingHistoryStatusService booksBorrowingHistoryStatusService;

    @InjectMocks
    BooksBorrowingHistoryService booksBorrowingHistoryService;


    @Test
    void showAllRecords() {
        //given
        List<BooksBorrowingHistory> rentalBooksInformationList = new ArrayList<>();

        when(booksBorrowingHistoryRepository.findAll()).thenReturn(rentalBooksInformationList);

        //when
        var result = booksBorrowingHistoryService.showAllRecords();

        //then
        verify(booksBorrowingHistoryRepository).findAll();
        assertThat(result).isEqualTo(rentalBooksInformationList);
    }

    @Test
    void borrowBook() {
        //given
        long id = 1L;
        Book book = new Book();
        book.setId(id);
        Reader reader = new Reader();
        reader.setId(id);
        BooksBorrowingHistory borrowingHistory = new BooksBorrowingHistory();
        borrowingHistory.setIdBook(id);
        borrowingHistory.setIdReader(id);
        borrowingHistory.setBorrowDate(LocalDate.now());
        borrowingHistory.setExpectedReturnDate(LocalDate.now().plusDays(7));

        //when
        booksBorrowingHistoryService.borrowBook(book, reader);

        //then
        verify(booksBorrowingHistoryRepository).save(borrowingHistory);
    }

    @Test
    void returnBook() {
        //given
        Book book = new Book();
        BooksBorrowingHistory booksInformation = new BooksBorrowingHistory();
        Long id = 1L;
        Long ISBN = 9780385513251L;
        book.setId(id);
        book.setISBN(ISBN);
        booksInformation.setIdBook(id);
        booksInformation.setISBN(ISBN);

        when(booksBorrowingHistoryRepository.findByIdBookAndIsbn(book.getId(), book.getISBN())).thenReturn(Optional.of(booksInformation));

        //when
        booksBorrowingHistoryService.returnBook(book);

        //then
        verify(booksBorrowingHistoryRepository).findByIdBookAndIsbn(id, ISBN);
    }

    @Test
    void returnBookIfEmptyRecord() {
        //given
        Book book = new Book();
        BooksBorrowingHistory booksInformation = new BooksBorrowingHistory();
        Long id = 1L;
        Long ISBN = 9780385513251L;
        book.setId(id);
        book.setISBN(ISBN);
        booksInformation.setIdBook(id);
        booksInformation.setISBN(ISBN);

        when(booksBorrowingHistoryRepository.findByIdBookAndIsbn(book.getId(), book.getISBN())).thenReturn(Optional.empty());

        //when
        var result = assertThrows(BooksBorrowingHistoryException.class, () -> booksBorrowingHistoryService.returnBook(book));

        //then
        verify(booksBorrowingHistoryRepository).findByIdBookAndIsbn(id, ISBN);
    }


    @Test
    void renewBook() {
        //given
        Long idReader = 1L;
        Long idBook = 1L;
        BooksBorrowingHistory booksInformation = new BooksBorrowingHistory();
        booksInformation.setIdReader(idReader);
        booksInformation.setIdBook(idBook);
        booksInformation.setExpectedReturnDate(LocalDate.now());
        LocalDate date = booksInformation.getExpectedReturnDate();

        when(booksBorrowingHistoryRepository.findByReaderIdBookIdNotReturn(idBook, idReader)).thenReturn(Optional.of(booksInformation));

        //when
        booksBorrowingHistoryService.renewBook(idBook, idReader);

        //then
        verify(booksBorrowingHistoryRepository).findByReaderIdBookIdNotReturn(idBook, idReader);
        assertThat(booksInformation.getExpectedReturnDate()).isEqualTo(date.plusDays(7));
        assertThat(booksInformation.getUsedBookRenew()).isEqualTo((short)1);
    }

    @Test
    void renewBookIfRecordNotExist() {
        //given
        long bookId = 1L;
        long readerId = 1L;

        when(booksBorrowingHistoryRepository.findByReaderIdBookIdNotReturn(bookId, readerId)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(BooksBorrowingHistoryException.class, () -> booksBorrowingHistoryService.renewBook(bookId, readerId));

        //then
        verify(booksBorrowingHistoryRepository).findByReaderIdBookIdNotReturn(readerId, bookId);
        assertThat(result.getMessage()).isEqualTo("Selected record not found");

    }

    @Test
    void renewBookIfAlreadyRenewed() {
        //given
        long bookId = 1L;
        long readerId = 1L;
        BooksBorrowingHistory booksBorrowingHistory = new BooksBorrowingHistory();
        booksBorrowingHistory.setIdReader(readerId);
        booksBorrowingHistory.setIdBook(bookId);
        booksBorrowingHistory.setUsedBookRenew((short) 1);

        when(booksBorrowingHistoryRepository.findByReaderIdBookIdNotReturn(bookId, readerId)).thenReturn(Optional.of(booksBorrowingHistory));

        //when
        var result = assertThrows(BooksBorrowingHistoryException.class, () -> booksBorrowingHistoryService.renewBook(bookId, readerId));

        //then
        verify(booksBorrowingHistoryRepository).findByReaderIdBookIdNotReturn(bookId, readerId);
        assertThat(result.getMessage()).isEqualTo("The possibility of extending the book returning period has been used");

    }

    @Test
    void bookStatus() {
        //given
        BooksBorrowingHistorySummary summary = new BooksBorrowingHistorySummary();
        summary.setAmountOfBooks(10);
        summary.setAmountOfBorrowedBooks(2);
        summary.setAmountOfAvailableBooks(8);
        summary.setBooksAfterDueDateReturned(0);
        summary.setBooksAfterDueDateNotReturned(2);
        summary.setReadersAfterDueDate(1);

        when(booksStatusService.countAllBooks()).thenReturn(10L);
        when(booksStatusService.countBorrowedBooks()).thenReturn(2L);
        when(booksStatusService.countBooksAvailableToBorrow()).thenReturn(8L);
        when(booksBorrowingHistoryStatusService.countAllReadersAfterDueDate()).thenReturn(0L);
        when(booksBorrowingHistoryStatusService.countBooksAfterDueDateNotReturned()).thenReturn(2L);
        when(booksBorrowingHistoryStatusService.countAllReadersAfterDueDate()).thenReturn(1L);

        //when
        booksBorrowingHistoryService.bookStatus();

        //then
        verify(booksStatusService).countAllBooks();
        verify(booksStatusService).countBorrowedBooks();
        verify(booksStatusService).countBooksAvailableToBorrow();
        verify(booksBorrowingHistoryStatusService).countAllReadersAfterDueDate();
        verify(booksBorrowingHistoryStatusService).countBooksAfterDueDateNotReturned();
        verify(booksBorrowingHistoryStatusService).countBooksAfterTheDueDate();

    }
}