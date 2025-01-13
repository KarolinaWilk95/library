package ksiazkopol.library.reader;

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
        Long id = 1L;
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
        Long id = 1L;

        doThrow(ReaderNotFoundException.class).when(readerService).getReaderByID(id);

        //when
        var result = assertThrows(ReaderNotFoundException.class, () -> readerController.findByID(id));

        //then
        verify(readerService).getReaderByID(id);

    }

    @Test
    void deleteByIDIfExist() {
        //given
        Long id = 1L;

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
        Long id = 1L;
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
        Long bookId = 1L;
        Long readerId = 1L;

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
        Long bookId = 1L;
        Long readerId = 1L;

        doNothing().when(readerService).returnBook(bookId, readerId);

        //when
        var result = readerController.returnBook(bookId, readerId);

        //then
        verify(readerService).returnBook(bookId, readerId);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}