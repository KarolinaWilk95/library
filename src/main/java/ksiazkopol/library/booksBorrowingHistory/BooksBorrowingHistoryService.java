package ksiazkopol.library.booksBorrowingHistory;

import ksiazkopol.library.book.Book;
import ksiazkopol.library.book.BookService;
import ksiazkopol.library.book.BooksStatusService;
import ksiazkopol.library.reader.Reader;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class BooksBorrowingHistoryService {

    private final BooksBorrowingHistoryRepository booksBorrowingHistoryRepository;
    private final BooksStatusService booksStatusService;
    private final BooksBorrowingHistoryStatusService booksBorrowingHistoryStatusService;

    public BooksBorrowingHistoryService(BooksBorrowingHistoryRepository booksBorrowingHistoryRepository, BooksStatusService booksStatusService, BooksBorrowingHistoryStatusService booksBorrowingHistoryStatusService) {
        this.booksBorrowingHistoryRepository = booksBorrowingHistoryRepository;
        this.booksStatusService = booksStatusService;
        this.booksBorrowingHistoryStatusService = booksBorrowingHistoryStatusService;
    }

    public List<BooksBorrowingHistory> showAllRecords() {
        return booksBorrowingHistoryRepository.findAll();
    }

    public void borrowBook(Book book, Reader reader) {
        BooksBorrowingHistory newRecord = new BooksBorrowingHistory();
        LocalDate borrowDate = LocalDate.now();

        newRecord.setIdBook(book.getId());
        newRecord.setBookTitle(book.getTitle());
        newRecord.setBookAuthor(book.getAuthor());
        newRecord.setISBN(book.getISBN());
        newRecord.setIdReader(reader.getId());
        newRecord.setName(reader.getName());
        newRecord.setSurname(reader.getSurname());
        newRecord.setBorrowDate(borrowDate);
        newRecord.setExpectedReturnDate(borrowDate.plusDays(7));

        booksBorrowingHistoryRepository.save(newRecord);

    }


    public void returnBook(Book book) {
        var result = booksBorrowingHistoryRepository.findByIdBookAndIsbn(book.getId(), book.getISBN());

        if (result.isEmpty()) {
            throw new BooksBorrowingHistoryException("Selected record not found");
        }

        result.get().setActualReturnDate(LocalDate.now());

        booksBorrowingHistoryRepository.save(result.get());

    }

    public void renewBook(Long bookId, Long readerId) {
        Optional<BooksBorrowingHistory> borrowingBooksInformationOptional = booksBorrowingHistoryRepository.findByReaderIdBookIdNotReturn(bookId, readerId);

        if (borrowingBooksInformationOptional.isEmpty()) {
            throw new BooksBorrowingHistoryException("Selected record not found");
        }

        if (borrowingBooksInformationOptional.get().getUsedBookRenew() != 0) {
            throw new BooksBorrowingHistoryException("The possibility of extending the book returning period has been used");

        }

        BooksBorrowingHistory booksInformation = borrowingBooksInformationOptional.get();

        booksInformation.setExpectedReturnDate(booksInformation.getExpectedReturnDate().plusDays(7));
        booksInformation.setUsedBookRenew((short) 1);
        booksBorrowingHistoryRepository.save(booksInformation);
    }

    public BooksBorrowingHistorySummary bookStatus() {
        BooksBorrowingHistorySummary booksBorrowingHistorySummary = new BooksBorrowingHistorySummary();
        booksBorrowingHistorySummary.setAmountOfBooks(booksStatusService.countAllBooks());

        booksBorrowingHistorySummary.setAmountOfBorrowedBooks(booksStatusService.countBorrowedBooks());

        booksBorrowingHistorySummary.setAmountOfAvailableBooks(booksStatusService.countBooksAvailableToBorrow());

        booksBorrowingHistorySummary.setBooksAfterDueDateReturned(booksBorrowingHistoryStatusService.countBooksAfterTheDueDate());

        booksBorrowingHistorySummary.setBooksAfterDueDateNotReturned(booksBorrowingHistoryStatusService.countBooksAfterDueDateNotReturned());

        booksBorrowingHistorySummary.setReadersAfterDueDate(booksBorrowingHistoryStatusService.countAllReadersAfterDueDate());

        return booksBorrowingHistorySummary;
    }
}
