package ksiazkopol.library.bookseries;

import ksiazkopol.library.book.BookNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BookSeriesControllerTest {

    @Mock
    private BookSeriesService bookSeriesService;
    @InjectMocks
    private BookSeriesController bookSeriesController;

    @Test
    void findAllSeries() {
        //given
        List<BookSeries> bookSeriesList = new ArrayList<>();

        when(bookSeriesService.findAll()).thenReturn(bookSeriesList);

        //when
        var result = bookSeriesController.findAllSeries();

        //then
        verify(bookSeriesService).findAll();
        assertThat(result).isEqualTo(bookSeriesList);
    }

    @Test
    void addBookSeries() {
        //given
        BookSeries bookSeries = new BookSeries();
        bookSeries.setId(null);

        BookSeries bookSeriesToAdd = new BookSeries();
        Long id = 1L;
        bookSeriesToAdd.setId(id);

        when(bookSeriesService.addBookSeries(bookSeries)).thenReturn(bookSeriesToAdd);

        //when
        BookSeriesAPI bookSeriesAPI = new BookSeriesAPI(bookSeries);
        var result = bookSeriesController.addBookSeries(bookSeriesAPI);

        //then
        verify(bookSeriesService).addBookSeries(bookSeries);
        assertThat(result.getId()).isEqualTo(bookSeriesToAdd.getId());
    }

    @Test
    void deleteBookSeries() {
        //given
        Long id = 1L;
        BookSeries bookSeries = new BookSeries();
        bookSeries.setId(id);

        doNothing().when(bookSeriesService).deleteBookSeries(id);

        //when
        var result = bookSeriesController.deleteBookSeries(id);

        //then
        verify(bookSeriesService).deleteBookSeries(id);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    }


    @Test
    void deleteBookSeriesIfNotExist() {
        //given
        Long id = 1L;

        doThrow(BookSeriesNotFoundException.class).when(bookSeriesService).deleteBookSeries(id);

        //when
        var result = assertThrows(BookSeriesNotFoundException.class, () -> bookSeriesController.deleteBookSeries(id));

        //then
        verify(bookSeriesService).deleteBookSeries(id);
    }

    @Test
    void findById() {
        //given
        Long id = 1L;
        BookSeries bookSeries = new BookSeries();
        bookSeries.setId(id);

        when(bookSeriesService.findById(id)).thenReturn(bookSeries);

        //when
        var result = bookSeriesController.findById(id);

        //then
        verify(bookSeriesService).findById(id);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
    }

    @Test
    void findByIdIfNotExist() {
        //given
        Long id = 1L;

        doThrow(BookSeriesNotFoundException.class).when(bookSeriesService).findById(id);

        //when
        var result = assertThrows(BookSeriesNotFoundException.class, () -> bookSeriesController.findById(id));

        //then
        verify(bookSeriesService).findById(id);

    }

    @Test
    void updateById() {
        //given
        Long id = 1L;
        BookSeries bookSeries = new BookSeries();
        bookSeries.setId(id);

        BookSeries bookSeriesToUpdate = new BookSeries();
        bookSeriesToUpdate.setId(1L);
        bookSeriesToUpdate.setNameOfSeries("Jeżycjada");
        bookSeriesToUpdate.setAuthor("Małgorzata Musierowicz");

        when(bookSeriesService.updateById(id, bookSeriesToUpdate)).thenReturn(bookSeriesToUpdate);

        //when
        var result = bookSeriesController.updateById(id, bookSeriesToUpdate);

        //then
        verify(bookSeriesService).updateById(id, bookSeriesToUpdate);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void updateByIdIfNotExist() {
        //given
        Long id = 1L;

        BookSeries bookSeriesToUpdate = new BookSeries();
        bookSeriesToUpdate.setId(1L);
        bookSeriesToUpdate.setNameOfSeries("Jeżycjada");
        bookSeriesToUpdate.setAuthor("Małgorzata Musierowicz");

        doThrow(BookSeriesNotFoundException.class).when(bookSeriesService).updateById(id, bookSeriesToUpdate);

        //when
        var result = assertThrows(BookSeriesNotFoundException.class, () -> bookSeriesController.updateById(id, bookSeriesToUpdate));

        //then
        verify(bookSeriesService).updateById(id, bookSeriesToUpdate);
    }

    @Test
    void addBookToBookSeries() {
        //given
        Long idBook = 1L;
        Long idBookSeries = 1L;

        doNothing().when(bookSeriesService).addBookToBookSeries(idBookSeries, idBook);

        //when
        var result = bookSeriesController.addBookToBookSeries(idBookSeries, idBook);

        //then
        verify(bookSeriesService).addBookToBookSeries(idBookSeries, idBook);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addBookToBookSeriesIfBookNotExist() {
        //given
        Long idBook = 1L;
        Long idBookSeries = 1L;

        doThrow(BookNotFoundException.class).when(bookSeriesService).addBookToBookSeries(idBookSeries,idBook);

        //when
        var result = assertThrows(BookNotFoundException.class, () -> bookSeriesController.addBookToBookSeries(idBookSeries,idBook));

        //then
        verify(bookSeriesService).addBookToBookSeries(idBookSeries, idBook);
    }
    @Test
    void addBookToBookSeriesIfBookSeriesNotExist() {
        //given
        Long idBook = 1L;
        Long idBookSeries = 1L;

        doThrow(BookSeriesNotFoundException.class).when(bookSeriesService).addBookToBookSeries(idBookSeries,idBook);

        //when
        var result = assertThrows(BookSeriesNotFoundException.class, () -> bookSeriesController.addBookToBookSeries(idBookSeries,idBook));

        //then
        verify(bookSeriesService).addBookToBookSeries(idBookSeries, idBook);
    }

    @Test
    void deleteBookFromBookSeries() {
        //given
        Long idBookSeries = 1L;
        BookSeries bookSeries =new BookSeries();
        bookSeries.setId(idBookSeries);
        Long idBook = 1L;

        doNothing().when(bookSeriesService).deleteBookFromBookSeries(idBookSeries,idBook);

        //when
        var result = bookSeriesController.deleteBookFromBookSeries(idBookSeries,idBook);

        //then
        verify(bookSeriesService).deleteBookFromBookSeries(idBookSeries,idBook);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteBookFromBookSeriesIfBookNotExist() {
        //given
        Long idBook = 1L;
        Long idBookSeries = 1L;

        doThrow(BookNotFoundException.class).when(bookSeriesService).deleteBookFromBookSeries(idBookSeries,idBook);

        //when
        var result = assertThrows(BookNotFoundException.class, () -> bookSeriesController.deleteBookFromBookSeries(idBookSeries,idBook));

        //then
        verify(bookSeriesService).deleteBookFromBookSeries(idBookSeries, idBook);
    }
    @Test
    void deleteBookFromBookSeriesIfBookSeriesNotExist() {
        //given
        Long idBook = 1L;
        Long idBookSeries = 1L;

        doThrow(BookSeriesNotFoundException.class).when(bookSeriesService).deleteBookFromBookSeries(idBookSeries,idBook);

        //when
        var result = assertThrows(BookSeriesNotFoundException.class, () -> bookSeriesController.deleteBookFromBookSeries(idBookSeries,idBook));

        //then
        verify(bookSeriesService).deleteBookFromBookSeries(idBookSeries, idBook);
    }
}