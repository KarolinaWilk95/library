package ksiazkopol.library.bookseries;

import ksiazkopol.library.book.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookSeriesService {

    private final BookSeriesRepository bookSeriesRepository;
    private final BookService bookService;

    public BookSeriesService(BookSeriesRepository bookSeriesRepository, BookService bookService) {
        this.bookSeriesRepository = bookSeriesRepository;
        this.bookService = bookService;
    }

    public List<BookSeries> findAll() {
        return bookSeriesRepository.findAll();
    }

    public BookSeries addBookSeries(BookSeries bookSeries) {
        return bookSeriesRepository.save(bookSeries);
    }

    @Transactional
    public void deleteBookSeries(Long id) {
        Optional<BookSeries> bookSeriesOptional = bookSeriesRepository.findById(id);

        if (bookSeriesOptional.isPresent()) {
            bookSeriesRepository.deleteById(id);
        } else {
            throw new BookSeriesNotFoundException("Selected book series not found");
        }
    }

    public BookSeries findById(Long id) {
        Optional<BookSeries> bookSeries = bookSeriesRepository.findById(id);

        if (bookSeries.isEmpty()) {
            throw new BookSeriesNotFoundException("Selected book series not found");
        }

        return bookSeries.get();
    }

    @Transactional
    public BookSeries updateById(Long id, BookSeries updatedBookSeries) {
        Optional<BookSeries> bookSeriesOptional = bookSeriesRepository.findById(id);

        if (bookSeriesOptional.isEmpty()) {
            throw new BookSeriesNotFoundException("Selected book series not found");
        }

        BookSeries bookSeries = bookSeriesOptional.get();
        bookSeries.setNameOfSeries(updatedBookSeries.getNameOfSeries());
        bookSeries.setAuthor(updatedBookSeries.getAuthor());

        return bookSeriesRepository.save(bookSeries);
    }


    public void addBookToBookSeries(Long idBookSeries, Long idBook) {
        Optional<BookSeries> bookSeriesOptional = bookSeriesRepository.findById(idBookSeries);

        if (bookSeriesOptional.isEmpty()) {
            throw new BookSeriesNotFoundException("Selected book series not found");
        } else {
            BookSeries bookSeries = bookSeriesOptional.get();
            bookService.addBookToBookSeries(idBook, bookSeries);
        }
    }

    public void deleteBookFromBookSeries(Long idBookSeries, Long idBook) {
        Optional<BookSeries> bookSeriesOptional = bookSeriesRepository.findById(idBookSeries);

        if(bookSeriesOptional.isEmpty()){
            throw new BookSeriesNotFoundException("Selected book series not found");
        }

        bookService.deleteBookFromBookSeries(idBook);

    }
}
