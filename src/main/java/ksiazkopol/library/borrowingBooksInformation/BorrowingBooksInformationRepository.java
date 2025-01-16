package ksiazkopol.library.borrowingBooksInformation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingBooksInformationRepository extends JpaRepository<BorrowingBooksInformation, Long> {

    @Query("select b from BorrowingBooksInformation b where b.idBook = :idBook and b.ISBN = :ISBN")
    List<BorrowingBooksInformation> findByIdBookAndIsbn(@Param("idBook") Long idBook, @Param("ISBN") Long ISBN);

    @Query("select count(b) from BorrowingBooksInformation b where b.actualReturnDate - b.borrowDate > 7")
    long countBooksAfterTheDueDate();

    @Query("select count(b) from BorrowingBooksInformation b where b.actualReturnDate is null and TIMESTAMPDIFF(day, b.expectedReturnDate, b.borrowDate) > 7")
    long countBooksAfterDueDateNotReturned();


    @Query("select count(b.idReader) from BorrowingBooksInformation b where b.actualReturnDate is null and TIMESTAMPDIFF(day, b.expectedReturnDate, b.borrowDate) > 7")
    long countAllReadersAfterDueDate();


}
