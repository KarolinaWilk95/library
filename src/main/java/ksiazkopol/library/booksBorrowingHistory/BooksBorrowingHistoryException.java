package ksiazkopol.library.booksBorrowingHistory;

import ksiazkopol.library.exception.ResourceNotFoundException;

public class BooksBorrowingHistoryException extends ResourceNotFoundException {
    public BooksBorrowingHistoryException(String message) {
        super(message);
    }
}
