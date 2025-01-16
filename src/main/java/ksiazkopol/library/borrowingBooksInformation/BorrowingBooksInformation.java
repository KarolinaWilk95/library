package ksiazkopol.library.borrowingBooksInformation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "borrowing_books_information")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingBooksInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private Long idBook;

    @Column
    private String bookTitle;

    @Column
    private String bookAuthor;

    @Column
    private Long ISBN;

    @Column
    private Long idReader;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private LocalDate borrowDate;

    @Column
    private LocalDate expectedReturnDate;

    @Column
    private LocalDate actualReturnDate;

}
