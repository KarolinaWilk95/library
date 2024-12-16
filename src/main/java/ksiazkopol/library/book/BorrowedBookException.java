package ksiazkopol.library.book;

public class BorrowedBookException extends RuntimeException {
    public BorrowedBookException() {
        super();
    }

    protected BorrowedBookException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BorrowedBookException(Throwable cause) {
        super(cause);
    }

    public BorrowedBookException(String message, Throwable cause) {
        super(message, cause);
    }

    public BorrowedBookException(String message) {
        super(message);
    }
}
