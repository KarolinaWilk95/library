package ksiazkopol.library.reader;

import ksiazkopol.library.book.BookRepository;
import ksiazkopol.library.dao.ReaderSearchRequest;
import ksiazkopol.library.dao.ReaderSearchRequestRepository;
import ksiazkopol.library.exception.ReaderNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReaderService {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final ReaderSearchRequestRepository readerSearchRequestRepository;


    public ReaderService(BookRepository bookRepository, ReaderRepository readerRepository, ReaderSearchRequestRepository readerSearchRequestRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.readerSearchRequestRepository = readerSearchRequestRepository;
    }

    public Reader addReader(Reader reader) {
        return readerRepository.save(reader);
    }

    public List<Reader> findAllReaders() {
        return readerRepository.findAll();
    }

    public Optional<Reader> getReaderByID(Long id) {
        Optional<Reader> readerInRepository = readerRepository.findById(id);

        if (readerInRepository.isPresent()) {
            return readerRepository.findById(id);
        } else {
            throw new ReaderNotFound("Selected reader not found");
        }
    }

    @Transactional
    public void deleteByID(Long id) {
        Optional<Reader> reader = readerRepository.findById(id);
        if (reader.isPresent()) {
            readerRepository.delete(reader.get());
        } else {
            throw new ReaderNotFound("Selected reader not found");
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
            throw new ReaderNotFound("Selected reader not found");
        }
    }

    public List<Reader> search(ReaderSearchRequest readerSearchRequest) {
        return readerSearchRequestRepository.findByCriteria(readerSearchRequest);
    }

    //borrow the book

    //return the book

    //show all borrowed books


}
