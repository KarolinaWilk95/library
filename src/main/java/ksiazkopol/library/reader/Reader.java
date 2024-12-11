package ksiazkopol.library.reader;

import jakarta.persistence.*;
import ksiazkopol.library.book.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "readers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String name;
    @Column
    private String surname;

    @OneToMany(mappedBy = "reader")
    private Set<Book> books = new HashSet<>();
}
