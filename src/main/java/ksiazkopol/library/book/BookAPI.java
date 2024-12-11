package ksiazkopol.library.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    public BookAPI(Book book) {
        this.id = book.getId();
        this.ISBN = book.getISBN();
        this.publicationDate = book.getPublicationDate();
        this.publisher = book.getPublisher();
        this.genre = book.getGenre();
        this.author = book.getAuthor();
        this.title = book.getTitle();
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
        return model;
    }
}
