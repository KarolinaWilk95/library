package ksiazkopol.library.borrowingBooksInformation;

import ksiazkopol.library.book.Book;
import ksiazkopol.library.reader.Reader;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowingBooksInformationService {

    private final BorrowingBooksInformationRepository borrowingBooksInformationRepository;

    public BorrowingBooksInformationService(BorrowingBooksInformationRepository borrowingBooksInformationRepository) {
        this.borrowingBooksInformationRepository = borrowingBooksInformationRepository;
    }

    public List<BorrowingBooksInformation> showAllRecords() {
        return borrowingBooksInformationRepository.findAll();
    }

    public void borrowBook(Book book, Reader reader) {
        BorrowingBooksInformation newRecord = new BorrowingBooksInformation();
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

        borrowingBooksInformationRepository.save(newRecord);

    }


    public void returnBook(Book book) {
        var result = borrowingBooksInformationRepository.findByIdBookAndIsbn(book.getId(), book.getISBN()).get(0);
        result.setActualReturnDate(LocalDate.now());

        borrowingBooksInformationRepository.save(result);

    }

    public Long countBooksAfterTheDueDate() {
        return borrowingBooksInformationRepository.countBooksAfterTheDueDate();
    }

    public Long countBooksAfterDueDateNotReturned() {
        return borrowingBooksInformationRepository.countBooksAfterDueDateNotReturned();
    }

    public Long countAllReadersAfterDueDate(){
        return borrowingBooksInformationRepository.countAllReadersAfterDueDate();
    }

}
