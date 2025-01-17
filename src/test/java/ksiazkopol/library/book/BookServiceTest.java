package ksiazkopol.library.book;

import ksiazkopol.library.bookseries.BookSeries;
import ksiazkopol.library.rentalBooksInformation.RentalBooksInformationService;
import ksiazkopol.library.dao.BookSearchRequest;
import ksiazkopol.library.dao.BookSearchRequestRepository;
import ksiazkopol.library.reader.Reader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookRepository bookRepository;
    @Mock
    BookSearchRequestRepository bookSearchRequestRepository;
    @Mock
    RentalBooksInformationService booksInformationService;

    @InjectMocks
    BookService bookService;

    @Test
    void addBook() {
        //given
        Book book = new Book();
        book.setId(null);

        Book savedBook = new Book();
        savedBook.setId(1L);

        when(bookRepository.save(book)).thenReturn(savedBook);

        //when
        var result = bookService.addBook(book);

        //then
        verify(bookRepository).save(book);

        assertThat(result).isEqualTo(savedBook);

    }

    @Test
    void findBooks() {
        //given
        Book book = new Book();
        book.setId(1L);
        List<Book> bookList = List.of(book);

        when(bookRepository.findAll()).thenReturn(bookList);

        //when
        var result = bookService.findAllBooks();

        //then
        verify(bookRepository).findAll();

        assertThat(result).isEqualTo(bookList);

    }

    @Test
    void findAllBooksForReaderIfExist() {
        //given
        var idReader = 1L;
        List<Book> bookList = List.of();

        when(bookRepository.showAllBooksByReader(idReader, Sort.by(Sort.Order.asc("borrowDate")))).thenReturn(bookList);

        //when
        var result = bookService.findAllBooksForReader(idReader);

        //then
        verify(bookRepository).showAllBooksByReader(idReader, Sort.by(Sort.Order.asc("borrowDate")));

        assertThat(result).isEqualTo(bookList);
    }

    @Test
    void findAllBooksForReaderIfNotExist() {
        //given
        List<Book> bookList = List.of();

        when(bookRepository.showAllBooksByReader(null, Sort.by(Sort.Order.asc("borrowDate")))).thenReturn(bookList);

        //when
        var result = bookService.findAllBooksForReader(null);

        //then
        verify(bookRepository).showAllBooksByReader(null, Sort.by(Sort.Order.asc("borrowDate")));

        assertThat(result).isEmpty();
    }

    @Test
    void findBookByIDIfExist() {
        //given
        var idBook = 1L;
        Book book = new Book();
        book.setId(idBook);

        when(bookRepository.findById(idBook)).thenReturn(Optional.of(book));

        //when
        var result = bookService.findBookByID(idBook);

        //then
        verify(bookRepository).findById(idBook);

        assertThat(result).isEqualTo(book);
    }

    @Test
    void findBookByIDIfNotExist() {
        //given ????????????

        when(bookRepository.findById(null)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(BookNotFoundException.class, () -> bookService.findBookByID(null));

        //then
        verify(bookRepository).findById(null);

        assertThat(result).hasMessage("Selected book not found");

    }

    @Test
    void deleteByIdIfExist() {
        //given
        var bookId = 1L;
        Book book = new Book();
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(bookId);

        //when
        bookService.deleteById(bookId);

        //then
        verify(bookRepository).deleteById(bookId);
        verify(bookRepository).findById(bookId);

    }

    @Test
    void deleteByIdIfNotExist() {
        //given
        when(bookRepository.findById(null)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(BookNotFoundException.class, () -> bookService.deleteById(null));

        //then
        verify(bookRepository).findById(null);

        assertThat(result).hasMessage("Selected book not found");


    }

    @Test
    void updateByIDIfExist() {
        //given
        var bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setId(bookId);
        book.setTitle("Fantastyczne zwierzęta i jak je odnaleźć");
        book.setAuthor("J. K. Rowling");
        book.setGenre("Fantastyka");
        book.setPublisher("Bloomsbury");
        book.setPublicationDate(LocalDate.ofEpochDay(2024 - 12 - 19));
        book.setISBN(9781338216790L);

        Book updatedBook = new Book();
        updatedBook.setId(bookId);
        updatedBook.setTitle("Fantastic Beasts and Where to Find Them");
        updatedBook.setAuthor("J. K. Rowling");
        updatedBook.setGenre("Fantasy");
        updatedBook.setPublisher("Bloomsbury");
        updatedBook.setPublicationDate(LocalDate.ofEpochDay(2001 - 12 - 1));
        updatedBook.setISBN(9781338216790L);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);

        //when
        var result = bookService.updateByID(bookId, updatedBook);

        //then
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(updatedBook);

        assertThat(result).isEqualTo(updatedBook);
    }

    @Test
    void updateByIDIfNotExist() {
        //given
        var bookId = 1L;
        Book updatedBook = new Book();
        updatedBook.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(BookNotFoundException.class, () -> bookService.updateByID(bookId, updatedBook));

        //then
        verify(bookRepository).findById(bookId);

        assertThat(result).hasMessage("Selected book not found");

    }

    @Test
    void search() {
        //given
        var searchRequest = new BookSearchRequest();
        searchRequest.setAuthor("Rowling");
        Book book = new Book();
        book.setAuthor("J.K. Rowling");
        List<Book> bookList = List.of(book);


        when(bookSearchRequestRepository.findByCriteria(searchRequest)).thenReturn(bookList);

        //when
        var result = bookService.search(searchRequest);

        //then
        verify(bookSearchRequestRepository).findByCriteria(searchRequest);

        assertThat(result).isEqualTo(bookList);
    }

    @Test
    void returnBookIfCorrectId() {
        //given
        var bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        Reader reader = new Reader();
        book.setReader(reader);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        //when
        var result = bookService.returnBook(bookId);

        //then
        verify(bookRepository).findById(bookId);
        verify(booksInformationService).returnBook(book);
        assertThat(result).isEqualTo(book);


    }

    @Test
    void returnBookIfNotCorrectId() {
        //given
        var bookId = 1L;
        Book book = new Book();
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(BookNotFoundException.class, () -> bookService.returnBook(bookId));

        //then
        verify(bookRepository).findById(bookId);

        assertThat(result).hasMessage("Selected book not found");

    }

    @Test
    void borrowBookIsAvailable() {
        //given
        var bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        Reader reader = new Reader();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        //when
        var result = bookService.borrowBook(bookId, reader);

        //then
        verify(bookRepository).findById(bookId);
        verify(booksInformationService).borrowBook(book, reader);
        assertThat(result).isEqualTo(book);

    }

    @Test
    void borrowBookNotCorrectId() {
        //given
        var bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        Reader reader = new Reader();

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(BookNotFoundException.class, () -> bookService.borrowBook(bookId, reader));

        //then
        verify(bookRepository).findById(bookId);

        assertThat(result).hasMessage("Selected book not found");

    }

    @Test
    void borrowBookAlreadyBorrowed() {
        //given
        var bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        LocalDate date = LocalDate.ofEpochDay(2024 - 12 - 19);
        book.setBorrowDate(date);
        Reader reader = new Reader();
        book.setReader(reader);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
//        when(book.getBorrowDate()).thenReturn(date);

        //when
        var result = assertThrows(BorrowedBookException.class, () -> bookService.borrowBook(bookId, reader));

        //then
        verify(bookRepository).findById(bookId);

        assertThat(result).hasMessage("Selected book is already borrowed");

    }

    @Test
    void addBookToBookSeries() {
        //given
        Book book = new Book();
        Long idBook = 1L;
        book.setId(idBook);
        BookSeries bookSeries = new BookSeries();
        Long idBookSeries = 1L;
        bookSeries.setId(idBookSeries);

        when(bookRepository.findById(idBook)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        //when
        var result = bookService.addBookToBookSeries(idBook, bookSeries);

        //then
        verify(bookRepository).findById(idBook);
        verify(bookRepository).save(book);
        assertThat(result).isEqualTo(book);
    }

    @Test
    void addBookToBookSeriesIfBookNotExist() {
        //given
        Long idBook = 1L;
        BookSeries bookSeries = new BookSeries();
        Long idBookSeries = 1L;
        bookSeries.setId(idBookSeries);

        when(bookRepository.findById(idBook)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(BookNotFoundException.class, () -> bookService.addBookToBookSeries(idBook, bookSeries));

        //then
        verify(bookRepository).findById(idBook);
        assertThat(result).hasMessage("Selected book not found");

    }

    @Test
    void addBookToBookSeriesBookAlreadyAssigned() {
        //given
        Book book = new Book();
        Long idBook = 1L;
        book.setId(idBook);
        BookSeries bookSeries = new BookSeries();
        Long idBookSeries = 1L;
        bookSeries.setId(idBookSeries);
        book.setBookSeries(bookSeries);

        when(bookRepository.findById(idBook)).thenReturn(Optional.of(book));

        //when
        var result = assertThrows(BookAssignedException.class, () -> bookService.addBookToBookSeries(idBook, bookSeries));

        //then
        verify(bookRepository).findById(idBook);
        assertThat(result).hasMessage("Selected book is already assigned to another book series");
    }

    @Test
    void deleteBookFromBookSeries() {
        //given
        Book book = new Book();
        Long idBook = 1L;
        book.setId(idBook);
        BookSeries bookSeries = new BookSeries();
        Long idBookSeries = 1L;
        bookSeries.setId(idBookSeries);
        book.setBookSeries(bookSeries);

        when(bookRepository.findById(idBook)).thenReturn(Optional.of(book));

        //when
        bookService.deleteBookFromBookSeries(idBook);

        //then
        verify(bookRepository).findById(idBook);
        verify(bookRepository).save(book);
    }

    @Test
    void deleteBookToBookSeriesIfBookNotExist() {
        //given
        Long idBook = 1L;

        when(bookRepository.findById(idBook)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(BookNotFoundException.class, () -> bookService.deleteBookFromBookSeries(idBook));

        //then
        verify(bookRepository).findById(idBook);
        assertThat(result).hasMessage("Selected book not found");

    }

    @Test
    void deleteBookToBookSeriesBookNeverAssigned() {
        //given
        Book book = new Book();
        Long idBook = 1L;
        book.setId(idBook);

        when(bookRepository.findById(idBook)).thenReturn(Optional.of(book));

        //when
        var result = assertThrows(BookAssignedException.class, () -> bookService.deleteBookFromBookSeries(idBook));

        //then
        verify(bookRepository).findById(idBook);
        assertThat(result).hasMessage("Selected book was never assigned to book series");
    }
}