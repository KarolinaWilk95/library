package ksiazkopol.library.reader;


import ksiazkopol.library.book.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReaderAPI {
    private Long id;
    private String name;
    private String surname;
    private Set<Book> books = new HashSet<>();

    public ReaderAPI(Reader reader) {
        this.name = reader.getName();
        this.surname = reader.getSurname();
        this.id = reader.getId();
        this.books = reader.getBooks();
    }

    public Reader toModel() {
        Reader reader = new Reader();
        reader.setName(name);
        reader.setSurname(surname);
        reader.setId(id);
        reader.setBooks(books);
        return reader;
    }
}
