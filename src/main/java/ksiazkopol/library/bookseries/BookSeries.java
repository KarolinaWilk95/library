package ksiazkopol.library.bookseries;


import jakarta.persistence.*;
import ksiazkopol.library.book.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "bookSeries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookSeries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String nameOfSeries;

    @Column
    private String author;

    @OneToMany(mappedBy = "bookSeries", fetch = FetchType.EAGER)
    private Collection<Book> book;

}
