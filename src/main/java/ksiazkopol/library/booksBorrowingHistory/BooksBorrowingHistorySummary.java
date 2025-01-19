package ksiazkopol.library.booksBorrowingHistory;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooksBorrowingHistorySummary {

    private long amountOfBooks;
    private long amountOfBorrowedBooks;
    private long amountOfAvailableBooks;
    private long booksAfterDueDateReturned;
    private long booksAfterDueDateNotReturned;
    private long readersAfterDueDate;

}
