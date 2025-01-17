package ksiazkopol.library.rentalBooksInformation;

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
class RentalBooksInformationServiceTest {

    @Mock
    RentalBooksInformationRepository rentalBooksInformationRepository;

    @InjectMocks
    RentalBooksInformationService rentalBooksInformationService;

    @Test
    void showAllRecords() {
        //given
        List<RentalBooksInformation> rentalBooksInformationList = new ArrayList<>();

        when(rentalBooksInformationRepository.findAll()).thenReturn(rentalBooksInformationList);

        //when
        var result = rentalBooksInformationService.showAllRecords();

        //then
        verify(rentalBooksInformationRepository).findAll();
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

        when(rentalBooksInformationRepository.findByIdBookAndIsbn(book.getId(),book.getISBN())).thenReturn(Optional.of(booksInformation));

        //when
        rentalBooksInformationService.returnBook(book);

        //then
        verify(rentalBooksInformationRepository).findByIdBookAndIsbn(id,ISBN);
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

        when(rentalBooksInformationRepository.findByIdBookAndIsbn(book.getId(),book.getISBN())).thenReturn(Optional.empty());

        //when
        rentalBooksInformationService.returnBook(book);

        //then
        verify(rentalBooksInformationRepository).findByIdBookAndIsbn(id,ISBN);
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

        when(rentalBooksInformationRepository.findByReaderIdBookIdNotReturn(idBook,idReader)).thenReturn(Optional.of(booksInformation));

        //when
        rentalBooksInformationService.renewBook(idBook,idReader);

        //then
        verify(rentalBooksInformationRepository).findByReaderIdBookIdNotReturn(idBook,idReader);
    }
}