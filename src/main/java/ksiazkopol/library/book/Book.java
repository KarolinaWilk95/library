package ksiazkopol.library.book;

import jakarta.persistence.*;
import ksiazkopol.library.bookseries.BookSeries;
import ksiazkopol.library.reader.Reader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String title;
    @Column
    private String author;
    @Column
    private String genre;
    @Column
    private String publisher;
    @Column
    private LocalDate publicationDate;
    @Column
    private Long ISBN;
    @Column
    private LocalDate borrowDate;
    @Column
    private LocalDate expectedReturnDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "readerId")
    private Reader reader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookSeriesId")
    private BookSeries bookSeries;

}
