package ksiazkopol.library.reader;

import ksiazkopol.library.dao.ReaderSearchRequest;
import ksiazkopol.library.exception.ReaderNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ReaderController {

    final private ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @PostMapping("api/readers")
    public Reader addReader(@RequestBody Reader reader) {
        return readerService.addReader(reader);
    }

    @GetMapping("api/readers")
    public List<Reader> findAllReaders() {
        return readerService.findAllReaders();
    }

    @GetMapping("/api/readers/{id}")
    public ResponseEntity<Reader> findByID(@PathVariable Long id) {
        Optional<Reader> reader = readerService.getReaderByID(id);

        if (reader.isPresent()) {
            return ResponseEntity.ok(reader.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/readers/{id}")
    public ResponseEntity<Reader> deleteByID(@PathVariable Long id) {
        try {
            readerService.deleteByID(id);
            return ResponseEntity.ok().build();
        } catch (ReaderNotFound e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/api/readers/{id}")
    public ResponseEntity<Reader> updateByID(@PathVariable Long id, @RequestBody
    Reader reader) {
        try {
            readerService.updateByID(id, reader);
            return ResponseEntity.ok().build();
        } catch (ReaderNotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/readers/search")
    public List<Reader> search(@RequestParam(name = "name", required = false) String name,
                               @RequestParam(name = "surname", required = false) String surname) {
        ReaderSearchRequest readerSearchRequest = new ReaderSearchRequest();
        readerSearchRequest.setName(name);
        readerSearchRequest.setSurname(surname);

        return readerService.search(readerSearchRequest);
    }
}

