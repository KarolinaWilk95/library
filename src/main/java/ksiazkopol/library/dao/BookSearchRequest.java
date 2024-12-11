package ksiazkopol.library.dao;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookSearchRequest {
    private String title;
    private String author;
    private String genre;
    private String publisher;
    private Integer publicationYear;
    private LocalDate publicationDate;
    private Long ISBN;
}
