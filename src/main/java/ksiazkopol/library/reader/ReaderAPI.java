package ksiazkopol.library.reader;


import com.fasterxml.jackson.annotation.JsonInclude;
import ksiazkopol.library.book.BookAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ReaderAPI {
    private Long id;
    private String name;
    private String surname;
    private List<BookAPI> books;

    public ReaderAPI(Reader reader) {
        this.name = reader.getName();
        this.surname = reader.getSurname();
        this.id = reader.getId();
        books = BookAPI.toApi(reader.getBooks());
    }

    public Reader toModel() {
        Reader reader = new Reader();
        reader.setName(name);
        reader.setSurname(surname);
        reader.setId(id);
        reader.setBooks(null);
        return reader;
    }
}
