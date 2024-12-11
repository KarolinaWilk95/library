package ksiazkopol.library.reader;

import ksiazkopol.library.book.Book;
import ksiazkopol.library.book.BookAPI;
import ksiazkopol.library.dao.ReaderSearchRequest;
import ksiazkopol.library.exception.ReaderNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ReaderController {

    final private ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @PostMapping("api/readers")
    public ReaderAPI addReader(@RequestBody ReaderAPI reader) {
        Reader result = readerService.addReader(reader.toModel());
        return new ReaderAPI(result);
    }

    @GetMapping("api/readers")
    public List<ReaderAPI> findAllReaders() {
        List<Reader> result = readerService.findAllReaders();
        return result.stream().map(ReaderAPI::new).toList();
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
        try {
            readerService.deleteByID(id);
            return ResponseEntity.ok().build();
        } catch (ReaderNotFound e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/api/readers/{id}")
    public ResponseEntity<ReaderAPI> updateByID(@PathVariable Long id, @RequestBody
    Reader reader) {
        try {
            readerService.updateByID(id, reader);
            return ResponseEntity.ok().build();
        } catch (ReaderNotFound e) {
            return ResponseEntity.notFound().build();
        }
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

    @GetMapping("api/readers/{id}/list")
    public Set<BookAPI> findAllBorrowedBooks(@PathVariable Long id) {
        Set<Book> result = readerService.findAllBorrowedBooks(id);
        return result.stream().map(BookAPI::new).collect(Collectors.toSet());
    }
}

