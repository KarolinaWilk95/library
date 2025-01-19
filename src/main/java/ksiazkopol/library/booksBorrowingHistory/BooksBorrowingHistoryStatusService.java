package ksiazkopol.library.booksBorrowingHistory;

import org.springframework.stereotype.Service;

@Service
public class BooksBorrowingHistoryStatusService {

    private final BooksBorrowingHistoryRepository booksBorrowingHistoryRepository;

    public BooksBorrowingHistoryStatusService(BooksBorrowingHistoryRepository booksBorrowingHistoryRepository) {
        this.booksBorrowingHistoryRepository = booksBorrowingHistoryRepository;
    }

    public Long countBooksAfterTheDueDate() {
        return booksBorrowingHistoryRepository.countBooksAfterTheDueDate();
    }

    public Long countBooksAfterDueDateNotReturned() {
        return booksBorrowingHistoryRepository.countBooksAfterDueDateNotReturned();
    }

    public Long countAllReadersAfterDueDate() {
        return booksBorrowingHistoryRepository.countAllReadersAfterDueDate();
    }

}
