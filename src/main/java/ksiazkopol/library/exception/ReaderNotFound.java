package ksiazkopol.library.exception;

public class ReaderNotFound extends RuntimeException{
    public ReaderNotFound() {
        super();
    }

    public ReaderNotFound(String message) {
        super(message);
    }

    public ReaderNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public ReaderNotFound(Throwable cause) {
        super(cause);
    }

    protected ReaderNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
