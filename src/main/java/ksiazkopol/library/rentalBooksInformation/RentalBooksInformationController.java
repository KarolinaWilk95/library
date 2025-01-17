package ksiazkopol.library.rentalBooksInformation;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RentalBooksInformationController {

    private final RentalBooksInformationService rentalBooksInformationService;


    public RentalBooksInformationController(RentalBooksInformationService auditService) {
        this.rentalBooksInformationService = auditService;
    }

    @GetMapping("/api/rental")
    public ResponseEntity<List<RentalBooksInformation>> showAllRecords() {
        List<RentalBooksInformation> rentalBooksInformationList = rentalBooksInformationService.showAllRecords();
        return ResponseEntity.ok().body(rentalBooksInformationList);
    }

    @GetMapping("/api/books/info")
    public RentalBooksInfoAPI booksStatus() {
        return rentalBooksInformationService.bookStatus();
    }
}
