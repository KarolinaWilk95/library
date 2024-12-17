package ksiazkopol.library.reader;

import ksiazkopol.library.book.Book;
import ksiazkopol.library.book.BookService;
import ksiazkopol.library.dao.ReaderSearchRequest;
import ksiazkopol.library.dao.ReaderSearchRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ReaderService {


    private final ReaderRepository readerRepository;
    private final ReaderSearchRequestRepository readerSearchRequestRepository;
    private final BookService bookService;


    public ReaderService(ReaderRepository readerRepository, ReaderSearchRequestRepository readerSearchRequestRepository, BookService bookService) {
        this.readerRepository = readerRepository;
        this.readerSearchRequestRepository = readerSearchRequestRepository;
        this.bookService = bookService;

    }

    public Reader addReader(Reader reader) {
        return readerRepository.save(reader);
    }

    @Transactional
    public List<Reader> findAllReaders() {
        return readerRepository.findAll();
    }

    public Optional<Reader> getReaderByID(Long id) {
        Optional<Reader> readerInRepository = readerRepository.findById(id);

        if (readerInRepository.isPresent()) {
            return readerRepository.findById(id);
        } else {
            throw new ReaderNotFoundException("Selected reader not found");
        }
    }

    @Transactional
    public void deleteByID(Long id) {
        Optional<Reader> reader = readerRepository.findById(id);
        if (reader.isPresent()) {
            Reader readerExisting = reader.get();
            for (Book book : readerExisting.getBooks()) {
                book.setReader(null);
            }
            readerExisting.setBooks(null);
            readerRepository.delete(readerExisting);
        } else {
            throw new ReaderNotFoundException("Selected reader not found");
        }
    }

    @Transactional
    public void updateByID(Long id, Reader newReader) {
        Optional<Reader> readerInRepository = readerRepository.findById(id);

        if (readerInRepository.isPresent()) {
            Reader reader = readerInRepository.get();
            reader.setName(newReader.getName());
            reader.setSurname(newReader.getSurname());
        } else {
            throw new ReaderNotFoundException("Selected reader not found");
        }
    }

    public List<Reader> search(ReaderSearchRequest readerSearchRequest) {
        return readerSearchRequestRepository.findByCriteria(readerSearchRequest);
    }


    @Transactional
    public void borrowBook(Long bookId, Long readerId) {
        Optional<Reader> readerInRepository = readerRepository.findById(readerId);
        Reader reader = readerInRepository.get();

        if (readerInRepository.isEmpty()) {
            throw new ReaderNotFoundException("Selected reader not found");
        } else {
            bookService.borrowBook(bookId, reader);
        }

    }

    @Transactional
    public void returnBook(Long bookId, Long readerId) {
        Optional<Reader> readerInRepository = readerRepository.findById(readerId);

        if (!readerInRepository.isPresent()) {
            throw new ReaderNotFoundException("Selected reader not found");
        }


        bookService.returnBook(bookId);
    }


    public Collection<Book> findAllBorrowedBooks(Long id) {
        boolean readerInRepository = readerRepository.existsById(id);

        if (readerInRepository) {
            return bookService.findAllBooksForReader(id);
        } else {
            throw new ReaderNotFoundException("Selected reader not found");
        }
    }

}
