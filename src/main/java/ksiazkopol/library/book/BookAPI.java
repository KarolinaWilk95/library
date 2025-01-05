package ksiazkopol.library.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAPI {
    private Long id;
    private String title;
    private String author;
    private String genre;
    private String publisher;
    private LocalDate publicationDate;
    private Long ISBN;
    private LocalDate borrowDate;
    private LocalDate returnDate;


    public BookAPI(Book book) {
        this.id = book.getId();
        this.ISBN = book.getISBN();
        this.publicationDate = book.getPublicationDate();
        this.publisher = book.getPublisher();
        this.genre = book.getGenre();
        this.author = book.getAuthor();
        this.title = book.getTitle();
        this.borrowDate = book.getBorrowDate();
        this.returnDate = book.getReturnDate();
    }

    public Book toModel() {
        Book model = new Book();
        model.setId(id);
        model.setTitle(title);
        model.setAuthor(author);
        model.setGenre(genre);
        model.setPublisher(publisher);
        model.setPublicationDate(publicationDate);
        model.setISBN(ISBN);
        model.setBorrowDate(borrowDate);
        model.setReturnDate(returnDate);
        return model;
    }

    public static List<BookAPI> toApi(Collection<Book> modelBooks) {

        if (modelBooks == null || modelBooks.isEmpty()) {
            return Collections.emptyList();
        }

        return modelBooks.stream()
                .map((BookAPI::new))
                .toList();

    }
}
