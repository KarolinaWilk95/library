package ksiazkopol.library.book;

import ksiazkopol.library.dao.BookSearchRequestRepository;
import ksiazkopol.library.dao.BookSearchRequest;
import ksiazkopol.library.exception.BookNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Book> showAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> findBookByID(Long id) {
        Optional<Book> bookInRepository = bookRepository.findById(id);

        if (bookInRepository.isPresent()) {
            return bookRepository.findById(id);
        } else {
            throw new BookNotFound("Selected book not found");
        }
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
        } else {
            throw new BookNotFound("Selected book not found");
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
            throw new BookNotFound("Selected book not found");
        }
    }

    public List<Book> search(BookSearchRequest bookSearchRequest) {
        return bookSearchRequestRepository.findByCriteria(bookSearchRequest);
    }
}
