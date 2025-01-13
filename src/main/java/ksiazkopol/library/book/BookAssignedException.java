package ksiazkopol.library.book;

import ksiazkopol.library.exception.ResourceNotFoundException;

public class BookAssignedException extends ResourceNotFoundException {
    public BookAssignedException(String message) {
        super(message);
    }
}
