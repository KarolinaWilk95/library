package ksiazkopol.library.bookseries;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookSeriesController {

    private final BookSeriesService bookSeriesService;

    public BookSeriesController(BookSeriesService bookSeriesService) {
        this.bookSeriesService = bookSeriesService;
    }

    @GetMapping("/api/book-series")
    public List<BookSeriesAPI> findAllSeries() {
        List<BookSeries> bookSeriesList = bookSeriesService.findAll();
        return bookSeriesList.stream()
                .map(BookSeriesAPI::new)
                .toList();
    }

    @PostMapping("/api/book-series")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public BookSeriesAPI addBookSeries(@RequestBody BookSeriesAPI bookSeries) {
        BookSeries result = bookSeriesService.addBookSeries(bookSeries.toModel());
        return new BookSeriesAPI(result);
    }

    @DeleteMapping("/api/book-series/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BookSeriesAPI> deleteBookSeries(@PathVariable Long id) {
        bookSeriesService.deleteBookSeries(id);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/api/book-series/{id}")
    @PreAuthorize("hasAnyRole('READER','LIBRARIAN')")
        public ResponseEntity<BookSeriesAPI> findById(@PathVariable Long id) {
        BookSeries bookSeriesResponseEntity = bookSeriesService.findById(id);
        return ResponseEntity.ok(new BookSeriesAPI(bookSeriesResponseEntity));
    }

    @PutMapping("/api/book-series/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BookSeriesAPI> updateById(@PathVariable Long id, @RequestBody BookSeries bookSeries) {
        BookSeries bookSeriesResponseEntity = bookSeriesService.updateById(id,bookSeries);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/book-series/{idBookSeries}/books/{idBook}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BookSeriesAPI> addBookToBookSeries(@PathVariable Long idBookSeries, @PathVariable Long idBook) {
        bookSeriesService.addBookToBookSeries(idBookSeries, idBook);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/book-series/{idBookSeries}/books/{idBook}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BookSeriesAPI> deleteBookFromBookSeries(@PathVariable Long idBookSeries, @PathVariable Long idBook) {
        bookSeriesService.deleteBookFromBookSeries(idBookSeries, idBook);
        return ResponseEntity.ok().build();
    }
    }
