package ksiazkopol.library.exception;


public class BookNotFound extends RuntimeException{
    public BookNotFound() {
        super();
    }

    public BookNotFound(String message) {
        super(message);
    }

    public BookNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public BookNotFound(Throwable cause) {
        super(cause);
    }

    protected BookNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
