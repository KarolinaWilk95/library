package ksiazkopol.library.book;

import org.springframework.stereotype.Service;

@Service
public class BooksStatusService {

   final private BookRepository bookRepository;

    public BooksStatusService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public long countAllBooks() {
        return bookRepository.countAllBooks();
    }

    public long countBorrowedBooks() {
        return bookRepository.countBorrowedBooks();
    }

    public long countBooksAvailableToBorrow() {
        return bookRepository.countBooksAvailableToBorrow();
    }
}
