package ksiazkopol.library.reader;

import ksiazkopol.library.book.Book;
import ksiazkopol.library.book.BookService;
import ksiazkopol.library.booksBorrowingHistory.BooksBorrowingHistoryService;
import ksiazkopol.library.dao.ReaderSearchRequest;
import ksiazkopol.library.dao.ReaderSearchRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReaderServiceTest {

    @Mock
    ReaderRepository readerRepository;
    @Mock
    BookService bookService;
    @Mock
    ReaderSearchRequestRepository readerSearchRequestRepository;
    @Mock
    BooksBorrowingHistoryService booksBorrowingHistoryService;


    @InjectMocks
    ReaderService readerService;

    @Test
    void addReader() {
        //given
        long readerId = 1L;
        var reader = new Reader();
        reader.setId(null);
        var newReader = new Reader();
        newReader.setId(readerId);

        when(readerRepository.save(reader)).thenReturn(newReader);

        //when
        var result = readerService.addReader(reader);

        //then
        verify(readerRepository).save(reader);

        assertThat(result).isEqualTo(newReader);

    }

    @Test
    void findAllReaders() {
        //given
        var listOfReaders = new ArrayList<Reader>();

        when(readerRepository.findAll()).thenReturn(listOfReaders);

        //when
        var result = readerService.findAllReaders();

        //then
        verify(readerRepository).findAll();

        assertThat(result).isEqualTo(listOfReaders);

    }

    @Test
    void getReaderByIDIfExist() {
        //given
        long readerId = 1L;
        Reader reader = new Reader();
        reader.setId(readerId);

        when(readerRepository.findById(readerId)).thenReturn(Optional.of(reader));

        //when
        var result = readerService.getReaderByID(readerId);

        //then
        verify(readerRepository).findById(readerId);

        assertThat(result).hasValue(reader);

    }

    @Test
    void getReaderByIDIfNotExist() {
        //given
        long readerId = 1L;

        when(readerRepository.findById(readerId)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(ReaderNotFoundException.class, () -> readerService.getReaderByID(readerId));

        //then
        verify(readerRepository).findById(readerId);

        assertThat(result).hasMessage("Selected reader not found");

    }

    @Test
    void deleteByIDIfExist() {
        //given
        long readerId = 1L;
        var reader = new Reader();
        reader.setId(readerId);
        reader.setBooks(new ArrayList<>());
        Book book = new Book();
        book.setReader(reader);
        when(readerRepository.findById(readerId)).thenReturn(Optional.of(reader));
        doNothing().when(readerRepository).delete(reader);

        //when
        readerService.deleteByID(readerId);

        //then
        verify(readerRepository).delete(reader);
        verify(readerRepository).findById(readerId);

    }

    @Test
    void deleteByIDIfNotExist() {
        //given
        long readerId = 1L;
        when(readerRepository.findById(readerId)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(ReaderNotFoundException.class, () -> readerService.deleteByID(readerId));

        //then
        verify(readerRepository).findById(readerId);

        assertThat(result).hasMessage("Selected reader not found");
    }

    @Test
    void updateByIDIfExist() {
        //given
        long readerId = 1L;
        var reader = new Reader();
        reader.setId(readerId);
        reader.setName("Karolina");
        reader.setSurname("Wójcik");

        var updatedReader = new Reader();
        updatedReader.setId(readerId);
        updatedReader.setName("Karolina");
        updatedReader.setSurname("Wilk");

        when(readerRepository.findById(readerId)).thenReturn(Optional.of(reader));

        //when
        readerService.updateByID(readerId, updatedReader);

        //then
        verify(readerRepository).findById(readerId);

    }

    @Test
    void updateByIDIfNotExist() {
        //given
        long readerId = 1L;

        var updatedReader = new Reader();
        updatedReader.setId(readerId);
        updatedReader.setName("Karolina");
        updatedReader.setSurname("Wilk");

        when(readerRepository.findById(readerId)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(ReaderNotFoundException.class, () -> readerService.updateByID(readerId, updatedReader));

        //then
        verify(readerRepository).findById(readerId);

        assertThat(result).hasMessage("Selected reader not found");

    }

    @Test
    void borrowBook() {
        //given
        long readerId = 1L;
        long bookId = 1L;
        var book = new Book();
        book.setId(bookId);
        var reader = new Reader();
        reader.setId(readerId);

        when(readerRepository.findById(readerId)).thenReturn(Optional.of(reader));

        //when
        readerService.borrowBook(bookId, readerId);

        //then
        verify(readerRepository).findById(readerId);
        verify(bookService).borrowBook(bookId, reader);

    }

    @Test
    void borrowBookIfReaderNotExist() {
        //given
        long readerId = 1L;
        long bookId = 1L;
        var book = new Book();
        book.setId(bookId);

        when(readerRepository.findById(readerId)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(ReaderNotFoundException.class, () -> readerService.borrowBook(bookId, readerId));

        //then
        verify(readerRepository).findById(readerId);

        assertThat(result).hasMessage("Selected reader not found");

    }

    @Test
    void returnBook() {
        //given
        long readerId = 1L;
        var reader = new Reader();
        reader.setId(readerId);
        long bookId = 1L;

        when(readerRepository.findById(readerId)).thenReturn(Optional.of(reader));

        //when
        readerService.returnBook(bookId, readerId);

        //then
        verify(readerRepository).findById(readerId);
        verify(bookService).returnBook(bookId);
    }

    @Test
    void returnBookIfReaderNotExist() {
        //given
        long bookId = 1L;
        long readerId = 1L;

        when(readerRepository.findById(readerId)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(ReaderNotFoundException.class, () -> readerService.returnBook(bookId, readerId));

        //then
        verify(readerRepository).findById(readerId);

        assertThat(result).hasMessage("Selected reader not found");
    }

    @Test
    void findAllBorrowedBooks() {
        //given
        long readerId = 1L;
        Reader reader = new Reader();
        reader.setId(readerId);

        when(readerRepository.findById(readerId)).thenReturn(Optional.of(reader));

        //when
        var result = readerService.findAllBorrowedBooks(readerId);

        //then
        verify(readerRepository).findById(readerId);
        verify(bookService).findAllBooksForReader(readerId);

    }

    @Test
    void findAllBorrowedBooksIfReaderNotExist() {
        //given
        long readerId = 1L;

        when(readerRepository.findById(readerId)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(ReaderNotFoundException.class, () -> readerService.findAllBorrowedBooks(readerId));

        //then
        verify(readerRepository).findById(readerId);

        assertThat(result).hasMessage("Selected reader not found");
    }

    @Test
    void search() {
        //given
        ReaderSearchRequest request = new ReaderSearchRequest();
        request.setName("Karolina");
        Reader reader = new Reader();
        reader.setId(1L);
        reader.setName("Karolina");
        reader.setSurname("Wilk");
        reader.setBooks(new ArrayList<>());
        List<Reader> list = new ArrayList<>();
        list.add(reader);

        when(readerSearchRequestRepository.findByCriteria(request)).thenReturn(list);

        //when
        var result = readerService.search(request);

        //then
        verify(readerSearchRequestRepository).findByCriteria(request);
        assertThat(result).isEqualTo(list);
    }


    @Test
    void renewBookIfReaderExist() {
        //given
        long idBook = 1L;
        long idReader = 1L;
        Reader reader = new Reader();
        reader.setId(idReader);
        reader.setName("Karolina");
        reader.setSurname("Wilk");
        reader.setBooks(new ArrayList<>());

        when(readerRepository.findById(idReader)).thenReturn(Optional.of(reader));

        //when
        readerService.renewBook(idBook, idReader);

        //then
        verify(readerRepository).findById(idReader);
        verify(bookService).findBookByIDToRenew(idBook);
        verify(booksBorrowingHistoryService).renewBook(idBook, idReader);
    }

    @Test
    void renewBookIfReaderNotExist() {
        //given
        long idBook = 1L;
        long idReader = 1L;

        when(readerRepository.findById(idReader)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(ReaderNotFoundException.class, () -> readerService.renewBook(idBook, idReader));

        //then
        verify(readerRepository).findById(idReader);
        assertThat(result.getMessage()).isEqualTo("Selected reader not found");
    }
}