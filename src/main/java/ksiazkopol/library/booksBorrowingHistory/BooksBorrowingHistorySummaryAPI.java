package ksiazkopol.library.booksBorrowingHistory;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooksBorrowingHistorySummaryAPI {

    private long amountOfBooks;
    private long amountOfBorrowedBooks;
    private long amountOfAvailableBooks;
    private long booksAfterDueDateReturned;
    private long booksAfterDueDateNotReturned;
    private long readersAfterDueDate;


    public BooksBorrowingHistorySummaryAPI(BooksBorrowingHistorySummary booksBorrowingHistorySummary) {
        this.amountOfBooks = booksBorrowingHistorySummary.getAmountOfBooks();
        this.amountOfBorrowedBooks = booksBorrowingHistorySummary.getAmountOfBorrowedBooks();
        this.amountOfAvailableBooks=booksBorrowingHistorySummary.getAmountOfAvailableBooks();
        this.booksAfterDueDateReturned = booksBorrowingHistorySummary.getBooksAfterDueDateReturned();
        this.booksAfterDueDateNotReturned = booksBorrowingHistorySummary.getBooksAfterDueDateNotReturned();
        this.readersAfterDueDate = booksBorrowingHistorySummary.getReadersAfterDueDate();
    }

    public BooksBorrowingHistorySummary toModel() {
        BooksBorrowingHistorySummary model = new BooksBorrowingHistorySummary();
        model.setAmountOfBooks(amountOfBooks);
        model.setAmountOfBorrowedBooks(amountOfBorrowedBooks);
        model.setAmountOfAvailableBooks(amountOfAvailableBooks);
        model.setBooksAfterDueDateReturned(booksAfterDueDateReturned);
        model.setBooksAfterDueDateNotReturned(booksAfterDueDateNotReturned);
        model.setReadersAfterDueDate(readersAfterDueDate);
        return model;
    }


    public static List<BooksBorrowingHistorySummaryAPI> toApi(Collection<BooksBorrowingHistorySummary> model) {

        if (model == null || model.isEmpty()) {
            return Collections.emptyList();
        }

        return model.stream()
                .map((BooksBorrowingHistorySummaryAPI::new))
                .toList();

    }
}
