package ksiazkopol.library.book;

import ksiazkopol.library.dao.BookSearchRequest;
import ksiazkopol.library.dao.BookSearchRequestRepository;
import ksiazkopol.library.reader.ReaderNotFoundException;
import ksiazkopol.library.reader.Reader;
import ksiazkopol.library.reader.ReaderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final BookSearchRequestRepository bookSearchRequestRepository;


    public BookService(BookRepository bookRepository, ReaderRepository readerRepository, BookSearchRequestRepository bookSearchRequestRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.bookSearchRequestRepository = bookSearchRequestRepository;
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> showAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> findBookByID(Long id) {
        Optional<Book> bookInRepository = bookRepository.findById(id);

        if (bookInRepository.isPresent()) {
            return bookRepository.findById(id);
        } else {
            throw new BookNotFoundException("Selected book not found");
        }
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
        } else {
            throw new BookNotFoundException("Selected book not found");
        }
    }

    @Transactional
    public void updateByID(Long id, Book newBook) {
        Optional<Book> bookInRepository = bookRepository.findById(id);

        if (bookInRepository.isPresent()) {
            Book book = bookInRepository.get();
            book.setTitle(newBook.getTitle());
            book.setAuthor(newBook.getAuthor());
            book.setGenre(newBook.getGenre());
            book.setPublisher(newBook.getPublisher());
            book.setPublicationDate(newBook.getPublicationDate());
            book.setISBN(newBook.getISBN());
        } else {
            throw new BookNotFoundException("Selected book not found");
        }
    }

    public List<Book> search(BookSearchRequest bookSearchRequest) {
        return bookSearchRequestRepository.findByCriteria(bookSearchRequest);
    }

    @Transactional
    public void borrowBook(Long id, Long readerId) {
        //Optional <Book>
        Optional<Book> bookInRepository = bookRepository.findById(id);
        Optional<Reader> readerInRepository = readerRepository.findById(readerId);
        LocalDate currentDate = LocalDate.now();

        if (readerInRepository.isEmpty()) {
            throw new ReaderNotFoundException("Selected reader not found");
        } else if (bookInRepository.isEmpty()) {
            throw new BookNotFoundException("Selected book not found");
        } else if (bookInRepository.get().getBorrowDate() != null) {
            throw new BorrowedBookException("Selected book is already borrowed");
        } else {
            Book book = bookInRepository.get();
            book.setReader(readerInRepository.get());
            book.setBorrowDate(currentDate);
        }
    }

    @Transactional
    public void returnBook(Long id, Long readerId) {
        Optional<Book> bookInRepository = bookRepository.findById(id);
        Optional<Reader> readerInRepository = readerRepository.findById(readerId);

        if (!readerInRepository.isPresent()) {
            throw new ReaderNotFoundException("Selected reader not found");
        } else if (!bookInRepository.isPresent()) {
            throw new BookNotFoundException("Selected book not found");
        } else {
            Book book = bookInRepository.get();
            book.setReader(null);
            book.setBorrowDate(null);
        }
    }
}
