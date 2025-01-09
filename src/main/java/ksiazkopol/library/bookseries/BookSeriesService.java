package ksiazkopol.library.bookseries;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookSeriesService {

    private final BookSeriesRepository bookSeriesRepository;

    public BookSeriesService(BookSeriesRepository bookSeriesRepository) {
        this.bookSeriesRepository = bookSeriesRepository;
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

    public Optional<BookSeries> findById(Long id) {
        Optional<BookSeries> bookSeries = bookSeriesRepository.findById(id);

        if (bookSeries.isEmpty()) {
            throw new BookSeriesNotFoundException("Selected book series not found");
        }

        return bookSeries;
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
}
