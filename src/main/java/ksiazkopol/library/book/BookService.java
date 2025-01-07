package ksiazkopol.library.book;

import ksiazkopol.library.dao.BookSearchRequest;
import ksiazkopol.library.dao.BookSearchRequestRepository;
import ksiazkopol.library.reader.Reader;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookSearchRequestRepository bookSearchRequestRepository;


    public BookService(BookRepository bookRepository, BookSearchRequestRepository bookSearchRequestRepository) {
        this.bookRepository = bookRepository;
        this.bookSearchRequestRepository = bookSearchRequestRepository;
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> findBooks() {
        return bookRepository.findAll();
    }

    public List<Book> findAllBooksForReader(Long readerId) {
        return bookRepository.showAllBooksByReader(readerId, Sort.by(Sort.Order.asc("borrowDate")));
    }

    public Optional<Book> findBookByID(Long id) {
        Optional<Book> bookInRepository = bookRepository.findById(id);

        if (bookInRepository.isEmpty()) {
            throw new BookNotFoundException("Selected book not found");
        }
        return bookInRepository;
    }

    @Transactional
    public Book deleteById(Long id) {
        Optional<Book> book = bookRepository.findById(id);

        if (book.isEmpty()) {
            throw new BookNotFoundException("Selected book not found");
        }

        bookRepository.deleteById(id);
        return book.get();
    }

    @Transactional
    public Book updateByID(Long id, Book newBook) {
        Optional<Book> bookInRepository = bookRepository.findById(id);

        if (bookInRepository.isEmpty()) {
            throw new BookNotFoundException("Selected book not found");
        }

        Book book = bookInRepository.get();
        book.setTitle(newBook.getTitle());
        book.setAuthor(newBook.getAuthor());
        book.setGenre(newBook.getGenre());
        book.setPublisher(newBook.getPublisher());
        book.setPublicationDate(newBook.getPublicationDate());
        book.setISBN(newBook.getISBN());

        return bookRepository.save(book);
    }

    public List<Book> search(BookSearchRequest bookSearchRequest) {
        return bookSearchRequestRepository.findByCriteria(bookSearchRequest);
    }

    @Transactional
    public Book returnBook(Long bookId) {
        Optional<Book> bookInRepository = bookRepository.findById(bookId);

        if (bookInRepository.isEmpty()) {
            throw new BookNotFoundException("Selected book not found");
        }

        if (bookInRepository.get().getBorrowDate() == null) {
            throw new BookNotFoundException("Selected book wasn't borrowed");
        }

        Book book = bookInRepository.get();
        book.setReader(null);
        book.setBorrowDate(null);
        book.setReturnDate(null);

        return book;
    }

    @Transactional
    public Book borrowBook(Long bookId, Reader reader) {

        Optional<Book> bookInRepository = bookRepository.findById(bookId);

        if (bookInRepository.isEmpty()) {
            throw new BookNotFoundException("Selected book not found");
        }

        Book book = bookInRepository.get();

        if (book.getReader() != null) {
            throw new BorrowedBookException("Selected book is already borrowed");
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate returnDate = currentDate.plusDays(7);
        book.setBorrowDate(currentDate);
        book.setReturnDate(returnDate);
        book.setReader(reader);

        return book;
    }


}
