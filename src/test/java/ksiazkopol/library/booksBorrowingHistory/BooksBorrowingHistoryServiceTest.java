package ksiazkopol.library.booksBorrowingHistory;

import ksiazkopol.library.book.Book;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BooksBorrowingHistoryServiceTest {

    @Mock
    BooksBorrowingHistoryRepository booksBorrowingHistoryRepository;

    @InjectMocks
    BooksBorrowingHistoryService booksBorrowingHistoryService;

    @Test
    void showAllRecords() {
        //given
        List<RentalBooksInformation> rentalBooksInformationList = new ArrayList<>();

        when(booksBorrowingHistoryRepository.findAll()).thenReturn(rentalBooksInformationList);

        //when
        var result = booksBorrowingHistoryService.showAllRecords();

        //then
        verify(booksBorrowingHistoryRepository).findAll();
        assertThat(result).isEqualTo(rentalBooksInformationList);
    }

    @Test
    void borrowBook() {
    }

    @Test
    void returnBook() {
        //given
        Book book = new Book();
        RentalBooksInformation booksInformation = new RentalBooksInformation();
        Long id = 1L;
        Long ISBN = 9780385513251L;
        book.setId(id);
        book.setISBN(ISBN);
        booksInformation.setIdBook(id);
        booksInformation.setISBN(ISBN);

        when(booksBorrowingHistoryRepository.findByIdBookAndIsbn(book.getId(),book.getISBN())).thenReturn(Optional.of(booksInformation));

        //when
        booksBorrowingHistoryService.returnBook(book);

        //then
        verify(booksBorrowingHistoryRepository).findByIdBookAndIsbn(id,ISBN);
    }

    @Test
    void returnBookIfEmptyRecord() {
        //given
        Book book = new Book();
        RentalBooksInformation booksInformation = new RentalBooksInformation();
        Long id = 1L;
        Long ISBN = 9780385513251L;
        book.setId(id);
        book.setISBN(ISBN);
        booksInformation.setIdBook(id);
        booksInformation.setISBN(ISBN);

        when(booksBorrowingHistoryRepository.findByIdBookAndIsbn(book.getId(),book.getISBN())).thenReturn(Optional.empty());

        //when
        booksBorrowingHistoryService.returnBook(book);

        //then
        verify(booksBorrowingHistoryRepository).findByIdBookAndIsbn(id,ISBN);
    }


    @Test
    void renewBook() {
        //given
        Long idReader = 1L;
        Long idBook = 1L;
        RentalBooksInformation booksInformation = new RentalBooksInformation();
        booksInformation.setIdReader(idReader);
        booksInformation.setIdBook(idBook);
        booksInformation.setExpectedReturnDate(LocalDate.now());

        when(booksBorrowingHistoryRepository.findByReaderIdBookIdNotReturn(idBook,idReader)).thenReturn(Optional.of(booksInformation));

        //when
        booksBorrowingHistoryService.renewBook(idBook,idReader);

        //then
        verify(booksBorrowingHistoryRepository).findByReaderIdBookIdNotReturn(idBook,idReader);
    }
}