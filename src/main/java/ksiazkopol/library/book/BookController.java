package ksiazkopol.library.book;

import ksiazkopol.library.dao.BookSearchRequest;
import ksiazkopol.library.exception.BookNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    private final BookService bookService;


    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("api/books")
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @GetMapping("api/books")
    public List<Book> showBooks() {
        return bookService.showAllBooks();
    }

    @GetMapping("/api/books/{id}")
    public ResponseEntity<Book> findByID(@PathVariable Long id) {
        Optional<Book> book = bookService.findBookByID(id);

        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<Book> deleteByID(@PathVariable Long id) {
        try {
            bookService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (BookNotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/books/{id}")
    public ResponseEntity<Book> updateByID(@PathVariable Long id, @RequestBody
    Book book) {
        try {
            bookService.updateByID(id, book);
            return ResponseEntity.ok().build();
        } catch (BookNotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/books/search")
    public List<Book> search(@RequestParam(name = "author", required = false) String author,
                             @RequestParam(name = "title", required = false) String title,
                             @RequestParam(name = "genre", required = false) String genre,
                             @RequestParam(name = "publisher", required = false) String publisher,
                             @RequestParam(name = "date", required = false) LocalDate publicationDate,
                             @RequestParam(name = "year", required = false) Integer yearOfPublication,
                             @RequestParam(name = "ISBN", required = false) Long ISBN) {
        BookSearchRequest bookSearchRequest = new BookSearchRequest();
        bookSearchRequest.setAuthor(author);
        bookSearchRequest.setTitle(title);
        bookSearchRequest.setGenre(genre);
        bookSearchRequest.setPublisher(publisher);
        bookSearchRequest.setPublicationDate(publicationDate);
        bookSearchRequest.setPublicationYear(yearOfPublication);
        bookSearchRequest.setISBN(ISBN);

        return bookService.search(bookSearchRequest);
    }
}
