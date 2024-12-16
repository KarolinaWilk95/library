package ksiazkopol.library.reader;

import ksiazkopol.library.exception.ResourceNotFoundException;

public class ReaderNotFoundException extends ResourceNotFoundException {
    public ReaderNotFoundException() {
        super();
    }

    public ReaderNotFoundException(String message) {
        super(message);
    }

    public ReaderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReaderNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ReaderNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
