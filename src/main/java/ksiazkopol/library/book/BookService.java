package ksiazkopol.library.book;

import ksiazkopol.library.bookseries.BookSeries;
import ksiazkopol.library.dao.BookSearchRequest;
import ksiazkopol.library.dao.BookSearchRequestRepository;
import ksiazkopol.library.reader.Reader;
import ksiazkopol.library.rentalBooksInformation.RentalBooksInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookSearchRequestRepository bookSearchRequestRepository;
    private final RentalBooksInformationService rentalBooksInformationService;


    public BookService(BookRepository bookRepository, BookSearchRequestRepository bookSearchRequestRepository, RentalBooksInformationService rentalBooksInformationService) {
        this.bookRepository = bookRepository;
        this.bookSearchRequestRepository = bookSearchRequestRepository;
        this.rentalBooksInformationService = rentalBooksInformationService;
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }


    public List<Book> findBooks(List<String> details) {
        List<Book> bookList = bookRepository.findAll();
        List<Book> newBookList = new ArrayList<>();


        for (Book book : bookList) {
            Book bookToList = new Book();
            bookToList.setId(book.getId());
            for (String detail : details) {
                switch (detail) {
                    case "title":
                        bookToList.setTitle(book.getTitle());
                        break;
                    case "author":
                        bookToList.setAuthor(book.getAuthor());
                        break;
                    case "genre":
                        bookToList.setGenre(book.getGenre());
                        break;
                    case "publisher":
                        bookToList.setPublisher(book.getPublisher());
                        break;
                    case "publicationDate":
                        bookToList.setPublicationDate(book.getPublicationDate());
                        break;
                    case "ISBN":
                        bookToList.setISBN(book.getISBN());
                        break;
                }
            }
            newBookList.add(bookToList);
        }

        return newBookList;
    }

    public List<Book> findAllBooksForReader(Long readerId) {
        return bookRepository.showAllBooksByReader(readerId, Sort.by(Sort.Order.asc("borrowDate")));
    }

    public Book findBookByID(Long id) {
        Optional<Book> bookInRepository = bookRepository.findById(id);

        if (bookInRepository.isEmpty()) {
            throw new BookNotFoundException("Selected book not found");
        }
        return bookInRepository.get();
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

        if (bookInRepository.get().getReader() == null) {
            throw new BookNotFoundException("Selected book wasn't borrowed");
        }


        Book book = bookInRepository.get();
        rentalBooksInformationService.returnBook(book);
        book.setReader(null);
        book.setBorrowDate(null);
        book.setExpectedReturnDate(null);

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

        rentalBooksInformationService.borrowBook(book, reader);
        LocalDate currentDate = LocalDate.now();
        LocalDate returnDate = currentDate.plusDays(7);
        book.setBorrowDate(currentDate);
        book.setExpectedReturnDate(returnDate);
        book.setReader(reader);
        bookRepository.save(book);
        return book;
    }


    public Book addBookToBookSeries(Long idBook, BookSeries bookSeries) {
        Optional<Book> bookOptional = bookRepository.findById(idBook);

        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException("Selected book not found");
        }

        Book book = bookOptional.get();

        if (book.getBookSeries() != null) {
            throw new BookAssignedException("Selected book is already assigned to another book series");
        }

        book.setBookSeries(bookSeries);
        bookRepository.save(book);
        return book;
    }


    public void deleteBookFromBookSeries(Long idBook) {
        Optional<Book> bookOptional = bookRepository.findById(idBook);

        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException("Selected book not found");
        }

        if (bookOptional.get().getBookSeries() == null) {
            throw new BookAssignedException("Selected book was never assigned to book series");
        }

        Book book = bookOptional.get();
        book.setBookSeries(null);
        bookRepository.save(book);
    }

    public Book showBookDetails(Long idBook, List<String> values) {
        Optional<Book> bookOptional = bookRepository.findById(idBook);
        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException("Selected book not found");
        }
        Book book = new Book();
        for (String value : values) {
            switch (value) {
                case "title":
                    book.setTitle(bookOptional.get().getTitle());
                    break;
                case "author":
                    book.setAuthor(bookOptional.get().getAuthor());
                    break;
                case "genre":
                    book.setGenre(bookOptional.get().getGenre());
                    break;
                case "publisher":
                    book.setPublisher(bookOptional.get().getPublisher());
                    break;
                case "publicationDate":
                    book.setPublicationDate(bookOptional.get().getPublicationDate());
                    break;
                case "ISBN":
                    book.setISBN(bookOptional.get().getISBN());
                    break;
            }
        }
        return book;
    }


    public long countAllBooks() {
        return bookRepository.countAllBooks();
    }

    public long countBorrowedBooks() {
        return bookRepository.countBorrowedBooks();
    }

    public long countBooksAvailableToBorrow() {
        return bookRepository.countBooksAvailableToBorrow();
    }

}
