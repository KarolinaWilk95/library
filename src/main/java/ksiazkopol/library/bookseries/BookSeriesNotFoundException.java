package ksiazkopol.library.bookseries;

import ksiazkopol.library.exception.ResourceNotFoundException;

public class BookSeriesNotFoundException extends ResourceNotFoundException {
    public BookSeriesNotFoundException(String message) {
        super(message);
    }
}
