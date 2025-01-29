package ksiazkopol.library.book;

import ksiazkopol.library.dao.BookSearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;


    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("api/books")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public BookAPI addBook(@RequestBody BookAPI book) {
        Book result = bookService.addBook(book.toModel());
        return new BookAPI(result);
    }

    @GetMapping("api/books")
    public List<BookAPI> findBooks(@RequestParam List<String> details) {
        List<Book> result = bookService.findBooks(details);
        return BookAPI.toApi(result);
    }

    @GetMapping("api/books/all")
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
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BookAPI> deleteByID(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/books/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BookAPI> updateByID(@PathVariable Long id, @RequestBody
    Book book) {
        bookService.updateByID(id, book);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/books/search")
    @PreAuthorize("hasAnyRole('READER','LIBRARIAN')")
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


    //http://localhost:8080/api/books/1/showBookDetails?values=title,genre
    @GetMapping("/api/books/{id}/showBookDetails")
    @PreAuthorize("hasAnyRole('READER','LIBRARIAN')")
    public BookAPI showBookDetails(@PathVariable Long id,
                                   @RequestParam List<String> values) {

        Book result = bookService.showBookDetails(id, values);
        return new BookAPI(result);

    }

}
