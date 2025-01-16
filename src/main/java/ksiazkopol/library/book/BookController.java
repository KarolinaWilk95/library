package ksiazkopol.library.book;

import ksiazkopol.library.dao.BookSearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;


    public BookController(BookService bookService,
                          BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    @PostMapping("api/books")
    public BookAPI addBook(@RequestBody BookAPI book) {
        Book result = bookService.addBook(book.toModel());
        return new BookAPI(result);
    }

    @GetMapping("api/books")
    public List<BookAPI> findBooks(@RequestParam List<String> details) {
        List<Book> result = bookService.findBooks(details);
        return BookAPI.toApi(result);
    }

    @GetMapping("api/allBooks")
    public List<BookAPI> findAllBooks() {
        List<Book> result = bookService.findAllBooks();
        return BookAPI.toApi(result);
    }

    @GetMapping("/api/books/{id}")
    public ResponseEntity<BookAPI> findByID(@PathVariable Long id) {
        Book book = bookService.findBookByID(id);
        return ResponseEntity.ok(new BookAPI(book));

    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<BookAPI> deleteByID(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/books/{id}")
    public ResponseEntity<BookAPI> updateByID(@PathVariable Long id, @RequestBody
    Book book) {
        bookService.updateByID(id, book);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/books/search")
    public List<BookAPI> search(@RequestParam(name = "author", required = false) String author,
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

        List<Book> result = bookService.search(bookSearchRequest);

        return BookAPI.toApi(result);
    }


    //http://localhost:8080/api/books/1/showBookDetails?author&title&genre
    @GetMapping("/api/books/{id}/showBookDetails")
    public BookAPI showBookDetails(@PathVariable Long id,
                                   @RequestParam List<String> values) {

        Book result = bookService.showBookDetails(id, values);
        return new BookAPI(result);

    }

    @GetMapping("/api/books/info")
    public List<String> booksStatus() {
        var result = bookService.booksStatus();
        return result;
    }
}
