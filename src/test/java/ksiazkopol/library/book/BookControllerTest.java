package ksiazkopol.library.book;

import ksiazkopol.library.dao.BookSearchRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;
    @InjectMocks
    private BookController bookController;


    @Test
    void addBook() {
        //given
        var book = new Book();
        book.setId(null);
        var newBook = new Book();
        book.setId(1L);

        when(bookService.addBook(book)).thenReturn(newBook);

        //when
        BookAPI bookToModel = new BookAPI(book);
        var result = bookController.addBook(bookToModel);

        //then
        verify(bookService).addBook(book);

        assertThat(result.getId()).isEqualTo(newBook.getId());
    }

    @Test
    void showBooks() {
        //given
        List<Book> listOfBooks = new ArrayList<>();
        when(bookService.findAllBooks()).thenReturn(listOfBooks);

        //when
        var result = bookController.findAllBooks();

        //then
        verify(bookService).findAllBooks();

        assertThat(result).isEqualTo(listOfBooks);

    }

    @Test
    void findByIDIfExist() {
        //given
        Long bookId = 1L;
        var bookExist = new BookAPI();
        bookExist.setId(bookId);
        when(bookService.findBookByID(bookId)).thenReturn(bookExist.toModel());

        //when
        var result = bookController.findByID(bookId);

        //then
        verify(bookService).findBookByID(bookId);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(bookExist);

    }

    @Test
    void findByIDIfNotExist() {
        //given
        Long bookId = 1L;

        doThrow(BookNotFoundException.class).when(bookService).findBookByID(bookId);

        //when
        var result = assertThrows(BookNotFoundException.class, () -> bookController.findByID(bookId));

        //then
        verify(bookService).findBookByID(bookId);

    }

    @Test
    void deleteByIDIfExist() {
        //given
        Long id = 1L;
        var bookExist = new Book();
        bookExist.setId(id);

        when(bookService.deleteById(id)).thenReturn(bookExist);

        //when
        var result = bookController.deleteByID(id);

        //then
        verify(bookService).deleteById(id);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void deleteByIDIfNotExist() {
        //given
        Long id = 1L;

        doThrow(BookNotFoundException.class).when(bookService).deleteById(id);

        //when
        var result = assertThrows(BookNotFoundException.class, () -> bookController.deleteByID(id));

        //then
        verify(bookService).deleteById(id);

    }


    @Test
    void updateByIDIfExist() {
        //given
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Fantastyczne zwierzęta i jak je odnaleźć");
        book.setAuthor("J. K. Rowling");
        book.setGenre("Fantastyka");
        book.setPublisher("Bloomsbury");
        book.setPublicationDate(LocalDate.ofEpochDay(2024 - 12 - 19));
        book.setISBN(9781338216790L);

        Book updatedBook = new Book();
        updatedBook.setTitle("Fantastic Beasts and Where to Find Them");
        updatedBook.setAuthor("J. K. Rowling");
        updatedBook.setGenre("Fantasy");
        updatedBook.setPublisher("Bloomsbury");
        updatedBook.setPublicationDate(LocalDate.ofEpochDay(2001 - 12 - 1));
        updatedBook.setISBN(9781338216790L);

        when(bookService.updateByID(bookId, updatedBook)).thenReturn(updatedBook);

        //when
        var result = bookController.updateByID(bookId, updatedBook);

        //then
        verify(bookService).updateByID(bookId, updatedBook);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNull();
    }

    @Test
    void updateByIDIfNotExist() {
        //given
        Long bookId = 1L;

        Book updatedBook = new Book();
        updatedBook.setId(bookId);

        doThrow(BookNotFoundException.class).when(bookService).updateByID(bookId, updatedBook);

        //when
        var result = assertThrows(BookNotFoundException.class, () -> bookController.updateByID(bookId, updatedBook));


        //then
        verify(bookService).updateByID(bookId, updatedBook);

    }


    @Test
    void searchFoundMatch() {
        //given
        var request = new BookSearchRequest();
        request.setAuthor("Rowling");
        var book = new Book();
        book.setAuthor("J.K.Rowling");
        List<Book> bookList = List.of(book);

        when(bookService.search(request)).thenReturn(bookList);

        //when
        var result = bookController.search("Rowling", null, null, null, null, null, null);

        //then
        verify(bookService).search(request);

        var expectedBookApi = new BookAPI();
        expectedBookApi.setAuthor("J.K.Rowling");

        assertThat(result).isNotNull()
                .isNotEmpty()
                .containsExactly(expectedBookApi);

    }

}