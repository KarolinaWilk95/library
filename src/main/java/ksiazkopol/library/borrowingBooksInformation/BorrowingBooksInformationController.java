package ksiazkopol.library.borrowingBooksInformation;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BorrowingBooksInformationController {

    private final BorrowingBooksInformationService borrowingBooksInformationService;


    public BorrowingBooksInformationController(BorrowingBooksInformationService auditService) {
        this.borrowingBooksInformationService = auditService;
    }

    @GetMapping("/api/rental")
    public ResponseEntity<List<BorrowingBooksInformation>> showAllRecords(){
        List<BorrowingBooksInformation> borrowingBooksInformationList = borrowingBooksInformationService.showAllRecords();
        return ResponseEntity.ok().body(borrowingBooksInformationList);
    }


}
