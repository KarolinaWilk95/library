package ksiazkopol.library.rentalBooksInformation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalBooksInfoAPI {

    private long amountOfBooks;
    private long amountOfBorrowedBooks;
    private long amountOfAvailableBooks;
    private long booksAfterDueDateReturned;
    private long booksAfterDueDateNotReturned;
    private long readersAfterDueDate;

}
