package ksiazkopol.library.booksBorrowingHistory;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooksBorrowingHistoryAPI {
    private Long id;
    private Long idBook;
    private String bookTitle;
    private String bookAuthor;
    private Long ISBN;
    private Long idReader;
    private String name;
    private String surname;
    private LocalDate borrowDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private short usedBookRenew;


    public BooksBorrowingHistoryAPI(BooksBorrowingHistory borrowingHistory) {
        this.id = borrowingHistory.getId();
        this.idBook = borrowingHistory.getIdBook();
        this.bookTitle = borrowingHistory.getBookTitle();
        this.bookAuthor = borrowingHistory.getBookAuthor();
        this.ISBN = borrowingHistory.getISBN();
        this.idReader = borrowingHistory.getIdReader();
        this.name = borrowingHistory.getName();
        this.surname = borrowingHistory.getSurname();
        this.borrowDate = borrowingHistory.getBorrowDate();
        this.expectedReturnDate = borrowingHistory.getExpectedReturnDate();
        this.actualReturnDate = borrowingHistory.getActualReturnDate();
        this.usedBookRenew = borrowingHistory.getUsedBookRenew();
    }

    public BooksBorrowingHistory toModel() {
        BooksBorrowingHistory booksBorrowingHistory = new BooksBorrowingHistory();
        booksBorrowingHistory.setId(id);
        booksBorrowingHistory.setIdBook(idBook);
        booksBorrowingHistory.setBookTitle(bookTitle);
        booksBorrowingHistory.setBookAuthor(bookAuthor);
        booksBorrowingHistory.setISBN(ISBN);
        booksBorrowingHistory.setIdReader(idReader);
        booksBorrowingHistory.setName(name);
        booksBorrowingHistory.setSurname(surname);
        booksBorrowingHistory.setBorrowDate(borrowDate);
        booksBorrowingHistory.setExpectedReturnDate(expectedReturnDate);
        booksBorrowingHistory.setActualReturnDate(actualReturnDate);
        booksBorrowingHistory.setUsedBookRenew(usedBookRenew);
        return booksBorrowingHistory;
    }

    public static List<BooksBorrowingHistoryAPI> toApi(Collection<BooksBorrowingHistory> model) {

        if (model == null || model.isEmpty()) {
            return Collections.emptyList();
        }

        return model.stream()
                .map((BooksBorrowingHistoryAPI::new))
                .toList();

    }

}
