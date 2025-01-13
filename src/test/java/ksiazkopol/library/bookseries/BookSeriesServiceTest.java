package ksiazkopol.library.bookseries;

import ksiazkopol.library.book.BookService;
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
class BookSeriesServiceTest {

    @Mock
    BookSeriesRepository bookSeriesRepository;
    @Mock
    BookService bookService;

    @InjectMocks
    BookSeriesService bookSeriesService;

    @Test
    void findAll() {
        //given
        List<BookSeries> bookSeriesList = new ArrayList<>();

        when(bookSeriesRepository.findAll()).thenReturn(bookSeriesList);

        //when
        var result = bookSeriesService.findAll();

        //then
        verify(bookSeriesRepository).findAll();
        assertThat(result).isEqualTo(bookSeriesList);

    }

    @Test
    void addBookSeries() {
        //given
        BookSeries bookSeries = new BookSeries();
        Long id = 1L;
        BookSeries savedBookSeries = new BookSeries();
        savedBookSeries.setId(id);

        when(bookSeriesRepository.save(bookSeries)).thenReturn(savedBookSeries);

        //when
        var result = bookSeriesService.addBookSeries(bookSeries);

        //then
        verify(bookSeriesRepository).save(bookSeries);
        assertThat(result).isEqualTo(savedBookSeries);

    }

    @Test
    void deleteBookSeries() {
        //given
        BookSeries bookSeries = new BookSeries();
        Long id = 1L;
        bookSeries.setId(id);

        when(bookSeriesRepository.findById(id)).thenReturn(Optional.of(bookSeries));
        doNothing().when(bookSeriesRepository).deleteById(id);

        //when
        bookSeriesService.deleteBookSeries(id);

        //then
        verify(bookSeriesRepository).deleteById(id);
        verify(bookSeriesRepository).findById(id);

    }

    @Test
    void deleteBookSeriesIfNotExist() {
        //given
        Long id = 1L;

        when(bookSeriesRepository.findById(id)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(BookSeriesNotFoundException.class, () -> bookSeriesService.deleteBookSeries(id));

        //then
        verify(bookSeriesRepository).findById(id);
        assertThat(result).hasMessage("Selected book series not found");
    }

    @Test
    void findById() {
        //given
        BookSeries bookSeries = new BookSeries();
        Long id = 1L;
        bookSeries.setId(id);

        when(bookSeriesRepository.findById(id)).thenReturn(Optional.of(bookSeries));

        //when
        var result = bookSeriesService.findById(id);

        //then
        verify(bookSeriesRepository).findById(id);
        assertThat(result).isEqualTo(bookSeries);
    }

    @Test
    void findByIdIfNotExist() {
        //given
        Long id = 1L;

        when(bookSeriesRepository.findById(id)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(BookSeriesNotFoundException.class, () -> bookSeriesService.findById(id));

        //then
        verify(bookSeriesRepository).findById(id);
        assertThat(result).hasMessage("Selected book series not found");

    }

    @Test
    void updateById() {
        //given
        BookSeries bookSeries = new BookSeries();
        BookSeries bookSeriesToUpdate = new BookSeries();
        Long id = 1L;
        bookSeries.setId(id);
        bookSeries.setAuthor("Malgorzata Musierowicz");
        bookSeries.setNameOfSeries("Jezycjada");
        bookSeriesToUpdate.setId(id);
        bookSeriesToUpdate.setAuthor("Małgorzata Musierowicz");
        bookSeriesToUpdate.setNameOfSeries("Jeżycjada");

        when(bookSeriesRepository.findById(id)).thenReturn(Optional.of(bookSeries));
        when(bookSeriesRepository.save(bookSeries)).thenReturn(bookSeriesToUpdate);

        //when
        var result = bookSeriesService.updateById(id, bookSeriesToUpdate);

        //then
        verify(bookSeriesRepository).findById(id);
        verify(bookSeriesRepository).save(bookSeries);
        assertThat(result).isEqualTo(bookSeriesToUpdate);

    }

    @Test
    void updateByIdIfNotExist() {
        //given
        BookSeries bookSeriesToUpdate = new BookSeries();
        Long id = 1L;
        bookSeriesToUpdate.setId(id);
        bookSeriesToUpdate.setAuthor("Małgorzata Musierowicz");
        bookSeriesToUpdate.setNameOfSeries("Jeżycjada");

        when(bookSeriesRepository.findById(id)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(BookSeriesNotFoundException.class, () -> bookSeriesService.updateById(id, bookSeriesToUpdate));

        //then
        verify(bookSeriesRepository).findById(id);
        assertThat(result).hasMessage("Selected book series not found");

    }

    @Test
    void addBookToBookSeries() {
        //given
        BookSeries bookSeries = new BookSeries();
        Long idBookSeries = 1L;
        Long idBook = 1L;

        when(bookSeriesRepository.findById(idBookSeries)).thenReturn(Optional.of(bookSeries));

        //when
        bookSeriesService.addBookToBookSeries(idBookSeries, idBook);

        //then
        verify(bookSeriesRepository).findById(idBookSeries);
        verify(bookService).addBookToBookSeries(idBook, bookSeries);

    }

    @Test
    void addBookToBookSeriesIfBookSeriesNotExist() {
        //given
        Long idBookSeries = 1L;
        Long idBook = 1L;

        when(bookSeriesRepository.findById(idBookSeries)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(BookSeriesNotFoundException.class, () -> bookSeriesService.addBookToBookSeries(idBookSeries, idBook));

        //then
        verify(bookSeriesRepository).findById(idBookSeries);
        assertThat(result).hasMessage("Selected book series not found");

    }

    @Test
    void deleteBookFromBookSeries() {
        //given
        BookSeries bookSeries = new BookSeries();
        Long idBookSeries = 1L;
        Long idBook = 1L;

        when(bookSeriesRepository.findById(idBookSeries)).thenReturn(Optional.of(bookSeries));

        //when
        bookSeriesService.deleteBookFromBookSeries(idBookSeries, idBook);

        //then
        verify(bookSeriesRepository).findById(idBookSeries);
        verify(bookService).deleteBookFromBookSeries(idBook);

    }

    @Test
    void deleteBookFromBookSeriesIfBookSeriesNotExist() {
        //given
        Long idBookSeries = 1L;
        Long idBook = 1L;

        when(bookSeriesRepository.findById(idBookSeries)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(BookSeriesNotFoundException.class, () -> bookSeriesService.deleteBookFromBookSeries(idBookSeries, idBook));

        //then
        verify(bookSeriesRepository).findById(idBookSeries);
        assertThat(result).hasMessage("Selected book series not found");

    }
}