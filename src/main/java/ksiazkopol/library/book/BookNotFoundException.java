package ksiazkopol.library.book;


import ksiazkopol.library.exception.ResourceNotFoundException;

public class BookNotFoundException extends ResourceNotFoundException {
    public BookNotFoundException() {
        super();
    }

    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookNotFoundException(Throwable cause) {
        super(cause);
    }

    protected BookNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
