package ksiazkopol.library.reader;

import ksiazkopol.library.book.Book;
import ksiazkopol.library.book.BookAPI;
import ksiazkopol.library.dao.ReaderSearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ReaderController {

    final private ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @PostMapping("/api/readers")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ReaderAPI addReader(@RequestBody ReaderAPI reader) {
        Reader result = readerService.addReader(reader.toModel());
        return new ReaderAPI(result);
    }

    @GetMapping("/api/readers")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public List<ReaderAPI> findAllReaders() {
        List<Reader> result = readerService.findAllReaders();
        return result.stream()
                .map(ReaderAPI::new)
                .toList();
    }

    @GetMapping("/api/readers/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<ReaderAPI> findByID(@PathVariable Long id) {
        Optional<Reader> reader = readerService.getReaderByID(id);
        return ResponseEntity.ok(new ReaderAPI(reader.get()));

    }

    @DeleteMapping("/api/readers/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<ReaderAPI> deleteByID(@PathVariable Long id) {
        readerService.deleteByID(id);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/api/readers/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<ReaderAPI> updateByID(@PathVariable Long id, @RequestBody Reader reader) {
        readerService.updateByID(id, reader);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/readers/search")
    @PreAuthorize("hasRole('LIBRARIAN')")
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

    @GetMapping("/api/readers/{id}/borrowed-books")
    @PreAuthorize("hasAnyRole('LIBRARIAN','READER')")
    public List<BookAPI> findAllBorrowedBooks(@PathVariable Long idReader) {
        List<Book> result = readerService.findAllBorrowedBooks(idReader);
        return result.stream()
                .map(BookAPI::new)
                .collect(Collectors.toList());
    }


    @PutMapping("/api/readers/{readerId}/books/{id}")
    @PreAuthorize("hasAnyRole('READER','LIBRARIAN')")
    public ResponseEntity<BookAPI> borrowBook(@PathVariable Long id,
                                              @PathVariable Long readerId) {

        readerService.borrowBook(id, readerId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/readers/{readerId}/books/{id}")
    @PreAuthorize("hasAnyRole('READER','LIBRARIAN')")
    public ResponseEntity<BookAPI> returnBook(@PathVariable Long id,
                                              @PathVariable Long readerId) {
        readerService.returnBook(id, readerId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/readers/{readerId}/books/{bookId}/renew")
    @PreAuthorize("hasAnyRole('READER','LIBRARIAN')")
    public ResponseEntity<Void> renewBook(@PathVariable Long bookId,
                                          @PathVariable Long readerId) {
        readerService.renewBook(bookId, readerId);
        return ResponseEntity.ok().build();
    }
}


