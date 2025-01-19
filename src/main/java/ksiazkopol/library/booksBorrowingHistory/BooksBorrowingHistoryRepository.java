package ksiazkopol.library.booksBorrowingHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BooksBorrowingHistoryRepository extends JpaRepository<BooksBorrowingHistory, Long> {


    @Query("select b from BooksBorrowingHistory b where b.idBook = :idBook and b.ISBN = :ISBN and b.actualReturnDate is null")
    Optional<BooksBorrowingHistory> findByIdBookAndIsbn(@Param("idBook") Long idBook, @Param("ISBN") Long ISBN);

    @Query("select count(b) from BooksBorrowingHistory b where b.actualReturnDate - b.borrowDate > 7")
    long countBooksAfterTheDueDate();

    @Query("select count(b) from BooksBorrowingHistory b where b.actualReturnDate is null and TIMESTAMPDIFF(day, b.expectedReturnDate, b.borrowDate) > 7")
    long countBooksAfterDueDateNotReturned();


    @Query("select count(b.idReader) from BooksBorrowingHistory b where b.actualReturnDate is null and TIMESTAMPDIFF(day, b.expectedReturnDate, b.borrowDate) > 7")
    long countAllReadersAfterDueDate();

    @Query("""
            select b from BooksBorrowingHistory b
            where b.idBook = :idBook
            and b.idReader = :idReader
            and b.actualReturnDate is null""")
    Optional<BooksBorrowingHistory> findByReaderIdBookIdNotReturn(Long idBook, Long idReader);


}
