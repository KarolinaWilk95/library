package ksiazkopol.library.reader;

import ksiazkopol.library.book.Book;
import ksiazkopol.library.book.BookAPI;
import ksiazkopol.library.book.BookService;
import ksiazkopol.library.dao.ReaderSearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ReaderController {

    final private ReaderService readerService;
    final private BookService bookService;

    public ReaderController(ReaderService readerService, BookService bookService) {
        this.readerService = readerService;
        this.bookService = bookService;
    }

    @PostMapping("api/readers")
    public ReaderAPI addReader(@RequestBody ReaderAPI reader) {
        Reader result = readerService.addReader(reader.toModel());
        return new ReaderAPI(result);
    }

    @GetMapping("api/readers")
    public List<ReaderAPI> findAllReaders() {
        List<Reader> result = readerService.findAllReaders();
        return result.stream()
                .map(ReaderAPI::new)
                .toList();
    }

    @GetMapping("/api/readers/{id}")
    public ResponseEntity<ReaderAPI> findByID(@PathVariable Long id) {
        Optional<Reader> reader = readerService.getReaderByID(id);
        if (reader.isPresent()) {
            return ResponseEntity.ok(new ReaderAPI(reader.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/readers/{id}")
    public ResponseEntity<ReaderAPI> deleteByID(@PathVariable Long id) {
        readerService.deleteByID(id);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/api/readers/{id}")
    public ResponseEntity<ReaderAPI> updateByID(@PathVariable Long id, @RequestBody Reader reader) {
        readerService.updateByID(id, reader);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/readers/search")
    public List<ReaderAPI> search(@RequestParam(name = "name", required = false) String name,
                                  @RequestParam(name = "surname", required = false) String surname) {
        ReaderSearchRequest readerSearchRequest = new ReaderSearchRequest();
        readerSearchRequest.setName(name);
        readerSearchRequest.setSurname(surname);

        List<Reader> result = readerService.search(readerSearchRequest);

        return result.stream()
                .map(ReaderAPI::new)
                .toList();
    }

    @GetMapping("api/readers/{id}/borrowed-books")
    public List<BookAPI> findAllBorrowedBooks(@PathVariable Long id) {
        Collection<Book> result = readerService.findAllBorrowedBooks(id);
        return result.stream()
                .map(BookAPI::new)
                .collect(Collectors.toList());
    }


    @PutMapping("api/readers/{readerId}/books/{id}")
    public ResponseEntity<BookAPI> borrowBook(@PathVariable Long id,
                                              @PathVariable Long readerId) {

        readerService.borrowBook(id, readerId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("api/readers/{readerId}/books/{id}")
    public ResponseEntity<BookAPI> returnBook(@PathVariable Long id,
                                              @PathVariable Long readerId) {
        readerService.returnBook(id, readerId);
        return ResponseEntity.ok().build();
    }
}

