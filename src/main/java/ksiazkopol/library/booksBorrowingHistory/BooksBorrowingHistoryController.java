package ksiazkopol.library.booksBorrowingHistory;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class BooksBorrowingHistoryController {

    private final BooksBorrowingHistoryService booksBorrowingHistoryService;


    public BooksBorrowingHistoryController(BooksBorrowingHistoryService booksBorrowingHistoryService) {
        this.booksBorrowingHistoryService = booksBorrowingHistoryService;
    }

    @GetMapping("/api/status")
    @PreAuthorize("hasAnyRole('LIBRARIAN','ANALYST')")
    public List<BooksBorrowingHistoryAPI> showAllRecords() {
        Collection<BooksBorrowingHistory> rentalBooksInformationList = booksBorrowingHistoryService.showAllRecords();
        return BooksBorrowingHistoryAPI.toApi(rentalBooksInformationList);
    }

    @GetMapping("/api/status/summary")
    @PreAuthorize("hasAnyRole('LIBRARIAN','ANALYST')")
    public BooksBorrowingHistorySummaryAPI booksStatus() {
        return new BooksBorrowingHistorySummaryAPI(booksBorrowingHistoryService.bookStatus());
    }
}
