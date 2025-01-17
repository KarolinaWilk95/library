package ksiazkopol.library.rentalBooksInformation;

import ksiazkopol.library.exception.ResourceNotFoundException;

public class RentalBooksInformationException extends ResourceNotFoundException {
    public RentalBooksInformationException(String message) {
        super(message);
    }
}
