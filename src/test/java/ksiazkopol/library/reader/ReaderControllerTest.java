package ksiazkopol.library.reader;

import ksiazkopol.library.book.Book;
import ksiazkopol.library.book.BookAPI;
import ksiazkopol.library.dao.ReaderSearchRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReaderControllerTest {

    @Mock
    private ReaderService readerService;
    @Mock
    ReaderSearchRequest readerSearchRequest;
    @InjectMocks
    private ReaderController readerController;


    @Test
    void addOneReader() {
        //given
        var reader = new Reader();
        var newReader = new Reader();
        reader.setId(null);
        newReader.setId(1L);
        newReader.setBooks(new ArrayList<>());

        when(readerService.addReader(reader)).thenReturn(newReader);

        ReaderAPI readerToModel = new ReaderAPI(reader);

        //when
        var result = readerController.addReader(readerToModel);

        //then
        verify(readerService).addReader(reader);

        assertThat(result.getId()).isEqualTo(newReader.getId());
        assertThat(result.getBooks()).isEqualTo(newReader.getBooks());
    }


    @Test
    void findAllReaders() {
        //given
        List<Reader> readers = new ArrayList<>();

        when(readerService.findAllReaders()).thenReturn(readers);

        //when
        var result = readerController.findAllReaders();

        //then
        verify(readerService).findAllReaders();

        assertThat(result).isEqualTo(readers);

    }


    @Test
    void findByIDIfExist() {
        //given
        long id = 1L;
        Reader reader = new Reader();
        reader.setId(id);

        when(readerService.getReaderByID(id)).thenReturn(Optional.of(reader));

        //when
        var result = readerController.findByID(id);

        //then
        verify(readerService).getReaderByID(id);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getId()).isEqualTo(reader.getId());

    }

    @Test
    void findByIDIfNotExist() {
        //given
        long id = 1L;

        doThrow(ReaderNotFoundException.class).when(readerService).getReaderByID(id);

        //when
        var result = assertThrows(ReaderNotFoundException.class, () -> readerController.findByID(id));

        //then
        verify(readerService).getReaderByID(id);

    }

    @Test
    void deleteByIDIfExist() {
        //given
        long id = 1L;

        doNothing().when(readerService).deleteByID(id);

        //when
        var result = readerController.deleteByID(id);

        //then
        verify(readerService).deleteByID(id);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    }


    @Test
    void updateByID() {
        //given
        long id = 1L;
        Reader reader = new Reader();
        reader.setId(id);
        reader.setName("Karolina");
        reader.setSurname("Wójcik");
        reader.setBooks(new ArrayList<>());

        Reader updatedReader = new Reader();
        updatedReader.setId(id);
        updatedReader.setName("Karolina");
        updatedReader.setSurname("Wilk");
        updatedReader.setBooks(new ArrayList<>());

        doNothing().when(readerService).updateByID(id, updatedReader);

        //when
        var result = readerController.updateByID(id, updatedReader);

        //then
        verify(readerService).updateByID(id, updatedReader);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void borrowBook() {
        //given
        long bookId = 1L;
        long readerId = 1L;

        doNothing().when(readerService).borrowBook(bookId, readerId);

        //when
        var result = readerController.borrowBook(bookId, readerId);

        //then
        verify(readerService).borrowBook(bookId, readerId);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void returnBook() {
        //given
        long bookId = 1L;
        long readerId = 1L;

        doNothing().when(readerService).returnBook(bookId, readerId);

        //when
        var result = readerController.returnBook(bookId, readerId);

        //then
        verify(readerService).returnBook(bookId, readerId);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void findAllBorrowedBooks() {
        //given
        long idBook = 1L;
        long idReader = 1L;
        Book book = new Book();
        book.setId(idBook);
        List<Book> list = new ArrayList<>();
        list.add(book);


        when(readerService.findAllBorrowedBooks(idReader)).thenReturn(list);

        //when
        var result = readerController.findAllBorrowedBooks(idReader);

        //then
        verify(readerService).findAllBorrowedBooks(idReader);
        assertThat(result).isEqualTo(list.stream().map(BookAPI::new).toList());
    }

    @Test
    void renewBook() {
        //given
        long idReader = 1L;
        long idBook = 1L;

        doNothing().when(readerService).renewBook(idBook, idReader);

        //when
        readerController.renewBook(idBook, idReader);

        //then
        verify(readerService).renewBook(idBook, idReader);
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

        when(readerService.search(request)).thenReturn(list);

        //when
        var result = readerController.search("Karolina", null);

        //then
        verify(readerService).search(request);
        assertThat(result).isEqualTo(list.stream().map(ReaderAPI::new).toList());
        assertThat(result).hasSize(1);
    }
}