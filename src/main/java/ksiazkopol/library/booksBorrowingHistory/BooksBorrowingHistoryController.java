package ksiazkopol.library.booksBorrowingHistory;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class BooksBorrowingHistoryController {

    private final BooksBorrowingHistoryService booksBorrowingHistoryService;


    public BooksBorrowingHistoryController(BooksBorrowingHistoryService auditService) {
        this.booksBorrowingHistoryService = auditService;
    }

    @GetMapping("/api/status")
    public List<BooksBorrowingHistoryAPI> showAllRecords() {
        Collection<BooksBorrowingHistory> rentalBooksInformationList = booksBorrowingHistoryService.showAllRecords();

        return BooksBorrowingHistoryAPI.toApi(rentalBooksInformationList);
    }

    @GetMapping("/api/books/info")
    public BooksBorrowingHistorySummaryAPI booksStatus() {
        BooksBorrowingHistorySummaryAPI borrowingHistorySummaryAPI = new BooksBorrowingHistorySummaryAPI(booksBorrowingHistoryService.bookStatus());

        return borrowingHistorySummaryAPI;
    }
}
