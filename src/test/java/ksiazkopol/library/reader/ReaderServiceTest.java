package ksiazkopol.library.reader;

import ksiazkopol.library.book.Book;
import ksiazkopol.library.book.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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

    @InjectMocks
    ReaderService readerService;

    @Test
    void addReader() {
        //given

        Long readerId = 1L;
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

        Long readerId = 1L;
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

        Long readerId = 1L;

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

        Long readerId = 1L;
        var reader = new Reader();
        reader.setId(readerId);
        reader.setBooks(new ArrayList<>());
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

        Long readerId = 1L;
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

        Long readerId = 1L;
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
        Long readerId = 1L;

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

//    @Test
//    void search() {
//    }

    @Test
    void borrowBook() {
        //given

        Long readerId = 1L;
        Long bookId = 1L;
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

        Long readerId = 1L;
        Long bookId = 1L;
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

        Long readerId = 1L;
        var reader = new Reader();
        reader.setId(readerId);
        Long bookId = 1L;

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

        Long bookId = 1L;
        Long readerId = 1L;

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

        Long readerId = 1L;

        when(readerRepository.existsById(readerId)).thenReturn(true);

        //when

        var result = readerService.findAllBorrowedBooks(readerId);

        //then

        verify(readerRepository).existsById(readerId);
        verify(bookService).findAllBooksForReader(readerId);

    }

    @Test
    void findAllBorrowedBooksIfReaderNotExist() {
        //given

        Long readerId = 1L;

        when(readerRepository.existsById(readerId)).thenReturn(false);

        //when

        var result = assertThrows(ReaderNotFoundException.class, () -> readerService.findAllBorrowedBooks(readerId));

        //then
        verify(readerRepository).existsById(readerId);
        assertThat(result).hasMessage("Selected reader not found");
    }
}