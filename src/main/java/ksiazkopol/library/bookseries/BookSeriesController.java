package ksiazkopol.library.bookseries;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookSeriesController {

    private final BookSeriesService bookSeriesService;

    public BookSeriesController(BookSeriesService bookSeriesService) {
        this.bookSeriesService = bookSeriesService;
    }

    @GetMapping("/api/bookseries")
    public List<BookSeriesAPI> findAllSeries() {
        List<BookSeries> bookSeriesList = bookSeriesService.findAll();
        return bookSeriesList.stream()
                .map(BookSeriesAPI::new)
                .toList();
    }

    @PostMapping("/api/bookseries")
    public ResponseEntity<BookSeriesAPI> addBookSeries(@RequestBody BookSeries bookSeries) {
        BookSeries result = bookSeriesService.addBookSeries(bookSeries);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/bookseries/{id}")
    public ResponseEntity<BookSeriesAPI> deleteBookSeries(@PathVariable Long id) {
        Optional<BookSeries> bookSeriesOptional = bookSeriesService.findById(id);

        if(bookSeriesOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            bookSeriesService.deleteBookSeries(id);
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/api/bookseries/{id}")
    public ResponseEntity<BookSeriesAPI> findById(@PathVariable Long id) {

        Optional<BookSeries> bookSeriesOptional = bookSeriesService.findById(id);

        if (bookSeriesOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(new BookSeriesAPI(bookSeriesOptional.get()));
        }


    }

    @PutMapping("/api/bookseries/{id}")
    public ResponseEntity<BookSeriesAPI> updateById(@PathVariable Long id, @RequestBody BookSeries bookSeries) {
        Optional<BookSeries> bookSeriesOptional = bookSeriesService.findById(id);

        if (bookSeriesOptional.isPresent()) {
            bookSeriesService.updateById(id, bookSeries);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
