package ksiazkopol.library.bookseries;

import ksiazkopol.library.book.BookAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookSeriesAPI {
    private Long id;
    private String nameOfSeries;
    private String author;
    private List<BookAPI> bookSeriesList;


    public BookSeriesAPI(BookSeries bookSeries) {
        this.nameOfSeries = bookSeries.getNameOfSeries();
        this.author = bookSeries.getAuthor();
        bookSeriesList = BookAPI.toApi(bookSeries.getBook());
    }

    public BookSeries toModel() {
        BookSeries bookSeries = new BookSeries();
        bookSeries.setNameOfSeries(nameOfSeries);
        bookSeries.setAuthor(author);
        bookSeries.setBook(null);
        return bookSeries;
    }
}
