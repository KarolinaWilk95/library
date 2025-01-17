package ksiazkopol.library.rentalBooksInformation;

import ksiazkopol.library.book.Book;
import ksiazkopol.library.book.BookService;
import ksiazkopol.library.reader.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RentalBooksInformationService {

    private final RentalBooksInformationRepository rentalBooksInformationRepository;
    private final BookService bookService;
    private final RentalBooksInformationService rentalBooksInformationService;

    public RentalBooksInformationService(RentalBooksInformationRepository rentalBooksInformationRepository, BookService bookService, RentalBooksInformationService rentalBooksInformationService) {
        this.rentalBooksInformationRepository = rentalBooksInformationRepository;
        this.bookService = bookService;
        this.rentalBooksInformationService = rentalBooksInformationService;
    }

    public List<RentalBooksInformation> showAllRecords() {
        return rentalBooksInformationRepository.findAll();
    }

    public void borrowBook(Book book, Reader reader) {
        RentalBooksInformation newRecord = new RentalBooksInformation();
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

        rentalBooksInformationRepository.save(newRecord);

    }


    public void returnBook(Book book) {
        var result = rentalBooksInformationRepository.findByIdBookAndIsbn(book.getId(), book.getISBN());

        if (result.isEmpty()) {
            throw new RentalBooksInformationException("Selected record not found");
        }

        result.get().setActualReturnDate(LocalDate.now());

        rentalBooksInformationRepository.save(result.get());

    }

    public Long countBooksAfterTheDueDate() {
        return rentalBooksInformationRepository.countBooksAfterTheDueDate();
    }

    public Long countBooksAfterDueDateNotReturned() {
        return rentalBooksInformationRepository.countBooksAfterDueDateNotReturned();
    }

    public Long countAllReadersAfterDueDate() {
        return rentalBooksInformationRepository.countAllReadersAfterDueDate();
    }

    public void renewBook(Long bookId, Long readerId) {
        Optional<RentalBooksInformation> borrowingBooksInformationOptional = rentalBooksInformationRepository.findByReaderIdBookIdNotReturn(bookId, readerId);

        if (borrowingBooksInformationOptional.isEmpty()) {
            throw new RentalBooksInformationException("Selected record not found");
        }

        if (borrowingBooksInformationOptional.get().getUsedBookRenew() != 0) {
            throw new RentalBooksInformationException("The possibility of extending the book rental period has been used");

        }

        RentalBooksInformation booksInformation = borrowingBooksInformationOptional.get();

        booksInformation.setExpectedReturnDate(booksInformation.getExpectedReturnDate().plusDays(7));
        booksInformation.setUsedBookRenew((short) 1);
        rentalBooksInformationRepository.save(booksInformation);
    }

    public RentalBooksInfoAPI bookStatus() {
        RentalBooksInfoAPI rentalBooksInfoAPI = new RentalBooksInfoAPI();
        rentalBooksInfoAPI.setAmountOfBooks(bookService.countAllBooks());
        rentalBooksInfoAPI.setAmountOfBorrowedBooks(bookService.countBorrowedBooks());
        rentalBooksInfoAPI.setAmountOfAvailableBooks(bookService.countBooksAvailableToBorrow());
        rentalBooksInfoAPI.setBooksAfterDueDateReturned(rentalBooksInformationService.countAllReadersAfterDueDate());
        rentalBooksInfoAPI.setBooksAfterDueDateNotReturned(rentalBooksInformationService.countBooksAfterDueDateNotReturned());
        rentalBooksInfoAPI.setReadersAfterDueDate(rentalBooksInformationService.countAllReadersAfterDueDate());

        return rentalBooksInfoAPI;
    }
}
